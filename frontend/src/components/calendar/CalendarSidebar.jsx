import { useState } from "react" ;
import { createCalendar, deleteCalendar } from "../../api/calendarApi";

export default function CalendarSidebar({
  userId,
  calendars,
  setCalendars,
  activeCalendarId,
  onSelect,
}) {
  const [newCalendarName, setNewCalendarName] = useState("");
  const [isCreating, setIsCreating] = useState(false);

  const handleCreate = async () => {
    if (!newCalendarName.trim()) return;

    setIsCreating(true);

    try {
      const created = await createCalendar(
        userId,
        newCalendarName.trim(),
        calendars.length
      );

      setCalendars([...calendars, created]);
      setNewCalendarName("");
    } catch (err) {
      console.error(err);
      alert("Failed to create calendar");
    } finally {
      setIsCreating(false);
    }
  };

  return (
    <div className="w-64 bg-white border-r p-4 flex flex-col">
      <h2 className="font-semibold mb-4 cursor-default">Calendars</h2>

      <div className="flex-1 space-y-1">
        {calendars.length === 0 && (
          <div className="text-sm text-gray-500 cursor-default">
            No calendars yet
          </div>
        )}

        {calendars.map((cal) => (
          <div key={cal.id} className="flex items-center justify-between">
            <button
              onClick={() => onSelect(cal.id)}
              className={`text-left px-2 py-1 rounded transition cursor-pointer flex-1 ${
                cal.id === activeCalendarId
                  ? "bg-blue-100 text-blue-700"
                  : "hover:bg-gray-100"
              }`}
            >
              {cal.name}
            </button>

            <button
              onClick={async (e) => {
                e.stopPropagation(); // prevent triggering onSelect
                try {
                  await deleteCalendar(cal.id); // import from your api
                  setCalendars(calendars.filter((c) => c.id !== cal.id));
                  if (activeCalendarId === cal.id) setActiveCalendarId(null);
                } catch (err) {
                  console.error("Failed to delete calendar:", err);
                }
              }}
              className="ml-2 px-2 py-1 rounded bg-red-500 text-white hover:bg-red-400 active:scale-95 transition cursor-pointer"
            >
              ✕
            </button>
          </div>
        ))}
      </div>

      <input
        type="text"
        value={newCalendarName}
        onChange={(e) => setNewCalendarName(e.target.value)}
        placeholder="New calendar name"
        className="mt-4 px-2 py-1 rounded border focus:outline-none"
      />

      <button
        onClick={handleCreate}
        disabled={!newCalendarName.trim() || isCreating}
        className="mt-4 px-2 py-1 rounded bg-blue-500 text-white hover:bg-blue-400 active:scale-95 transition cursor-pointer"
      >
        + Add Calendar
      </button>
    </div>
  );
}
