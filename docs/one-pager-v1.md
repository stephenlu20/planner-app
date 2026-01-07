# One-Pager (V1)

## Product Vision
Create a personal planning system that combines calendars, events, structured tracking, and journaling into a single coherent experience. The app allows users to plan activities, capture structured data while doing them, and automatically generate meaningful journal entries from real actions.

The goal is to reduce friction between planning, doing, and reflecting.

---

## Core Problems This Solves
- Traditional planners lack depth for complex activities (e.g., workouts, routines)  
- Habit and tracking apps collect data but lack context and narrative  
- Journaling apps require separate effort and are often disconnected from real activity  
- Existing calendar apps treat events as shallow, disposable objects  

---

## Key Concepts

### Calendars (Primary Organizers)
- Users can create multiple physically separate calendars (e.g., Work, Fitness, Personal)  
- Each event belongs to exactly one calendar  
- Calendars can be toggled on/off in views  
- Calendars act as semantic ownership, not just filters  

### Events (Dated Instances)
- Events live on a specific day within a calendar  
- Each event has:
  - A title  
  - An optional note  
  - An `isCompleted` flag (manual user toggle)  
  - Optional structured fields  
  - Optional sub-tasks  
- Events represent something the user intended to do or actually did  

### Structured Fields (Fixed Types)
- Events and sub-tasks may contain structured fields selected by the user:
  - Text  
  - Number  
  - Checkbox / list  
  - Table (e.g., sets, intervals)  
- Fields are predefined for usability and consistency but composable per task  

### Notes & Journaling
- Notes can exist at:
  - Event level  
  - Sub-task level  
- Notes are optional and tied to context (what the user actually did)  
- Users can:
  - Select a calendar  
  - Export or view all notes as a chronological journal  
- Each journal entry includes:
  - Date  
  - Event / sub-task label  
  - Calendar context  
  - Durations  
  - Tables (sets, reps, weights)  
  - Distances, counts, etc.  
- Journaling is a derived view, not a separate workflow  
- Excluded from journal:
  - Internal metadata  
  - Field IDs  
  - Technical labels  
  - Validation rules  
  - Empty / null fields  
- The journal reads like a clean activity log, not a database dump  

### Templates
- Users can create templates representing reusable event structures  
- Templates contain:
  - Event title  
  - Sub-tasks  
  - Fields  
  - Default notes (optional)  
- Templates are not tied to dates  
- Users can apply repetition rules (daily, weekly, monthly)  
- Generated events are fully detached instances  
- Editing a template never alters past events  

---

## Guiding Principles
- **Context over abstraction** – data and notes tied to real activity  
- **Flexibility without chaos** – fixed field types, user-chosen structure  
- **History is immutable** – past events represent what actually happened  
- **Granularity is optional** – simple events stay simple; complex events can be deep  

---
## Stretch Goal Feature (V1.5)

### Sub-Task (Optional Granularity)
- Events can contain sub-tasks for deeper structure  
- Sub-tasks can have:
  - Their own notes  
  - Their own structured fields  
- Common uses:
  - Workout exercises  
  - Multi-step routines  
  - Component activities  
- Granularity is opt-in, not required  

---

## Non-Goals (V1)
- Shared calendars or collaboration  
- Social or community features  
- Cross-user templates  
- Advanced analytics or AI insights  
- "True" nested sub-tasks

---

## Success Criteria
The product succeeds if:
- Users can plan complex routines without friction  
- Journaling happens naturally as a byproduct of action  
- Users feel the system adapts to their life instead of forcing a workflow
