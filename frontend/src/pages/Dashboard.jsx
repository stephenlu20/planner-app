import Calendar from "../components/calendar/Calendar";

export default function Dashboard({ calendarId }) {
  return (
    <div className="space-y-4">
      <h2 className="text-xl font-semibold">Dashboard</h2>
      <Calendar calendarId={calendarId} />
    </div>
  );
}