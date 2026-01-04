import { useState } from "react";

const DAYS = ["Sunday", "Monday", "Tueday", "Wednesday", "Thursday", "Friday", "Saturday"];

export default function Calendar() {
  const [currentDate, setCurrentDate] = useState(new Date());

  const year = currentDate.getFullYear();
  const month = currentDate.getMonth();

  const firstDayOfMonth = new Date(year, month, 1).getDay();
  const daysInMonth = new Date(year, month + 1, 0).getDate();

  const prevMonth = () =>
    setCurrentDate(new Date(year, month - 1, 1));
  const nextMonth = () =>
    setCurrentDate(new Date(year, month + 1, 1));

  const monthLabel = currentDate.toLocaleString("default", {
    month: "long",
    year: "numeric",
  });

  const cells = [];
  for (let i = 0; i < firstDayOfMonth; i++) cells.push(null);
  for (let d = 1; d <= daysInMonth; d++) cells.push(d);

  return (
    <div className="bg-white rounded shadow p-4">
      <div className="flex items-center justify-between mb-4">
        <button
          onClick={prevMonth}
          className="px-3 py-1 rounded bg-gray-200 hover:bg-gray-300 active:scale-95 transition cursor-pointer caret-transparent"
        >
          Previous
        </button>

        <h2 className="text-xl font-semibold caret-transparent cursor-default">{monthLabel}</h2>

        <button
          onClick={nextMonth}
          className="px-3 py-1 rounded bg-gray-200 hover:bg-gray-300 active:scale-95 transition cursor-pointer caret-transparent"
        >
          Next
        </button>
      </div>

      <div className="grid grid-cols-7 gap-2 text-center">
        {DAYS.map((day) => (
          <div key={day} className="font-semibold text-gray-600 caret-transparent cursor-default">
            {day}
          </div>
        ))}

        {cells.map((day, idx) => (
          <div
            key={idx}
            className={`h-20 border rounded flex items-start justify-end p-1 ${
              day ? "bg-gray-50" : "bg-transparent"
            }`}
          >
            {day && <span className="text-sm caret-transparent cursor-default">{day}</span>}
          </div>
        ))}
      </div>
    </div>
  );
}
