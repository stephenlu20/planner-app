import { useState, useEffect } from "react";
import { getEntriesBySubject } from "../../api/entryApi";
import EntryRenderer from "../entries/EntryRenderer";

export default function JournalDayEntry({ date, events }) {
  const dayOfWeek = date.toLocaleDateString('en-US', { weekday: 'long' });
  const formattedDate = date.toLocaleDateString('en-US', { 
    month: 'long', 
    day: 'numeric', 
    year: 'numeric' 
  });

  return (
    <div className="mb-8 break-inside-avoid">
      {/* Date Header */}
      <div className="mb-4 pb-2 border-b-2 border-gray-300">
        <h3 className="text-lg font-bold text-gray-800">{dayOfWeek}</h3>
        <p className="text-sm text-gray-600">{formattedDate}</p>
      </div>

      {/* Events for this day */}
      <div className="space-y-6 ml-2">
        {events.length === 0 ? (
          <p className="text-sm text-gray-500 italic">No events recorded</p>
        ) : (
          events.map((event) => (
            <JournalEventEntry key={event.id} event={event} />
          ))
        )}
      </div>
    </div>
  );
}

// Single event entry within a day
function JournalEventEntry({ event }) {
  const [entries, setEntries] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchEntries = async () => {
      setLoading(true);
      try {
        const data = await getEntriesBySubject("EVENT", event.id);
        setEntries(data);
      } catch (err) {
        console.error("Failed to fetch entries:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchEntries();
  }, [event.id]);

  return (
    <div className="pl-4 border-l-2 border-gray-200">
      {/* Event Title */}
      <div className="flex items-center gap-2 mb-2">
        <h4 className="font-semibold text-gray-800">{event.title || "Event"}</h4>
        {event.completed && (
          <span className="text-xs px-2 py-0.5 bg-green-100 text-green-700 rounded">
            ✓ Completed
          </span>
        )}
      </div>

      {/* Event Note */}
      {event.note && (
        <div className="mb-3">
          <p className="text-sm text-gray-700 whitespace-pre-wrap">{event.note}</p>
        </div>
      )}

      {/* Entries */}
      {loading ? (
        <p className="text-xs text-gray-500 italic">Loading details...</p>
      ) : entries.length > 0 ? (
        <div className="space-y-3 mt-3">
          {entries.map((entry) => (
            <div key={entry.id}>
              
              <EntryRenderer
                entry={entry}
                readOnly={true}
              />
            </div>
          ))}
        </div>
      ) : null}
    </div>
  );
}