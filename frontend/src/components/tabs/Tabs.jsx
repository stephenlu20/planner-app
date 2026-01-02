export default function Tabs({ tabs, activeTab, onChange }) {
  return (
    <div className="flex gap-2 border-b mb-6">
      {tabs.map(tab => (
        <button
          key={tab}
          onClick={() => onChange(tab)}
          className={`px-4 py-2 text-sm font-medium border-b-2
            ${activeTab === tab
              ? "border-indigo-500 text-indigo-600"
              : "border-transparent text-gray-500 hover:text-gray-700"}
          `}
        >
          {tab}
        </button>
      ))}
    </div>
  );
}
