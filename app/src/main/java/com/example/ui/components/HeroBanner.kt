package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.ContentItem
import kotlinx.coroutines.delay

@Composable
fun HeroBanner(
    featuredItems: List<ContentItem>,
    onPlayClick: (ContentItem) -> Unit,
    onInfoClick: (ContentItem) -> Unit,
    onWatchlistToggle: (ContentItem) -> Unit,
    isItemInWatchlist: (ContentItem) -> Boolean,
    modifier: Modifier = Modifier
) {
    if (featuredItems.isEmpty()) return

    var currentIndex by remember { mutableIntStateOf(0) }
    val currentItem = featuredItems[currentIndex.coerceIn(0, featuredItems.size - 1)]
    val context = LocalContext.current

    // Auto rotate every 6 seconds
    LaunchedEffect(featuredItems.size) {
        while (true) {
            delay(6000)
            currentIndex = (currentIndex + 1) % featuredItems.size
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(480.dp)
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(Color(0xFF14141E))
            .border(1.dp, Color(0xFF262635), RoundedCornerShape(28.dp))
    ) {
        // Backdrop Image with Fade Transition
        AnimatedContent(
            targetState = currentItem,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "hero_backdrop"
        ) { item ->
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(item.backdropUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Deep Cinematic Gradient Overlays
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x33000000),
                            Color(0x66000000),
                            Color(0xCC050508),
                            Color(0xFF050508)
                        ),
                        startY = 60f
                    )
                )
        )

        // Content Area at Bottom of Banner
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // "Top Rated" pill badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0x661E1E28))
                    .border(1.dp, Color(0x66FFFFFF), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Top Rated",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title
            Text(
                text = currentItem.title,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Subtitle / Language / Genre
            Text(
                text = "${currentItem.language} · ${currentItem.genres.take(2).joinToString(" / ")}",
                color = Color(0xFFAAAAAA),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons: Play + Bookmark + Info
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Large White Pill [ ▶ Play ]
                Button(
                    onClick = { onPlayClick(currentItem) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .height(48.dp)
                        .weight(1f)
                        .testTag("hero_play_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Play",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Bookmark / Watchlist Button
                val inWatchlist = isItemInWatchlist(currentItem)
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0x44000000))
                        .border(1.5.dp, Color(0x88FFFFFF), CircleShape)
                        .clickable { onWatchlistToggle(currentItem) }
                        .testTag("hero_watchlist_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (inWatchlist) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Watchlist",
                        tint = if (inWatchlist) Color(0xFFE50914) else Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Info Button
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0x44000000))
                        .border(1.5.dp, Color(0x88FFFFFF), CircleShape)
                        .clickable { onInfoClick(currentItem) }
                        .testTag("hero_info_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Carousel Pagination Dots
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                featuredItems.forEachIndexed { index, _ ->
                    val isCurrent = index == currentIndex
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .size(if (isCurrent) 18.dp to 6.dp else 6.dp to 6.dp)
                            .clip(CircleShape)
                            .background(if (isCurrent) Color.White else Color(0x55FFFFFF))
                            .clickable { currentIndex = index }
                    )
                }
            }
        }
    }
}

private fun Modifier.size(sizePair: Pair<androidx.compose.ui.unit.Dp, androidx.compose.ui.unit.Dp>): Modifier {
    return this.width(sizePair.first).height(sizePair.second)
}
