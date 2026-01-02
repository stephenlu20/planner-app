import { useState } from "react";
import Tabs from "../components/tabs/Tabs";
import UserTester from "../components/testers/UserTester";
import CalendarTester from "../components/testers/CalendarTester";
import EventTester from "../components/testers/EventTester";

const TAB_MAP = {
  Users: UserTester,
  Calendars: CalendarTester,
  Events: EventTester,
};

export default function ApiTester() {
  const [activeTab, setActiveTab] = useState("Users");
  const ActiveComponent = TAB_MAP[activeTab];

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-4">API Tester</h1>

      <Tabs
        tabs={Object.keys(TAB_MAP)}
        activeTab={activeTab}
        onChange={setActiveTab}
      />

      <div className="mt-4">
        <ActiveComponent />
      </div>
    </div>
  );
}
