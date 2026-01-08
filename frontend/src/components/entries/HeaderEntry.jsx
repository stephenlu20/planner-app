import { useState, useEffect } from "react";

export default function HeaderEntry({ entry, onChange, readOnly }) {
  const [noteEnabled, setNoteEnabled] = useState(Boolean(entry.note));

  useEffect(() => {
    // Sync external changes
    setNoteEnabled(Boolean(entry.note));
  }, [entry.note]);

  const handleLabelChange = (e) => {
    onChange?.({ ...entry, label: e.target.value });
  };

  const handleToggle = () => {
    const next = !noteEnabled;
    setNoteEnabled(next);

    // IMPORTANT: clear note immediately when toggled off
    if (!next) {
      onChange?.({ ...entry, note: "" });
    }
  };

  const handleNoteChange = (e) => {
    onChange?.({ ...entry, note: e.target.value });
  };

  return (
    <div className="flex flex-col gap-1 py-2">
      {/* Header row */}
      <div className="flex items-center gap-3">
        {readOnly ? (
          <div className="text-sm font-semibold uppercase tracking-wide text-gray-800">
            {entry.label}
          </div>
        ) : (
          <input
            type="text"
            value={entry.label}
            onChange={handleLabelChange}
            placeholder="Section header"
            className="flex-1 text-sm font-semibold uppercase tracking-wide bg-transparent border-b border-gray-300 focus:outline-none focus:border-gray-500"
            required
          />
        )}

        {/* Slider toggle */}
        {!readOnly && (
          <button
            type="button"
            role="switch"
            aria-checked={noteEnabled}
            onClick={handleToggle}
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
        )}
      </div>

      {/* Note */}
      {noteEnabled && (
        <textarea
          value={entry.note || ""}
          onChange={handleNoteChange}
          placeholder="Note (optional)"
          disabled={readOnly}
          className={`mt-1 text-sm border rounded px-2 py-1 w-full ${
            readOnly ? "bg-gray-100" : "bg-white cursor-text"
          }`}
        />
      )}
    </div>
  );
}
