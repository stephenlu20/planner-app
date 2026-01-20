import { useState, useEffect, useRef } from "react";

export default function TableEntry({ entry, onChange, readOnly, allowResize = true }) {
  const [table, setTable] = useState({ rows: 1, cols: 1, cells: [[""]] });
  const [hoveredRow, setHoveredRow] = useState(null);
  const [hoveredCol, setHoveredCol] = useState(null);
  const [cellPositions, setCellPositions] = useState({ rows: [], cols: [] });

  const tableRef = useRef(null);

  useEffect(() => {
    try {
      setTable(entry.value ? JSON.parse(entry.value) : { rows: 1, cols: 1, cells: [[""]] });
    } catch {
      setTable({ rows: 1, cols: 1, cells: [[""]] });
    }
  }, [entry.value]);

  // Calculate actual cell positions after render
  useEffect(() => {
    if (!tableRef.current || readOnly || !allowResize) return;

    const updatePositions = () => {
      const rows = tableRef.current.querySelectorAll('tbody tr');
      const rowPositions = Array.from(rows).map(row => ({
        top: row.offsetTop,
        height: row.offsetHeight
      }));

      const firstRow = rows[0];
      const cells = firstRow?.querySelectorAll('td') || [];
      const colPositions = Array.from(cells).map(cell => ({
        left: cell.offsetLeft,
        width: cell.offsetWidth
      }));

      setCellPositions({ rows: rowPositions, cols: colPositions });
    };

    updatePositions();
    window.addEventListener('resize', updatePositions);
    return () => window.removeEventListener('resize', updatePositions);
  }, [table, readOnly, allowResize]);

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

  const removeRow = (rowIndex) => {
    if (table.rows <= 1) return;
    const newCells = table.cells.filter((_, i) => i !== rowIndex);
    updateTable({ ...table, rows: table.rows - 1, cells: newCells });
  };

  const removeCol = (colIndex) => {
    if (table.cols <= 1) return;
    const newCells = table.cells.map(r => r.filter((_, i) => i !== colIndex));
    updateTable({ ...table, cols: table.cols - 1, cells: newCells });
  };

  return (
    <div className="space-y-2">
      {/* Label */}
      {readOnly && <div className="font-medium mb-1">{entry.label}</div>}

      {/* Table wrapper with extra padding for delete buttons (only if allowResize) */}
      <div 
        className="relative inline-block" 
        style={{ 
          paddingRight: allowResize && !readOnly ? '32px' : '0', 
          paddingBottom: allowResize && !readOnly ? '32px' : '0' 
        }}
      >
        <div ref={tableRef}>
          {/* Table */}
          <table className="border-collapse border w-full">
            <tbody>
              {table.cells.map((row, rIdx) => (
                <tr key={rIdx}>
                  {row.map((cell, cIdx) => (
                    <td
                      key={cIdx}
                      className={`border p-1 ${readOnly ? "bg-gray-100" : "bg-white"}`}
                    >
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
        </div>

        {/* Delete buttons - only show if allowResize is true */}
        {!readOnly && allowResize && cellPositions.rows.length > 0 && (
          <>
            {/* Row delete buttons (right side) */}
            {cellPositions.rows.map((pos, rIdx) => (
              <div
                key={`row-${rIdx}`}
                className="absolute"
                style={{
                  top: pos.top,
                  right: 0,
                  height: pos.height,
                  width: '28px',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center'
                }}
                onMouseEnter={() => setHoveredRow(rIdx)}
                onMouseLeave={() => setHoveredRow(null)}
              >
                {hoveredRow === rIdx && (
                  <button
                    type="button"
                    onClick={() => removeRow(rIdx)}
                    className="bg-red-500 text-white w-6 h-6 rounded hover:bg-red-600 flex items-center justify-center text-lg"
                  >
                    ×
                  </button>
                )}
              </div>
            ))}

            {/* Column delete buttons (bottom) */}
            {cellPositions.cols.map((pos, cIdx) => (
              <div
                key={`col-${cIdx}`}
                className="absolute"
                style={{
                  left: pos.left,
                  bottom: 0,
                  width: pos.width,
                  height: '28px',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center'
                }}
                onMouseEnter={() => setHoveredCol(cIdx)}
                onMouseLeave={() => setHoveredCol(null)}
              >
                {hoveredCol === cIdx && (
                  <button
                    type="button"
                    onClick={() => removeCol(cIdx)}
                    className="bg-red-500 text-white w-6 h-6 rounded hover:bg-red-600 flex items-center justify-center text-lg"
                  >
                    ×
                  </button>
                )}
              </div>
            ))}
          </>
        )}
      </div>

      {/* Add row/column buttons - only show if allowResize is true */}
      {!readOnly && allowResize && (
        <div className="flex gap-2 mt-1 mb-2" style={{ marginTop: '-12px' }}>
          <button type="button" onClick={addRow} className="text-sm text-blue-500">
            + Row
          </button>
          <button type="button" onClick={addCol} className="text-sm text-blue-500">
            + Column
          </button>
        </div>
      )}

      {/* Note */}
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