# articles-fetcher

Spring Boot application that periodically fetches articles from a public REST API,
stores them in its own PostgreSQL database without duplicates, and exposes endpoints
to read and mark them as read.

Built as a practice implementation of a recruitment task.

## Tech stack

- Java 25
- Spring Boot 4.1.1 (Web, Data JPA, RestClient)
- PostgreSQL 18 (Docker / Podman Compose)
- Maven

## Architecture

```
com.dch.articlesfetcher
├── client/          external API contract
│   ├── PostResponse         DTO matching the external API
│   ├── PostClient           interface (what we fetch)
│   └── RestClientPostClient implementation (how we fetch)
├── article/         domain
│   ├── Article              JPA entity, unique external_id
│   ├── ArticleRepository    existing-ids lookup, unread lookup
│   ├── ArticleMapper        PostResponse -> Article
│   ├── ArticleDto           API response record
│   ├── ArticleDtoMapper     Article -> ArticleDto
│   ├── ArticleService       fetch/save/read logic
│   └── ArticleNotFoundException
├── scheduler/
│   └── ArticleFetchScheduler  fixedDelay job, delegates to service
├── config/
│   ├── PostClientProperties   articles.client.*
│   ├── UnreadArticlesProperties articles.unread.*
│   ├── RestClientConfiguration  RestClient bean (Boot builder)
│   └── ClockConfiguration       UTC clock bean
└── web/
    └── GlobalExceptionHandler   RestControllerAdvice + ProblemDetail
```

Key boundaries:

- External API contract (`client`) is separated from the domain (`article`) —
  a change in the external API touches only the client package.
- The scheduler contains no business logic; it only decides *when*, the service decides *what*.
- Controllers never expose entities; responses go through `ArticleDto`.

## How duplicates are prevented

1. `articles.external_id` has a **unique constraint** in the database — the last line
   of defense, independent of application code.
2. Before saving, the service runs **one query** for already-existing external ids
   (`findExistingExternalIds`) and filters them out in memory — avoids the N+1 pattern
   of checking each post individually.

Verified behavior: second run logs `Saved 0 new articles (100 fetched, 100 already existed)`.

## Scheduler

- `@Scheduled(fixedDelay)` — the next run is measured from the **end** of the previous one,
  so runs can never overlap (single instance).
- Interval and initial delay come from configuration, not code.
- The job body is wrapped in try/catch — an exception is logged and does not
  affect subsequent runs.
- Known limitation: with multiple application instances each has its own scheduler;
  that would require ShedLock or a database lock.

## REST API

| Method | Path | Description | Response |
|---|---|---|---|
| GET | `/api/articles/unread` | Up to N unread articles, oldest first | `200`, list of `ArticleDto` |
| POST | `/api/articles/{id}/read` | Mark article as read | `204` |
| POST | `/api/articles/{id}/read` (unknown id) | — | `404`, `application/problem+json` |

Errors follow RFC 9457 (Problem Details), produced by `GlobalExceptionHandler`.

## Configuration

`src/main/resources/application.yaml`:

```yaml
spring:
  http:
    clients:
      connect-timeout: 3s
      read-timeout: 5s
  datasource:
    url: jdbc:postgresql://localhost:5433/articles
    username: articles
    password: articles
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

articles:
  client:
    base-url: https://jsonplaceholder.typicode.com
  fetch:
    initial-delay: 10s
    delay: 5m
  unread:
    limit: 5
```

Notes:

- `spring.http.clients.*` is the Boot 4 property namespace (the Boot 3 `spring.http.client.*`
  keys are deprecated).
- `ddl-auto: update` is acceptable for a practice project; production would use
  Flyway or Liquibase migrations.

## Running locally

1. Start PostgreSQL:

   ```bash
   docker compose up -d
   ```

   (Postgres 18 image mounts the data volume at `/var/lib/postgresql` —
   the pre-18 `/var/lib/postgresql/data` path no longer works.)

2. Run the application (IntelliJ or `./mvnw spring-boot:run`).

3. After ~10 s the scheduler fetches and stores articles. Then:

   ```bash
   curl http://localhost:8080/api/articles/unread
   curl -X POST http://localhost:8080/api/articles/1/read
   ```

## Design decisions worth defending in an interview

- **Two DTOs, two mappers** — `PostResponse` (their contract) vs `ArticleDto` (ours);
  each mapping is a dedicated `@Component` for consistency and testability. Manual
  mapping chosen over MapStruct for full control.
- **`Clock` as a bean** — `Instant.now(clock)` instead of `Instant.now()` makes
  time-dependent logic deterministic in tests (`Clock.fixed`).
- **`readAt` as `Instant`, not `boolean`** — same cost, more information
  (`null` = unread, value = when it was read).
- **No explicit `save()` in `markAsRead`** — the entity is managed inside the
  transaction; Hibernate dirty checking generates the UPDATE.
- **`Limit.of(n)` parameter** (Spring Data 3.2+) instead of `Top5` in the method
  name — the limit lives in configuration, not in an identifier.
- **`@Transactional(readOnly = true)`** on queries — skips dirty checking,
  declares intent.
- **UTC everywhere in storage** — timezone is a presentation concern.

## Known follow-ups

- Tests: `@DataJpaTest` with Testcontainers, service test with a stubbed `PostClient`.
- `spring.jpa.open-in-view: false` (currently on by default, warns at startup).
- Hibernate JDBC batching for the initial 100 inserts.
- Pagination of the external API (JSONPlaceholder returns a fixed set of 100 posts,
  so it is not needed here).