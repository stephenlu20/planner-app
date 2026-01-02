import { useState } from "react";
import { createUser, getUserById, getUserByUsername, deleteUser, getAllUsers } from "../../api/userApi";

export default function UserTester() {
  const [username, setUsername] = useState("");
  const [fetchById, setFetchById] = useState("");
  const [fetchByName, setFetchByName] = useState("");
  const [deleteId, setDeleteId] = useState("");
  const [output, setOutput] = useState("");

  const handleCreate = async () => {
    try {
      const res = await createUser(username);
      setOutput(JSON.stringify(res, null, 2));
    } catch (err) {
      setOutput(err.response?.data || err.message);
    }
  };

  const handleGetAll = async () => {
    try {
      const res = await getAllUsers();
      setOutput(JSON.stringify(res, null, 2));
    } catch (err) {
      setOutput(err.response?.data || err.message);
    }
  };

  const handleGetById = async () => {
    try {
      const res = await getUserById(fetchById);
      setOutput(JSON.stringify(res, null, 2));
    } catch (err) {
      setOutput(err.response?.data || err.message);
    }
  };

  const handleGetByUsername = async () => {
    try {
      const res = await getUserByUsername(fetchByName);
      setOutput(JSON.stringify(res, null, 2));
    } catch (err) {
      setOutput(err.response?.data || err.message);
    }
  };

  const handleDelete = async () => {
    try {
      await deleteUser(deleteId);
      setOutput(`User ${deleteId} deleted successfully`);
    } catch (err) {
      setOutput(err.response?.data || err.message);
    }
  };

  return (
    <div className="p-6 space-y-6 font-sans">
      <h2 className="text-xl font-bold">User API Tester</h2>

      <section className="p-4 border rounded space-y-2">
        <h3 className="font-semibold">Create User</h3>
        <input
          className="border p-1"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />
        <button
          onClick={handleCreate}
          className="bg-blue-500 text-white px-3 py-1 rounded hover:cursor-pointer active:scale-95 transition-transform"
        >
          Create
        </button>
      </section>

      <section className="p-4 border rounded space-y-2">
        <h3 className="font-semibold">Get All Users</h3>
        <button
          onClick={handleGetAll}
          className="bg-purple-500 text-white px-3 py-1 rounded hover:cursor-pointer active:scale-95 transition-transform"
        >
          Fetch All
        </button>
      </section>

      <section className="p-4 border rounded space-y-2">
        <h3 className="font-semibold">Get User by ID</h3>
        <input
          className="border p-1"
          placeholder="User ID"
          value={fetchById}
          onChange={(e) => setFetchById(e.target.value)}
        />
        <button
          onClick={handleGetById}
          className="bg-gray-500 text-white px-3 py-1 rounded hover:cursor-pointer active:scale-95 transition-transform"
        >
          Fetch
        </button>
      </section>

      <section className="p-4 border rounded space-y-2">
        <h3 className="font-semibold">Get User by Username</h3>
        <input
          className="border p-1"
          placeholder="Username"
          value={fetchByName}
          onChange={(e) => setFetchByName(e.target.value)}
        />
        <button
          onClick={handleGetByUsername}
          className="bg-green-500 text-white px-3 py-1 rounded hover:cursor-pointer active:scale-95 transition-transform"
        >
          Fetch
        </button>
      </section>

      <section className="p-4 border rounded space-y-2">
        <h3 className="font-semibold">Delete User</h3>
        <input
          className="border p-1"
          placeholder="User ID"
          value={deleteId}
          onChange={(e) => setDeleteId(e.target.value)}
        />
        <button
          onClick={handleDelete}
          className="bg-red-500 text-white px-3 py-1 rounded hover:cursor-pointer active:scale-95 transition-transform"
        >
          Delete
        </button>
      </section>

      <pre className="p-4 border rounded bg-gray-100 overflow-auto">{output}</pre>
    </div>
  );
}
