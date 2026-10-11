# ElitePlex Provider & API Audit Report

## 1. Provider Registry Overview
ElitePlex uses a unified, content-type-aware provider architecture across Movies, Web Series, and Anime. Every candidate provider has been audited for availability, capabilities, route schemas, and media stream resolution contracts.

| Provider Name | Base URL | Movies | Series | Anime | Subtitles | Verified Status |
| :--- | :--- | :---: | :---: | :---: | :---: | :--- |
| **ElitePlex API (Core)** | `https://eliteplex-api.vercel.app/api/` | Yes | Yes | Yes | Yes | **Primary Verified CDN** |
| **MovieBox** | `https://movie-box-api-v1.onrender.com/` | Yes | Yes | No | Yes | High-Bandwidth Backup |
| **TheMovieBox** | `https://themoviebox-api.vercel.app/api/` | Yes | Yes | No | Yes | Direct Media Resolver |
| **TMDB API** | `https://api.themoviedb.org/3/` | Yes | Yes | Yes | No | Metadata & Artwork Engine |
| **Stela Resolver** | `https://stela.raphs4.dev/` | Yes | Yes | Yes | Yes | Multi-Server Fallback |
| **Nova Edge** | `https://nova.nabilkelsen.workers.dev/` | Yes | Yes | Yes | No | Cloudflare Edge Relay |
| **FreakyAniki** | `https://freakyaniki.elaxo.lol/` | No | No | Yes | Yes | Dedicated Anime CDN |
| **Cnos** | `https://cnos.raphs4.dev` | Yes | Yes | No | No | Experimental Standby |
| **Flix2Watch** | `https://flix2watch.pro/` | Yes | Yes | No | No | Web Mirror |
| **Opui** | `https://opui.raphs4.dev/resolve?id=` | Yes | No | No | No | Single-Stream Resolver |
| **Vz** | `https://api.vz.raphs4.dev/resolve?id=` | Yes | Yes | No | No | Auxiliary Proxy |
| **Alexaluf** | `https://alexaluf.xyz/` | No | No | No | No | Unverified Offline |

---

## 2. Verified Endpoint Contracts & Parameter Schemas

### 2.1 ElitePlex API Core
- **Search**: `GET /api/search?q={query}&type={movie|series|anime}`
  - Response: Array of normalized content objects containing `id`, `title`, `poster`, `rating`, `year`, `type`.
- **Movie Details**: `GET /api/movie/{id}`
  - Response: Normalized movie metadata, runtime, backdrop, genres, direct stream sources.
- **Series Details & Seasons**: `GET /api/series/{id}`
  - Response: Series details with `totalSeasons`, `seasons` array.
- **Episode Listing**: `GET /api/series/{id}/season/{seasonNumber}`
  - Response: Array of episodes with `episodeNumber`, `title`, `duration`, `thumbnailUrl`, `streamId`.
- **Stream Resolution**: `GET /api/resolve?id={contentId}&ep={episodeId}&quality={1080p|720p}`
  - Response: Direct playable MP4/HLS media URL and subtitle array.

### 2.2 MovieBox Integration Contract
- **Movie Route**: `GET /movies/search?title={title}`
- **Series Route**: `GET /series/details?id={seriesId}`
- **Capabilities**: Full 1080p and 720p stream links for Western and Bollywood titles; Anime requests fall back to FreakyAniki or ElitePlex Core.

### 2.3 Anime Module Shared Architecture & Provider-Specific Capabilities

The Anime module integrates directly with the unified provider architecture, reusing common data models, stream resolution infrastructure, and local SQLite persistence while rigorously enforcing provider capability gates:

1. **FreakyAniki Anime CDN (`https://freakyaniki.elaxo.lol/`)**:
   - **Supported Media**: Anime Series, Simulcasts, Anime Films.
   - **Audio / Subtitle Tracks**: Japanese Original Audio, Dual Audio (English Dub, Hindi Dub where broadcast), Soft Subtitles (.vtt / .srt).
   - **Endpoints**:
     - `GET /anime/search?q={query}&studio={studio}&genre={genre}`
     - `GET /anime/series/{seriesId}`
     - `GET /anime/episodes/{seriesId}/{seasonNumber}`
     - `GET /anime/resolve/{seriesId}?ep={episodeNumber}&quality={1080p|720p}`
   - **Contract Enforcement**: Provider explicitly rejected for general Western movies or web series; active only when `item.type == ANIME_SERIES || item.type == ANIME_MOVIE`.

2. **ElitePlex Core CDN (`https://eliteplex-api.vercel.app/api/`)**:
   - **Supported Media**: Complete cross-type coverage (Movies, Web Series, Anime).
   - **Anime Capabilities**: High-speed direct MP4/HLS streams with multi-language subtitle tracks for top shonen (*Solo Leveling*, *Attack on Titan*, *Demon Slayer*, *Jujutsu Kaisen*).
   - **Contract**: Resolves anime episodes via `/api/resolve?id={contentId}&ep=s{season}e{ep}&quality=1080p`.

3. **MovieBox Global (`https://movie-box-api-v1.onrender.com/`)**:
   - **Anime Support**: **No dedicated anime support**.
   - **Fallback Gate**: Calls to MovieBox for anime content are intercepted at the repository layer and routed to FreakyAniki or ElitePlex Core, preventing dead-end network errors.

4. **Stela Resolver (`https://stela.raphs4.dev/`)**:
   - **Anime Support**: Yes (multi-server fallback proxy at 720p HD).
   - **Contract**: `GET /resolve?type=anime&id={contentId}&ep={episodeNumber}`.

5. **Nova Edge Cloudflare Worker (`https://nova.nabilkelsen.workers.dev/`)**:
   - **Anime Support**: Yes (Edge caching for static anime media assets and thumbnails).

---

## 3. Fallback Resolution Pipeline for Anime
When an anime episode or movie is launched:
1. **Capability Verification**: The provider registry checks whether the user's selected provider supports Anime.
2. **Primary Resolver**: Dispatches to `FreakyAniki Anime CDN (1080p Sub/Dub)` or `ElitePlex Fast CDN (1080p Multi-Audio)`.
3. **Episode-Specific Stream Resolution**: Uses `{seriesId}_s{seasonNumber}_e{episodeNumber}` to retrieve the authentic episode media stream rather than resolving the series title.
4. **Fallback Relay**: If the primary stream encounters network latency or buffering, the player dynamically switches to `Stela Anime Edge (720p HD)`.
5. **Progress & Identity Preservation**: Episode watch timestamps, duration, and completion states are stored independently in `ElitePlexStorage` SQLite database under the episode's composite key.
