# Specification Sheet (V1)

## Product Name

*TBD* (e.g. Chronicle, Entrybook, Ledger)

---

## Product Description

A multi-calendar planner and habit tracking application that combines **structured logging**, **hierarchical events**, and **journaling**.

The application enables users to plan activities, entry meaningful details, and reflect on progress over time without enforcing rigid schemas. Structure is optional, user-defined, and transparent.

---

## Core Goals (V1)

- Support multiple independent calendars
- Allow flexible task granularity (events and sub-events)
- Combine structured data with free-form notes
- Enable journal-style review and export
- Keep the system simple, predictable, and user-driven

---

## Core Concepts

### Calendar

A domain-specific container (e.g. Fitness, Personal, Work) that holds Events.

Calendars act as boundaries for:

- Visibility toggling
- Journal exports
- Future analytics

---

### Event

A planned or completed activity on a calendar.

- Events may be hierarchical (parent/child)
- Top-level Events:
    - Have a date
    - Can be marked complete (`isCompleted`)
- Sub-events inherit date and calendar context from their parent
- Each Event may contain:
    - One optional note
    - Zero or more Entries

---

### Entry

A structured piece of entryed data associated with an Event or Template.

Examples include:

- Duration
- Numeric value
- Checkbox/list
- Table (e.g. workout sets)

Entries are:

- User-labeled
- Ordered
- Typed (fixed set of supported types)

Entries are used for journaling detail and future analytics.

---

### Template

A reusable, hierarchical structure for generating Events.

- Templates belong to a User
- Templates may be hierarchical (self-referencing)
- Templates define structure only:
    - No dates
    - No completion state
- Applying a Template generates fully detached Events

Templates represent intent, not history.

---

## Key Features (V1)

### 1. Multiple Calendars

- Users can create and manage multiple calendars
- Calendars can be toggled on/off in views
- Events belong to exactly one calendar

---

### 2. Events & Sub-Events

- Events may contain child Events
- Granularity is optional and user-defined
- Simple tasks remain simple
- Complex activities (e.g. workouts) may be deeply structured

---

### 3. Entries (Structured Data)

Supported Entry types (V1):

- Text
- Number
- Duration
- Checkbox/List
- Table

Entries:

- Are optional
- Are user-defined
- Provide structure without rigid schemas

---

### 4. Notes & Journaling

- Each Event may contain **one optional note**
- Notes are free-form text
- Journals are **derived views**, not stored entities

Journal views include:

- Event titles
- Notes
- Relevant Entry values (e.g. durations, tables)

---

### 5. Templates

- Users can create Templates
- Templates mirror Event structure
- Applying a Template:
    - Creates Events on the calendar
    - Copies structure and default Entries
- Generated Events are not linked back to the Template

---

## Data Model Summary

```
User
├── Calendar
│   └── Event
│       └── Event (child)
│           └── Entry
│
└── Template
    └── Template (child)
        └── Entry
```

A **User** owns all data in the system. Each User may create multiple **Calendars**, which represent distinct planning domains such as fitness, personal life, or work.

A **Calendar** contains many **Events**. Each Event belongs to exactly one Calendar and represents a planned or completed activity on a specific date.

Events may be **hierarchical**. A top-level Event represents a primary activity and may contain child Events (sub-events) that provide additional structure or detail. Only top-level Events are directly associated with a date and completion state; sub-events inherit this context from their parent.

Each Event may contain:

- One optional free-form note, used for journaling or reflection
- Zero or more **Entries**, which store structured data such as durations, numeric values, checkbox lists, or tables

Entries represent entryed details that matter to the user and may later be used for analytics or visualization. Entries are optional and user-defined.

In addition to Calendars and Events, a User may create **Templates**. Templates define reusable, hierarchical structures that mirror the shape of Events but do not exist on the calendar timeline.

Templates:

- May contain child Templates
- May contain default Entries
- Do not have dates or completion state

When a Template is applied, it generates one or more fully detached Events on a Calendar. Once created, Events do not maintain a relationship to the Template that generated them.

This model allows users to plan simple tasks or highly detailed activities using the same underlying structure, while keeping the system flexible, readable, and future-proof.

---

## Design Constraints

- Events belong to exactly one Calendar
- Hierarchy is modeled via self-referencing entities
- Granularity is optional
- Templates generate detached instances
- Journals are projections, not stored data
- Entries do not enforce global schemas

---

## Non-Goals (V1)

- Collaboration or shared calendars
- Multiple notes per Event
- Enforced schemas for analytics
- Automatic interpretation of inconsistent data
- Social or gamification features

---

## UX Principles

- User-defined meaning over enforced structure
- Transparency in how data is rendered and visualized
- Optional complexity
- Journals should be readable, factual, and low-noise

---

## Future Considerations (Post-V1)

- Visual analytics (graphs per calendar)
- User-selected data sources for graphs
- Soft consistency hints (not enforcement)
- Export formats (PDF, Markdown)
- Template versioning or bulk updates

---

## Success Criteria (V1)

- Users can plan both simple and complex activities without friction
- Journals feel useful and reflective, not overwhelming
- Data entered in V1 supports analytics in later versions
- The system is understandable without diagrams

---

## Summary

This application is designed as a **truthful entry of intention and action**, balancing planning, execution, and reflection without forcing users into rigid patterns
- Template versioning or bulk updates

---
