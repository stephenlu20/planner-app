import { useState, useEffect } from "react";
import { createPortal } from "react-dom";
import { getEventsByCalendar } from "../../api/eventApi";
import JournalDayEntry from "./JournalDayEntry";

export default function JournalSidebar({ calendar, isOpen, onClose }) {
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(false);
  const [groupedEvents, setGroupedEvents] = useState({});
  const [sortOrder, setSortOrder] = useState("oldest"); // "oldest" | "newest"

  // Fetch events when sidebar opens
  useEffect(() => {
    if (!calendar || !isOpen) return;

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
  }, [calendar, isOpen]);

  // Group events by date
  useEffect(() => {
    const grouped = {};

    events.forEach(event => {
      if (!event.dateTime) return;

      const date = new Date(event.dateTime);
      const dateKey = date.toISOString().split("T")[0];

      if (!grouped[dateKey]) {
        grouped[dateKey] = { date, events: [] };
      }

      grouped[dateKey].events.push(event);
    });

    setGroupedEvents(grouped);
  }, [events]);

  // Sort dates
  const sortedDates = Object.keys(groupedEvents).sort((a, b) => {
    return sortOrder === "oldest"
      ? new Date(a) - new Date(b)
      : new Date(b) - new Date(a);
  });

  if (!isOpen) return null;

  return createPortal(
    <>
      {/* Overlay */}
      <div
        className="fixed inset-0 bg-black/20 z-40"
        onClick={onClose}
      />

      {/* Sidebar */}
      <div className="fixed right-0 top-0 bottom-0 w-full max-w-2xl bg-white shadow-2xl z-50 flex flex-col">
        {/* Header */}
        <div className="shrink-0 border-b bg-gray-50 px-6 py-4">
          <div className="flex items-center justify-between mb-2">
            <h2 className="text-xl font-bold text-gray-800">
              {calendar?.name || "Calendar"} Journal
            </h2>
            <button
              onClick={onClose}
              className="text-gray-600 hover:text-gray-800 text-2xl leading-none cursor-pointer"
              aria-label="Close journal"
            >
              ×
            </button>
          </div>

          <div className="flex items-center justify-between">
            <p className="text-sm text-gray-600">
              {sortedDates.length}{" "}
              {sortedDates.length === 1 ? "day" : "days"} with events
            </p>

            <button
              onClick={() =>
                setSortOrder(sortOrder === "oldest" ? "newest" : "oldest")
              }
              className="px-3 py-1 rounded border border-gray-300 hover:bg-gray-50 transition cursor-pointer text-xs"
              title={
                sortOrder === "oldest"
                  ? "Switch to newest first"
                  : "Switch to oldest first"
              }
            >
              {sortOrder === "oldest" ? "↓ Oldest" : "↑ Newest"}
            </button>
          </div>
        </div>

        {/* Content */}
        <div className="flex-1 overflow-y-auto px-6 py-6">
          {loading ? (
            <div className="text-center py-12 text-gray-500">
              Loading journal entries...
            </div>
          ) : sortedDates.length === 0 ? (
            <div className="text-center py-12 text-gray-500">
              <p className="text-lg mb-2">No journal entries yet</p>
              <p className="text-sm">
                Events you create will appear here
              </p>
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
      </div>
    </>,
    document.body
  );
}
