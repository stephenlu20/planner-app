export default function TabButton({ label, active, onClick }) {
  return (
    <button
      onClick={onClick}
      className={`px-4 py-2 rounded-t-lg font-medium focus:outline-none transition transform active:scale-95 cursor-pointer
        ${active ? "bg-indigo-500 text-white" : "bg-gray-200 text-gray-700 hover:bg-gray-300"}`}
    >
      {label}
    </button>
  );
}
