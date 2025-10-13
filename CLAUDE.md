# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

An Android app that tracks Steam game achievements using the Steam API. Displays achievement percentages per game, calculates average completion across all games, and counts fully completed ("maxed") games.

## Common Commands

### Building & Running
```bash
./gradlew app:build              # Build the app
./gradlew app:assembleDebug      # Build debug APK
./gradlew app:installDebug       # Install debug build on connected device
```

### Testing
```bash
./gradlew app:test               # Run all unit tests
./gradlew app:testDebugUnitTest  # Run debug unit tests only
./gradlew app:connectedDebugAndroidTest  # Run instrumented tests on device
```

### Code Quality
```bash
./gradlew app:lintKotlin         # Run ktlint checks
./gradlew app:formatKotlin       # Auto-format Kotlin code
./gradlew app:lint               # Run Android lint
```

### Running Specific Tests
```bash
./gradlew app:test --tests com.jc.steamachievementschecker.core.GetMyAchievementsUseCaseTest
```

## Architecture

The project follows **Clean Architecture** with clear separation of concerns:

### Layer Structure

**Presentation Layer** (`presentation/`)
- Jetpack Compose UI with MVVM pattern
- `AchievementsListViewModel`: Manages UI state (Loading/Success), fetches achievements, calculates statistics
- `AchievementsListScreen`: Main UI displaying games list with achievement percentages

**Domain/Core Layer** (`core/`)
- Business logic encapsulated in use cases (operator invoke pattern)
- Repository interfaces defined here
- Key use cases:
  - `GetMyAchievementsUseCase`: Returns cached data if available, otherwise fetches online
  - `ForceRefreshMyAchievementsUseCase`: Always fetches fresh data from Steam API
  - `FetchAchievementsOnlineUseCase`: Orchestrates fetching games and achievements, saves to DB
  - `SortGameInfoUseCase`: Sorts games by achievement percentage
  - `ComputeShortNameUseCase`: Generates short names for display

**Data Layer** (`data/`)
- `data/network/`: Steam API integration using Retrofit
  - `SteamApi`: API endpoints for games and achievements
  - `SteamAchievementsRepository`: Implements `AchievementsRepository`
  - `SteamSecrets`: Contains API_KEY and USER_ID (hardcoded for this project)
- `data/db/`: Room database for offline caching
  - `AppDatabase`: Single database with GameInfoDao
  - `RoomGameInfoRepository`: Implements `GameInfoRepository`
  - Uses `fallbackToDestructiveMigration(true)` for schema changes

### Dependency Injection

Uses **Koin** for DI. All modules configured in `di/KoinModule.kt`:
- `apiModule`: Retrofit + OkHttp setup with 10s timeout
- `daoModule`: Room database and DAO
- `coreModule`: Use cases and repository implementations
- `viewModelModule`: ViewModel instances

### Data Flow Pattern

1. ViewModel calls use case
2. Use case checks `GameInfoRepository.hasOfflineDataAvailable()`
3. If offline data exists → return from Room DB
4. If not → `FetchAchievementsOnlineUseCase` calls Steam API via `AchievementsRepository`
5. Results saved to Room DB for offline access
6. Data sorted and transformed to `GameInfoItem` for UI

### Key Models

- `Game`: Domain model from API (id, name)
- `GameInfo`: Domain model with achievement percentage (id, name, achievementsPercentage)
- `GameInfoItem`: Presentation model with shortName for UI
- `GameInfoDbEntity`: Room entity for persistence

## Testing

Tests use MockK for mocking. Test structure mirrors source structure:
- Unit tests in `app/src/test/`: Use cases, repositories, ViewModels
- `MainDispatcherRule`: Test rule for coroutines in ViewModelTest
- All use cases have corresponding test files

## Code Quality Standards

**IMPORTANT**: When making any code edits, always run the linter afterward to ensure code style compliance:

```bash
./gradlew app:lintKotlin       # Run ktlint checks
```

If linter fails, fix the issues before proceeding. You can also use:

```bash
./gradlew app:formatKotlin     # Auto-format Kotlin code
```

Claude must verify the linter passes after any code modifications.

## Steam API Integration

The app fetches data from two endpoints:
1. `IPlayerService/GetOwnedGames`: List of user's games
2. `ISteamUserStats/GetPlayerAchievements`: Achievement stats per game

API credentials in `SteamSecrets.kt` are hardcoded (not using BuildConfig or environment variables).
