import { useState, useEffect } from "react";
import Calendar from "../components/calendar/Calendar";
import JournalSidebar from "../components/journal/JournalSideBar";

export default function Dashboard({ calendarId, calendar }) {
  const [showJournalSidebar, setShowJournalSidebar] = useState(false);
  const [showJournalFull, setShowJournalFull] = useState(false);

  return (
    <div className="space-y-4">
      {/* Header with Journal buttons */}
      <div className="flex items-center justify-between height: 1em" >
        <h2 className="text-4xl font-semibold">Cadence</h2>
        
        {calendar && (
          <div className="flex gap-2">
            <button
              onClick={() => setShowJournalSidebar(true)}
              className="px-4 py-2 rounded border border-gray-300 bg-white hover:bg-gray-100 hover:border-gray-400 transition cursor-pointer text-sm"
            >
              📖 Journal
            </button>
          </div>
        )}
      </div>

      {/* Calendar */}
      <Calendar calendarId={calendarId} />

      {/* Journal Sidebar */}
      <JournalSidebar
        calendar={calendar}
        isOpen={showJournalSidebar}
        onClose={() => setShowJournalSidebar(false)}
      />

      {/* Journal Full View */}
      {showJournalFull && (
        <JournalView
          calendar={calendar}
          onClose={() => setShowJournalFull(false)}
        />
      )}
    </div>
  );
}