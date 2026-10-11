package com.example.ui.screens

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ContentItem
import com.example.data.model.ContentType
import com.example.data.model.StreamingPlatform
import com.example.data.repository.ElitePlexRepository
import com.example.ui.components.PosterCard

@Composable
fun PlatformExplorerScreen(
    platform: StreamingPlatform,
    onBackClick: () -> Unit,
    onContentClick: (ContentItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedType by remember { mutableStateOf(ContentType.SERIES) }

    val contentList = remember(platform, selectedType) {
        ElitePlexRepository.getPlatformContent(platform, selectedType)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF050505))
            .statusBarsPadding()
            .testTag("platform_explorer_screen")
    ) {
        // Top App Bar: Back arrow + Platform Icon + Title (Matches Screenshot 1 & 2!)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.testTag("platform_explorer_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Platform Mini Badge
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF14141A))
                    .border(1.dp, Color(platform.badgeColorHex), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = platform.displayName.take(1),
                    color = Color(platform.badgeColorHex),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Text(
                text = "${platform.displayName} Explorer",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Segmented Toggle Pill: [ Movies ] [ Series ] (Matches Screenshot 1 & 2!)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(26.dp))
                .background(Color(0xFF14141E))
                .border(1.dp, Color(0xFF282838), RoundedCornerShape(26.dp))
                .padding(4.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                val isMovies = selectedType == ContentType.MOVIE
                val isSeries = selectedType == ContentType.SERIES

                // Movies Tab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(if (isMovies) Color.White else Color.Transparent)
                        .clickable { selectedType = ContentType.MOVIE }
                        .testTag("explorer_tab_movies"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Movie,
                            contentDescription = "Movies",
                            tint = if (isMovies) Color.Black else Color(0xFFA0A0AA),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Movies",
                            color = if (isMovies) Color.Black else Color(0xFFA0A0AA),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Series Tab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(if (isSeries) Color.White else Color.Transparent)
                        .clickable { selectedType = ContentType.SERIES }
                        .testTag("explorer_tab_series"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tv,
                            contentDescription = "Series",
                            tint = if (isSeries) Color.Black else Color(0xFFA0A0AA),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Series",
                            color = if (isSeries) Color.Black else Color(0xFFA0A0AA),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // 2-Column Grid of Poster Cards (Matches Screenshot 1 & 2!)
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(contentList) { item ->
                PosterCard(
                    item = item,
                    onClick = { onContentClick(item) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                )
            }
        }
    }
}
