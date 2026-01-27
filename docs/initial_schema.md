# Calendar Core – Database Schema (Phase 1)

This schema defines a **minimal, iCal-compatible calendar core**. It is designed to:
- Support RRULE-style recurrence
- Avoid storing expanded instances
- Allow per-occurrence overrides
- Remain extensible for journaling later

SQLite-friendly, but portable to Postgres/MySQL.

---

## 1. calendars

Represents a logical calendar (personal, work, shared later).

```sql
calendars (
  id              TEXT PRIMARY KEY,        -- UUID
  name            TEXT NOT NULL,
  description     TEXT,
  color           TEXT,
  timezone        TEXT NOT NULL,            -- IANA TZ (e.g. America/New_York)
  created_at      TEXT NOT NULL,
  updated_at      TEXT NOT NULL
)
```

Notes:
- Even with one calendar initially, **model this now**.
- Timezone here is a default, not authoritative per event.

---

## 2. events

Defines an event **rule**, not individual occurrences.

```sql
events (
  id              TEXT PRIMARY KEY,        -- UUID
  calendar_id     TEXT NOT NULL,
  title           TEXT NOT NULL,
  description     TEXT,
  start_time      TEXT NOT NULL,            -- ISO-8601 ZonedDateTime
  end_time        TEXT NOT NULL,
  all_day         INTEGER NOT NULL,         -- 0/1
  timezone        TEXT NOT NULL,            -- IANA TZ
  status          TEXT NOT NULL,            -- CONFIRMED | CANCELLED

  created_at      TEXT NOT NULL,
  updated_at      TEXT NOT NULL,

  FOREIGN KEY (calendar_id) REFERENCES calendars(id)
)
```

Rules:
- `start_time` is the **anchor** for recurrence
- Never store expanded instances here

---

## 3. recurrence_rules

Structured representation of RRULE (not stored as string).

```sql
recurrence_rules (
  id              TEXT PRIMARY KEY,        -- UUID
  event_id        TEXT NOT NULL UNIQUE,

  frequency       TEXT NOT NULL,            -- DAILY | WEEKLY | MONTHLY
  interval        INTEGER NOT NULL DEFAULT 1,

  count           INTEGER,                  -- optional
  until_time      TEXT,                     -- optional ISO-8601

  created_at      TEXT NOT NULL,
  updated_at      TEXT NOT NULL,

  FOREIGN KEY (event_id) REFERENCES events(id)
)
```

Notes:
- One rule per event (Phase 1)
- BYDAY / BYMONTHDAY come later

---

## 4. recurrence_by_day (optional but clean)

Supports weekly patterns (e.g. MO, WE).

```sql
recurrence_by_day (
  recurrence_rule_id  TEXT NOT NULL,
  day_of_week         TEXT NOT NULL,        -- MO, TU, WE...

  PRIMARY KEY (recurrence_rule_id, day_of_week),
  FOREIGN KEY (recurrence_rule_id) REFERENCES recurrence_rules(id)
)
```

You may skip this table initially if you only support single-day weekly recurrences.

---

## 5. recurrence_exceptions

Defines skipped occurrences (EXDATE in iCal).

```sql
recurrence_exceptions (
  id              TEXT PRIMARY KEY,        -- UUID
  event_id        TEXT NOT NULL,
  occurrence_time TEXT NOT NULL,            -- ISO-8601 ZonedDateTime

  created_at      TEXT NOT NULL,

  FOREIGN KEY (event_id) REFERENCES events(id)
)
```

Notes:
- This represents "this occurrence does not happen"
- Do not delete base event

---

## 6. recurrence_overrides

Defines modified single occurrences (RECURRENCE-ID behavior).

```sql
recurrence_overrides (
  id                  TEXT PRIMARY KEY,    -- UUID
  event_id             TEXT NOT NULL,
  original_time        TEXT NOT NULL,        -- RECURRENCE-ID

  override_start_time  TEXT NOT NULL,
  override_end_time    TEXT NOT NULL,
  title                TEXT,
  description          TEXT,

  created_at           TEXT NOT NULL,
  updated_at           TEXT NOT NULL,

  FOREIGN KEY (event_id) REFERENCES events(id)
)
```

Rules:
- Overrides replace a **single derived instance**
- They do NOT change the recurrence rule

---

## 7. Derived Concept (Not Stored): Occurrence

Occurrences are computed at query time.

Conceptual shape:
```text
Occurrence {
  eventId
  startTime
  endTime
  isOverride
  isException
}
```

This is what the calendar UI renders and what journals will reference.

---

## 8. Journaling (Planned, Not Implemented Yet)

Future tables will reference:
- event_id
- occurrence_time

Never the recurrence rule itself.

---

## Core Guarantees This Schema Provides

- iCal RRULE compatibility
- Clean import/export path
- Stable per-occurrence identity
- No recurrence explosion
- Journaling-friendly

---

## Non-Goals (Phase 1)

Explicitly not included yet:
- Attendees
- Notifications
- Sharing
- Multiple calendars per user
- External sync metadata

---

## One Rule to Remember

> Events define rules. Occurrences are projections. Overrides are exceptions.

If this rule holds, everything else stays sane.

