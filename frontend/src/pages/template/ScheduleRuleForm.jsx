import { useState, useEffect, useRef } from "react";

export default function ScheduleRuleForm({ rule, onChange }) {
  const [frequency, setFrequency] = useState(rule?.frequency || "DAILY");
  const [startDate, setStartDate] = useState(rule?.startDate || "");
  const [endDate, setEndDate] = useState(rule?.endDate || "");
  const [daysOfWeek, setDaysOfWeek] = useState(rule?.daysOfWeek || []);
  const [monthlyPattern, setMonthlyPattern] = useState(rule?.monthlyPatternType || "DAY_OF_MONTH");
  const [dayOfMonth, setDayOfMonth] = useState(rule?.dayOfMonth || 1);
  const [weekOrdinal, setWeekOrdinal] = useState(rule?.weekOrdinal || "FIRST");
  const [weekday, setWeekday] = useState(rule?.weekday || "MONDAY");

  const DAYS_OF_WEEK = ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"];
  const WEEK_ORDINALS = ["FIRST", "SECOND", "THIRD", "FOURTH", "LAST"];

  // Track if we've initialized from a loaded rule to avoid re-syncing
  const hasInitialized = useRef(false);

  // Sync state when we first get rule data (but not on subsequent onChange updates)
  useEffect(() => {
    // Only sync if we haven't initialized yet AND rule has an ID (meaning it came from database, not from our own onChange)
    if (!hasInitialized.current && rule?.id) {
      hasInitialized.current = true;
      
      setFrequency(rule.frequency || "DAILY");
      setStartDate(rule.startDate || "");
      setEndDate(rule.endDate || "");
      setDaysOfWeek(rule.daysOfWeek || []);
      setMonthlyPattern(rule.monthlyPatternType || "DAY_OF_MONTH");
      setDayOfMonth(rule.dayOfMonth || 1);
      setWeekOrdinal(rule.weekOrdinal || "FIRST");
      setWeekday(rule.weekday || "MONDAY");
    }
  }, [rule]);

  useEffect(() => {
    const ruleData = {
      frequency,
      startDate,
      endDate: endDate || null,
      active: true // Always active
    };

    if (frequency === "WEEKLY") {
      ruleData.daysOfWeek = daysOfWeek;
    }

    if (frequency === "MONTHLY") {
      ruleData.monthlyPatternType = monthlyPattern;
      if (monthlyPattern === "DAY_OF_MONTH") {
        ruleData.dayOfMonth = parseInt(dayOfMonth);
      } else {
        ruleData.weekOrdinal = weekOrdinal;
        ruleData.weekday = weekday;
      }
    }

    onChange(ruleData);
  }, [frequency, startDate, endDate, daysOfWeek, monthlyPattern, dayOfMonth, weekOrdinal, weekday, onChange]);

  const toggleDayOfWeek = (day) => {
    if (daysOfWeek.includes(day)) {
      setDaysOfWeek(daysOfWeek.filter(d => d !== day));
    } else {
      setDaysOfWeek([...daysOfWeek, day]);
    }
  };

  return (
    <div className="space-y-3 border-t pt-4">
      <h3 className="font-medium">Schedule Rule</h3>

      {/* Frequency */}
      <div>
        <label className="block text-sm font-medium mb-1">Frequency</label>
        <select
          value={frequency}
          onChange={(e) => setFrequency(e.target.value)}
          className="w-full border rounded px-3 py-2"
        >
          <option value="DAILY">Daily</option>
          <option value="WEEKLY">Weekly</option>
          <option value="MONTHLY">Monthly</option>
        </select>
      </div>

      {/* Date Range */}
      <div className="grid grid-cols-2 gap-2">
        <div>
          <label className="block text-sm font-medium mb-1">Start Date</label>
          <input
            type="date"
            value={startDate}
            onChange={(e) => setStartDate(e.target.value)}
            className="w-full border rounded px-3 py-2"
          />
        </div>
        <div>
          <label className="block text-sm font-medium mb-1">End Date (Optional)</label>
          <input
            type="date"
            value={endDate}
            onChange={(e) => setEndDate(e.target.value)}
            className="w-full border rounded px-3 py-2"
          />
        </div>
      </div>

      {/* Weekly: Days of Week */}
      {frequency === "WEEKLY" && (
        <div>
          <label className="block text-sm font-medium mb-2">Days of Week</label>
          <div className="flex flex-wrap gap-2">
            {DAYS_OF_WEEK.map(day => (
              <button
                key={day}
                type="button"
                onClick={() => toggleDayOfWeek(day)}
                className={`px-3 py-1 text-sm rounded border transition ${
                  daysOfWeek.includes(day)
                    ? "bg-blue-500 text-white border-blue-500"
                    : "bg-white text-gray-700 border-gray-300 hover:border-blue-300"
                }`}
              >
                {day.slice(0, 3)}
              </button>
            ))}
          </div>
          {daysOfWeek.length === 0 && (
            <p className="text-xs text-gray-500 mt-1">
              If no days selected, will use start date's day of week
            </p>
          )}
        </div>
      )}

      {/* Monthly: Pattern Type */}
      {frequency === "MONTHLY" && (
        <>
          <div>
            <label className="block text-sm font-medium mb-1">Monthly Pattern</label>
            <select
              value={monthlyPattern}
              onChange={(e) => setMonthlyPattern(e.target.value)}
              className="w-full border rounded px-3 py-2"
            >
              <option value="DAY_OF_MONTH">Day of Month (e.g., 15th)</option>
              <option value="NTH_WEEKDAY_OF_MONTH">Nth Weekday (e.g., First Monday)</option>
            </select>
          </div>

          {monthlyPattern === "DAY_OF_MONTH" && (
            <div>
              <label className="block text-sm font-medium mb-1">Day of Month (1-31)</label>
              <input
                type="number"
                min="1"
                max="31"
                value={dayOfMonth}
                onChange={(e) => setDayOfMonth(e.target.value)}
                className="w-full border rounded px-3 py-2"
              />
              <p className="text-xs text-gray-500 mt-1">
                For months with fewer days, will use last day of month
              </p>
            </div>
          )}

          {monthlyPattern === "NTH_WEEKDAY_OF_MONTH" && (
            <div className="grid grid-cols-2 gap-2">
              <div>
                <label className="block text-sm font-medium mb-1">Ordinal</label>
                <select
                  value={weekOrdinal}
                  onChange={(e) => setWeekOrdinal(e.target.value)}
                  className="w-full border rounded px-3 py-2"
                >
                  {WEEK_ORDINALS.map(ord => (
                    <option key={ord} value={ord}>
                      {ord.charAt(0) + ord.slice(1).toLowerCase()}
                    </option>
                  ))}
                </select>
              </div>
              <div>
                <label className="block text-sm font-medium mb-1">Weekday</label>
                <select
                  value={weekday}
                  onChange={(e) => setWeekday(e.target.value)}
                  className="w-full border rounded px-3 py-2"
                >
                  {DAYS_OF_WEEK.map(day => (
                    <option key={day} value={day}>
                      {day.charAt(0) + day.slice(1).toLowerCase()}
                    </option>
                  ))}
                </select>
              </div>
            </div>
          )}
        </>
      )}
    </div>
  );
}