import { useState } from "react";
import {
  createEntry,
  getEntryById,
  getEntriesByUser,
  getEntriesBySubject,
  updateEntry,
  deleteEntry
} from "../../api/entryApi";

export default function EntryTester() {
  const [result, setResult] = useState(null);

  const [createUserId, setCreateUserId] = useState("");
  const [createType, setCreateType] = useState("");
  const [createSubjectType, setCreateSubjectType] = useState("");
  const [createSubjectId, setCreateSubjectId] = useState("");
  const [createLabel, setCreateLabel] = useState("");
  const [createValue, setCreateValue] = useState("");

  // GET BY ID
  const [getId, setGetId] = useState("");

  // GET BY USER
  const [getUserId, setGetUserId] = useState("");

  // GET BY SUBJECT
  const [subjectType, setSubjectType] = useState("");
  const [subjectId, setSubjectId] = useState("");

  // UPDATE
  const [updateId, setUpdateId] = useState("");
  const [updateType, setUpdateType] = useState("");
  const [updateSubjectType, setUpdateSubjectType] = useState("");
  const [updateSubjectId, setUpdateSubjectId] = useState("");
  const [updateLabel, setUpdateLabel] = useState("");
  const [updateValue, setUpdateValue] = useState("");

  // DELETE
  const [deleteId, setDeleteId] = useState("");

  const buttonClasses =
    "px-4 py-2 rounded font-medium cursor-pointer transition transform active:scale-95";

  const boxClasses = "border rounded-lg p-4 space-y-3 bg-white shadow-sm";

  return (
    <div className="p-6 space-y-6">
      {/* CREATE */}
      <div className={boxClasses}>
        <h2 className="font-semibold">Create Entry</h2>

        <input
          className="border px-2 py-1 rounded w-full"
          placeholder="User ID"
          value={createUserId}
          onChange={(e) => setCreateUserId(e.target.value)}
        />

        <div className="flex gap-2">
          <input
            className="border px-2 py-1 rounded w-full"
            placeholder="Type (e.g. TEXT, NUMBER, CHECKBOX, TABLE)"
            value={createType}
            onChange={(e) => setCreateType(e.target.value)}
          />
          <input
            className="border px-2 py-1 rounded w-full"
            placeholder="Subject Type (EVENT, TEMPLATE)"
            value={createSubjectType}
            onChange={(e) => setCreateSubjectType(e.target.value)}
          />
        </div>

        <input
          className="border px-2 py-1 rounded w-full"
          placeholder="Subject ID"
          value={createSubjectId}
          onChange={(e) => setCreateSubjectId(e.target.value)}
        />

        <input
          className="border px-2 py-1 rounded w-full"
          placeholder="Label"
          value={createLabel}
          onChange={(e) => setCreateLabel(e.target.value)}
        />

        <input
          className="border px-2 py-1 rounded w-full"
          placeholder="Value"
          value={createValue}
          onChange={(e) => setCreateValue(e.target.value)}
        />

        <button
          className={`${buttonClasses} bg-blue-600 text-white hover:bg-blue-700`}
          onClick={async () => {
            try {
              const res = await createEntry(createUserId, {
                type: createType,
                subjectType: createSubjectType,
                subjectId: createSubjectId,
                label: createLabel,
                value: createValue
              });
              setResult(res);
            } catch (err) {
              setResult({ error: err.response?.data || err.message });
            }
          }}
        >
          Create
        </button>
      </div>

      {/* GET BY ID */}
      <div className={boxClasses}>
        <h2 className="font-semibold">Get Entry by ID</h2>

        <input
          className="border px-2 py-1 rounded w-full"
          placeholder="Entry ID"
          value={getId}
          onChange={(e) => setGetId(e.target.value)}
        />

        <button
          className={`${buttonClasses} bg-gray-600 text-white hover:bg-gray-700`}
          onClick={async () => {
            try {
              const res = await getEntryById(getId);
              setResult(res);
            } catch (err) {
              setResult({ error: err.response?.data || err.message });
            }
          }}
        >
          Fetch
        </button>
      </div>

      {/* GET BY USER */}
      <div className={boxClasses}>
        <h2 className="font-semibold">Get Entries by User</h2>

        <input
          className="border px-2 py-1 rounded w-full"
          placeholder="User ID"
          value={getUserId}
          onChange={(e) => setGetUserId(e.target.value)}
        />

        <button
          className={`${buttonClasses} bg-purple-600 text-white hover:bg-purple-700`}
          onClick={async () => {
            try {
              const res = await getEntriesByUser(getUserId);
              setResult(res);
            } catch (err) {
              setResult({ error: err.response?.data || err.message });
            }
          }}
        >
          Fetch
        </button>
      </div>

      {/* GET BY SUBJECT */}
      <div className={boxClasses}>
        <h2 className="font-semibold">Get Entries by Subject</h2>

        <div className="flex gap-2">
          <input
            className="border px-2 py-1 rounded w-full"
            placeholder="Subject Type"
            value={subjectType}
            onChange={(e) => setSubjectType(e.target.value)}
          />
          <input
            className="border px-2 py-1 rounded w-full"
            placeholder="Subject ID"
            value={subjectId}
            onChange={(e) => setSubjectId(e.target.value)}
          />
        </div>

        <button
          className={`${buttonClasses} bg-green-600 text-white hover:bg-green-700`}
          onClick={async () => {
            try {
              const res = await getEntriesBySubject(subjectType, subjectId);
              setResult(res);
            } catch (err) {
              setResult({ error: err.response?.data || err.message });
            }
          }}
        >
          Fetch
        </button>
      </div>

      {/* UPDATE */}
      <div className={boxClasses}>
        <h2 className="font-semibold">Update Entry</h2>

        <input
          className="border px-2 py-1 rounded w-full"
          placeholder="Entry ID"
          value={updateId}
          onChange={(e) => setUpdateId(e.target.value)}
        />

        <input
          className="border px-2 py-1 rounded w-full"
          placeholder="Label"
          value={updateLabel}
          onChange={(e) => setUpdateLabel(e.target.value)}
        />

        <input
          className="border px-2 py-1 rounded w-full"
          placeholder="Value"
          value={updateValue}
          onChange={(e) => setUpdateValue(e.target.value)}
        />

        <button
          className={`${buttonClasses} bg-yellow-500 text-white hover:bg-yellow-600`}
          onClick={async () => {
            try {
              const res = await updateEntry(updateId, {
                type: updateType,
                subjectType: updateSubjectType,
                subjectId: updateSubjectId,
                label: updateLabel,
                value: updateValue
              });
              setResult(res);
            } catch (err) {
              setResult({ error: err.response?.data || err.message });
            }
          }}
        >
          Update
        </button>
      </div>

      {/* DELETE */}
      <div className={boxClasses}>
        <h2 className="font-semibold">Delete Entry</h2>

        <input
          className="border px-2 py-1 rounded w-full"
          placeholder="Entry ID"
          value={deleteId}
          onChange={(e) => setDeleteId(e.target.value)}
        />

        <button
          className={`${buttonClasses} bg-red-600 text-white hover:bg-red-700`}
          onClick={async () => {
            try {
              await deleteEntry(deleteId);
              setResult({ message: "Deleted successfully" });
            } catch (err) {
              setResult({ error: err.response?.data || err.message });
            }
          }}
        >
          Delete
        </button>
      </div>

      {/* RESULT */}
      {result && (
        <pre className="bg-gray-100 p-3 rounded text-sm overflow-x-auto">
          {JSON.stringify(result, null, 2)}
        </pre>
      )}
    </div>
  );
}
