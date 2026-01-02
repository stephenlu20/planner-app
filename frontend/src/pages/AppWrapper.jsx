import { useState, useEffect } from "react";
import ApiTester from "./ApiTester";
import ApplicationView from "./ApplicationView";

export default function AppWrapper() {
  const [view, setView] = useState("app");
  const [userId, setUserId] = useState(null);

  useEffect(() => {
    const storedId = localStorage.getItem("userId");
    if (storedId) setUserId(storedId);
  }, []);

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="flex justify-end p-4 space-x-2 border-b bg-white shadow">
        <button
          onClick={() => setView("app")}
          className={`px-4 py-2 rounded transition ${
            view === "app" ? "bg-blue-500 text-white" : "bg-gray-200 text-gray-700"
          } hover:bg-blue-400 hover:text-white active:scale-95 active:bg-blue-600 cursor-pointer`}
        >
          Application View
        </button>
        <button
          onClick={() => setView("api")}
          className={`px-4 py-2 rounded transition ${
            view === "api" ? "bg-blue-500 text-white" : "bg-gray-200 text-gray-700"
          } hover:bg-blue-400 hover:text-white active:scale-95 active:bg-blue-600 cursor-pointer`}
        >
          API Test View
        </button>
      </div>

      <div className="p-6">
        {view === "api" ? (
          <ApiTester />
        ) : (
          <ApplicationView userId={userId} setUserId={setUserId} />
        )}
      </div>
    </div>
  );
}
