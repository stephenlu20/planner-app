import { useState, useEffect } from "react" ;
import LoginPage from "./LoginPage";
import Dashboard from "./Dashboard";
import CalendarSidebar from "../components/CalendarSidebar";
import { getCalendarsForUser } from "../api/calendarApi";

export default function ApplicationView({ userId, setUserId }) {

  const [calendars, setCalendars] = useState([]);
  const [activeCalendarId, setActiveCalendarId] = useState(null);

  useEffect(() => {
    if (!userId) return;

    const fetchCalendars = async () => {
      try {
        const data = await getCalendarsForUser(userId);
        setCalendars(data);

        if (data.length > 0) setActiveCalendarId(data[0].id);
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
    <div className="flex min-h-screen">
      <CalendarSidebar
        userId={userId}
        calendars={calendars}
        setCalendars={setCalendars}
        activeCalendarId={activeCalendarId}
        onSelect={setActiveCalendarId}
      />

      <div className="flex-1 p-6">
        <div className="flex justify-end mb-4">
          <button
            onClick={handleLogout}
            className="px-4 py-2 rounded bg-red-500 text-white hover:bg-red-400 active:scale-95 transition cursor-pointer caret-transparent"
          >
            Logout
          </button>
        </div>
        <Dashboard userId={userId} />
      </div>
    </div>
  );
}