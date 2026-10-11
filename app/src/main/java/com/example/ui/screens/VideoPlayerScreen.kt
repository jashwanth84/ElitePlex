package com.example.ui.screens

import android.net.Uri
import android.widget.VideoView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ClosedCaption
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Hd
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.data.local.ElitePlexStorage
import com.example.data.model.ContentItem
import com.example.data.model.Episode
import com.example.data.model.PlaybackProgress
import com.example.data.repository.ElitePlexRepository
import com.example.ui.theme.CinemaRed
import kotlinx.coroutines.delay

@Composable
fun VideoPlayerScreen(
    item: ContentItem,
    episode: Episode?,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val storage = remember { ElitePlexStorage(context) }

    val streamSources = remember(item.id, episode?.id) {
        ElitePlexRepository.getAvailableStreamSources(item.id, episode?.id, storage.preferredProvider)
    }
    var selectedSourceIndex by remember { mutableIntStateOf(0) }
    val currentStream = streamSources[selectedSourceIndex.coerceIn(0, streamSources.size - 1)]

    val subtitleTracks = remember(item.id) { ElitePlexRepository.getSubtitlesForContent(item.id) }
    var selectedSubtitle by remember { mutableStateOf<String?>("en") }

    var videoViewRef by remember { mutableStateOf<VideoView?>(null) }
    var isPlaying by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(true) }
    var currentPositionMs by remember { mutableLongStateOf(0L) }
    var durationMs by remember { mutableLongStateOf(1000L) }
    var showControls by remember { mutableStateOf(true) }

    var showSubtitleDialog by remember { mutableStateOf(false) }
    var showQualityDialog by remember { mutableStateOf(false) }

    // Initial saved position
    val savedProgress = remember(item.id) { storage.getPlaybackProgress(item.id) }

    // Periodic position updater and progress save
    LaunchedEffect(videoViewRef, isPlaying) {
        while (true) {
            delay(1000)
            videoViewRef?.let { vv ->
                if (vv.isPlaying) {
                    val pos = vv.currentPosition.toLong()
                    val dur = vv.duration.toLong().coerceAtLeast(1000L)
                    currentPositionMs = pos
                    durationMs = dur
                    isLoading = false

                    // Save to local storage for Continue Watching!
                    storage.savePlaybackProgress(
                        PlaybackProgress(
                            contentId = item.id,
                            title = item.title,
                            posterUrl = item.posterUrl,
                            contentType = item.type,
                            episodeId = episode?.id,
                            episodeTitle = episode?.title,
                            positionMs = pos,
                            durationMs = dur
                        )
                    )
                }
            }
        }
    }

    // Auto-hide controls after 4 seconds of inactivity
    LaunchedEffect(showControls) {
        if (showControls) {
            delay(4000)
            showControls = false
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            videoViewRef?.stopPlayback()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                showControls = !showControls
            }
            .testTag("video_player_screen")
    ) {
        // Android Native VideoView Player
        AndroidView(
            factory = { ctx ->
                VideoView(ctx).apply {
                    setVideoURI(Uri.parse(currentStream.streamUrl))
                    setOnPreparedListener { mp ->
                        isLoading = false
                        durationMs = mp.duration.toLong().coerceAtLeast(1000L)
                        // Resume from previous position if available
                        if (savedProgress != null && savedProgress.positionMs > 0 && savedProgress.positionMs < durationMs) {
                            seekTo(savedProgress.positionMs.toInt())
                        }
                        start()
                        isPlaying = true
                    }
                    setOnCompletionListener {
                        isPlaying = false
                        currentPositionMs = durationMs
                    }
                    setOnErrorListener { _, _, _ ->
                        isLoading = false
                        // Fallback to next stream source if error occurs
                        if (selectedSourceIndex < streamSources.size - 1) {
                            selectedSourceIndex++
                            setVideoURI(Uri.parse(streamSources[selectedSourceIndex].streamUrl))
                            start()
                        }
                        true
                    }
                    videoViewRef = this
                }
            },
            update = { vv ->
                // Update stream URI if source changes
                if (vv.tag != currentStream.streamUrl) {
                    vv.tag = currentStream.streamUrl
                    vv.setVideoURI(Uri.parse(currentStream.streamUrl))
                    vv.start()
                    isPlaying = true
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Loading spinner
        if (isLoading) {
            CircularProgressIndicator(
                color = CinemaRed,
                modifier = Modifier
                    .size(54.dp)
                    .align(Alignment.Center)
            )
        }

        // Subtitle Overlay at Bottom (Simulated Subtitle Display)
        if (selectedSubtitle != null && isPlaying) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = if (showControls) 90.dp else 36.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.Black.copy(alpha = storage.subtitleBackgroundOpacity))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = if (selectedSubtitle == "hi") "[ध्वनि पृष्ठभूमि संगीत बज रहा है]" else "[Subtitles: ${item.title}]",
                    color = Color.White,
                    fontSize = storage.subtitleFontSize.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Overlay Controls (Animated Fade In/Out)
        AnimatedVisibility(
            visible = showControls,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xCC000000),
                                Color(0x33000000),
                                Color(0x99000000),
                                Color(0xEE000000)
                            )
                        )
                    )
            ) {
                // Top App Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0x55000000))
                                .testTag("player_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }

                        Column {
                            Text(
                                text = item.title,
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (episode != null) {
                                Text(
                                    text = "S${episode.seasonNumber} E${episode.episodeNumber}: ${episode.title}",
                                    color = Color(0xFFA0A0B0),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    // Quality Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x66000000))
                            .clickable { showQualityDialog = true }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Hd,
                                contentDescription = "Quality",
                                tint = CinemaRed,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = currentStream.quality,
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Center Playback Buttons: 10s Rewind | Play/Pause | 10s Forward
                Row(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalArrangement = Arrangement.spacedBy(36.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Rewind 10s
                    IconButton(
                        onClick = {
                            videoViewRef?.let { vv ->
                                val newPos = (vv.currentPosition - 10000).coerceAtLeast(0)
                                vv.seekTo(newPos)
                                currentPositionMs = newPos.toLong()
                            }
                        },
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(Color(0x66000000))
                            .testTag("player_rewind_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.FastRewind,
                            contentDescription = "Rewind 10s",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    // Play/Pause
                    IconButton(
                        onClick = {
                            videoViewRef?.let { vv ->
                                if (vv.isPlaying) {
                                    vv.pause()
                                    isPlaying = false
                                } else {
                                    vv.start()
                                    isPlaying = true
                                }
                            }
                        },
                        modifier = Modifier
                            .size(68.dp)
                            .clip(CircleShape)
                            .background(CinemaRed)
                            .testTag("player_play_pause_button")
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = Color.White,
                            modifier = Modifier.size(38.dp)
                        )
                    }

                    // Forward 10s
                    IconButton(
                        onClick = {
                            videoViewRef?.let { vv ->
                                val newPos = (vv.currentPosition + 10000).coerceAtMost(durationMs.toInt())
                                vv.seekTo(newPos)
                                currentPositionMs = newPos.toLong()
                            }
                        },
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(Color(0x66000000))
                            .testTag("player_forward_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.FastForward,
                            contentDescription = "Forward 10s",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                // Bottom Controls: Timestamps, Scrub Slider, Subtitles, Settings, Fullscreen
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    // Scrub Slider
                    Slider(
                        value = if (durationMs > 0) (currentPositionMs.toFloat() / durationMs.toFloat()).coerceIn(0f, 1f) else 0f,
                        onValueChange = { fraction ->
                            val targetMs = (fraction * durationMs).toLong()
                            currentPositionMs = targetMs
                            videoViewRef?.seekTo(targetMs.toInt())
                        },
                        colors = SliderDefaults.colors(
                            thumbColor = CinemaRed,
                            activeTrackColor = CinemaRed,
                            inactiveTrackColor = Color(0x55FFFFFF)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp)
                            .testTag("player_seek_slider")
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Current / Total Time
                        Text(
                            text = "${formatTime(currentPositionMs)} / ${formatTime(durationMs)}",
                            color = Color(0xFFA0A0B0),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )

                        // Action Buttons: CC + Server/Quality + Fullscreen
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Subtitle Button
                            IconButton(
                                onClick = { showSubtitleDialog = true },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ClosedCaption,
                                    contentDescription = "Subtitles",
                                    tint = if (selectedSubtitle != null) CinemaRed else Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            // Quality & Provider Switcher Button
                            IconButton(
                                onClick = { showQualityDialog = true },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Settings",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            // Fullscreen Button
                            IconButton(
                                onClick = { /* Fullscreen already matches layout */ },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Fullscreen,
                                    contentDescription = "Fullscreen",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Subtitle Dialog
        if (showSubtitleDialog) {
            AlertDialog(
                onDismissRequest = { showSubtitleDialog = false },
                containerColor = Color(0xFF161622),
                title = { Text("Subtitles & Captions", color = Color.White) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedSubtitle = null
                                    showSubtitleDialog = false
                                }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Off",
                                color = if (selectedSubtitle == null) CinemaRed else Color.White,
                                fontWeight = if (selectedSubtitle == null) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                        subtitleTracks.forEach { sub ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedSubtitle = sub.language
                                        showSubtitleDialog = false
                                    }
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = sub.label,
                                    color = if (selectedSubtitle == sub.language) CinemaRed else Color.White,
                                    fontWeight = if (selectedSubtitle == sub.language) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showSubtitleDialog = false }) {
                        Text("Close", color = CinemaRed)
                    }
                }
            )
        }

        // Stream Quality & Provider Resolver Dialog
        if (showQualityDialog) {
            AlertDialog(
                onDismissRequest = { showQualityDialog = false },
                containerColor = Color(0xFF161622),
                title = { Text("Stream Server & Resolution", color = Color.White) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Select active media source resolver:",
                            color = Color(0xFFA0A0B0),
                            fontSize = 12.sp
                        )
                        streamSources.forEachIndexed { index, src ->
                            val isSel = index == selectedSourceIndex
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) Color(0xFF2E1218) else Color(0xFF1E1E2C))
                                    .clickable {
                                        selectedSourceIndex = index
                                        videoViewRef?.setVideoURI(Uri.parse(src.streamUrl))
                                        videoViewRef?.start()
                                        isPlaying = true
                                        showQualityDialog = false
                                    }
                                    .padding(10.dp)
                            ) {
                                Column {
                                    Text(
                                        text = src.providerName,
                                        color = if (isSel) CinemaRed else Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = "${src.quality} Direct Media stream",
                                        color = Color(0xFFA0A0B0),
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showQualityDialog = false }) {
                        Text("Done", color = CinemaRed)
                    }
                }
            )
        }
    }
}

private fun formatTime(millis: Long): String {
    val totalSec = millis / 1000
    val min = totalSec / 60
    val sec = totalSec % 60
    return String.format("%02d:%02d", min, sec)
}
