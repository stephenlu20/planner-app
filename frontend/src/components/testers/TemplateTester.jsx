import { useState } from "react";
import {
  createTemplate,
  getTemplateById,
  getTemplatesByUser,
  updateTemplate,
  deleteTemplate
} from "../../api/templateApi";

export default function TemplateTester() {
  const [result, setResult] = useState(null);

  // CREATE
  const [createUserId, setCreateUserId] = useState("");
  const [createData, setCreateData] = useState({
    name: "",
    note: "",
    color: "",
    defaultDuration: ""
  });

  // GET BY ID
  const [getId, setGetId] = useState("");

  // GET BY USER
  const [getUserId, setGetUserId] = useState("");

  // UPDATE
  const [updateId, setUpdateId] = useState("");
  const [updateData, setUpdateData] = useState({
    name: "",
    note: "",
    color: "",
    defaultDuration: ""
  });

  // DELETE
  const [deleteId, setDeleteId] = useState("");

  const boxClass = "border rounded p-4 space-y-3 bg-white";
  const inputClass = "border px-2 py-1 rounded w-full";
  const buttonClass =
    "px-4 py-2 rounded font-medium bg-blue-600 text-white hover:bg-blue-700";

  return (
    <div className="p-6 space-y-6">
      {/* CREATE */}
      <div className={boxClass}>
        <h3 className="font-semibold">Create Template</h3>

        <input
          className={inputClass}
          placeholder="User ID"
          value={createUserId}
          onChange={(e) => setCreateUserId(e.target.value)}
        />

        {["name", "note", "color", "defaultDuration"].map((field) => (
          <input
            key={field}
            className={inputClass}
            placeholder={field}
            value={createData[field]}
            onChange={(e) =>
              setCreateData({ ...createData, [field]: e.target.value })
            }
          />
        ))}

        <button
          className={buttonClass}
          onClick={async () => {
            try {
              const res = await createTemplate(createUserId, {
                ...createData,
                defaultDuration: Number(createData.defaultDuration)
              });
              setResult(res);
            } catch (e) {
              setResult({ error: e.message });
            }
          }}
        >
          Create
        </button>
      </div>

      {/* GET BY ID */}
      <div className={boxClass}>
        <h3 className="font-semibold">Get Template by ID</h3>

        <input
          className={inputClass}
          placeholder="Template ID"
          value={getId}
          onChange={(e) => setGetId(e.target.value)}
        />

        <button
          className={buttonClass}
          onClick={async () => {
            try {
              setResult(await getTemplateById(getId));
            } catch (e) {
              setResult({ error: e.message });
            }
          }}
        >
          Fetch
        </button>
      </div>

      {/* GET BY USER */}
      <div className={boxClass}>
        <h3 className="font-semibold">Get Templates by User</h3>

        <input
          className={inputClass}
          placeholder="User ID"
          value={getUserId}
          onChange={(e) => setGetUserId(e.target.value)}
        />

        <button
          className={buttonClass}
          onClick={async () => {
            try {
              setResult(await getTemplatesByUser(getUserId));
            } catch (e) {
              setResult({ error: e.message });
            }
          }}
        >
          Fetch
        </button>
      </div>

      {/* UPDATE */}
      <div className={boxClass}>
        <h3 className="font-semibold">Update Template</h3>

        <input
          className={inputClass}
          placeholder="Template ID"
          value={updateId}
          onChange={(e) => setUpdateId(e.target.value)}
        />

        {["name", "note", "color", "defaultDuration"].map((field) => (
          <input
            key={field}
            className={inputClass}
            placeholder={field}
            value={updateData[field]}
            onChange={(e) =>
              setUpdateData({ ...updateData, [field]: e.target.value })
            }
          />
        ))}

        <button
          className={buttonClass}
          onClick={async () => {
            try {
              setResult(
                await updateTemplate(updateId, {
                  ...updateData,
                  defaultDuration: Number(updateData.defaultDuration)
                })
              );
            } catch (e) {
              setResult({ error: e.message });
            }
          }}
        >
          Update
        </button>
      </div>

      {/* DELETE */}
      <div className={boxClass}>
        <h3 className="font-semibold">Delete Template</h3>

        <input
          className={inputClass}
          placeholder="Template ID"
          value={deleteId}
          onChange={(e) => setDeleteId(e.target.value)}
        />

        <button
          className="px-4 py-2 rounded font-medium bg-red-600 text-white hover:bg-red-700"
          onClick={async () => {
            try {
              await deleteTemplate(deleteId);
              setResult({ message: "Deleted successfully" });
            } catch (e) {
              setResult({ error: e.message });
            }
          }}
        >
          Delete
        </button>
      </div>

      {/* RESULT */}
      {result && (
        <pre className="bg-gray-100 p-3 rounded text-sm overflow-auto">
          {JSON.stringify(result, null, 2)}
        </pre>
      )}
    </div>
  );
}
