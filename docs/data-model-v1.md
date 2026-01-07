# Data Model (V1)

## Overview

### User
Represents an individual account.

- A User can have many **Calendars**
- A User can have many **Templates**

---

### Calendar
A container for Events, representing a domain of planning (e.g., Fitness, Personal, Work).

- Belongs to exactly one User  
- Contains many Events  
- Provides boundaries for:
  - Visibility toggling
  - Journal exports
  - Analytics (future V2)

---

### Event
Represents a planned or completed activity on a calendar. Supports hierarchy (sub-events) and journaling.

- Belongs to exactly one Calendar  
- May have a **parent Event** (self-referencing)

**Top-level Events:**
- Must have a date  
- Contain the `isCompleted` flag  

**Child Events:**
- Inherit calendar and date context  

**Each Event may contain:**
- One optional note (`noteText`)  
- Zero or more **Logs**

---

### Log
Represents structured data attached to an Event or Template.

- Belongs to exactly one Event or Template  
- Has a type: text, number, duration, table, checkbox/list  
- Contains:
  - Label (user-facing description)
  - Value(s)
  - Optional metadata (units, table columns, constraints)  

**Purpose:**
- Entry quantitative or qualitative details  
- Support journaling  
- Enable analytics and future visualization

---

### Template
Represents a reusable hierarchical structure for generating Events.

- Belongs to exactly one User  
- May have a **parent Template** (self-referencing)  
- Contains:
  - Title
  - Optional default Logs
  - Ordering of child Templates  

**Notes:**
- Templates do not have dates or completion state  
- Templates define structure only  
- Applying a Template generates fully detached Events  

---

### Notes
- Each Event may contain **one optional note**  
- Notes are stored directly on the Event as text  
- Notes are used for journaling and reflections  
- There is **no separate Note entity** in V1

---

### Hierarchical Model (Conceptual)
```
User
├── Calendar
│   └── Event
│       └── Event (child)
│           └── Log
│
└── Template
    └── Template (child)
        └── Log
```
- Events and Templates share the same recursive structure  
- Logs are attached at any level to provide structured detail

---

### Key Design Constraints

- Tasks/Events belong to exactly one Calendar  
- Hierarchy is modeled via self-referencing nodes  
- Granularity is optional and user-driven  
- Templates generate detached Event instances  
- Journals are derived views, not stored as separate entities  
- Logs provide structure without rigid schemas

---

### Non-Goals for V1

- Multiple notes per Event  
- Shared calendars or collaborative editing  
- Enforced schemas for analytics  
- Cross-calendar events
