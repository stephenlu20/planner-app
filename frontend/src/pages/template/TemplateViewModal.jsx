export default function TemplateViewModal({ template, onClose }) {
  return (
    <div className="fixed inset-0 bg-black/40 flex items-center justify-center">
      <div className="bg-white rounded shadow-lg w-full max-w-md p-6">
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

          <div>
            <span className="font-medium">Default Duration:</span>
            <div>
              {template.defaultDuration} minutes
            </div>
          </div>

          <div>
            <span className="font-medium">Color:</span>
            <div>{template.color}</div>
          </div>
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
