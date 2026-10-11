# ElitePlex — Dark Cinematic Streaming Platform

ElitePlex is a premium dark cinematic streaming application for Android that provides unified multi-provider media discovery and playback for **Movies**, **Web Series**, and **Anime**.

---

## 📸 Visual Alignment with Reference Screenshots

ElitePlex has been engineered to faithfully reproduce the exact design language, component layout, and color palette shown in the reference screenshots:

1. **Floating Header**: Rounded pill floating bar with an "EP" play shield monogram, bold "ElitePlex" typography, and an instant search launcher.
2. **Cinematic Hero Carousel**: Dynamic full-bleed backdrop banners featuring "Top Rated" pill badges, title, audio/genre metadata, `[ ▶ Play ]`, bookmarking, info buttons, and pagination dots (*Screenshot 6 & 7*).
3. **Streaming Platforms**: Circular brand badges for **Netflix**, **Prime Video**, **JioHotstar**, **Lionsgate Play**, **Sony LIV**, and **ZEE5**. Tapping any platform opens a dedicated **Platform Explorer** with a segmented `[ Movies ]` / `[ Series ]` toggle pill (*Screenshots 1 & 2*).
4. **Browse Movies**: Responsive 2-column poster grid with star ratings (`★ 8.3`), play icons, and category badges (*Screenshot 3*).
5. **Browse TV Series**: 2-column grid of series like *Naruto: Shippuden*, *Money Heist*, *Solo Leveling*, and *Bigg Boss* (*Screenshot 4*).
6. **Continue Watching**: Persistent watch-progress row with red linear progress indicators on poster cards (*Screenshot 6*).
7. **Pill Floating Bottom Navigation**: Custom dark dock featuring active white pill indicators for **Home**, **Movie**, **Series**, **Anime**, and **Settings**.

---

## 🚀 Core Features

- **Unified Multi-Provider Architecture**:
  - One shared, content-type-aware provider registry supporting **ElitePlex Core CDN**, **MovieBox Global**, **TheMovieBox**, **Stela Resolver**, **Nova Edge**, and **FreakyAniki Anime**.
  - Intelligent automatic stream fallback from 1080p Ultra HD to 720p HD and 480p SD if a primary source encounters network issues.
- **Dedicated Anime Module**:
  - Features trending anime series, simulcasts, and award-winning anime films with independent season and episode stream resolution.
- **Series Seasons & Episodes**:
  - Accurate season tabs and episode lists with thumbnails, episode numbers, durations, and direct playback.
- **Native Video Player**:
  - Built with Android's native `VideoView` / `MediaPlayer`.
  - Supports 10-second fast-forward/rewind, scrubbable seek slider, live duration timestamps, subtitle track switching, stream quality switching, and fullscreen mode.
- **Local SQLite Persistence**:
  - **Watchlist**: Save movies, series, and anime with instant database updates.
  - **Continue Watching**: Automatically records millisecond playback positions and durations to continue right where you left off.
  - **Preferences**: Customizable subtitle font size and background opacity.
- **Instant Search**:
  - Real-time search matching across all media categories with quick filters.

---

## 🛠️ Technology Stack

- **UI & Layout**: Jetpack Compose (Material 3) with custom Edge-to-Edge window insets
- **Language**: Kotlin 2.2.10
- **Image Pipeline**: Coil Compose 2.7.0
- **Database & Storage**: Android Native `SQLiteOpenHelper` & `SharedPreferences`
- **Video Engine**: Android `VideoView` / `MediaPlayer`
- **Build System**: Gradle 9.x with Kotlin DSL (`build.gradle.kts`)

---

## 📂 Project Structure

```
app/src/main/java/com/example/
├── MainActivity.kt                # Root activity & navigation state controller
├── data/
│   ├── model/
│   │   └── Models.kt              # ContentItem, Season, Episode, StreamSource, SubtitleTrack
│   ├── local/
│   │   └── ElitePlexStorage.kt    # SQLiteOpenHelper & SharedPreferences persistence
│   └── repository/
│       └── ElitePlexRepository.kt # Provider registry, catalog, and stream resolver
└── ui/
    ├── theme/
    │   ├── Color.kt               # Dark cinematic color tokens (#050505, #E50914, #14141E)
    │   └── Theme.kt               # ElitePlexTheme Material 3 wrapper
    ├── components/
    │   ├── FloatingHeader.kt      # EP branded floating top bar
    │   ├── FloatingBottomNav.kt   # Pill bottom navigation dock
    │   ├── PosterCard.kt          # Rounded poster with star badge and play icon
    │   ├── HeroBanner.kt          # Large featured banner with carousel dots
    │   ├── PlatformRow.kt         # Circular streaming platform badges
    │   └── ContinueWatchingRow.kt # Progress-bar cards row
    └── screens/
        ├── HomeScreen.kt          # Hero, platforms, continue watching, trending rows
        ├── BrowseMoviesScreen.kt  # 2-column movie poster grid with genre chips
        ├── BrowseSeriesScreen.kt  # 2-column series poster grid
        ├── PlatformExplorerScreen.kt # Segmented Movies/Series toggle explorer
        ├── AnimeScreen.kt         # Complete Anime discovery and stream engine
        ├── DetailScreen.kt        # Synopsis, season tabs, episode list, providers
        ├── VideoPlayerScreen.kt   # VideoView player with seek, CC & quality settings
        ├── SearchScreen.kt        # Real-time search across all content
        ├── WatchlistScreen.kt     # Saved items grid from SQLite database
        └── SettingsScreen.kt      # Quality, subtitle styling & CDN registry status
```
