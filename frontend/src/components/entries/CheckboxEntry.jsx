import { useState, useEffect } from "react";

export default function CheckboxEntry({ entry, onChange }) {
  const [items, setItems] = useState([]);

  useEffect(() => {
    try {
      setItems(entry.value ? JSON.parse(entry.value) : []);
    } catch {
      setItems([]);
    }
  }, [entry.value]);

  const updateItems = (newItems) => {
    setItems(newItems);
    onChange({ ...entry, value: JSON.stringify(newItems) });
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

  return (
    <div>
      {items.map((item, idx) => (
        <div key={idx} className="flex gap-2 items-center mb-1">
          <input type="checkbox" checked={item.checked} onChange={() => toggleItem(idx)} />
          <input
            type="text"
            value={item.label}
            onChange={(e) => updateLabel(idx, e.target.value)}
            className="border px-2 py-1 flex-1"
          />
          <button onClick={() => removeItem(idx)} className="text-red-500">×</button>
        </div>
      ))}
      <button type="button" onClick={addItem} className="text-sm text-blue-500">+ Add Item</button>
    </div>
  );
}
