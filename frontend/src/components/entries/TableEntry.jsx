import { useState, useEffect } from "react";

export default function TableEntry({ entry, onChange }) {
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
      <table className="border-collapse border mb-2">
        <tbody>
          {table.cells.map((row, rIdx) => (
            <tr key={rIdx}>
              {row.map((cell, cIdx) => (
                <td key={cIdx} className="border p-1">
                  <input
                    value={cell}
                    onChange={(e) => updateCell(rIdx, cIdx, e.target.value)}
                    className="w-full"
                  />
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
      <div className="flex gap-2">
        <button type="button" onClick={addRow} className="text-sm text-blue-500">+ Row</button>
        <button type="button" onClick={addCol} className="text-sm text-blue-500">+ Column</button>
      </div>
    </div>
  );
}
