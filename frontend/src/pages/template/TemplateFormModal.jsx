import { useState, useEffect, readOnly } from "react";
import { createTemplate, updateTemplate } from "../../api/templateApi";
import { createEntry, updateEntry, deleteEntry, getEntriesBySubject } from "../../api/entryApi";
import EntryRenderer from "../../components/entries/EntryRenderer";

export default function TemplateFormModal({ userId, template, onClose, onSaved }) {
  const isEdit = Boolean(template);

  const [name, setName] = useState("");
  const [note, setNote] = useState("");
  const [defaultDuration, setDefaultDuration] = useState(30);
  const [color, setColor] = useState("BLUE");
  const [entries, setEntries] = useState([]);
  const [removedEntryIds, setRemovedEntryIds] = useState([]);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState(null);

  const COLORS = ["BLUE", "GREEN", "RED", "YELLOW", "PURPLE"];
  const ENTRY_TYPES = ["TEXT", "NUMBER", "CHECKBOX", "TABLE"];
  const readOnly = false;

  // Fetch entries for template if editing
  useEffect(() => {
    if (template) {
      setName(template.name ?? "");
      setNote(template.note ?? "");
      setDefaultDuration(template.defaultDuration ?? 30);
      setColor(template.color ?? "BLUE");

      (async () => {
        try {
          const fetched = await getEntriesBySubject("TEMPLATE", template.id);
          setEntries(
            fetched.map(e => ({
              id: e.id,
              label: e.label || "",
              type: e.type || "TEXT",
              value: e.value || "",
              previousValues: { [e.type]: e.value || "" }
            }))
          );
        } catch (err) {
          console.error("Failed to fetch entries", err);
          setEntries([]);
        }
      })();
    } else {
      setEntries([]);
    }
    setRemovedEntryIds([]);
  }, [template]);

  // Add new entry
  const addEntry = () => setEntries([...entries, { label: "", type: "TEXT", value: "", previousValues: {} }]);

  // Remove entry (mark for deletion if it has an id)
  const removeEntry = (index) => {
    const entryToRemove = entries[index];
    if (entryToRemove.id) setRemovedEntryIds([...removedEntryIds, entryToRemove.id]);
    setEntries(entries.filter((_, i) => i !== index));
  };

  // Update an entry locally, including type switching
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

        newEntries[index].type = value;
        newEntries[index].value = newValue;
        newEntries[index].previousValues = previousValues;
      } else {
        newEntries[index][fieldOrEntry] = value;
      }
    } else {
      newEntries[index] = fieldOrEntry;
    }

    setEntries(newEntries);
  };

  // Handle submit: delete removed, update existing, create new entries
  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    setError(null);

    try {
      // 1️⃣ Save template first
      const templatePayload = { name, note, defaultDuration: Number(defaultDuration), color };
      const savedTemplate = isEdit
        ? await updateTemplate(template.id, templatePayload)
        : await createTemplate(userId, templatePayload);

      // 2️⃣ Delete removed entries
      for (let entryId of removedEntryIds) {
        try {
          await deleteEntry(entryId);
        } catch (err) {
          console.error("Failed to delete entry:", err);
        }
      }
      setRemovedEntryIds([]);

      // 3️⃣ Create or update remaining entries
      for (let entry of entries) {
        if (!entry.label.trim()) continue;

        const payload = {
          type: entry.type,
          subjectType: "TEMPLATE",
          subjectId: savedTemplate.id,
          label: entry.label,
          value: entry.value
        };

        if (entry.id) {
          await updateEntry(entry.id, payload);
        } else {
          const savedEntry = await createEntry(userId, payload);
          entry.id = savedEntry.id;
        }
      }

      // 4️⃣ Optionally refresh entries to keep template updated
      const updatedEntries = await getEntriesBySubject("TEMPLATE", savedTemplate.id);
      savedTemplate.entries = updatedEntries;

      onSaved(savedTemplate);
      onClose();
    } catch (err) {
      console.error(err);
      setError("Failed to save template or entries");
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-black/40 flex items-center justify-center">
      <div className="bg-white rounded shadow-lg w-full max-w-md p-6 max-h-[90vh] overflow-y-auto">
        <h2 className="text-lg font-semibold mb-4">
          {isEdit ? "Edit Template" : "New Template"}
        </h2>

        <form onSubmit={handleSubmit} className="space-y-4">
          {/* Template fields */}
          <div>
            <label className="block text-sm font-medium mb-1">Name</label>
            <input
              value={name}
              onChange={(e) => setName(e.target.value)}
              className="w-full border rounded px-3 py-2"
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium mb-1">Note</label>
            <textarea
              value={note}
              onChange={(e) => setNote(e.target.value)}
              className="w-full border rounded px-3 py-2"
              rows={3}
            />
          </div>

          <div>
            <label className="block text-sm font-medium mb-1">Default Duration (minutes)</label>
            <input
              type="number"
              min={0}
              value={defaultDuration}
              onChange={(e) => setDefaultDuration(e.target.value)}
              className="w-full border rounded px-3 py-2"
            />
          </div>

          <div>
            <label className="block text-sm font-medium mb-1">Color</label>
            <select
              value={color}
              onChange={(e) => setColor(e.target.value)}
              className="w-full border rounded px-3 py-2 cursor-pointer"
            >
              {COLORS.map(c => <option key={c} value={c}>{c}</option>)}
            </select>
          </div>

          {/* Entries section */}
          <div className="mt-4 border-t pt-4">
            <h3 className="font-medium mb-2">Entries</h3>
            {entries.map((entry, index) => (
              <div key={index} className="flex flex-col gap-2 mb-2">
                <div className="flex gap-2 items-center">
                  <input
                    value={entry.label}
                    onChange={(e) => updateEntryLocal(index, "label", e.target.value)}
                    placeholder="Label"
                    className="border rounded px-2 py-1 flex-1"
                    required
                  />
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
              + Add Entry
            </button>
          </div>

          {error && <div className="text-sm text-red-600">{error}</div>}

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
                : isEdit ? "Save Changes" : "Create"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
