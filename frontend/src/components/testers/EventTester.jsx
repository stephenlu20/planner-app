import { useState } from "react";
import {
  createEvent,
  toggleEvent,
  reorderEvents,
  deleteEvent
} from "../../api/eventApi";

export default function EventTester() {
  const [note, setNote] = useState("");
  const [orderIndex, setOrderIndex] = useState(0);
  const [userId, setUserId] = useState("");
  const [calendarId, setCalendarId] = useState("");
  const [eventId, setEventId] = useState("");
  const [result, setResult] = useState(null);

  const handleCreate = async () => {
    const res = await createEvent(note, orderIndex, userId, calendarId);
    setResult(res);
  };

  const handleToggle = async () => {
    const res = await toggleEvent(eventId);
    setResult(res);
  };

  return (
    <div className="space-y-4">
      <h2 className="text-xl font-semibold">Event API</h2>

      <input
        className="border px-3 py-2 rounded w-96"
        placeholder="User ID"
        value={userId}
        onChange={e => setUserId(e.target.value)}
      />

      <input
        className="border px-3 py-2 rounded w-96"
        placeholder="Calendar ID"
        value={calendarId}
        onChange={e => setCalendarId(e.target.value)}
      />

      <input
        className="border px-3 py-2 rounded w-96"
        placeholder="Event note"
        value={note}
        onChange={e => setNote(e.target.value)}
      />

      <input
        type="number"
        className="border px-3 py-2 rounded w-32"
        value={orderIndex}
        onChange={e => setOrderIndex(Number(e.target.value))}
      />

      <input
        className="border px-3 py-2 rounded w-96"
        placeholder="Event ID (for toggle)"
        value={eventId}
        onChange={e => setEventId(e.target.value)}
      />

      <div className="flex gap-2">
        <button
          onClick={handleCreate}
          className="bg-indigo-500 text-white px-4 py-2 rounded"
        >
          Create Event
        </button>

        <button
          onClick={handleToggle}
          className="bg-green-600 text-white px-4 py-2 rounded"
        >
          Toggle Complete
        </button>
      </div>

      {result && (
        <pre className="bg-slate-100 p-4 rounded text-sm">
          {JSON.stringify(result, null, 2)}
        </pre>
      )}
    </div>
  );
}
