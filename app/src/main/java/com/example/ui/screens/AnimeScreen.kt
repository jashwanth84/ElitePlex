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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ContentItem
import com.example.data.model.ContentType
import com.example.data.repository.ElitePlexRepository
import com.example.ui.components.HeroBanner
import com.example.ui.components.PosterCard
import com.example.ui.theme.CinemaRed

@Composable
fun AnimeScreen(
    onContentClick: (ContentItem) -> Unit,
    onPlayClick: (ContentItem) -> Unit,
    onWatchlistToggle: (ContentItem) -> Unit,
    isItemInWatchlist: (ContentItem) -> Boolean,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    val filterTabs = listOf("All", "Series", "Movies", "Simulcast", "Top Rated")
    var selectedFilter by remember { mutableStateOf("All") }

    val studios = listOf("All", "MAPPA", "Ufotable", "Madhouse", "Studio Pierrot", "CoMix Wave Films", "Kyoto Animation")
    var selectedStudio by remember { mutableStateOf("All") }

    val animeHeroItems = ElitePlexRepository.getAnimeHeroItems()
    val animeProviders = ElitePlexRepository.getAnimeProviders()

    // Dynamic search and filter
    val displayedAnime = remember(searchQuery, selectedFilter, selectedStudio) {
        if (searchQuery.isNotBlank()) {
            ElitePlexRepository.searchAnime(searchQuery, if (selectedFilter == "All") null else selectedFilter, if (selectedStudio == "All") null else selectedStudio)
        } else {
            val base = when (selectedFilter) {
                "Movies" -> ElitePlexRepository.getAnimeMovies()
                "Series" -> ElitePlexRepository.getAnimeSeries()
                "Simulcast" -> ElitePlexRepository.getSimulcastAnime()
                "Top Rated" -> ElitePlexRepository.getAnimeHeroItems()
                else -> ElitePlexRepository.getTrendingAnime()
            }
            if (selectedStudio == "All") base else base.filter { it.studio.equals(selectedStudio, ignoreCase = true) }
        }
    }

    val simulcastAnime = ElitePlexRepository.getSimulcastAnime()
    val shonenAnime = ElitePlexRepository.getPopularShonen()
    val animeMovies = ElitePlexRepository.getAnimeMovies()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF050505))
            .testTag("anime_screen"),
        contentPadding = PaddingValues(top = 70.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Section Title & Provider Sync Status Badge
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = CinemaRed,
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = "ElitePlex Anime",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(CinemaRed)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${animeProviders.size} PROVIDERS SYNCED",
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Shared provider engine for Simulcasts, Japanese Audio & Hindi/English Dubs",
                    color = Color(0xFFA0A0AA),
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Active Anime Providers Chip Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Dns,
                        contentDescription = "Providers",
                        tint = Color(0xFF707080),
                        modifier = Modifier.size(14.dp)
                    )
                    animeProviders.forEach { prov ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF161622))
                                .border(0.5.dp, Color(0xFF2C2C3E), RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = prov.name.substringBefore(" "),
                                color = Color(0xFFB0B0C0),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // Anime Quick Search Input Bar
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(46.dp)
                    .clip(RoundedCornerShape(23.dp))
                    .background(Color(0xFF14141E))
                    .border(1.dp, Color(0xFF282838), RoundedCornerShape(23.dp))
                    .padding(horizontal = 14.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Color(0xFFA0A0B0),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        textStyle = TextStyle(
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        cursorBrush = SolidColor(CinemaRed),
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        decorationBox = { innerTextField ->
                            if (searchQuery.isEmpty()) {
                                Text(
                                    text = "Search Anime (e.g. Solo Leveling, Ufotable, Titans)...",
                                    color = Color(0xFF707080),
                                    fontSize = 13.sp
                                )
                            }
                            innerTextField()
                        }
                    )
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { searchQuery = "" },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = Color(0xFFA0A0B0),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        // Featured Anime Carousel
        if (searchQuery.isBlank()) {
            item {
                HeroBanner(
                    featuredItems = animeHeroItems,
                    onPlayClick = onPlayClick,
                    onInfoClick = onContentClick,
                    onWatchlistToggle = onWatchlistToggle,
                    isItemInWatchlist = isItemInWatchlist
                )
            }
        }

        // Filter Pills: All | Series | Movies | Simulcast | Top Rated
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filterTabs) { tab ->
                        val isSelected = tab == selectedFilter
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) Color.White else Color(0xFF1B1B26))
                                .clickable { selectedFilter = tab }
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = tab,
                                color = if (isSelected) Color.Black else Color(0xFFA0A0B0),
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }

                // Studio Filter Row
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(studios) { studio ->
                        val isSelected = studio == selectedStudio
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) CinemaRed else Color(0xFF13131D))
                                .border(0.5.dp, if (isSelected) CinemaRed else Color(0xFF282836), RoundedCornerShape(12.dp))
                                .clickable { selectedStudio = studio }
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = studio,
                                color = if (isSelected) Color.White else Color(0xFF888898),
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }

        // Filtered Results Header & Row
        item {
            ContentRowSection(
                title = if (searchQuery.isNotBlank()) "Anime Search Results" else "Anime Catalog (${selectedFilter})",
                countLabel = "${displayedAnime.size} titles",
                items = displayedAnime,
                onItemClick = onContentClick
            )
        }

        // Airing Simulcast Section
        if (searchQuery.isBlank() && selectedFilter == "All") {
            item {
                ContentRowSection(
                    title = "⚡ Weekly Simulcasts & Airing",
                    countLabel = "${simulcastAnime.size} series",
                    items = simulcastAnime,
                    onItemClick = onContentClick
                )
            }

            // Popular Shonen & Action Row
            item {
                ContentRowSection(
                    title = "🔥 Popular Action & Shonen",
                    countLabel = "${shonenAnime.size} titles",
                    items = shonenAnime,
                    onItemClick = onContentClick
                )
            }

            // Anime Movies Row
            item {
                ContentRowSection(
                    title = "🎬 Cinematic Anime Masterpieces",
                    countLabel = "${animeMovies.size} films",
                    items = animeMovies,
                    onItemClick = onContentClick
                )
            }
        }
    }
}
