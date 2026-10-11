package com.example.data.model

enum class ContentType {
    MOVIE,
    SERIES,
    ANIME_MOVIE,
    ANIME_SERIES
}

enum class StreamingPlatform(val displayName: String, val badgeColorHex: Long) {
    NETFLIX("Netflix", 0xFFE50914),
    PRIME_VIDEO("Prime Video", 0xFF00A8E1),
    JIO_HOTSTAR("JioHotstar", 0xFF14805E),
    LIONSGATE_PLAY("Lionsgate Play", 0xFFCC9900),
    SONY_LIV("Sony LIV", 0xFF1E272C),
    ZEE5("ZEE5", 0xFF8230C6)
}

data class ContentItem(
    val id: String,
    val title: String,
    val originalTitle: String = "",
    val posterUrl: String,
    val backdropUrl: String,
    val rating: Double,
    val year: Int,
    val genres: List<String>,
    val overview: String,
    val type: ContentType,
    val platform: StreamingPlatform? = null,
    val language: String = "Hindi",
    val durationOrEpisodes: String = "",
    val providerId: String = "eliteplex_core",
    val isTopRated: Boolean = false,
    val totalSeasons: Int = 1,
    val totalEpisodes: Int = 12,
    val studio: String = "",
    val animeStatus: String = "Completed",
    val hasDub: Boolean = true,
    val hasSub: Boolean = true
)

data class Season(
    val seasonNumber: Int,
    val title: String,
    val episodeCount: Int
)

data class Episode(
    val id: String,
    val seriesId: String,
    val seasonNumber: Int,
    val episodeNumber: Int,
    val title: String,
    val overview: String = "",
    val durationMinutes: Int = 24,
    val thumbnailUrl: String = "",
    val streamUrl: String = "",
    val isFiller: Boolean = false,
    val hasHindiDub: Boolean = true,
    val hasEnglishDub: Boolean = true
)

data class StreamSource(
    val providerName: String,
    val quality: String, // "1080p", "720p", "480p"
    val streamUrl: String,
    val isDirectMedia: Boolean = true,
    val isBackup: Boolean = false
)

data class SubtitleTrack(
    val language: String,
    val label: String,
    val url: String = ""
)

data class PlaybackProgress(
    val contentId: String,
    val title: String,
    val posterUrl: String,
    val contentType: ContentType,
    val episodeId: String? = null,
    val episodeTitle: String? = null,
    val positionMs: Long = 0,
    val durationMs: Long = 1000,
    val lastWatchedTimestamp: Long = System.currentTimeMillis()
) {
    val progressFraction: Float
        get() = if (durationMs > 0) (positionMs.toFloat() / durationMs.toFloat()).coerceIn(0f, 1f) else 0f
}

data class ProviderCapability(
    val providerId: String,
    val name: String,
    val baseUrl: String,
    val supportsMovies: Boolean,
    val supportsSeries: Boolean,
    val supportsAnime: Boolean,
    val supportsSubtitles: Boolean,
    val status: String
)
