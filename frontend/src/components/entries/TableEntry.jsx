import { useState, useEffect } from "react";

export default function TableEntry({ entry, onChange, readOnly }) {
  const [table, setTable] = useState({ rows: 1, cols: 1, cells: [[""]] });

  useEffect(() => {
    try {
      setTable(entry.value ? JSON.parse(entry.value) : { rows: 1, cols: 1, cells: [[""]] });
    } catch {
      setTable({ rows: 1, cols: 1, cells: [[""]] });
    }
  }, [entry.value]);

  const updateTable = (newTable) => {
    setTable(newTable);
    onChange({ ...entry, value: JSON.stringify(newTable) });
  };

  const updateCell = (row, col, val) => {
    const newCells = table.cells.map(r => [...r]);
    newCells[row][col] = val;
    updateTable({ ...table, cells: newCells });
  };

  const handleNoteChange = (e) => {
    onChange?.({ ...entry, note: e.target.value });
  };

  const addRow = () => {
    const newCells = [...table.cells, Array(table.cols).fill("")];
    updateTable({ ...table, rows: table.rows + 1, cells: newCells });
  };

  const addCol = () => {
    const newCells = table.cells.map(r => [...r, ""]);
    updateTable({ ...table, cols: table.cols + 1, cells: newCells });
  };

  return (
    <div>
      {/* Label (display only in read-only mode) */}
      {readOnly && <div className="font-medium">{entry.label}</div>}
      
      <table className="border-collapse border mb-2">
        <tbody>
          {table.cells.map((row, rIdx) => (
            <tr key={rIdx}>
              {row.map((cell, cIdx) => (
                <td key={cIdx} className={`border p-1 ${readOnly ? "bg-gray-100" : "bg-white cursor-text"}`}>
                  <input
                    value={cell}
                    disabled={readOnly}
                    onChange={(e) => updateCell(rIdx, cIdx, e.target.value)}
                    className={`w-full ${readOnly ? "bg-gray-100" : "bg-white cursor-text"}`}
                  />
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
      {!readOnly && (
        <div className="flex gap-2">
          <button type="button" onClick={addRow} disabled={readOnly} className="text-sm text-blue-500">
            + Row
          </button>
          <button type="button" onClick={addCol} disabled={readOnly} className="text-sm text-blue-500">
            + Column
          </button>
        </div>
      )}
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
