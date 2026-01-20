import { useState, useEffect } from "react";
import { getEntriesBySubject, updateEntry } from "../../api/entryApi";
import { toggleEventCompleted, updateEvent } from "../../api/eventApi";
import EntryRenderer from "../entries/EntryRenderer";

export default function EventDayCard({ event, onEventUpdate }) {
  const [entries, setEntries] = useState([]);
  const [localNote, setLocalNote] = useState(event.note || "");
  const [loading, setLoading] = useState(false);
  const [saving, setSaving] = useState(false);
  const [toggling, setToggling] = useState(false);
  const [hasUnsavedChanges, setHasUnsavedChanges] = useState(false);

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
    setHasUnsavedChanges(false);
  }, [event.note]);

  // Handle note changes
  const handleNoteChange = (e) => {
    setLocalNote(e.target.value);
    setHasUnsavedChanges(e.target.value !== (event.note || ""));
  };

  // Save event note
  const handleSave = async () => {
    setSaving(true);
    try {
      const updated = await updateEvent(event.id, localNote);
      if (onEventUpdate) {
        onEventUpdate(updated);
      }
      setHasUnsavedChanges(false);
    } catch (err) {
      console.error("Failed to update event:", err);
      alert("Failed to save changes");
    } finally {
      setSaving(false);
    }
  };

  // Toggle completion status
  const handleToggleComplete = async () => {
    setToggling(true);
    try {
      const updated = await toggleEventCompleted(event.id);
      if (onEventUpdate) {
        onEventUpdate(updated);
      }
    } catch (err) {
      console.error("Failed to toggle completion:", err);
      alert("Failed to update status");
    } finally {
      setToggling(false);
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
          <div className="flex items-center gap-2">
            <button
              onClick={handleToggleComplete}
              disabled={toggling}
              className={`px-3 py-1 text-xs rounded font-medium transition cursor-pointer ${
                event.completed
                  ? "bg-green-100 text-green-700 hover:bg-green-200"
                  : "bg-gray-100 text-gray-600 hover:bg-gray-200"
              } disabled:opacity-50`}
            >
              {toggling ? "..." : event.completed ? "✓ Completed" : "Mark Complete"}
            </button>
          </div>
        </div>
      </div>

      {/* Content */}
      <div className="p-4 space-y-4">
        {/* Event Note */}
        <div>
          <div className="flex items-center justify-between mb-1">
            <label className="block text-sm font-medium text-gray-700">
              Notes
            </label>
            {hasUnsavedChanges && (
              <span className="text-xs text-orange-600">Unsaved changes</span>
            )}
          </div>
          <textarea
            value={localNote}
            onChange={handleNoteChange}
            placeholder="Add notes about this event..."
            className="w-full border rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 resize-none"
            rows={3}
          />
          {hasUnsavedChanges && (
            <button
              onClick={handleSave}
              disabled={saving}
              className="mt-2 px-4 py-2 text-sm rounded bg-blue-500 text-white hover:bg-blue-600 disabled:opacity-50 transition cursor-pointer"
            >
              {saving ? "Saving..." : "Save Note"}
            </button>
          )}
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