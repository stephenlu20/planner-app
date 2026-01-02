import { useState } from "react";
import { createUser, getUserById, getUserByUsername, deleteUser, getAllUsers } from "../../api/userApi";

export default function UserTester() {
  const [username, setUsername] = useState("");
  const [fetchById, setFetchById] = useState("");
  const [fetchByName, setFetchByName] = useState("");
  const [deleteId, setDeleteId] = useState("");
  const [result, setResult] = useState(null);

  const buttonClasses =
    "px-4 py-2 rounded font-medium transition transform active:scale-95 cursor-pointer";

  return (
    <div className="p-6 space-y-6">
      <div className="space-y-2">
        <h2 className="font-semibold">Create User</h2>
        <input
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          className="border px-2 py-1 rounded"
        />
        <button
          onClick={async () => {
            try {
              const res = await createUser(username);
              setResult(res);
            } catch (err) {
              setResult({ error: err.response?.data || err.message });
            }
          }}
          className={`${buttonClasses} bg-blue-600 text-white hover:bg-blue-700`}
        >
          Create
        </button>
      </div>

      <div className="space-y-2">
        <h2 className="font-semibold">Get All Users</h2>
        <button
          onClick={async () => {
            try {
              const res = await getAllUsers();
              setResult(res);
            } catch (err) {
              setResult({ error: err.response?.data || err.message });
            }
          }}
          className={`${buttonClasses} bg-purple-600 text-white hover:bg-purple-700`}
        >
          Fetch All
        </button>
      </div>

      <div className="space-y-2">
        <h2 className="font-semibold">Get User by ID</h2>
        <input
          type="text"
          placeholder="User ID"
          value={fetchById}
          onChange={(e) => setFetchById(e.target.value)}
          className="border px-2 py-1 rounded"
        />
        <button
          onClick={async () => {
            try {
              const res = await getUserById(fetchById);
              setResult(res);
            } catch (err) {
              setResult({ error: err.response?.data || err.message });
            }
          }}
          className={`${buttonClasses} bg-gray-600 text-white hover:bg-gray-700`}
        >
          Fetch
        </button>
      </div>

      <div className="space-y-2">
        <h2 className="font-semibold">Get User by Username</h2>
        <input
          type="text"
          placeholder="Username"
          value={fetchByName}
          onChange={(e) => setFetchByName(e.target.value)}
          className="border px-2 py-1 rounded"
        />
        <button
          onClick={async () => {
            try {
              const res = await getUserByUsername(fetchByName);
              setResult(res);
            } catch (err) {
              setResult({ error: err.response?.data || err.message });
            }
          }}
          className={`${buttonClasses} bg-green-600 text-white hover:bg-green-700`}
        >
          Fetch
        </button>
      </div>

      <div className="space-y-2">
        <h2 className="font-semibold">Delete User</h2>
        <input
          type="text"
          placeholder="User ID"
          value={deleteId}
          onChange={(e) => setDeleteId(e.target.value)}
          className="border px-2 py-1 rounded"
        />
        <button
          onClick={async () => {
            try {
              await deleteUser(deleteId);
              setResult({ message: "Deleted successfully" });
            } catch (err) {
              setResult({ error: err.response?.data || err.message });
            }
          }}
          className={`${buttonClasses} bg-red-600 text-white hover:bg-red-700`}
        >
          Delete
        </button>
      </div>

      <div className="mt-4">
        {result && (
          <pre className="bg-gray-100 p-2 rounded">{JSON.stringify(result, null, 2)}</pre>
        )}
      </div>
    </div>
  );
}
