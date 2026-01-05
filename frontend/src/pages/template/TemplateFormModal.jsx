import { useState } from "react";
import { createTemplate } from "../../api/templateApi";

export default function TemplateFormModal({ userId, onClose, onCreated }) {
  const [name, setName] = useState("");
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    setError(null);

    try {
      const template = await createTemplate(userId, {
        name
      });

      onCreated(template);
      onClose();
    } catch (err) {
      setError("Failed to create template");
      console.error(err);
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-black/40 flex items-center justify-center">
      <div className="bg-white rounded shadow-lg w-full max-w-md p-6">
        <h2 className="text-lg font-semibold mb-4">
          New Template
        </h2>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium mb-1">
              Name
            </label>
            <input
              value={name}
              onChange={(e) => setName(e.target.value)}
              className="w-full border rounded px-3 py-2"
              required
            />
          </div>

          {error && (
            <div className="text-sm text-red-600">
              {error}
            </div>
          )}

          <div className="flex justify-end gap-2">
            <button
              type="button"
              onClick={onClose}
              className="px-3 py-2 text-sm rounded border transition cursor-pointer"
            >
              Cancel
            </button>

            <button
              type="submit"
              disabled={saving}
              className="px-4 py-2 text-sm rounded bg-blue-500 text-white disabled:opacity-50 transition cursor-pointer"
            >
              {saving ? "Creating..." : "Create"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
