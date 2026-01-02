import { useState } from "react";

export default function LoginPage({ onLogin }) {
  const [userId, setUserId] = useState("");

  const handleLogin = () => {
    if (!userId.trim()) {
      alert("Please enter your User ID");
      return;
    }

    // Save user ID (could also use context later)
    localStorage.setItem("userId", userId);
    if (onLogin) onLogin(userId);
  };

  const handleKeyPress = (e) => {
    if (e.key === "Enter") handleLogin();
  };

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-50 p-4">
      <div className="bg-white p-8 rounded shadow-md w-full max-w-sm">
        <h1 className="text-2xl font-bold mb-6 text-center">Login</h1>

        <label className="block mb-2 text-gray-700">User ID</label>
        <input
          type="text"
          value={userId}
          onChange={(e) => setUserId(e.target.value)}
          onKeyPress={handleKeyPress}
          className="w-full p-2 border rounded mb-4 focus:outline-none focus:ring-2 focus:ring-blue-400"
          placeholder="Enter your User ID"
        />

        <button
          onClick={handleLogin}
          className="w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-400 active:scale-95 transition cursor-pointer"
        >
          Login
        </button>
      </div>
    </div>
  );
}
