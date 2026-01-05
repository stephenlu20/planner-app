import Calendar from "../components/calendar/Calendar";

export default function Dashboard() {
  return (
    <div className="space-y-4">
      <h2 className="text-xl font-semibold">Dashboard</h2>
      <Calendar />
    </div>
  );
}