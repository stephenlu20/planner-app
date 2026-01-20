import { useState, useEffect } from "react";
import { getEntriesBySubject, updateEntry } from "../../api/entryApi";
import EntryRenderer from "../entries/EntryRenderer";

export default function EventDayCard({ event, onEventUpdate }) {
  const [entries, setEntries] = useState([]);
  const [localNote, setLocalNote] = useState(event.note || "");
  const [loading, setLoading] = useState(false);

  // Fetch entries for this event
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

  // Update local note state when event changes
  useEffect(() => {
    setLocalNote(event.note || "");
  }, [event.note]);

  // Handle note blur (save on losing focus)
  const handleNoteBlur = async () => {
    if (localNote === event.note) return; // No change
    
    try {
      // TODO: Add API call to update event note
      // For now, just notify parent
      if (onEventUpdate) {
        onEventUpdate({ ...event, note: localNote });
      }
    } catch (err) {
      console.error("Failed to update note:", err);
    }
  };

  // Handle entry value/note changes
  const handleEntryChange = async (index, updatedEntry) => {
    const newEntries = [...entries];
    newEntries[index] = updatedEntry;
    setEntries(newEntries);

    // Save to backend
    try {
      await updateEntry(updatedEntry.id, {
        label: updatedEntry.label,
        value: updatedEntry.value,
        note: updatedEntry.note
      });
    } catch (err) {
      console.error("Failed to update entry:", err);
    }
  };

  return (
    <div className="border rounded-lg bg-white shadow-sm overflow-hidden">
      {/* Header */}
      <div className="bg-gray-50 border-b px-4 py-3">
        <div className="flex items-center justify-between">
          <h3 className="font-semibold text-lg">{event.title || "Event"}</h3>
          <div className="text-xs text-gray-500">
            {event.completed ? (
              <span className="px-2 py-1 bg-green-100 text-green-700 rounded">✓ Completed</span>
            ) : (
              <span className="px-2 py-1 bg-gray-100 text-gray-600 rounded">Pending</span>
            )}
          </div>
        </div>
      </div>

      {/* Content */}
      <div className="p-4 space-y-4">
        {/* Event Note */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Notes
          </label>
          <textarea
            value={localNote}
            onChange={(e) => setLocalNote(e.target.value)}
            onBlur={handleNoteBlur}
            placeholder="Add notes about this event..."
            className="w-full border rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 resize-none"
            rows={3}
          />
        </div>

        {/* Entries */}
        {loading ? (
          <div className="text-sm text-gray-500">Loading entries...</div>
        ) : entries.length > 0 ? (
          <div className="space-y-4">
            <div className="text-sm font-medium text-gray-700">Details</div>
            {entries.map((entry, index) => (
              <div key={entry.id} className="space-y-1">
                {/* Show label as read-only text */}
                {entry.type !== "HEADER" && (
                  <div className="text-sm font-medium text-gray-700">
                    {entry.label}
                  </div>
                )}
                
                {/* Render the entry with editable value/note */}
                <EntryRenderer
                  entry={entry}
                  onChange={(updatedEntry) => handleEntryChange(index, updatedEntry)}
                  readOnly={false}
                  allowResize={false}
                />
              </div>
            ))}
          </div>
        ) : (
          <div className="text-sm text-gray-500">No additional details</div>
        )}
      </div>
    </div>
  );
}