import { useState, useEffect } from "react";
import LoginPage from "./LoginPage";
import Dashboard from "./Dashboard";
import CalendarSidebar from "../components/CalendarSidebar";
import Templates from "./Templates";
import { getCalendarsForUser } from "../api/calendarApi";

export default function ApplicationView({ userId, setUserId }) {
  const [activeView, setActiveView] = useState("calendars");
  const [calendars, setCalendars] = useState([]);
  const [activeCalendarId, setActiveCalendarId] = useState(null);

  useEffect(() => {
    if (!userId) return;

    const fetchCalendars = async () => {
      try {
        const data = await getCalendarsForUser(userId);
        setCalendars(data);

        if (data.length > 0) {
          setActiveCalendarId(data[0].id);
        }
      } catch (err) {
        console.error("Failed to fetch calendars:", err);
      }
    };

    fetchCalendars();
  }, [userId]);

  const handleLogout = () => {
    localStorage.removeItem("userId");
    setUserId(null);
  };

  if (!userId) return <LoginPage onLogin={setUserId} />;

  return (
    <div className="min-h-screen flex flex-col">
      {/* Top bar */}
      <div className="flex justify-between items-center p-4 border-b bg-white">
        <div className="flex gap-2">
          <button
            onClick={() => setActiveView("calendars")}
            className={`px-3 py-1 rounded transition ${
              activeView === "calendars"
                ? "bg-blue-500 text-white"
                : "bg-gray-100 hover:bg-gray-200"
            }`}
          >
            Calendars
          </button>

          <button
            onClick={() => setActiveView("templates")}
            className={`px-3 py-1 rounded transition ${
              activeView === "templates"
                ? "bg-blue-500 text-white"
                : "bg-gray-100 hover:bg-gray-200"
            }`}
          >
            Templates
          </button>
        </div>

        <button
          onClick={handleLogout}
          className="px-4 py-2 rounded bg-red-500 text-white hover:bg-red-400 active:scale-95 transition"
        >
          Logout
        </button>
      </div>

      {/* Main content */}
      {activeView === "calendars" && (
        <div className="flex flex-1">
          <CalendarSidebar
            userId={userId}
            calendars={calendars}
            setCalendars={setCalendars}
            activeCalendarId={activeCalendarId}
            onSelect={setActiveCalendarId}
          />

          <div className="flex-1 p-6">
            <Dashboard
              userId={userId}
              calendarId={activeCalendarId}
            />
          </div>
        </div>
      )}

      {activeView === "templates" && (
        <div className="flex-1 p-6">
          <Templates userId={userId} />
        </div>
      )}
    </div>
  );
}
