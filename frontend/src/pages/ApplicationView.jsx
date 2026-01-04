import LoginPage from "./LoginPage";
import Dashboard from "./Dashboard";
import CalendarSidebar from "../components/CalendarSidebar";

export default function ApplicationView({ userId, setUserId }) {
  const handleLogout = () => {
    localStorage.removeItem("userId");
    setUserId(null);
  };

  if (!userId) return <LoginPage onLogin={setUserId} />;

  const calendars = [];
  const activeCalendarId = null;

  return (
    <div className="flex min-h-screen">
      <CalendarSidebar
        calendars={calendars}
        activeCalendarId={activeCalendarId}
        onSelect={() => {}}
        onCreate={() => {}}
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