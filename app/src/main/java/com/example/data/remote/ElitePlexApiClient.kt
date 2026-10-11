package com.example.data.remote

import android.net.Uri
import android.util.Log
import com.example.data.model.ContentItem
import com.example.data.model.ContentType
import com.example.data.model.StreamingPlatform
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object ElitePlexApiClient {
    private const val TAG = "ElitePlexApiClient"
    private const val BASE_URL = "https://eliteplex-api.vercel.app"
    private const val TMDB_POSTER_BASE = "https://image.tmdb.org/t/p/w500"
    private const val TMDB_BACKDROP_BASE = "https://image.tmdb.org/t/p/w1280"

    suspend fun getHomeContent(): Result<Map<String, List<ContentItem>>> = withContext(Dispatchers.IO) {
        try {
            val jsonStr = executeGet("$BASE_URL/api/home") ?: return@withContext Result.failure(Exception("Empty response"))
            val root = JSONObject(jsonStr)

            val resultMap = mutableMapOf<String, List<ContentItem>>()

            val sections = listOf("trending", "popular_movies", "popular_tv", "top_movies", "top_tv")
            for (sec in sections) {
                if (root.has(sec)) {
                    val array = root.optJSONArray(sec)
                    if (array != null) {
                        resultMap[sec] = parseContentArray(array)
                    }
                }
            }
            Result.success(resultMap)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching /api/home: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun getMovies(): Result<List<ContentItem>> = withContext(Dispatchers.IO) {
        try {
            val jsonStr = executeGet("$BASE_URL/api/movies") ?: return@withContext Result.failure(Exception("Empty response"))
            val root = JSONObject(jsonStr)
            val items = root.optJSONArray("items") ?: root.optJSONArray("results") ?: JSONArray()
            Result.success(parseContentArray(items, defaultType = ContentType.MOVIE))
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching /api/movies: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun getSeries(): Result<List<ContentItem>> = withContext(Dispatchers.IO) {
        try {
            val jsonStr = executeGet("$BASE_URL/api/series") ?: return@withContext Result.failure(Exception("Empty response"))
            val root = JSONObject(jsonStr)
            val items = root.optJSONArray("items") ?: root.optJSONArray("results") ?: JSONArray()
            Result.success(parseContentArray(items, defaultType = ContentType.SERIES))
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching /api/series: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun searchContent(query: String): Result<List<ContentItem>> = withContext(Dispatchers.IO) {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return@withContext Result.success(emptyList())

        try {
            val encoded = Uri.encode(trimmed)
            val jsonStr = executeGet("$BASE_URL/api/search?q=$encoded") ?: return@withContext Result.failure(Exception("Empty response"))
            val root = JSONObject(jsonStr)
            val items = root.optJSONArray("items") ?: root.optJSONArray("results") ?: JSONArray()
            Result.success(parseContentArray(items))
        } catch (e: Exception) {
            Log.e(TAG, "Error searching: ${e.message}", e)
            Result.failure(e)
        }
    }

    private fun parseContentArray(array: JSONArray, defaultType: ContentType? = null): List<ContentItem> {
        val list = mutableListOf<ContentItem>()
        for (i in 0 until array.length()) {
            val obj = array.optJSONObject(i) ?: continue
            val item = parseNormalizedContentItem(obj, defaultType)
            if (item != null) {
                list.add(item)
            }
        }
        return list
    }

    /**
     * Maps external API fields into internal normalized ContentItem fields:
     * - contentId
     * - providerId
     * - title
     * - posterUrl (handles absolute, relative, and null)
     * - backdropUrl (handles absolute, relative, and null)
     * - rating
     * - overview
     * - genres
     * - releaseYear
     * - contentType
     */
    fun parseNormalizedContentItem(json: JSONObject, defaultType: ContentType? = null): ContentItem? {
        val id = when {
            json.has("id") -> json.optString("id")
            json.has("tmdb_id") -> json.optString("tmdb_id")
            json.has("subject_id") -> json.optString("subject_id")
            else -> return null
        }
        if (id.isBlank() || id == "null") return null

        val title = when {
            json.has("title") && json.optString("title").isNotBlank() -> json.optString("title")
            json.has("name") && json.optString("name").isNotBlank() -> json.optString("name")
            else -> "Untitled"
        }

        val rawPoster = when {
            json.has("poster") -> json.optString("poster")
            json.has("poster_path") -> json.optString("poster_path")
            json.has("image") -> json.optString("image")
            json.has("thumbnail") -> json.optString("thumbnail")
            else -> ""
        }

        val rawBackdrop = when {
            json.has("backdrop") -> json.optString("backdrop")
            json.has("backdrop_path") -> json.optString("backdrop_path")
            json.has("banner") -> json.optString("banner")
            else -> ""
        }

        val posterUrl = normalizeImageUrl(rawPoster, isBackdrop = false)
        val backdropUrl = normalizeImageUrl(rawBackdrop, isBackdrop = true).ifBlank { posterUrl }

        val rating = when {
            json.has("rating") -> json.optDouble("rating", 0.0)
            json.has("vote_average") -> json.optDouble("vote_average", 0.0)
            else -> 0.0
        }

        val yearStr = when {
            json.has("year") -> json.optString("year")
            json.has("release_date") -> json.optString("release_date")
            json.has("first_air_date") -> json.optString("first_air_date")
            else -> ""
        }
        val year = parseYear(yearStr)

        val rawType = json.optString("type", "").lowercase()
        val contentType = when {
            defaultType != null -> defaultType
            rawType.contains("anime") -> ContentType.ANIME_SERIES
            rawType == "movie" -> ContentType.MOVIE
            rawType == "tv" || rawType == "series" -> ContentType.SERIES
            else -> ContentType.MOVIE
        }

        val overview = json.optString("overview", "").trim()

        val genres = mutableListOf<String>()
        val genreArr = json.optJSONArray("genres")
        if (genreArr != null) {
            for (g in 0 until genreArr.length()) {
                val gStr = genreArr.optString(g, "").trim()
                if (gStr.isNotBlank()) genres.add(gStr)
            }
        }
        if (genres.isEmpty()) {
            if (contentType == ContentType.ANIME_SERIES || contentType == ContentType.ANIME_MOVIE) {
                genres.add("Animation")
                genres.add("Action")
            } else if (contentType == ContentType.SERIES) {
                genres.add("Drama")
            } else {
                genres.add("Feature")
            }
        }

        val providerId = json.optString("provider", "eliteplex_core")

        return ContentItem(
            id = id,
            title = title,
            originalTitle = json.optString("original_title", ""),
            posterUrl = posterUrl,
            backdropUrl = backdropUrl,
            rating = if (rating.isNaN()) 0.0 else rating,
            year = year,
            genres = genres,
            overview = overview.ifBlank { "Available for streaming on ElitePlex." },
            type = contentType,
            platform = StreamingPlatform.NETFLIX,
            language = if (title.contains("[Hindi]")) "Hindi" else "English / Multi",
            providerId = providerId,
            isTopRated = rating >= 7.8
        )
    }

    private fun normalizeImageUrl(raw: String?, isBackdrop: Boolean): String {
        if (raw.isNullOrBlank() || raw == "null") return ""
        val trimmed = raw.trim()

        return when {
            trimmed.startsWith("https://", ignoreCase = true) -> trimmed
            trimmed.startsWith("http://", ignoreCase = true) -> "https://" + trimmed.substring(7)
            trimmed.startsWith("/") -> {
                // TMDB or relative image path
                val base = if (isBackdrop) TMDB_BACKDROP_BASE else TMDB_POSTER_BASE
                "$base$trimmed"
            }
            else -> {
                // If it looks like a TMDB hash (e.g. "fgSm5ylwiXb...jpg")
                if (trimmed.endsWith(".jpg", ignoreCase = true) || trimmed.endsWith(".png", ignoreCase = true) || trimmed.endsWith(".webp", ignoreCase = true)) {
                    val base = if (isBackdrop) TMDB_BACKDROP_BASE else TMDB_POSTER_BASE
                    "$base/$trimmed"
                } else {
                    ""
                }
            }
        }
    }

    private fun parseYear(raw: String): Int {
        if (raw.isBlank()) return 2024
        val digits = raw.take(4).filter { it.isDigit() }
        return digits.toIntOrNull() ?: 2024
    }

    private fun executeGet(urlString: String): String? {
        var conn: HttpURLConnection? = null
        return try {
            val url = URL(urlString)
            conn = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                connectTimeout = 7000
                readTimeout = 7000
                setRequestProperty("User-Agent", "ElitePlex-Android/1.0")
                setRequestProperty("Accept", "application/json")
                instanceFollowRedirects = true
            }

            val code = conn.responseCode
            if (code in 200..299) {
                val reader = BufferedReader(InputStreamReader(conn.inputStream))
                val sb = java.lang.StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    sb.append(line)
                }
                reader.close()
                sb.toString()
            } else {
                Log.w(TAG, "HTTP error $code for $urlString")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Network failure for $urlString: ${e.message}")
            null
        } finally {
            conn?.disconnect()
        }
    }
}
