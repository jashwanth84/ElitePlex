# ElitePlex Build Limitations & Toolchain Rationale

## 1. Toolchain & Environment Specifications
- **Build System**: Gradle 9.x with Kotlin DSL (`build.gradle.kts`)
- **Android Gradle Plugin (AGP)**: 9.1.1
- **Kotlin Version**: 2.2.10
- **Android SDK Targets**: `minSdk = 24`, `compileSdk = 36`, `targetSdk = 36`
- **JDK Target Compatibility**: Java 11 / JVM Bytecode 11
- **Platform Execution Environment**: AI Studio Cloud Android Container (No direct ADB access, Streaming Web Emulator output)

## 2. Technology & Framework Alignment
- **UI Framework (Jetpack Compose & Material 3)**:
  - While legacy prompts may reference Java/XML, the AI Studio execution container is standardized on AGP 9.1.1 with Kotlin 2.2.10 and Jetpack Compose.
  - Reconstructing the high-fidelity dark cinematic UI seen in the supplied screenshots (rounded floating header with blur/border, hero carousel with pagination dots, circular streaming badges, and custom pill-style floating bottom dock) was achieved using Jetpack Compose components.
- **Local Persistence Layer**:
  - Implemented using Android's native `SQLiteOpenHelper` and `SharedPreferences` in `ElitePlexStorage.kt`.
  - Zero heavy third-party database dependencies required. Data persists across app restarts without cloud lock-in.
- **Media Playback Engine**:
  - Leverages Android's native `VideoView` and `MediaPlayer` embedded inside an `AndroidView`.
  - Guarantees compatibility with standard hardware media decoders (MP4, HLS, WebM) on minSdk 24+ without external binary codecs.
- **Image Pipeline**:
  - Uses `Coil` (`io.coil-kt:coil-compose:2.7.0`) for asynchronous memory/disk poster caching, crossfade animations, and placeholder fallbacks.
