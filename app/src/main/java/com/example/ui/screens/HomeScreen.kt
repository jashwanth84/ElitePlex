package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ContentItem
import com.example.data.model.PlaybackProgress
import com.example.data.model.StreamingPlatform
import com.example.data.repository.ElitePlexRepository
import com.example.ui.components.ContinueWatchingRow
import com.example.ui.components.HeroBanner
import com.example.ui.components.PlatformRow
import com.example.ui.components.PosterCard

@Composable
fun HomeScreen(
    onContentClick: (ContentItem) -> Unit,
    onPlayClick: (ContentItem) -> Unit,
    onPlatformClick: (StreamingPlatform) -> Unit,
    onWatchlistToggle: (ContentItem) -> Unit,
    isItemInWatchlist: (ContentItem) -> Boolean,
    continueWatchingList: List<Pair<ContentItem, PlaybackProgress>>,
    modifier: Modifier = Modifier
) {
    val liveCatalog by ElitePlexRepository.liveCatalogFlow.collectAsState()
    val heroItems = remember(liveCatalog) { ElitePlexRepository.getHeroItems() }
    val trendingMovies = remember(liveCatalog) { ElitePlexRepository.getTrendingMovies() }
    val cinemaMovies = remember(liveCatalog) { ElitePlexRepository.getTrendingCinema() }
    val bollywoodMovies = remember(liveCatalog) { ElitePlexRepository.getTrendingBollywood() }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF050505))
            .testTag("home_screen"),
        contentPadding = PaddingValues(top = 70.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        // Hero Featured Carousel
        item {
            HeroBanner(
                featuredItems = heroItems,
                onPlayClick = onPlayClick,
                onInfoClick = onContentClick,
                onWatchlistToggle = onWatchlistToggle,
                isItemInWatchlist = isItemInWatchlist
            )
        }

        // Streaming Platforms row (Netflix, Prime Video, JioHotstar, Lionsgate Play, etc.)
        item {
            PlatformRow(onPlatformClick = onPlatformClick)
        }

        // Continue Watching row
        if (continueWatchingList.isNotEmpty()) {
            item {
                ContinueWatchingRow(
                    continueWatchingList = continueWatchingList,
                    onItemClick = { item, _ -> onPlayClick(item) }
                )
            }
        }

        // Trending Movies Section (15 titles)
        item {
            ContentRowSection(
                title = "Trending Movies",
                countLabel = "${trendingMovies.size} titles",
                items = trendingMovies,
                onItemClick = onContentClick
            )
        }

        // Trending Movies - Cinema (15 titles)
        item {
            ContentRowSection(
                title = "Trending Movies - Cinema",
                countLabel = "${cinemaMovies.size} titles",
                items = cinemaMovies,
                onItemClick = onContentClick
            )
        }

        // Trending Movies - Bollywood (15 titles)
        item {
            ContentRowSection(
                title = "Trending Movies - Bollywood",
                countLabel = "${bollywoodMovies.size} titles",
                items = bollywoodMovies,
                onItemClick = onContentClick
            )
        }
    }
}

@Composable
fun ContentRowSection(
    title: String,
    countLabel: String,
    items: List<ContentItem>,
    onItemClick: (ContentItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = countLabel,
                color = Color(0xFFA0A0AA),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            items(items) { item ->
                PosterCard(
                    item = item,
                    onClick = { onItemClick(item) },
                    modifier = Modifier
                        .width(160.dp)
                        .height(245.dp)
                )
            }
        }
    }
}
