import { useState, useEffect } from "react";
import { getEntriesBySubject } from "../../api/entryApi";
import EntryRenderer from "../../components/entries/EntryRenderer";

export default function TemplateViewModal({ template, onClose }) {
  const [entries, setEntries] = useState([]);

  useEffect(() => {
    if (template?.id) {
      (async () => {
        try {
          const fetched = await getEntriesBySubject("TEMPLATE", template.id);
          setEntries(fetched);
        } catch (err) {
          console.error("Failed to fetch entries:", err);
        }
      })();
    }
  }, [template]);

  const readOnly = true;
  return (
    <div className="fixed inset-0 bg-black/40 flex items-center justify-center">
      <div className="bg-white rounded shadow-lg w-full max-w-md p-6 max-h-[90vh] overflow-y-auto">
        <h2 className="text-lg font-semibold mb-4">
          Template Details
        </h2>

        <div className="space-y-3 text-sm">
          <div>
            <span className="font-medium">Name:</span>
            <div>{template.name}</div>
          </div>

          <div>
            <span className="font-medium">Note:</span>
            <div className="text-gray-700">
              {template.note || "—"}
            </div>
          </div>
        </div>
        <div className="mt-4 border-t pt-4">
          <h3 className="font-medium mb-2">Entries</h3>
          {entries.length ? (
            entries.map((entry) => (
              <div key={entry.id} className="mb-2">
                <EntryRenderer entry={entry} readOnly={readOnly} />
              </div>
            ))
          ) : (
            <div className="text-gray-500 text-sm">No entries</div>
          )}
        </div>

        <div className="flex justify-end mt-6">
          <button
            onClick={onClose}
            className="px-4 py-2 rounded bg-blue-500 text-white hover:bg-blue-400 transition cursor-pointer"
          >
            Close
          </button>
        </div>
      </div>
    </div>
  );
}
