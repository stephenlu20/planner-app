import { useState, useEffect } from "react";

export default function CheckboxEntry({ entry, onChange, readOnly }) {
  const [items, setItems] = useState([]);
  const [note, setNote] = useState(entry.note || "");

  useEffect(() => {
    try {
      setItems(entry.value ? JSON.parse(entry.value) : []);
    } catch {
      setItems([]);
    }
  }, [entry.value]);

  useEffect(() => {
    setNote(entry.note || "");
  }, [entry.note]);

  const updateItems = (newItems) => {
    setItems(newItems);
    onChange({ ...entry, value: JSON.stringify(newItems), note });
  };

  const toggleItem = (idx) => {
    const newItems = [...items];
    newItems[idx].checked = !newItems[idx].checked;
    updateItems(newItems);
  };

  const updateLabel = (idx, label) => {
    const newItems = [...items];
    newItems[idx].label = label;
    updateItems(newItems);
  };

  const addItem = () => updateItems([...items, { label: "", checked: false }]);
  const removeItem = (idx) => updateItems(items.filter((_, i) => i !== idx));

  const handleNoteChange = (e) => {
    const newNote = e.target.value;
    setNote(newNote);
    onChange({ ...entry, value: JSON.stringify(items), note: newNote });
  };

  return (
    <div className="space-y-2">
      {items.map((item, idx) => (
        <div key={idx} className="flex gap-2 items-center">
          <input type="checkbox" checked={item.checked} disabled={readOnly} onChange={() => toggleItem(idx)} />
          <input
            type="text"
            value={item.label}
            disabled={readOnly}
            onChange={(e) => updateLabel(idx, e.target.value)}
            className="border px-2 py-1 flex-1"
          />
          {!readOnly && (
            <button onClick={() => removeItem(idx)} className="text-red-500">
              ×
            </button>
          )}
        </div>
      ))}

      {!readOnly && (
        <button type="button" onClick={addItem} className="text-sm text-blue-500">
          + Add Item
        </button>
      )}

      <textarea
        value={note}
        onChange={handleNoteChange}
        placeholder="Note (optional)"
        disabled={readOnly}
        className="w-full border rounded px-2 py-1 mt-2"
      />
    </div>
  );
}
