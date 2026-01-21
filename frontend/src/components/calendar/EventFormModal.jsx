import { useState, useEffect } from "react";
import { createEvent, updateEvent } from "../../api/eventApi";
import { createEntry, updateEntry, deleteEntry, getEntriesBySubject } from "../../api/entryApi";
import EntryRenderer from "../../components/entries/EntryRenderer";

export default function EventFormModal({ userId, calendarId, event, selectedDate, onClose, onSaved }) {
  const isEdit = Boolean(event);

  const [title, setTitle] = useState("");
  const [note, setNote] = useState("");
  const [dateTime, setDateTime] = useState("");
  const [entries, setEntries] = useState([]);
  const [removedEntryIds, setRemovedEntryIds] = useState([]);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState(null);

  const ENTRY_TYPES = ["TEXT", "NUMBER", "CHECKBOX", "TABLE", "HEADER"];
  const readOnly = false;

  // Load event and entries if editing
  useEffect(() => {
    if (event) {
      setTitle(event.title || "");
      setNote(event.note || "");
      
      // Format datetime for input (YYYY-MM-DDTHH:MM)
      if (event.dateTime) {
        const date = new Date(event.dateTime);
        const formatted = new Date(date.getTime() - date.getTimezoneOffset() * 60000)
          .toISOString()
          .slice(0, 16);
        setDateTime(formatted);
      }

      (async () => {
        try {
          const fetchedEntries = await getEntriesBySubject("EVENT", event.id);
          
          setEntries(
            fetchedEntries.map((e) => ({
              id: e.id,
              label: e.label || "",
              type: e.type || "TEXT",
              value: e.value || "",
              note: e.note || "",
              subjectType: "EVENT",
              subjectId: event.id,
              previousValues: { [e.type]: e.value || "" },
            }))
          );
        } catch (err) {
          console.error("Failed to fetch event entries", err);
          setEntries([]);
        }
      })();
    } else {
      // For new events, set default date/time
      if (selectedDate) {
        const date = new Date(selectedDate);
        const formatted = new Date(date.getTime() - date.getTimezoneOffset() * 60000)
          .toISOString()
          .slice(0, 16);
        setDateTime(formatted);
      }
      setEntries([]);
    }
    setRemovedEntryIds([]);
  }, [event, selectedDate]);

  const addEntry = () =>
    setEntries([
      ...entries,
      { 
        label: "", 
        type: "TEXT", 
        value: "", 
        note: "", 
        subjectType: "EVENT", 
        subjectId: event?.id ?? null, 
        previousValues: {} 
      },
    ]);

  const removeEntry = (index) => {
    const entryToRemove = entries[index];
    if (entryToRemove.id) setRemovedEntryIds([...removedEntryIds, entryToRemove.id]);
    setEntries(entries.filter((_, i) => i !== index));
  };

  const updateEntryLocal = (index, fieldOrEntry, value) => {
    const newEntries = [...entries];
    if (typeof fieldOrEntry === "string") {
      if (fieldOrEntry === "type") {
        const oldType = newEntries[index].type;
        const oldValue = newEntries[index].value;
        const previousValues = newEntries[index].previousValues || {};
        previousValues[oldType] = oldValue;

        let newValue = previousValues[value];
        if (newValue === undefined) {
          if (value === "CHECKBOX") newValue = JSON.stringify([]);
          else if (value === "TABLE") newValue = JSON.stringify({ rows: 1, cols: 1, cells: [[""]] });
          else newValue = "";
        }

        newEntries[index] = {
          ...newEntries[index],
          type: value,
          value: newValue,
          previousValues,
        };
      } else {
        newEntries[index] = { ...newEntries[index], [fieldOrEntry]: value };
      }
    } else if (typeof fieldOrEntry === "object") {
      newEntries[index] = { ...newEntries[index], ...fieldOrEntry };
    }
    setEntries(newEntries);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    setError(null);

    try {
      // Parse the datetime
      const eventDateTime = dateTime ? new Date(dateTime).toISOString() : null;

      let savedEvent;
      if (isEdit) {
        // Update event - for now we only support updating note via existing API
        savedEvent = await updateEvent(event.id, note);
        // TODO: We might need to extend the updateEvent API to handle title and dateTime
      } else {
        // Create new event
        // Note: Current createEvent API doesn't support title or dateTime, 
        // you may need to extend it
        savedEvent = await createEvent({
          note,
          orderIndex: 0, // You might want to calculate this
          userId,
          calendarId
        });
      }

      // Delete removed entries
      for (let entryId of removedEntryIds) {
        try {
          await deleteEntry(entryId);
        } catch (err) {
          console.error("Failed to delete entry:", err);
        }
      }
      setRemovedEntryIds([]);

      // Process entries
      for (let i = 0; i < entries.length; i++) {
        const entry = entries[i];

        if (!entry.label?.trim()) continue;

        const payload = {
          type: entry.type,
          subjectType: "EVENT",
          subjectId: savedEvent.id,
          label: entry.label || "",
          value: entry.type === "HEADER" ? null : entry.value || "",
          note: entry.type === "HEADER" ? (entry.note || "") : entry.note || "",
          orderIndex: i
        };

        if (entry.type === "HEADER" && !entry.note) {
          payload.note = null;
        }

        if (entry.id) {
          const updatePayload = {
            label: entry.label,
            value: entry.value ?? "",
            note: entry.note ?? "",
            orderIndex: i,
          };
          await updateEntry(entry.id, updatePayload);
        } else {
          const savedEntry = await createEntry(userId, payload);
          entry.id = savedEntry.id;
        }
      }

      // Refresh entries
      const updatedEntries = await getEntriesBySubject("EVENT", savedEvent.id);
      savedEvent.entries = updatedEntries;

      onSaved(savedEvent);
      onClose();
    } catch (err) {
      console.error(err);
      setError(err.response?.data?.message || "Failed to save event");
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50">
      <div className="bg-white rounded shadow-lg w-full max-w-2xl p-6 max-h-[90vh] overflow-y-auto">
        <h2 className="text-lg font-semibold mb-4">
          {isEdit ? "Edit Event" : "New Event"}
        </h2>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium mb-1">Title</label>
            <input
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              className="w-full border rounded px-3 py-2"
              placeholder="Event title"
            />
          </div>

          <div>
            <label className="block text-sm font-medium mb-1">Date & Time</label>
            <input
              type="datetime-local"
              value={dateTime}
              onChange={(e) => setDateTime(e.target.value)}
              className="w-full border rounded px-3 py-2"
            />
          </div>

          <div>
            <label className="block text-sm font-medium mb-1">Note</label>
            <textarea
              value={note}
              onChange={(e) => setNote(e.target.value)}
              className="w-full border rounded px-3 py-2"
              rows={3}
              placeholder="Event notes"
            />
          </div>

          <div className="mt-4 border-t pt-4">
            <h3 className="font-medium mb-2">Details</h3>
            {entries.map((entry, index) => (
              <div key={index} className="flex flex-col gap-2 mb-2">
                <div className="flex gap-2 items-center">
                  {entry.type !== "HEADER" && (
                    <input
                      value={entry.label}
                      onChange={(e) => updateEntryLocal(index, "label", e.target.value)}
                      placeholder="Label"
                      className="border rounded px-2 py-1 flex-1"
                      required
                    />
                  )}
                  <select
                    value={entry.type}
                    onChange={(e) => updateEntryLocal(index, "type", e.target.value)}
                    className="border rounded px-2 py-1"
                  >
                    {ENTRY_TYPES.map(t => <option key={t} value={t}>{t}</option>)}
                  </select>
                  <button
                    type="button"
                    onClick={() => removeEntry(index)}
                    className="px-2 py-1 text-red-500 font-bold"
                  >
                    ×
                  </button>
                </div>
                <EntryRenderer
                  entry={entry}
                  onChange={(updatedEntry) => updateEntryLocal(index, updatedEntry)}
                  readOnly={readOnly}
                />
              </div>
            ))}

            <button
              type="button"
              onClick={addEntry}
              className="text-sm text-blue-500 mt-1"
            >
              + Add Detail
            </button>
          </div>

          {error && (
            <div className="text-sm text-red-600 bg-red-50 border border-red-200 rounded p-2">
              {error}
            </div>
          )}

          <div className="flex justify-end gap-2 pt-2">
            <button
              type="button"
              onClick={onClose}
              className="px-3 py-2 text-sm rounded border cursor-pointer transition"
            >
              Cancel
            </button>

            <button
              type="submit"
              disabled={saving}
              className="px-4 py-2 text-sm rounded bg-blue-500 text-white disabled:opacity-50 cursor-pointer transition"
            >
              {saving
                ? isEdit ? "Saving..." : "Creating..."
                : isEdit ? "Save Changes" : "Create Event"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}