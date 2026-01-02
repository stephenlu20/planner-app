import { useState } from "react";
import {
  createCalendar,
  getCalendarsForUser,
  reorderCalendars,
  deleteCalendar
} from "../../api/calendarApi";

export default function CalendarTester() {
  const [userId, setUserId] = useState("");
  const [calendarName, setCalendarName] = useState("");
  const [calendars, setCalendars] = useState([]);
  const [result, setResult] = useState(null);

  const handleCreate = async () => {
    const res = await createCalendar(userId, calendarName);
    setResult(res);
  };

  const handleFetch = async () => {
    const res = await getCalendarsForUser(userId);
    setCalendars(res);
    setResult(res);
  };

  const handleReverseOrder = async () => {
    const ids = calendars.map(c => c.id).reverse();
    const res = await reorderCalendars(userId, ids);
    setCalendars(res);
    setResult(res);
  };

  return (
    <div className="space-y-4">
      <h2 className="text-xl font-semibold">Calendar API</h2>

      <input
        className="border px-3 py-2 rounded w-96"
        placeholder="User ID"
        value={userId}
        onChange={e => setUserId(e.target.value)}
      />

      <input
        className="border px-3 py-2 rounded w-96"
        placeholder="Calendar name"
        value={calendarName}
        onChange={e => setCalendarName(e.target.value)}
      />

      <div className="flex gap-2">
        <button
          onClick={handleCreate}
          className="bg-indigo-500 text-white px-4 py-2 rounded"
        >
          Create Calendar
        </button>

        <button
          onClick={handleFetch}
          className="bg-gray-600 text-white px-4 py-2 rounded"
        >
          Fetch Calendars
        </button>

        <button
          onClick={handleReverseOrder}
          className="bg-orange-500 text-white px-4 py-2 rounded"
        >
          Reverse Order
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
