import { useState, useEffect } from "react";
import { createTemplate, updateTemplate } from "../../api/templateApi";
import { createEntry, updateEntry, deleteEntry, getEntriesBySubject } from "../../api/entryApi";
import { createScheduleRule, getScheduleRulesByTemplate, updateScheduleRule } from "../../api/scheduleRuleApi";
import EntryRenderer from "../../components/entries/EntryRenderer";
import ScheduleRuleForm from "./ScheduleRuleForm";

export default function TemplateFormModal({ userId, template, onClose, onSaved }) {
  const isEdit = Boolean(template);

  const [name, setName] = useState("");
  const [note, setNote] = useState("");
  const [entries, setEntries] = useState([]);
  const [removedEntryIds, setRemovedEntryIds] = useState([]);
  const [scheduleRule, setScheduleRule] = useState(null);
  const [existingRuleId, setExistingRuleId] = useState(null);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState(null);

  const ENTRY_TYPES = ["TEXT", "NUMBER", "CHECKBOX", "TABLE", "HEADER"];
  const readOnly = false;

  // Load template and entries if editing
  useEffect(() => {
    if (template) {
      setName(template.name ?? "");
      setNote(template.note ?? "");

      (async () => {
        try {
          const [fetchedEntries, fetchedRules] = await Promise.all([
            getEntriesBySubject("TEMPLATE", template.id),
            getScheduleRulesByTemplate(template.id)
          ]);
          
          setEntries(
            fetchedEntries.map((e) => ({
              id: e.id,
              label: e.label || "",
              type: e.type || "TEXT",
              value: e.value || "",
              note: e.note || "",
              subjectType: "TEMPLATE",
              subjectId: template.id,
              previousValues: { [e.type]: e.value || "" },
            }))
          );

          if (fetchedRules.length > 0) {
            const rule = fetchedRules[0];
            setExistingRuleId(rule.id);
            setScheduleRule(rule);
          }
        } catch (err) {
          console.error("Failed to fetch template data", err);
          setEntries([]);
        }
      })();
    } else {
      setEntries([]);
    }
    setRemovedEntryIds([]);
  }, [template]);

  // Add new entry
  const addEntry = () =>
    setEntries([
      ...entries,
      { label: "", type: "TEXT", value: "", note: "", subjectType: "TEMPLATE", subjectId: template?.id ?? null, previousValues: {} },
    ]);

  // Remove entry
  const removeEntry = (index) => {
    const entryToRemove = entries[index];
    if (entryToRemove.id) setRemovedEntryIds([...removedEntryIds, entryToRemove.id]);
    setEntries(entries.filter((_, i) => i !== index));
  };

  // Update entry locally
  const updateEntryLocal = (index, fieldOrEntry, value) => {
    const newEntries = [...entries];
    if (typeof fieldOrEntry === "string") {
      if (fieldOrEntry === "type") {
        const oldType = newEntries[index].type;
        const oldValue = newEntries[index].value;
        const previousValues = newEntries[index].previousValues || {};
        previousValues[oldType] = oldValue;

        let newValue = previousValues[value];
        if (newValue === undefined) {
          if (value === "CHECKBOX") newValue = JSON.stringify([]);
          else if (value === "TABLE") newValue = JSON.stringify({ rows: 1, cols: 1, cells: [[""]] });
          else newValue = "";
        }

        newEntries[index] = {
          ...newEntries[index],
          type: value,
          value: newValue,
          previousValues,
        };
      } else {
        newEntries[index] = { ...newEntries[index], [fieldOrEntry]: value };
      }
    } else if (typeof fieldOrEntry === "object") {
      newEntries[index] = { ...newEntries[index], ...fieldOrEntry };
    }
    setEntries(newEntries);
  };

  // Handle submit
  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    setError(null);

    try {
      // Save or update the template first
      const templatePayload = {
        name,
        note
      };

      const savedTemplate = isEdit
        ? await updateTemplate(template.id, templatePayload)
        : await createTemplate(userId, templatePayload);

      // Delete removed entries
      for (let entryId of removedEntryIds) {
        try {
          await deleteEntry(entryId);
        } catch (err) {
          console.error("Failed to delete entry:", err);
        }
      }
      setRemovedEntryIds([]);

      // Process entries (create or update)
      for (let i = 0; i < entries.length; i++) {
        const entry = entries[i];

        // Skip empty labels
        if (!entry.label?.trim()) continue;

        // Build payload for backend
        const payload = {
          type: entry.type,
          subjectType: entry.subjectType || "TEMPLATE",
          subjectId: entry.subjectId || savedTemplate.id,
          label: entry.label || "",
          value: entry.type === "HEADER" ? null : entry.value || "",
          note: entry.type === "HEADER" ? (entry.note || "") : entry.note || "",
          orderIndex: i
        };

        if (entry.type === "HEADER" && !entry.note) {
          payload.note = null;
        }

        // Update existing entry
        if (entry.id) {
          const payload = {
            label: entry.label,
            value: entry.value ?? "",
            note: entry.note ?? "",
            orderIndex: i,
          };
          await updateEntry(entry.id, payload);
        } else {
          // Create new entry
          const savedEntry = await createEntry(userId, payload);
          entry.id = savedEntry.id;
        }
      }

      // Handle schedule rule
      if (scheduleRule && scheduleRule.startDate) {
        const rulePayload = {
          userId,
          templateId: savedTemplate.id,
          ...scheduleRule
        };

        if (existingRuleId) {
          await updateScheduleRule(existingRuleId, rulePayload);
        } else {
          await createScheduleRule(rulePayload);
        }
      }

      // Refresh entries attached to the template
      const updatedEntries = await getEntriesBySubject("TEMPLATE", savedTemplate.id);
      savedTemplate.entries = updatedEntries;

      // Notify parent and close modal
      onSaved(savedTemplate);
      onClose();
    } catch (err) {
      console.error(err);
      setError("Failed to save template");
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50">
      <div className="bg-white rounded shadow-lg w-full max-w-2xl p-6 max-h-[90vh] overflow-y-auto">
        <h2 className="text-lg font-semibold mb-4">
          {isEdit ? "Edit Template" : "New Template"}
        </h2>

        <form onSubmit={handleSubmit} className="space-y-4">
          {/* Template fields */}
          <div>
            <label className="block text-sm font-medium mb-1">Name</label>
            <input
              value={name}
              onChange={(e) => setName(e.target.value)}
              className="w-full border rounded px-3 py-2"
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium mb-1">Note</label>
            <textarea
              value={note}
              onChange={(e) => setNote(e.target.value)}
              className="w-full border rounded px-3 py-2"
              rows={3}
            />
          </div>

          {/* Entries section */}
          <div className="mt-4 border-t pt-4">
            <h3 className="font-medium mb-2">Entries</h3>
            {entries.map((entry, index) => (
              <div key={index} className="flex flex-col gap-2 mb-2">
                <div className="flex gap-2 items-center">
                  {entry.type !== "HEADER" && (
                    <input
                      value={entry.label}
                      onChange={(e) => updateEntryLocal(index, "label", e.target.value)}
                      placeholder="Label"
                      className="border rounded px-2 py-1 flex-1"
                      required
                    />
                  )}
                  <select
                    value={entry.type}
                    onChange={(e) => updateEntryLocal(index, "type", e.target.value)}
                    className="border rounded px-2 py-1"
                  >
                    {ENTRY_TYPES.map(t => <option key={t} value={t}>{t}</option>)}
                  </select>
                  <button
                    type="button"
                    onClick={() => removeEntry(index)}
                    className="px-2 py-1 text-red-500 font-bold"
                  >
                    ×
                  </button>
                </div>
                <EntryRenderer
                  entry={entry}
                  onChange={(updatedEntry) => updateEntryLocal(index, updatedEntry)}
                  readOnly={readOnly}
                />
              </div>
            ))}

            <button
              type="button"
              onClick={addEntry}
              className="text-sm text-blue-500 mt-1"
            >
              + Add Entry
            </button>
          </div>

          {/* Schedule Rule */}
          <ScheduleRuleForm
            rule={scheduleRule}
            onChange={setScheduleRule}
          />

          {error && <div className="text-sm text-red-600">{error}</div>}

          <div className="flex justify-end gap-2 pt-2">
            <button
              type="button"
              onClick={onClose}
              className="px-3 py-2 text-sm rounded border cursor-pointer transition"
            >
              Cancel
            </button>

            <button
              type="submit"
              disabled={saving}
              className="px-4 py-2 text-sm rounded bg-blue-500 text-white disabled:opacity-50 cursor-pointer transition"
            >
              {saving
                ? isEdit ? "Saving..." : "Creating..."
                : isEdit ? "Save Changes" : "Create"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}