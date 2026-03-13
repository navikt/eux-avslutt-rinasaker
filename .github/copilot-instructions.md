# Copilot Instructions — eux-avslutt-rinasaker

## Build & Test

Java 25 and Docker are required. Tests use Testcontainers with PostgreSQL 15 and Kafka (apache/kafka-native).

## Architecture

This is a Kotlin Spring Boot backend that automates closing RINA cases (rinasaker) in the EESSI system. It implements a **state machine** where each Rinasak progresses through statuses driven by scheduled NAIS jobs calling the API.

### State Machine Flow

```
NY_SAK → UVIRKSOM → TIL_AVSLUTNING_(LOKALT|GLOBALT) → AVSLUTTET_(LOKALT|GLOBALT) → TIL_ARKIVERING → ARKIVERT
```

Error states: `HANDLING_FEILET`, `HANDLING_MANGLER`, `AVSLUTTES_AV_MOTPART`, `OPPRETT_OPPGAVE`

### Core Components

- **Kafka listeners** (`kafka/listener/`) consume RINA case and document events, populating the database via `PopulerService`
- **Single REST endpoint** `POST /api/v1/prosesser/{prosess}/execute` dispatches to the appropriate service based on the process name (e.g., `sett-uvirksom`, `til-avslutning`, `avslutt`, `arkiver`)
- **Services** (`service/`) each handle one state transition. They iterate over BUC configurations in `Buc.kt` and process matching rinasaker
- **BUC rules** (`model/buc/Buc.kt`) define per-BUC-type configuration: inactivity thresholds, closing criteria, scope (local/global), and archiving delays. This is the business rules engine
- **HandlingService** wraps state transitions with error handling — on failure, the rinasak is moved to `HANDLING_FEILET` or `HANDLING_MANGLER`
- **EuxRinaTerminatorApiClient** (`integration/`) calls the external `eux-rina-terminator-api` to perform actual RINA operations (close, archive, delete drafts)

### Data Flow

1. Kafka events → `PopulerService` → `Rinasak`/`Dokument` entities in PostgreSQL
2. Scheduled NAIS jobs call the API endpoint for each process step
3. Services query by status + BUC type, evaluate BUC rules, transition status
4. External calls to `eux-rina-terminator-api` for RINA operations

## Key Conventions

### Kotlin Style

- **Extension functions on domain types** for business logic — services define functions like `fun Buc.avslutt()` and `fun Rinasak.tryAvsluttGlobalt()` creating a DSL-like pattern
- **Infix operators** for fluent API: `rinasak avsluttMed scope`, `1 er UVIRKSOM` (tests), `topic send event` (tests)
- **Data classes with `copy()`** for immutable state updates — never mutate entities, always `rinasak.copy(status = newStatus)`
- **`val log = logger {}`** using `io.github.oshai:kotlin-logging-jvm` for all logging
- **MDC context** via `no.nav.eux.logging.mdc(...)` and `clearLocalMdc()` for structured logging with `rinasakId`, `bucType`, `prosess`

### Project Structure

- `webapp/` — REST controller (single API endpoint)
- `service/` — one service per process step, all follow the same pattern
- `model/buc/` — BUC rule definitions (hardcoded config, not DB-driven)
- `model/entity/` — JPA entities (`Rinasak`, `Dokument`)
- `persistence/repository/` — Spring Data JPA repositories
- `kafka/` — Kafka config, listeners, and message model DTOs
- `integration/` — REST clients for external APIs using Spring `RestClient`

### Testing

- Single integration test class `AvsluttRinasakerTest` extends `AbstractTest`
- `AbstractTest` sets up Testcontainers (Postgres + Kafka), mock OAuth2, and provides helper methods
- Test datasets in `dataset/` package — each file defines Kafka messages and expected outcomes for a specific BUC scenario
- Mock external APIs via `MockWebServer` in `mock/` package
- Time manipulation helpers (`ManipulerTid.kt`) to simulate passage of days for inactivity thresholds

### Database

- PostgreSQL with Flyway migrations in `src/main/resources/db/migration/`
- Two tables: `rinasak` (case status tracking) and `dokument` (SED documents)
- Entities use `UUID` primary keys with `rinasakId: Int` as the RINA system identifier

### External Dependencies

- **eux-rina-terminator-api** — performs RINA operations (OAuth2 client credentials via Azure AD)
- **Kafka topics** — `eux-rina-case-events-v1`, `eux-rina-document-events-v1`
- Deployed on NAIS (GCP) in namespace `eessibasis`
