import LoginPage from "./LoginPage";
import Dashboard from "./Dashboard";

export default function ApplicationView({ userId, setUserId }) {
  const handleLogout = () => {
    localStorage.removeItem("userId");
    setUserId(null);
  };

  if (!userId) return <LoginPage onLogin={setUserId} />;

  return (
    <div>
      <div className="flex justify-end mb-4">
        <button
          onClick={handleLogout}
          className="px-4 py-2 rounded bg-red-500 text-white hover:bg-red-400 active:scale-95 transition cursor-pointer"
        >
          Logout
        </button>
      </div>
      <Dashboard userId={userId} />
    </div>
  );
}