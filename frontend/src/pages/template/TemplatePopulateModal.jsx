import { useState, useEffect } from "react";
import { previewTemplateSchedule, populateCalendar } from "../../api/templatePopulateApi";

export default function TemplatePopulateModal({ template, calendars, onClose, onPopulated }) {
  const [selectedCalendar, setSelectedCalendar] = useState("");
  const [deleteStrategy, setDeleteStrategy] = useState("DELETE_FUTURE");
  const [preview, setPreview] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // Helper functions for formatting
  const formatFrequency = (freq) => {
    if (!freq) return "N/A";
    return freq.charAt(0) + freq.slice(1).toLowerCase();
  };

  const formatDay = (day) => {
    if (!day) return "";
    return day.charAt(0) + day.slice(1).toLowerCase();
  };

  const formatDaysOfWeek = (days) => {
    if (!days || days.length === 0) return "";
    return days.map(formatDay).join(", ");
  };

  useEffect(() => {
    if (calendars.length > 0) {
      setSelectedCalendar(calendars[0].id);
    }
  }, [calendars]);

  const handlePreview = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await previewTemplateSchedule(template.id);
      setPreview(data);
    } catch (err) {
      setError(err.response?.data?.message || "Failed to preview schedule");
    } finally {
      setLoading(false);
    }
  };

  const handlePopulate = async () => {
    if (!selectedCalendar) {
      setError("Please select a calendar");
      return;
    }

    setLoading(true);
    setError(null);
    try {
      const events = await populateCalendar(template.id, selectedCalendar, deleteStrategy);
      onPopulated(events);
      onClose();
    } catch (err) {
      setError(err.response?.data?.message || "Failed to populate calendar");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50">
      <div className="bg-white rounded shadow-lg w-full max-w-md p-6 max-h-[90vh] overflow-y-auto">
        <h2 className="text-lg font-semibold mb-4">
          Populate Calendar from Template
        </h2>

        <div className="space-y-4">
          {/* Template name */}
          <div>
            <label className="block text-sm font-medium mb-1">Template</label>
            <div className="text-gray-700">{template.name}</div>
          </div>

          {/* Calendar selector */}
          <div>
            <label className="block text-sm font-medium mb-1">Calendar</label>
            <select
              value={selectedCalendar}
              onChange={(e) => setSelectedCalendar(e.target.value)}
              className="w-full border rounded px-3 py-2"
            >
              {calendars.map((cal) => (
                <option key={cal.id} value={cal.id}>
                  {cal.name}
                </option>
              ))}
            </select>
          </div>

          {/* Delete strategy */}
          <div>
            <label className="block text-sm font-medium mb-2">
              Existing Events
            </label>
            <div className="space-y-2">
              <label className="flex items-center">
                <input
                  type="radio"
                  value="KEEP_ALL"
                  checked={deleteStrategy === "KEEP_ALL"}
                  onChange={(e) => setDeleteStrategy(e.target.value)}
                  className="mr-2"
                />
                <span className="text-sm">Keep all existing events</span>
              </label>

              <label className="flex items-center">
                <input
                  type="radio"
                  value="DELETE_FUTURE"
                  checked={deleteStrategy === "DELETE_FUTURE"}
                  onChange={(e) => setDeleteStrategy(e.target.value)}
                  className="mr-2"
                />
                <span className="text-sm">Delete future events only</span>
              </label>

              <label className="flex items-center">
                <input
                  type="radio"
                  value="DELETE_ALL"
                  checked={deleteStrategy === "DELETE_ALL"}
                  onChange={(e) => setDeleteStrategy(e.target.value)}
                  className="mr-2"
                />
                <span className="text-sm text-red-600">Delete all events (dangerous)</span>
              </label>
            </div>
          </div>

          {/* Preview button */}
          <button
            onClick={handlePreview}
            disabled={loading}
            className="w-full px-4 py-2 rounded bg-gray-500 text-white hover:bg-gray-400 disabled:opacity-50 transition cursor-pointer"
          >
            {loading ? "Loading..." : "Preview Dates"}
          </button>

          {/* Preview results */}
          {preview && (
            <div className="border rounded p-3 bg-gray-50">
              <div className="text-sm font-medium mb-2">Schedule Preview</div>
              <div className="text-sm space-y-1">
                <div><strong>Frequency:</strong> {formatFrequency(preview.frequency)}</div>
                <div><strong>Total events:</strong> {preview.count}</div>
                <div><strong>Date range:</strong> {preview.firstDate} to {preview.lastDate || "ongoing"}</div>
                
                {preview.frequency === "WEEKLY" && preview.daysOfWeek && preview.daysOfWeek.length > 0 && (
                  <div className="mt-2 pt-2 border-t border-gray-200">
                    <strong>Days:</strong> {formatDaysOfWeek(preview.daysOfWeek)}
                  </div>
                )}
                
                {preview.frequency === "MONTHLY" && preview.monthlyPattern && (
                  <div className="mt-2 pt-2 border-t border-gray-200">
                    <strong>Pattern:</strong>{" "}
                    {preview.monthlyPattern === "DAY_OF_MONTH" 
                      ? `Day ${preview.dayOfMonth} of each month`
                      : `${formatDay(preview.weekOrdinal)} ${formatDay(preview.weekday)} of each month`
                    }
                  </div>
                )}
              </div>
            </div>
          )}

          {/* Error message */}
          {error && (
            <div className="text-sm text-red-600 bg-red-50 border border-red-200 rounded p-2">
              {error}
            </div>
          )}

          {/* Action buttons */}
          <div className="flex gap-2 pt-2">
            <button
              onClick={onClose}
              className="flex-1 px-4 py-2 rounded border hover:bg-gray-50 transition cursor-pointer"
            >
              Cancel
            </button>

            <button
              onClick={handlePopulate}
              disabled={loading || !selectedCalendar}
              className="flex-1 px-4 py-2 rounded bg-blue-500 text-white hover:bg-blue-400 disabled:opacity-50 transition cursor-pointer"
            >
              {loading ? "Populating..." : "Populate"}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}