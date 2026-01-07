export default function NumberEntry({ entry, onChange, readOnly }) {
  const handleValueChange = (e) => {
    onChange?.({ ...entry, value: e.target.value });
  };

  const handleNoteChange = (e) => {
    onChange?.({ ...entry, note: e.target.value });
  };

  return (
    <div className="space-y-1">
      {/* Label (display only in read-only mode) */}
      {readOnly && <div className="font-medium">{entry.label}</div>}

      {/* Value input */}
      <input
        type="number"
        value={entry.value || ""}
        onChange={handleValueChange}
        disabled={readOnly}
        className={`border rounded px-2 py-1 w-full ${readOnly ? "bg-gray-100" : "bg-white cursor-text"}`}
      />

      {/* Note input */}
      <textarea
        value={entry.note || ""}
        onChange={handleNoteChange}
        placeholder="Note (optional)"
        disabled={readOnly}
        className={`border rounded px-2 py-1 w-full ${readOnly ? "bg-gray-100" : "bg-white cursor-text"}`}
      />
    </div>
  );
}
