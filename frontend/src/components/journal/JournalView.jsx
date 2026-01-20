import { useState, useEffect } from "react";
import { getEventsByCalendar } from "../../api/eventApi";
import JournalDayEntry from "./JournalDayEntry";

export default function JournalView({ calendar, onClose }) {
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(false);
  const [groupedEvents, setGroupedEvents] = useState({});
  const [sortOrder, setSortOrder] = useState("oldest"); // "oldest" or "newest"

  // Fetch events
  useEffect(() => {
    if (!calendar) return;

    const fetchEvents = async () => {
      setLoading(true);
      try {
        const data = await getEventsByCalendar(calendar.id);
        setEvents(data);
      } catch (err) {
        console.error("Failed to fetch events:", err);
        setEvents([]);
      } finally {
        setLoading(false);
      }
    };

    fetchEvents();
  }, [calendar]);

  // Group events by date
  useEffect(() => {
    const grouped = {};
    
    events.forEach(event => {
      if (!event.dateTime) return;
      
      const date = new Date(event.dateTime);
      const dateKey = date.toISOString().split('T')[0]; // YYYY-MM-DD
      
      if (!grouped[dateKey]) {
        grouped[dateKey] = {
          date: date,
          events: []
        };
      }
      
      grouped[dateKey].events.push(event);
    });

    setGroupedEvents(grouped);
  }, [events]);

  // Sort dates based on sortOrder
  const sortedDates = Object.keys(groupedEvents).sort((a, b) => {
    if (sortOrder === "oldest") {
      return new Date(a) - new Date(b); // Ascending (oldest first)
    } else {
      return new Date(b) - new Date(a); // Descending (newest first)
    }
  });

  return (
    <div className="fixed inset-0 bg-white z-50 overflow-y-auto">
      {/* Header */}
      <div className="sticky top-0 bg-white border-b shadow-sm z-10">
        <div className="max-w-4xl mx-auto px-6 py-4">
          <div className="flex items-center justify-between mb-2">
            <div>
              <h1 className="text-2xl font-bold text-gray-800">
                {calendar?.name || "Calendar"} Journal
              </h1>
              <p className="text-sm text-gray-600 mt-1">
                {sortedDates.length} {sortedDates.length === 1 ? 'day' : 'days'} with events
              </p>
            </div>
            <div className="flex gap-2">
              <button
                onClick={() => setSortOrder(sortOrder === "oldest" ? "newest" : "oldest")}
                className="px-3 py-2 rounded border border-gray-300 hover:bg-gray-50 transition cursor-pointer text-sm"
                title={sortOrder === "oldest" ? "Switch to newest first" : "Switch to oldest first"}
              >
                {sortOrder === "oldest" ? "↓ Oldest First" : "↑ Newest First"}
              </button>
              <button
                onClick={onClose}
                className="px-4 py-2 rounded border border-gray-300 hover:bg-gray-50 transition cursor-pointer"
              >
                Close
              </button>
            </div>
          </div>
        </div>
      </div>

      {/* Content */}
      <div className="max-w-4xl mx-auto px-6 py-8">
        {loading ? (
          <div className="text-center py-12 text-gray-500">
            Loading journal entries...
          </div>
        ) : sortedDates.length === 0 ? (
          <div className="text-center py-12 text-gray-500">
            <p className="text-lg mb-2">No journal entries yet</p>
            <p className="text-sm">Events you create will appear here</p>
          </div>
        ) : (
          <div className="space-y-8">
            {sortedDates.map(dateKey => (
              <JournalDayEntry
                key={dateKey}
                date={groupedEvents[dateKey].date}
                events={groupedEvents[dateKey].events}
              />
            ))}
          </div>
        )}
      </div>

      {/* Print styles */}
      <style>{`
        @media print {
          .sticky { position: relative !important; }
          button { display: none !important; }
        }
      `}</style>
    </div>
  );
}