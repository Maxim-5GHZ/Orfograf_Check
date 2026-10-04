# ErrorFreeText

Сервис исправления опечаток через Яндекс.Спеллер. Принимает текст, асинхронно правит через `checkTexts`, отдает исходный + исправленный текст.

Стек: Java 17, Spring Boot, Gradle, PostgreSQL 15, Docker Compose.

## Запуск

```bash
cp .env.example .env
docker compose up --build
```

App: `http://localhost:8080` (`APP_PORT` в `.env`)
Postgres: `localhost:5432` (`POSTGRES_PORT` в `.env`)

Локально без docker (нужен запущенный postgres):

```bash
./gradlew -p backend bootRun
```

Тесты:

```bash
./gradlew -p backend test
```

## OpenAPI

springdoc (`springdoc-openapi-starter-webmvc-ui:2.7.0`, конфиг — `OpenApiConfig`, название — `Error Free Text API v1.0`):

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- JSON-спека: `http://localhost:8080/v3/api-docs`

Контроллер помечен `@Tag(name = "tasks")`, методы — `@Operation` (`POST` — "Создать задачу на исправление текста", `GET` — "Получить задачу по id").

## API
Orfograf_Check / 
### 1. Создать задачу

`POST /api/v1/tasks` → `201` + `{ "id": "uuid" }`

Валидация:
- `text`: обязателен, минимум 3 символа, должен содержать буквы (не только спецсимволы/цифры)
- `language`: обязателен, `ru` или `en` (регистр не важен)

```bash
curl -X POST http://localhost:8080/api/v1/tasks \
  -H "Content-Type: application/json" \
  -d '{"text":"Превет мир","language":"ru"}'
# {"id":"..."}
```

Ошибки валидации → `400` (формат строго по ТЗ, `path` всегда `"tasks"`):
```json
{"errorMessage":"text: text must be at least 3 characters","errorCode":40001,"timestamp":"...","path":"tasks"}
```

### 2. Получить задачу

`GET /api/v1/tasks/{id}` → `200`, строго по ТЗ:
- `COMPLETED` — статус + скорректированный текст,
- `FAILED` — статус + описание ошибки,
- `NEW`/`PROCESSING` — только `id` + `status`.

Статусы: `NEW` → `PROCESSING` → `COMPLETED` / `FAILED`. Шедулер забирает `NEW` каждые 5 сек (`SCHEDULER_INTERVAL_MS`).

```bash
ID=<id из POST>
curl http://localhost:8080/api/v1/tasks/$ID
```

Ответ (`COMPLETED`):
```json
{
  "id": "...",
  "status": "COMPLETED",
  "language": "ru",
  "originalText": "Превет мир",
  "correctedText": "Привет мир",
  "createdAt": "...",
  "updatedAt": "..."
}
```

`PROCESSING` — только статус:
```json
{"id": "...", "status": "PROCESSING"}
```

`FAILED` — статус + ошибка:
```json
{"id": "...", "status": "FAILED", "errorMessage": "..."}
```

Не найдено → `404`:
```json
{"errorMessage":"Task with id: ... not found","errorCode":40401,"timestamp":"...","path":"tasks"}
```

Ещё примеры curl:

```bash
# английский
curl -X POST http://localhost:8080/api/v1/tasks \
  -H "Content-Type: application/json" \
  -d '{"text":"Helo world","language":"en"}'

# ошибка: короткий текст
curl -X POST http://localhost:8080/api/v1/tasks \
  -H "Content-Type: application/json" \
  -d '{"text":"аб","language":"ru"}'

# ошибка: только цифры
curl -X POST http://localhost:8080/api/v1/tasks \
  -H "Content-Type: application/json" \
  -d '{"text":"123!!","language":"ru"}'

# ошибка: неверный язык
curl -X POST http://localhost:8080/api/v1/tasks \
  -H "Content-Type: application/json" \
  -d '{"text":"обычный текст","language":"de"}'
```

## Как это работает

1. `POST` сохраняет задачу в статусе `NEW`.
2. `CorrectionScheduler` раз в `SCHEDULER_INTERVAL_MS` забирает до 5 `NEW` задач.
3. `TaskService.process` ставит `PROCESSING`, вызывает Яндекс.Спеллер, при успехе — `COMPLETED` + `correctedText`, при ошибке — `FAILED` + `errorMessage`.
4. `SpellerOptionsResolver` считает `options` строго по ТЗ: `FIND_REPEAT_WORDS` и `IGNORE_CAPITALIZATION` всегда выключены (base 0), плюс `IGNORE_DIGITS=2` если есть цифры, плюс `IGNORE_URLS=4` если есть URL.
5. `TextChunker` режет тексты > 10000 символов по пробелам, `CorrectionApplier` накатывает правки с конца строки.
