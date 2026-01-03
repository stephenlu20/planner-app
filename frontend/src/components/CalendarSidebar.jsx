export default function CalendarSidebar({
  calendars,
  activeCalendarId,
  onSelect,
  onCreate,
}) {
  return (
    <div className="w-64 bg-white border-r p-4 flex flex-col">
      <h2 className="font-semibold mb-4">Calendars</h2>

      <div className="flex-1 space-y-1">
        {calendars.length === 0 && (
          <div className="text-sm text-gray-500">
            No calendars yet
          </div>
        )}

        {calendars.map((cal) => (
          <button
            key={cal.id}
            onClick={() => onSelect(cal.id)}
            className={`w-full text-left px-2 py-1 rounded transition cursor-pointer ${
              cal.id === activeCalendarId
                ? "bg-blue-100 text-blue-700"
                : "hover:bg-gray-100"
            }`}
          >
            {cal.name}
          </button>
        ))}
      </div>

      <button
        onClick={onCreate}
        className="mt-4 px-2 py-1 rounded bg-blue-500 text-white hover:bg-blue-400 active:scale-95 transition cursor-pointer"
      >
        + Add Calendar
      </button>
    </div>
  );
}
