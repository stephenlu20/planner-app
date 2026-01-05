import { useState, useEffect } from "react";
import { createTemplate, updateTemplate } from "../../api/templateApi";

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
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState(null);

  const COLORS = ["BLUE", "GREEN", "RED", "YELLOW", "PURPLE"];

  useEffect(() => {
    if (template) {
      setName(template.name ?? "");
      setNote(template.note ?? "");
      setDefaultDuration(template.defaultDuration ?? 30);
      setColor(template.color ?? "BLUE");
    }
  }, [template]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    setError(null);

    try {
      const payload = {
        name,
        note,
        defaultDuration: Number(defaultDuration),
        color
      };

      const saved = isEdit
        ? await updateTemplate(template.id, payload)
        : await createTemplate(userId, payload);

      onSaved(saved);
      onClose();
    } catch (err) {
      setError("Failed to save template");
      console.error(err);
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-black/40 flex items-center justify-center">
      <div className="bg-white rounded shadow-lg w-full max-w-md p-6">
        <h2 className="text-lg font-semibold mb-4">
          {isEdit ? "Edit Template" : "New Template"}
        </h2>

        <form onSubmit={handleSubmit} className="space-y-4">
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
              {COLORS.map((c) => (
                <option key={c} value={c}>
                  {c}
                </option>
              ))}
            </select>
          </div>

          {error && (
            <div className="text-sm text-red-600">{error}</div>
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
                : isEdit ? "Save Changes" : "Create"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
