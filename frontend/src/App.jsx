import { useState } from "react";
import TabButton from "./components/tabs/TabButton";
import UserTester from "./components/testers/UserTester";
import CalendarTester from "./components/testers/CalendarTester";
import TemplateTester from "./components/testers/TemplateTester"
import EventTester from "./components/testers/EventTester";
import EntryTester from "./components/testers/EntryTester";

function App() {
  const [activeTab, setActiveTab] = useState("Users");

  const renderTab = () => {
    switch (activeTab) {
      case "Users":
        return <UserTester />;
      case "Calendars":
        return <CalendarTester />;
      case "Templates":
        return <TemplateTester />;
      case "Events":
        return <EventTester />;
      case "Entry":
        return <EntryTester />;
      default:
        return null;
    }
  };

  return (
    <div className="min-h-screen p-6 bg-gray-50">
      <h1 className="text-3xl font-bold mb-6">Planner API Tester</h1>

      <div className="flex space-x-2 border-b-2 mb-4">
        {["Users", "Calendars", "Templates", "Events", "Entry"].map((tab) => (
          <TabButton
            key={tab}
            label={tab}
            active={activeTab === tab}
            onClick={() => setActiveTab(tab)}
          />
        ))}
      </div>

      <div className="p-4 bg-white rounded shadow">{renderTab()}</div>
    </div>
  );
}

export default App;
