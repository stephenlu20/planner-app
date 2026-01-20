import { useState, useEffect } from "react";
import { getEventsByCalendar } from "../../api/eventApi";

const DAYS = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];

export default function Calendar({ calendarId }) {
  const [currentDate, setCurrentDate] = useState(new Date());
  const [view, setView] = useState("week");
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(false);

  // Fetch events when calendar changes
  useEffect(() => {
    if (!calendarId) {
      setEvents([]);
      return;
    }

    const fetchEvents = async () => {
      setLoading(true);
      try {
        const data = await getEventsByCalendar(calendarId);
        setEvents(data);
      } catch (err) {
        console.error("Failed to fetch events:", err);
        setEvents([]);
      } finally {
        setLoading(false);
      }
    };

    fetchEvents();
  }, [calendarId]);

  // Helper function to get events for a specific date
  const getEventsForDate = (date) => {
    const targetYear = date.getFullYear();
    const targetMonth = date.getMonth();
    const targetDay = date.getDate();
    
    return events.filter(event => {
      if (!event.dateTime) return false;
      
      const eventDate = new Date(event.dateTime);
      
      return eventDate.getFullYear() === targetYear &&
             eventDate.getMonth() === targetMonth &&
             eventDate.getDate() === targetDay;
    });
  };

  // Helper to switch to day view for a specific date
  const switchToDayView = (date) => {
    setCurrentDate(date);
    setView("day");
  };

  const year = currentDate.getFullYear();
  const month = currentDate.getMonth();

  const prev = () => {
    if (view === "month") setCurrentDate(new Date(year, month - 1, 1));
    if (view === "week") setCurrentDate(new Date(currentDate.getTime() - 7 * 24 * 60 * 60 * 1000));
    if (view === "day") setCurrentDate(new Date(currentDate.getTime() - 24 * 60 * 60 * 1000));
  };

  const next = () => {
    if (view === "month") setCurrentDate(new Date(year, month + 1, 1));
    if (view === "week") setCurrentDate(new Date(currentDate.getTime() + 7 * 24 * 60 * 60 * 1000));
    if (view === "day") setCurrentDate(new Date(currentDate.getTime() + 24 * 60 * 60 * 1000));
  };

  const monthLabel = currentDate.toLocaleString("default", { month: "long", year: "numeric" });
  const weekLabel = `Week of ${new Date(currentDate.getTime() - currentDate.getDay() * 24 * 60 * 60 * 1000).toLocaleDateString()}`;
  const dayLabel = currentDate.toLocaleDateString(undefined, { month: "long", day: "numeric", year: "numeric" });

  const firstDayOfMonth = new Date(year, month, 1).getDay();
  const daysInMonth = new Date(year, month + 1, 0).getDate();
  const monthCells = [];
  for (let i = 0; i < firstDayOfMonth; i++) monthCells.push(null);
  for (let d = 1; d <= daysInMonth; d++) monthCells.push(d);

  const startOfWeek = new Date(currentDate);
  startOfWeek.setDate(currentDate.getDate() - currentDate.getDay());
  const weekCells = Array.from({ length: 7 }, (_, i) => {
    const d = new Date(startOfWeek);
    d.setDate(startOfWeek.getDate() + i);
    return d;
  });

  if (loading) {
    return (
      <div className="bg-white rounded shadow p-4">
        <div className="text-gray-500">Loading events...</div>
      </div>
    );
  }

  return (
    <div className="bg-white rounded shadow p-4">
      <div className="flex items-center justify-between mb-4">
        <div className="flex gap-2">
          <button
            onClick={() => setView("month")}
            className={`px-3 py-1 rounded ${view === "month" ? "bg-blue-500 text-white" : "bg-gray-200 hover:bg-gray-300"}`}
          >
            Month
          </button>
          <button
            onClick={() => setView("week")}
            className={`px-3 py-1 rounded ${view === "week" ? "bg-blue-500 text-white" : "bg-gray-200 hover:bg-gray-300"}`}
          >
            Week
          </button>
          <button
            onClick={() => setView("day")}
            className={`px-3 py-1 rounded ${view === "day" ? "bg-blue-500 text-white" : "bg-gray-200 hover:bg-gray-300"}`}
          >
            Day
          </button>
        </div>

        <div className="flex items-center gap-2">
          <button
            onClick={prev}
            className="px-3 py-1 rounded bg-gray-200 hover:bg-gray-300 transition"
          >
            Previous
          </button>

          <h2 className="text-xl font-semibold cursor-default">
            {view === "month" ? monthLabel : view === "week" ? weekLabel : dayLabel}
          </h2>

          <button
            onClick={next}
            className="px-3 py-1 rounded bg-gray-200 hover:bg-gray-300 transition"
          >
            Next
          </button>
        </div>
      </div>

      {view === "month" && (
        <div className="grid grid-cols-7 gap-2 text-center">
          {DAYS.map((day) => (
            <div key={day} className="font-semibold text-gray-600">{day}</div>
          ))}
          {monthCells.map((day, idx) => {
            if (!day) {
              return <div key={idx} className="h-24 bg-transparent" />;
            }

            const cellDate = new Date(year, month, day);
            const cellEvents = getEventsForDate(cellDate);
            const isToday = cellDate.toDateString() === new Date().toDateString();

            return (
              <div
                key={idx}
                onClick={() => switchToDayView(cellDate)}
                className={`h-24 border rounded flex flex-col items-start p-1 cursor-pointer transition hover:bg-gray-100 ${
                  isToday ? "bg-blue-50 border-blue-300" : "bg-gray-50"
                }`}
              >
                <span className={`text-sm font-medium ${isToday ? "text-blue-600" : ""}`}>
                  {day}
                </span>
                <div className="w-full mt-1 space-y-1 overflow-hidden">
                  {cellEvents.slice(0, 3).map((event) => (
                    <div
                      key={event.id}
                      className="text-xs px-1 py-0.5 bg-blue-200 text-blue-800 rounded truncate"
                      title={event.title || "Event"}
                    >
                      {event.title || "Event"}
                    </div>
                  ))}
                  {cellEvents.length > 3 && (
                    <div className="text-xs text-gray-500">
                      +{cellEvents.length - 3} more
                    </div>
                  )}
                </div>
              </div>
            );
          })}
        </div>
      )}

      {view === "week" && (
        <div className="grid grid-cols-7 gap-2 text-center">
          {weekCells.map((d) => {
            const cellEvents = getEventsForDate(d);
            const isToday = d.toDateString() === new Date().toDateString();

            return (
              <div
                key={d.toDateString()}
                onClick={() => switchToDayView(d)}
                className={`h-40 border rounded flex flex-col items-start p-2 cursor-pointer transition hover:bg-gray-100 ${
                  isToday ? "bg-blue-50 border-blue-300" : ""
                }`}
              >
                <span className={`font-semibold text-sm ${isToday ? "text-blue-600" : ""}`}>
                  {DAYS[d.getDay()]}
                </span>
                <span className={`text-xs text-gray-500 ${isToday ? "text-blue-500" : ""}`}>
                  {d.getMonth() + 1}/{d.getDate()}
                </span>
                <div className="w-full mt-2 space-y-1 overflow-hidden">
                  {cellEvents.slice(0, 4).map((event) => (
                    <div
                      key={event.id}
                      className="text-xs px-1 py-0.5 bg-blue-200 text-blue-800 rounded truncate"
                      title={event.title || "Event"}
                    >
                      {event.title || "Event"}
                    </div>
                  ))}
                  {cellEvents.length > 4 && (
                    <div className="text-xs text-gray-500">
                      +{cellEvents.length - 4} more
                    </div>
                  )}
                </div>
              </div>
            );
          })}
        </div>
      )}

      {view === "day" && (
        <div className="border rounded p-4 min-h-64 flex flex-col">
          <h3 className="font-semibold mb-2">{DAYS[currentDate.getDay()]}</h3>
          <span className="text-gray-500 mb-4">{dayLabel}</span>
          
          <div className="flex-1">
            {getEventsForDate(currentDate).length === 0 ? (
              <div className="text-sm text-gray-500">No events scheduled</div>
            ) : (
              <div className="space-y-2">
                {getEventsForDate(currentDate).map((event) => (
                  <div
                    key={event.id}
                    className="p-3 border rounded bg-gray-50 hover:bg-gray-100 transition"
                  >
                    <div className="font-medium text-sm">
                      {event.title || "Event"}
                    </div>
                    <div className="text-xs text-gray-500 mt-1">
                      {event.completed ? "✓ Completed" : "Pending"}
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
}