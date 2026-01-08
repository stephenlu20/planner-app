import { useState } from "react";

const DAYS = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];

export default function Calendar() {
  const [currentDate, setCurrentDate] = useState(new Date());
  const [view, setView] = useState("week");

  const year = currentDate.getFullYear();
  const month = currentDate.getMonth();
  // const dayOfMonth = currentDate.getDate();

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
          {monthCells.map((day, idx) => (
            <div
              key={idx}
              className={`h-20 border rounded flex items-start justify-end p-1 ${day ? "bg-gray-50" : "bg-transparent"}`}
            >
              {day && <span className="text-sm">{day}</span>}
            </div>
          ))}
        </div>
      )}

      {view === "week" && (
        <div className="grid grid-cols-7 gap-2 text-center">
          {weekCells.map((d) => (
            <div
              key={d.toDateString()}
              className="h-32 border rounded flex flex-col items-start p-1"
            >
              <span className="font-semibold text-sm">{DAYS[d.getDay()]}</span>
              <span className="text-xs text-gray-500">{d.getMonth() + 1}/{d.getDate()}</span>
            </div>
          ))}
        </div>
      )}

      {view === "day" && (
        <div className="border rounded p-4 h-64 flex flex-col">
          <h3 className="font-semibold mb-2">{DAYS[currentDate.getDay()]}</h3>
          <span className="text-gray-500">{dayLabel}</span>
          <div className="flex-1 mt-2 border-t pt-2 text-sm text-gray-700">
            No events scheduled
          </div>
        </div>
      )}
    </div>
  );
}
