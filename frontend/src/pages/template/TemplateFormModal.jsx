import { useState, useEffect } from "react";
import { createTemplate, updateTemplate } from "../../api/templateApi";
import { createEntry } from "../../api/entryApi";

export default function TemplateFormModal({
  userId,
  template,
  onClose,
  onSaved
}) {
  const isEdit = Boolean(template);

  const [name, setName] = useState("");
  const [note, setNote] = useState("");
  const [defaultDuration, setDefaultDuration] = useState(30);
  const [color, setColor] = useState("BLUE");
  const [entries, setEntries] = useState([]);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState(null);

  const COLORS = ["BLUE", "GREEN", "RED", "YELLOW", "PURPLE"];
  const ENTRY_TYPES = ["TEXT", "NUMBER", "DURATION", "TABLE", "CHECKBOX"];

  // Load template data if editing
  useEffect(() => {
    if (template) {
      setName(template.name ?? "");
      setNote(template.note ?? "");
      setDefaultDuration(template.defaultDuration ?? 30);
      setColor(template.color ?? "BLUE");
      // If template has entries, load them for editing
      setEntries(template.entries?.map(e => ({
        label: e.label || "",
        type: e.type || "TEXT",
        value: e.value || ""
      })) || []);
    }
  }, [template]);

  // Entry manipulation
  const addEntry = () => setEntries([...entries, { label: "", value: "", type: "TEXT" }]);
  const updateEntry = (index, field, value) => {
    const newEntries = [...entries];
    newEntries[index][field] = value;
    setEntries(newEntries);
  };
  const removeEntry = (index) => setEntries(entries.filter((_, i) => i !== index));

  // Submit handler
  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    setError(null);

    try {
      // Save template first
      const payload = { name, note, defaultDuration: Number(defaultDuration), color };
      const savedTemplate = isEdit
        ? await updateTemplate(template.id, payload)
        : await createTemplate(userId, payload);

      // Save entries sequentially
      for (let entry of entries) {
        // Skip empty labels
        if (!entry.label.trim()) continue;

        await createEntry(userId, {
          type: entry.type,
          subjectType: "TEMPLATE",
          subjectId: savedTemplate.id,
          label: entry.label,
          value: entry.value
        });
      }

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
            <label className="block text-sm font-medium mb-1">
              Default Duration (minutes)
            </label>
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
              {COLORS.map(c => (
                <option key={c} value={c}>{c}</option>
              ))}
            </select>
          </div>

          {/* Entries section */}
          <div className="mt-4 border-t pt-4">
            <h3 className="font-medium mb-2">Entries</h3>
            {entries.map((entry, index) => (
              <div key={index} className="flex gap-2 items-center mb-2">
                <input
                  value={entry.label}
                  onChange={(e) => updateEntry(index, "label", e.target.value)}
                  placeholder="Label"
                  className="border rounded px-2 py-1 flex-1"
                  required
                />
                <select
                  value={entry.type}
                  onChange={(e) => updateEntry(index, "type", e.target.value)}
                  className="border rounded px-2 py-1"
                >
                <input
                  value={entry.value}
                  onChange={(e) => updateEntry(index, "value", e.target.value)}
                  placeholder="Value"
                  className="border rounded px-2 py-1 flex-1"
                />
                  {ENTRY_TYPES.map(t => (
                    <option key={t} value={t}>{t}</option>
                  ))}
                </select>
                <button
                  type="button"
                  onClick={() => removeEntry(index)}
                  className="px-2 py-1 text-red-500 font-bold"
                >
                  ×
                </button>
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
