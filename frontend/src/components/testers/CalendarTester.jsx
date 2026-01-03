import { useState } from "react";
import { getCalendarsForUser, createCalendar, reorderCalendars, deleteCalendar, getAllCalendars } from "../../api/calendarApi";

export default function CalendarTester() {
  const [userIdCreate, setUserIdCreate] = useState("");
  const [nameCreate, setNameCreate] = useState("");
  const [orderCreate, setOrderCreate] = useState(0);

  const [userIdFetch, setUserIdFetch] = useState("");

  const [userIdReorder, setUserIdReorder] = useState("");
  const [orderedIds, setOrderedIds] = useState("");

  const [deleteId, setDeleteId] = useState("");

  const [output, setOutput] = useState("");

  const buttonClass = "px-3 py-1 rounded text-white hover:cursor-pointer active:scale-95 transition-transform";

  return (
    <div className="p-6 space-y-6">
      <h2 className="text-xl font-bold">Calendar API Tester</h2>

      <section className="p-4 border rounded space-y-2">
        <h3 className="font-semibold">Create Calendar</h3>
        <input placeholder="User ID" value={userIdCreate} onChange={(e) => setUserIdCreate(e.target.value)} className="border p-1" />
        <input placeholder="Name" value={nameCreate} onChange={(e) => setNameCreate(e.target.value)} className="border p-1" />
        <input type="number" placeholder="Order Index" value={orderCreate} onChange={(e) => setOrderCreate(Number(e.target.value))} className="border p-1" />
        <button
          className={`${buttonClass} bg-blue-500`}
          onClick={async () => {
            try {
              const res = await createCalendar(userIdCreate, nameCreate, orderCreate);
              setOutput(JSON.stringify(res, null, 2));
            } catch (err) {
              setOutput(err.response?.data || err.message);
            }
          }}
        >
          Create
        </button>
      </section>

      <section className="p-4 border rounded space-y-2">
        <h3 className="font-semibold">Get Calendars for User</h3>
        <input placeholder="User ID" value={userIdFetch} onChange={(e) => setUserIdFetch(e.target.value)} className="border p-1" />
        <button
          className={`${buttonClass} bg-purple-500`}
          onClick={async () => {
            try {
              const res = await getCalendarsForUser(userIdFetch);
              setOutput(JSON.stringify(res, null, 2));
            } catch (err) {
              setOutput(err.response?.data || err.message);
            }
          }}
        >
          Fetch
        </button>
      </section>

      <section className="p-4 border rounded space-y-2">
        <h3 className="font-semibold">Reorder Calendars</h3>
        <input placeholder="User ID" value={userIdReorder} onChange={(e) => setUserIdReorder(e.target.value)} className="border p-1" />
        <input
          placeholder="Comma-separated Calendar IDs"
          value={orderedIds}
          onChange={(e) => setOrderedIds(e.target.value)}
          className="border p-1 w-full max-w-xl whitespace-nowrap overflow-x-auto"
        />
        <button
          className={`${buttonClass} bg-green-500`}
          onClick={async () => {
            try {
              const idsArray = orderedIds.split(",").map((id) => id.trim());
              const res = await reorderCalendars(userIdReorder, idsArray);
              setOutput(JSON.stringify(res, null, 2));
            } catch (err) {
              setOutput(err.response?.data || err.message);
            }
          }}
        >
          Reorder
        </button>
      </section>

      <section className="p-4 border rounded space-y-2">
        <h3 className="font-semibold">Delete Calendar</h3>
        <input placeholder="Calendar ID" value={deleteId} onChange={(e) => setDeleteId(e.target.value)} className="border p-1" />
        <button
          className={`${buttonClass} bg-red-500`}
          onClick={async () => {
            try {
              await deleteCalendar(deleteId);
              setOutput(`Deleted calendar ${deleteId}`);
            } catch (err) {
              setOutput(err.response?.data || err.message);
            }
          }}
        >
          Delete
        </button>
      </section>

      <section className="p-4 border rounded space-y-2">
        <h3 className="font-semibold">Get All Calendars</h3>
        <button
          className="bg-purple-500 text-white px-3 py-1 rounded hover:cursor-pointer active:scale-95 transition-transform"
          onClick={async () => {
            try {
              const res = await getAllCalendars();
              setOutput(JSON.stringify(res, null, 2));
            } catch (err) {
              setOutput(err.response?.data || err.message);
            }
          }}
        >
          Fetch All
        </button>
      </section>

      <pre className="p-4 border rounded bg-gray-100 overflow-auto">{output}</pre>
    </div>
  );
}
