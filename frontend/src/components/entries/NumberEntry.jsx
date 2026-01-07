export default function NumberEntry({ entry, onChange, readOnly }) {
  const handleChange = (e) => {
    onChange?.({ ...entry, value: e.target.value });
  };

  return (
    <input
      type="number"
      value={entry.value || ""}
      onChange={handleChange}
      disabled={readOnly}
      className={`border rounded px-2 py-1 w-full ${readOnly ? "bg-gray-100" : "bg-white cursor-text"}`}
    />
  );
}
