import { useState } from "react";
import {
  createEvent,
  getEventsByUser,
  getEventsByCalendar,
  toggleEventCompleted,
  reorderEvents,
  deleteEvent
} from "../../api/eventApi";

export default function EventTester() {
  const [result, setResult] = useState(null);

  const [createData, setCreateData] = useState({
    userId: "",
    calendarId: "",
    note: "",
    orderIndex: ""
  });

  const [userId, setUserId] = useState("");
  const [calendarId, setCalendarId] = useState("");

  const [toggleId, setToggleId] = useState("");
  const [deleteId, setDeleteId] = useState("");

  const [reorderData, setReorderData] = useState({
    calendarId: "",
    orderedEventIds: ""
  });

  const box = "border rounded-lg p-4 space-y-3 bg-white shadow-sm";
  const input = "border px-3 py-1 rounded w-full";
  const button =
    "px-4 py-2 rounded font-medium transition transform active:scale-95 cursor-pointer";
  const heading = "font-semibold text-gray-800";

  return (
    <div className="p-6 space-y-6">

      {/* CREATE EVENT */}
      <div className={box}>
        <h3 className={heading}>Create Event</h3>

        <input
          className={input}
          placeholder="User ID"
          value={createData.userId}
          onChange={(e) => setCreateData({ ...createData, userId: e.target.value })}
        />

        <input
          className={input}
          placeholder="Calendar ID"
          value={createData.calendarId}
          onChange={(e) =>
            setCreateData({ ...createData, calendarId: e.target.value })
          }
        />

        <input
          className={input}
          placeholder="Note"
          value={createData.note}
          onChange={(e) => setCreateData({ ...createData, note: e.target.value })}
        />

        <input
          className={input}
          placeholder="Order Index"
          value={createData.orderIndex}
          onChange={(e) =>
            setCreateData({ ...createData, orderIndex: e.target.value })
          }
        />

        <button
          className={`${button} bg-blue-600 text-white hover:bg-blue-700`}
          onClick={async () => {
            try {
              const res = await createEvent({
                ...createData,
                orderIndex: Number(createData.orderIndex)
              });
              setResult(res);
            } catch (err) {
              setResult({ error: err.response?.data || err.message });
            }
          }}
        >
          Create
        </button>
      </div>

      <div className={box}>
        <h3 className={heading}>Get Events by User</h3>

        <input
          className={input}
          placeholder="User ID"
          value={userId}
          onChange={(e) => setUserId(e.target.value)}
        />

        <button
          className={`${button} bg-indigo-600 text-white hover:bg-indigo-700`}
          onClick={async () => {
            try {
              const res = await getEventsByUser(userId);
              setResult(res);
            } catch (err) {
              setResult({ error: err.response?.data || err.message });
            }
          }}
        >
          Fetch
        </button>
      </div>

      <div className={box}>
        <h3 className={heading}>Get Events by Calendar</h3>

        <input
          className={input}
          placeholder="Calendar ID"
          value={calendarId}
          onChange={(e) => setCalendarId(e.target.value)}
        />

        <button
          className={`${button} bg-purple-600 text-white hover:bg-purple-700`}
          onClick={async () => {
            try {
              const res = await getEventsByCalendar(calendarId);
              setResult(res);
            } catch (err) {
              setResult({ error: err.response?.data || err.message });
            }
          }}
        >
          Fetch
        </button>
      </div>

      <div className={box}>
        <h3 className={heading}>Toggle Event Completed</h3>

        <input
          className={input}
          placeholder="Event ID"
          value={toggleId}
          onChange={(e) => setToggleId(e.target.value)}
        />

        <button
          className={`${button} bg-green-600 text-white hover:bg-green-700`}
          onClick={async () => {
            try {
              const res = await toggleEventCompleted(toggleId);
              setResult(res);
            } catch (err) {
              setResult({ error: err.response?.data || err.message });
            }
          }}
        >
          Toggle
        </button>
      </div>

      <div className={box}>
        <h3 className={heading}>Reorder Events</h3>

        <input
          className={input}
          placeholder="Calendar ID"
          value={reorderData.calendarId}
          onChange={(e) =>
            setReorderData({ ...reorderData, calendarId: e.target.value })
          }
        />

        <input
          className={input}
          placeholder="Comma-separated Event IDs"
          value={reorderData.orderedEventIds}
          onChange={(e) =>
            setReorderData({ ...reorderData, orderedEventIds: e.target.value })
          }
        />

        <button
          className={`${button} bg-yellow-600 text-white hover:bg-yellow-700`}
          onClick={async () => {
            try {
              const res = await reorderEvents({
                calendarId: reorderData.calendarId,
                orderedEventIds: reorderData.orderedEventIds
                  .split(",")
                  .map((id) => id.trim())
              });
              setResult(res);
            } catch (err) {
              setResult({ error: err.response?.data || err.message });
            }
          }}
        >
          Reorder
        </button>
      </div>

      <div className={box}>
        <h3 className={heading}>Delete Event</h3>

        <input
          className={input}
          placeholder="Event ID"
          value={deleteId}
          onChange={(e) => setDeleteId(e.target.value)}
        />

        <button
          className={`${button} bg-red-600 text-white hover:bg-red-700`}
          onClick={async () => {
            try {
              await deleteEvent(deleteId);
              setResult({ message: "Event deleted" });
            } catch (err) {
              setResult({ error: err.response?.data || err.message });
            }
          }}
        >
          Delete
        </button>
      </div>

      {result && (
        <pre className="bg-gray-100 p-4 rounded overflow-auto">
          {JSON.stringify(result, null, 2)}
        </pre>
      )}
    </div>
  );
}
