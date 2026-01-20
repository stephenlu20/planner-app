import { useState, useEffect } from "react";
import { getEntriesBySubject, updateEntry } from "../../api/entryApi";
import { toggleEventCompleted, updateEvent } from "../../api/eventApi";
import EntryRenderer from "../entries/EntryRenderer";

export default function EventDayCard({ event, onEventUpdate }) {
  const [entries, setEntries] = useState([]);
  const [originalEntries, setOriginalEntries] = useState([]);
  const [localNote, setLocalNote] = useState(event.note || "");
  const [noteEnabled, setNoteEnabled] = useState(Boolean(event.note));
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
        setOriginalEntries(JSON.parse(JSON.stringify(data))); // Deep clone
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

  // Sync note enabled state with local note content
  useEffect(() => {
    setNoteEnabled(Boolean(localNote));
  }, [localNote]);

  // Check if there are any unsaved changes
  useEffect(() => {
    // Check note changes
    const noteChanged = localNote !== (event.note || "");
    
    // Check entry changes
    const entriesChanged = entries.some((entry, index) => {
      const original = originalEntries[index];
      if (!original) return true;
      return entry.value !== original.value || entry.note !== original.note;
    });

    setHasUnsavedChanges(noteChanged || entriesChanged);
  }, [localNote, entries, event.note, originalEntries]);

  // Handle note toggle
  const handleToggleNote = () => {
    const next = !noteEnabled;
    setNoteEnabled(next);

    // IMPORTANT: clear note immediately when toggled off
    if (!next) {
      setLocalNote("");
    }
  };

  // Handle note changes
  const handleNoteChange = (e) => {
    setLocalNote(e.target.value);
  };

  // Handle entry value/note changes (just update state, don't save yet)
  const handleEntryChange = (index, updatedEntry) => {
    const newEntries = [...entries];
    newEntries[index] = updatedEntry;
    setEntries(newEntries);
  };

  // Save ALL changes (note + all entries)
  const handleSaveAll = async () => {
    setSaving(true);
    try {
      // Save event note
      const updatedEvent = await updateEvent(event.id, localNote);
      
      // Save all entries
      const savePromises = entries.map((entry, index) => {
        const original = originalEntries[index];
        // Only save if changed
        if (original && (entry.value !== original.value || entry.note !== original.note)) {
          return updateEntry(entry.id, {
            label: entry.label,
            value: entry.value,
            note: entry.note
          });
        }
        return Promise.resolve();
      });
      
      await Promise.all(savePromises);
      
      // Update parent component
      if (onEventUpdate) {
        onEventUpdate(updatedEvent);
      }
      
      // Update original entries to match current state
      setOriginalEntries(JSON.parse(JSON.stringify(entries)));
      setHasUnsavedChanges(false);
    } catch (err) {
      console.error("Failed to save changes:", err);
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

  return (
    <div className="border rounded-lg bg-white shadow-sm max-h-[70vh] flex flex-col overflow-hidden">
      {/* Sticky Header */}
      <div className="sticky top-0 z-20 bg-white border-b shadow-sm shrink-0">
        <div className="px-4 py-3 bg-gray-50 rounded-t-lg">
          <div className="flex items-center justify-between mb-2">
            <h3 className="font-semibold text-lg">{event.title || "Event"}</h3>
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
          
          {/* Save button and unsaved indicator */}
          {hasUnsavedChanges && (
            <div className="flex items-center justify-between gap-3 pt-2 border-t border-gray-200">
              <span className="text-xs text-orange-600 font-medium">
                ● Unsaved changes
              </span>
              <button
                onClick={handleSaveAll}
                disabled={saving}
                className="px-4 py-2 text-sm rounded bg-blue-500 text-white hover:bg-blue-600 disabled:opacity-50 transition cursor-pointer font-medium"
              >
                {saving ? "Saving..." : "Save All Changes"}
              </button>
            </div>
          )}
        </div>
      </div>

      {/* Scrollable Content */}
      <div className="overflow-y-auto flex-1 p-4 space-y-4">
        {/* Event Note Section with Toggle */}
        <div>
          {/* Header row with toggle */}
          <div className="flex items-center justify-end mb-2">
            {/* Slider toggle */}
            <button
              type="button"
              role="switch"
              aria-checked={noteEnabled}
              onClick={handleToggleNote}
              className={`relative inline-flex h-5 w-9 items-center rounded-full transition ${
                noteEnabled ? "bg-blue-500" : "bg-gray-300"
              }`}
            >
              <span
                className={`inline-block h-4 w-4 transform rounded-full bg-white transition ${
                  noteEnabled ? "translate-x-4" : "translate-x-1"
                }`}
              />
            </button>
          </div>
          
          {/* Note textarea (conditionally rendered) */}
          {noteEnabled && (
            <textarea
              value={localNote}
              onChange={handleNoteChange}
              className="w-full border rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 resize-none"
              rows={3}
            />
          )}
        </div>

        {/* Entries */}
        {loading ? (
          <div className="text-sm text-gray-500">Loading entries...</div>
        ) : entries.length > 0 ? (
          <div className="space-y-4">
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