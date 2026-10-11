package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ClosedCaption
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Hd
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ElitePlexStorage
import com.example.data.repository.ElitePlexRepository
import com.example.ui.theme.CinemaRed

@Composable
fun SettingsScreen(
    storage: ElitePlexStorage,
    watchlistCount: Int,
    continueWatchingCount: Int,
    onOpenWatchlist: () -> Unit,
    onClearContinueWatching: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedQuality by remember { mutableStateOf(storage.defaultQuality) }
    var subtitleSize by remember { mutableFloatStateOf(storage.subtitleFontSize) }
    var subtitleOpacity by remember { mutableFloatStateOf(storage.subtitleBackgroundOpacity) }

    var showQualityPicker by remember { mutableStateOf(false) }
    var showProviderStatusDialog by remember { mutableStateOf(false) }
    var showSubtitleDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF050505))
            .testTag("settings_screen"),
        contentPadding = PaddingValues(top = 70.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Settings & Library",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        // Section 1: Library & History
        item {
            SettingsGroup(title = "My Library") {
                SettingsItem(
                    icon = Icons.Default.Bookmark,
                    title = "Watchlist",
                    subtitle = "$watchlistCount saved titles",
                    onClick = onOpenWatchlist
                )
                SettingsItem(
                    icon = Icons.Default.History,
                    title = "Continue Watching",
                    subtitle = "$continueWatchingCount active streams",
                    trailingAction = {
                        if (continueWatchingCount > 0) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Clear History",
                                tint = CinemaRed,
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable {
                                        onClearContinueWatching()
                                        Toast.makeText(context, "Continue watching history cleared", Toast.LENGTH_SHORT).show()
                                    }
                            )
                        }
                    },
                    onClick = {}
                )
            }
        }

        // Section 2: Playback & Streaming
        item {
            SettingsGroup(title = "Streaming & Playback") {
                SettingsItem(
                    icon = Icons.Default.Hd,
                    title = "Default Stream Quality",
                    subtitle = "$selectedQuality (Auto-resolution adaptive)",
                    onClick = { showQualityPicker = true }
                )
                SettingsItem(
                    icon = Icons.Default.ClosedCaption,
                    title = "Subtitle Appearance",
                    subtitle = "Font Size: ${subtitleSize.toInt()}sp · Opacity: ${(subtitleOpacity * 100).toInt()}%",
                    onClick = { showSubtitleDialog = true }
                )
                SettingsItem(
                    icon = Icons.Default.Dns,
                    title = "Provider Engines & CDN",
                    subtitle = "${ElitePlexRepository.providers.size} active providers configured",
                    onClick = { showProviderStatusDialog = true }
                )
            }
        }

        // Section 3: About ElitePlex
        item {
            SettingsGroup(title = "About") {
                SettingsItem(
                    icon = Icons.Default.Info,
                    title = "ElitePlex Platform",
                    subtitle = "Version 1.0.0 (Unified Cinematic Engine)",
                    onClick = {
                        Toast.makeText(context, "ElitePlex v1.0.0 · Multi-Provider Architecture", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }

    // Quality Picker Dialog
    if (showQualityPicker) {
        AlertDialog(
            onDismissRequest = { showQualityPicker = false },
            containerColor = Color(0xFF161622),
            title = { Text("Select Stream Quality", color = Color.White) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("1080p", "720p", "480p").forEach { q ->
                        val isSel = q == selectedQuality
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) Color(0xFF281216) else Color.Transparent)
                                .clickable {
                                    selectedQuality = q
                                    storage.defaultQuality = q
                                    showQualityPicker = false
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = when (q) {
                                    "1080p" -> "1080p Ultra HD (Recommended)"
                                    "720p" -> "720p High Definition"
                                    else -> "480p Standard (Data Saver)"
                                },
                                color = if (isSel) CinemaRed else Color.White,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                            )
                            if (isSel) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = CinemaRed)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showQualityPicker = false }) {
                    Text("Done", color = CinemaRed)
                }
            }
        )
    }

    // Subtitle Customization Dialog
    if (showSubtitleDialog) {
        AlertDialog(
            onDismissRequest = { showSubtitleDialog = false },
            containerColor = Color(0xFF161622),
            title = { Text("Subtitle Appearance", color = Color.White) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text(
                        text = "Font Size: ${subtitleSize.toInt()} sp",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Slider(
                        value = subtitleSize,
                        onValueChange = {
                            subtitleSize = it
                            storage.subtitleFontSize = it
                        },
                        valueRange = 14f..28f,
                        colors = SliderDefaults.colors(
                            thumbColor = CinemaRed,
                            activeTrackColor = CinemaRed
                        )
                    )

                    Text(
                        text = "Background Opacity: ${(subtitleOpacity * 100).toInt()}%",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Slider(
                        value = subtitleOpacity,
                        onValueChange = {
                            subtitleOpacity = it
                            storage.subtitleBackgroundOpacity = it
                        },
                        valueRange = 0.2f..1f,
                        colors = SliderDefaults.colors(
                            thumbColor = CinemaRed,
                            activeTrackColor = CinemaRed
                        )
                    )

                    // Preview Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF0F0F15))
                            .padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.Black.copy(alpha = subtitleOpacity))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Preview: Subtitle Track",
                                color = Color.White,
                                fontSize = subtitleSize.sp
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSubtitleDialog = false }) {
                    Text("Save", color = CinemaRed)
                }
            }
        )
    }

    // Provider Status Dialog
    if (showProviderStatusDialog) {
        AlertDialog(
            onDismissRequest = { showProviderStatusDialog = false },
            containerColor = Color(0xFF161622),
            title = { Text("Active Provider Engine Registry", color = Color.White) },
            text = {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(ElitePlexRepository.providers) { prov ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF1E1E2C))
                                .padding(10.dp)
                        ) {
                            Column {
                                Text(
                                    text = prov.name,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = prov.baseUrl,
                                    color = Color(0xFFA0A0AA),
                                    fontSize = 11.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = prov.status,
                                    color = CinemaRed,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showProviderStatusDialog = false }) {
                    Text("Close", color = CinemaRed)
                }
            }
        )
    }
}

@Composable
fun SettingsGroup(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = title,
            color = Color(0xFFA0A0B0),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF14141E))
                .border(1.dp, Color(0xFF242434), RoundedCornerShape(16.dp))
                .padding(vertical = 4.dp)
        ) {
            Column {
                content()
            }
        }
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    trailingAction: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0xFF222232)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = CinemaRed,
                modifier = Modifier.size(18.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                color = Color(0xFFA0A0AA),
                fontSize = 12.sp
            )
        }

        if (trailingAction != null) {
            trailingAction()
        } else {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = Color(0xFF606070),
                modifier = Modifier.size(14.dp)
            )
        }
    }
}
