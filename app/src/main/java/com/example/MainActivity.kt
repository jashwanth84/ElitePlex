package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.data.local.ElitePlexStorage
import com.example.data.model.ContentItem
import com.example.data.model.Episode
import com.example.data.model.PlaybackProgress
import com.example.data.model.StreamingPlatform
import com.example.data.repository.ElitePlexRepository
import com.example.ui.components.FloatingBottomNav
import com.example.ui.components.FloatingHeader
import com.example.ui.components.NavDestination
import com.example.ui.screens.AnimeScreen
import com.example.ui.screens.BrowseMoviesScreen
import com.example.ui.screens.BrowseSeriesScreen
import com.example.ui.screens.DetailScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PlatformExplorerScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.VideoPlayerScreen
import com.example.ui.screens.WatchlistScreen
import com.example.ui.theme.ElitePlexTheme

sealed class ScreenState {
    data class MainTab(val destination: NavDestination) : ScreenState()
    data class Detail(val item: ContentItem) : ScreenState()
    data class PlatformExplorer(val platform: StreamingPlatform) : ScreenState()
    data class Player(val item: ContentItem, val episode: Episode? = null) : ScreenState()
    object Search : ScreenState()
    object Watchlist : ScreenState()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ElitePlexTheme {
                ElitePlexApp()
            }
        }
    }
}

@Composable
fun ElitePlexApp() {
    val context = LocalContext.current
    val storage = remember { ElitePlexStorage(context) }

    var screenStack by remember {
        mutableStateOf<List<ScreenState>>(listOf(ScreenState.MainTab(NavDestination.HOME)))
    }
    val currentScreen = screenStack.lastOrNull() ?: ScreenState.MainTab(NavDestination.HOME)

    // Reactive Watchlist and Continue Watching state
    var watchlistIds by remember { mutableStateOf(storage.getAllWatchlistIds()) }
    var continueWatchingList by remember {
        mutableStateOf(
            storage.getAllContinueWatching().mapNotNull { prog ->
                ElitePlexRepository.getContentById(prog.contentId)?.let { it to prog }
            }
        )
    }

    fun refreshStorageState() {
        watchlistIds = storage.getAllWatchlistIds()
        continueWatchingList = storage.getAllContinueWatching().mapNotNull { prog ->
            ElitePlexRepository.getContentById(prog.contentId)?.let { it to prog }
        }
    }

    LaunchedEffect(currentScreen) {
        refreshStorageState()
    }

    fun navigateTo(screen: ScreenState) {
        screenStack = screenStack + screen
    }

    fun navigateBack(): Boolean {
        return if (screenStack.size > 1) {
            screenStack = screenStack.dropLast(1)
            refreshStorageState()
            true
        } else {
            false
        }
    }

    fun switchTab(destination: NavDestination) {
        screenStack = listOf(ScreenState.MainTab(destination))
        refreshStorageState()
    }

    // Android System BackHandler
    BackHandler(enabled = screenStack.size > 1) {
        navigateBack()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050505))
    ) {
        // Main Screen Router
        when (currentScreen) {
            is ScreenState.MainTab -> {
                when (currentScreen.destination) {
                    NavDestination.HOME -> {
                        HomeScreen(
                            onContentClick = { navigateTo(ScreenState.Detail(it)) },
                            onPlayClick = { navigateTo(ScreenState.Player(it)) },
                            onPlatformClick = { navigateTo(ScreenState.PlatformExplorer(it)) },
                            onWatchlistToggle = { item ->
                                if (watchlistIds.contains(item.id)) {
                                    storage.removeFromWatchlist(item.id)
                                } else {
                                    storage.addToWatchlist(item.id)
                                }
                                refreshStorageState()
                            },
                            isItemInWatchlist = { watchlistIds.contains(it.id) },
                            continueWatchingList = continueWatchingList
                        )
                    }
                    NavDestination.MOVIES -> {
                        BrowseMoviesScreen(
                            onContentClick = { navigateTo(ScreenState.Detail(it)) }
                        )
                    }
                    NavDestination.SERIES -> {
                        BrowseSeriesScreen(
                            onContentClick = { navigateTo(ScreenState.Detail(it)) }
                        )
                    }
                    NavDestination.ANIME -> {
                        AnimeScreen(
                            onContentClick = { navigateTo(ScreenState.Detail(it)) },
                            onPlayClick = { navigateTo(ScreenState.Player(it)) },
                            onWatchlistToggle = { item ->
                                if (watchlistIds.contains(item.id)) {
                                    storage.removeFromWatchlist(item.id)
                                } else {
                                    storage.addToWatchlist(item.id)
                                }
                                refreshStorageState()
                            },
                            isItemInWatchlist = { watchlistIds.contains(it.id) }
                        )
                    }
                    NavDestination.SETTINGS -> {
                        SettingsScreen(
                            storage = storage,
                            watchlistCount = watchlistIds.size,
                            continueWatchingCount = continueWatchingList.size,
                            onOpenWatchlist = { navigateTo(ScreenState.Watchlist) },
                            onClearContinueWatching = {
                                storage.clearContinueWatching()
                                refreshStorageState()
                            }
                        )
                    }
                }

                // Show Floating Header on tabs except Settings
                if (currentScreen.destination != NavDestination.SETTINGS) {
                    FloatingHeader(
                        onSearchClick = { navigateTo(ScreenState.Search) },
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .statusBarsPadding()
                    )
                }

                // Show Floating Bottom Nav on all Main Tabs
                FloatingBottomNav(
                    currentDestination = currentScreen.destination,
                    onNavigate = { switchTab(it) },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }

            is ScreenState.Detail -> {
                DetailScreen(
                    item = currentScreen.item,
                    onBackClick = { navigateBack() },
                    onPlayClick = { item, ep -> navigateTo(ScreenState.Player(item, ep)) },
                    onWatchlistToggle = { item ->
                        if (watchlistIds.contains(item.id)) {
                            storage.removeFromWatchlist(item.id)
                        } else {
                            storage.addToWatchlist(item.id)
                        }
                        refreshStorageState()
                    },
                    isInWatchlist = watchlistIds.contains(currentScreen.item.id)
                )
            }

            is ScreenState.PlatformExplorer -> {
                PlatformExplorerScreen(
                    platform = currentScreen.platform,
                    onBackClick = { navigateBack() },
                    onContentClick = { navigateTo(ScreenState.Detail(it)) }
                )
            }

            is ScreenState.Player -> {
                VideoPlayerScreen(
                    item = currentScreen.item,
                    episode = currentScreen.episode,
                    onBackClick = { navigateBack() }
                )
            }

            is ScreenState.Search -> {
                SearchScreen(
                    onBackClick = { navigateBack() },
                    onContentClick = { navigateTo(ScreenState.Detail(it)) }
                )
            }

            is ScreenState.Watchlist -> {
                val savedItems = ElitePlexRepository.getAllContent().filter { watchlistIds.contains(it.id) }
                WatchlistScreen(
                    watchlistItems = savedItems,
                    onBackClick = { navigateBack() },
                    onContentClick = { navigateTo(ScreenState.Detail(it)) }
                )
            }
        }
    }
}
