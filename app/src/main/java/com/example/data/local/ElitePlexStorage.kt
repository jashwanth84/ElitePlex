package com.example.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.data.model.ContentType
import com.example.data.model.PlaybackProgress

class ElitePlexStorage(context: Context) {

    private val dbHelper = DatabaseHelper(context)
    private val prefs = context.getSharedPreferences("eliteplex_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_DEFAULT_QUALITY = "pref_quality"
        private const val KEY_SUBTITLE_SIZE = "pref_sub_size"
        private const val KEY_SUBTITLE_BG = "pref_sub_bg"
        private const val KEY_PREFERRED_PROVIDER = "pref_provider"
    }

    // --- Watchlist Persistence ---
    fun addToWatchlist(contentId: String) {
        val db = dbHelper.writableDatabase
        val cv = ContentValues().apply {
            put(DatabaseHelper.COL_CONTENT_ID, contentId)
            put(DatabaseHelper.COL_ADDED_AT, System.currentTimeMillis())
        }
        db.insertWithOnConflict(DatabaseHelper.TABLE_WATCHLIST, null, cv, SQLiteDatabase.CONFLICT_REPLACE)
    }

    fun removeFromWatchlist(contentId: String) {
        val db = dbHelper.writableDatabase
        db.delete(DatabaseHelper.TABLE_WATCHLIST, "${DatabaseHelper.COL_CONTENT_ID} = ?", arrayOf(contentId))
    }

    fun isInWatchlist(contentId: String): Boolean {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_WATCHLIST,
            arrayOf(DatabaseHelper.COL_CONTENT_ID),
            "${DatabaseHelper.COL_CONTENT_ID} = ?",
            arrayOf(contentId),
            null, null, null
        )
        val exists = cursor.moveToFirst()
        cursor.close()
        return exists
    }

    fun getAllWatchlistIds(): List<String> {
        val list = mutableListOf<String>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_WATCHLIST,
            arrayOf(DatabaseHelper.COL_CONTENT_ID),
            null, null, null, null,
            "${DatabaseHelper.COL_ADDED_AT} DESC"
        )
        while (cursor.moveToNext()) {
            list.add(cursor.getString(0))
        }
        cursor.close()
        return list
    }

    // --- Continue Watching & Playback Progress ---
    fun savePlaybackProgress(progress: PlaybackProgress) {
        val db = dbHelper.writableDatabase
        val cv = ContentValues().apply {
            put(DatabaseHelper.COL_CONTENT_ID, progress.contentId)
            put(DatabaseHelper.COL_TITLE, progress.title)
            put(DatabaseHelper.COL_POSTER_URL, progress.posterUrl)
            put(DatabaseHelper.COL_CONTENT_TYPE, progress.contentType.name)
            put(DatabaseHelper.COL_EPISODE_ID, progress.episodeId ?: "")
            put(DatabaseHelper.COL_EPISODE_TITLE, progress.episodeTitle ?: "")
            put(DatabaseHelper.COL_POSITION_MS, progress.positionMs)
            put(DatabaseHelper.COL_DURATION_MS, progress.durationMs)
            put(DatabaseHelper.COL_LAST_WATCHED, System.currentTimeMillis())
        }
        db.insertWithOnConflict(DatabaseHelper.TABLE_PROGRESS, null, cv, SQLiteDatabase.CONFLICT_REPLACE)
    }

    fun getPlaybackProgress(contentId: String): PlaybackProgress? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_PROGRESS,
            null,
            "${DatabaseHelper.COL_CONTENT_ID} = ?",
            arrayOf(contentId),
            null, null, null
        )
        var progress: PlaybackProgress? = null
        if (cursor.moveToFirst()) {
            val title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TITLE))
            val posterUrl = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_POSTER_URL))
            val typeStr = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CONTENT_TYPE))
            val epId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EPISODE_ID))
            val epTitle = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EPISODE_TITLE))
            val pos = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_POSITION_MS))
            val dur = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DURATION_MS))
            val last = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_LAST_WATCHED))
            val cType = try { ContentType.valueOf(typeStr) } catch (_: Exception) { ContentType.MOVIE }

            progress = PlaybackProgress(
                contentId = contentId,
                title = title,
                posterUrl = posterUrl,
                contentType = cType,
                episodeId = if (epId.isNotEmpty()) epId else null,
                episodeTitle = if (epTitle.isNotEmpty()) epTitle else null,
                positionMs = pos,
                durationMs = dur,
                lastWatchedTimestamp = last
            )
        }
        cursor.close()
        return progress
    }

    fun getAllContinueWatching(): List<PlaybackProgress> {
        val list = mutableListOf<PlaybackProgress>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_PROGRESS,
            null,
            null, null, null, null,
            "${DatabaseHelper.COL_LAST_WATCHED} DESC"
        )
        while (cursor.moveToNext()) {
            val cid = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CONTENT_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TITLE))
            val posterUrl = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_POSTER_URL))
            val typeStr = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CONTENT_TYPE))
            val epId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EPISODE_ID))
            val epTitle = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EPISODE_TITLE))
            val pos = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_POSITION_MS))
            val dur = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DURATION_MS))
            val last = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_LAST_WATCHED))
            val cType = try { ContentType.valueOf(typeStr) } catch (_: Exception) { ContentType.MOVIE }

            list.add(
                PlaybackProgress(
                    contentId = cid,
                    title = title,
                    posterUrl = posterUrl,
                    contentType = cType,
                    episodeId = if (epId.isNotEmpty()) epId else null,
                    episodeTitle = if (epTitle.isNotEmpty()) epTitle else null,
                    positionMs = pos,
                    durationMs = dur,
                    lastWatchedTimestamp = last
                )
            )
        }
        cursor.close()
        return list
    }

    fun clearContinueWatching() {
        val db = dbHelper.writableDatabase
        db.delete(DatabaseHelper.TABLE_PROGRESS, null, null)
    }

    fun close() {
        dbHelper.close()
    }

    // --- User Preferences ---
    var defaultQuality: String
        get() = prefs.getString(KEY_DEFAULT_QUALITY, "1080p") ?: "1080p"
        set(value) = prefs.edit().putString(KEY_DEFAULT_QUALITY, value).apply()

    var subtitleFontSize: Float
        get() = prefs.getFloat(KEY_SUBTITLE_SIZE, 18f)
        set(value) = prefs.edit().putFloat(KEY_SUBTITLE_SIZE, value).apply()

    var subtitleBackgroundOpacity: Float
        get() = prefs.getFloat(KEY_SUBTITLE_BG, 0.6f)
        set(value) = prefs.edit().putFloat(KEY_SUBTITLE_BG, value).apply()

    var preferredProvider: String
        get() = prefs.getString(KEY_PREFERRED_PROVIDER, "eliteplex_core") ?: "eliteplex_core"
        set(value) = prefs.edit().putString(KEY_PREFERRED_PROVIDER, value).apply()

    private class DatabaseHelper(context: Context) :
        SQLiteOpenHelper(context, "eliteplex.db", null, 1) {

        companion object {
            const val TABLE_WATCHLIST = "watchlist"
            const val TABLE_PROGRESS = "playback_progress"

            const val COL_CONTENT_ID = "content_id"
            const val COL_ADDED_AT = "added_at"

            const val COL_TITLE = "title"
            const val COL_POSTER_URL = "poster_url"
            const val COL_CONTENT_TYPE = "content_type"
            const val COL_EPISODE_ID = "episode_id"
            const val COL_EPISODE_TITLE = "episode_title"
            const val COL_POSITION_MS = "position_ms"
            const val COL_DURATION_MS = "duration_ms"
            const val COL_LAST_WATCHED = "last_watched"
        }

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE $TABLE_WATCHLIST (
                    $COL_CONTENT_ID TEXT PRIMARY KEY,
                    $COL_ADDED_AT INTEGER
                )
                """.trimIndent()
            )

            db.execSQL(
                """
                CREATE TABLE $TABLE_PROGRESS (
                    $COL_CONTENT_ID TEXT PRIMARY KEY,
                    $COL_TITLE TEXT,
                    $COL_POSTER_URL TEXT,
                    $COL_CONTENT_TYPE TEXT,
                    $COL_EPISODE_ID TEXT,
                    $COL_EPISODE_TITLE TEXT,
                    $COL_POSITION_MS INTEGER,
                    $COL_DURATION_MS INTEGER,
                    $COL_LAST_WATCHED INTEGER
                )
                """.trimIndent()
            )
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_WATCHLIST")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_PROGRESS")
            onCreate(db)
        }
    }
}
