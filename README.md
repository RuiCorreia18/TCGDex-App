# TCGDex Card Browser

An Android application that browses Pokémon TCG cards using the [TCGDex API](https://tcgdex.dev/).

---

## Features

- Browse all Pokémon TCG cards
- Search cards by name
- View detailed card information including attacks, abilities, weaknesses, and stats

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose + Material3 |
| Architecture | MVVM + Clean Architecture |
| Dependency Injection | Hilt |
| Networking | Retrofit + kotlinx.serialization |
| Image Loading | Coil |
| Navigation | Navigation Compose |
| Async | Coroutines + StateFlow |

---

## Architecture

The project follows Clean Architecture with three layers:

```
presentation/   → Composables, ViewModels, UiState
domain/         → UseCases, Repository interfaces, Domain models
data/           → Repository implementation, RemoteDataSource, DTOs, Mappers
```

**Key decisions:**

- `RemoteDataSource` interface decouples the repository from Retrofit — swapping the HTTP client only requires a new `RemoteDataSourceImpl` without touching business logic
- `SearchCardsUseCase` runs card filtering on `Dispatchers.Default` since the API can return a large number of cards, making this a CPU-bound operation that should not run on the Main dispatcher
- Only cards with an image URL are displayed — filtered in the UseCase as a business logic decision
- `CancellationException` is always re-thrown in coroutine catch blocks to preserve structured concurrency
- `collectAsStateWithLifecycle` is used instead of `collectAsState` to respect the Android lifecycle and avoid unnecessary work in the background
- New search requests cancel the previous in-flight request via `searchJob?.cancel()` to prevent race conditions

---

## Project Structure

```
app/
└── src/main/java/com/example/tcgdex_app/
    ├── data/
    │   ├── remote/
    │   │   ├── api/          → TCGDexApiService (Retrofit)
    │   │   ├── datasource/   → RemoteDataSource interface + impl
    │   │   └── dto/          → Data Transfer Objects
    │   ├── repository/       → TCGDexRepositoryImpl
    │   └── Mappers.kt        → DTO → Domain model mapping
    ├── domain/
    │   ├── model/            → Card, CardDetails, Attack, Ability, Weakness
    │   ├── usecase/          → SearchCardsUseCase, GetCardDetailsUseCase
    │   └── TCGDexRepository  → Repository interface
    ├── presentation/
    │   ├── search/           → SearchCardsScreen, SearchCardsViewModel
    │   ├── details/          → CardDetailsScreen, CardDetailsViewModel
    │   └── AppNavGraph.kt    → Navigation graph
    └── di/
        ├── AppModule.kt      → Hilt bindings
        └── NetworkModule.kt  → Retrofit + OkHttp setup
```

---

## Setup

1. Clone the repository
2. Open in Android Studio
3. Run on an emulator or physical device (minSdk 24)

No API key required — the TCGDex API is open and free.

---

## Testing

Unit tests cover the classes with real business logic:

- `SearchCardsViewModelTest` — state transitions, error handling, search cancellation
- `CardDetailsViewModelTest` — loading, error, retry flow
- `SearchCardsUseCaseTest` — image filtering, dispatcher injection
- `MappersTest` — DTO to domain model mapping, null safety, edge cases

Tests use MockK for mocking and Turbine for Flow assertions.

```bash
./gradlew test
```