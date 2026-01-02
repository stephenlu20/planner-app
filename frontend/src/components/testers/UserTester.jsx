import { useState } from "react";
import {
  createUser,
  getUserById,
  getUserByUsername,
  deleteUser
} from "../../api/userApi";

export default function UserTester() {
  const [username, setUsername] = useState("");
  const [userId, setUserId] = useState("");
  const [result, setResult] = useState(null);

  const handleCreate = async () => {
    const res = await createUser(username);
    setResult(res);
  };

  const handleFetchById = async () => {
    const res = await getUserById(userId);
    setResult(res);
  };

  const handleFetchByUsername = async () => {
    const res = await getUserByUsername(username);
    setResult(res);
  };

  const handleDelete = async () => {
    await deleteUser(userId);
    setResult({ deletedUserId: userId });
  };

  return (
    <div className="space-y-4">
      <h2 className="text-xl font-semibold">User API</h2>

      <input
        className="border px-3 py-2 rounded w-96"
        placeholder="Username"
        value={username}
        onChange={e => setUsername(e.target.value)}
      />

      <input
        className="border px-3 py-2 rounded w-96"
        placeholder="User ID"
        value={userId}
        onChange={e => setUserId(e.target.value)}
      />

      <div className="flex gap-2">
        <button
          onClick={handleCreate}
          className="bg-indigo-500 text-white px-4 py-2 rounded"
        >
          Create User
        </button>

        <button
          onClick={handleFetchById}
          className="bg-gray-600 text-white px-4 py-2 rounded"
        >
          Get By ID
        </button>

        <button
          onClick={handleFetchByUsername}
          className="bg-sky-600 text-white px-4 py-2 rounded"
        >
          Get By Username
        </button>

        <button
          onClick={handleDelete}
          className="bg-red-600 text-white px-4 py-2 rounded"
        >
          Delete User
        </button>
      </div>

      {result && (
        <pre className="bg-slate-100 p-4 rounded text-sm">
          {JSON.stringify(result, null, 2)}
        </pre>
      )}
    </div>
  );
}
