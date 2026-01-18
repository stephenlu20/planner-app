import { useEffect, useState } from "react";
import { getTemplatesByUser, deleteTemplate } from "../../api/templateApi";
import { getCalendarsForUser } from "../../api/calendarApi";
import TemplateFormModal from "./TemplateFormModal";
import TemplateViewModal from "./TemplateViewModal";
import TemplatePopulateModal from "./TemplatePopulateModal";

export default function TemplatesPage({ userId }) {
  const [templates, setTemplates] = useState([]);
  const [calendars, setCalendars] = useState([]);
  const [loading, setLoading] = useState(true);
  const [viewTemplate, setViewTemplate] = useState(null);
  const [populateTemplate, setPopulateTemplate] = useState(null);

  const [showModal, setShowModal] = useState(false);
  const [activeTemplate, setActiveTemplate] = useState(null);

  const handleDeleteTemplate = async (templateId) => {
    const confirmed = window.confirm(
      "Are you sure you want to delete this template? This action cannot be undone."
    );
  
    if (!confirmed) return;
  
    try {
      await deleteTemplate(templateId);
      setTemplates((prev) => prev.filter((t) => t.id !== templateId));
    } catch (err) {
      console.error("Failed to delete template", err);
      alert("Failed to delete template");
    }
  };

  useEffect(() => {
    const loadData = async () => {
      try {
        const [templatesData, calendarsData] = await Promise.all([
          getTemplatesByUser(userId),
          getCalendarsForUser(userId)
        ]);
        setTemplates(templatesData);
        setCalendars(calendarsData);
      } catch (err) {
        console.error("Failed to load data", err);
      } finally {
        setLoading(false);
      }
    };

    loadData();
  }, [userId]);

  const handlePopulated = (events) => {
    alert(`Successfully created ${events.length} events!`);
  };

  if (loading) {
    return <div className="p-6">Loading templates…</div>;
  }

  return (
    <div className="p-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-semibold">Templates</h1>

        <button
          onClick={() => {
            setActiveTemplate(null);
            setShowModal(true);
          }}
          className="px-4 py-2 rounded bg-blue-500 text-white hover:bg-blue-400 transition cursor-pointer"
        >
          + New Template
        </button>
      </div>

      {templates.length === 0 ? (
        <div className="text-gray-500">
          You don't have any templates yet.
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {templates.map((template) => (
            <div
              key={template.id}
              className="border rounded p-4 bg-white shadow-sm"
            >
              <div className="font-medium mb-2">
                {template.name}
              </div>
              
              <div className="flex flex-wrap gap-2 mt-4">
                <button
                  onClick={() => setViewTemplate(template)}
                  className="text-sm text-gray-700 hover:underline transition cursor-pointer"
                >
                  View
                </button>

                <button
                  onClick={() => {
                    setActiveTemplate(template);
                    setShowModal(true);
                  }}
                  className="text-sm text-blue-600 hover:underline transition cursor-pointer"
                >
                  Edit
                </button>

                <button
                  onClick={() => setPopulateTemplate(template)}
                  className="text-sm text-green-600 hover:underline transition cursor-pointer"
                >
                  Populate
                </button>

                <button
                  onClick={() => handleDeleteTemplate(template.id)}
                  className="text-sm text-red-600 hover:underline transition cursor-pointer"
                >
                  Delete
                </button>
              </div>
            </div>
          ))}
        </div>
      )}

      {showModal && (
        <TemplateFormModal
          userId={userId}
          template={activeTemplate}
          onClose={() => setShowModal(false)}
          onSaved={(savedTemplate) => {
            setTemplates((prev) =>
              activeTemplate
                ? prev.map((t) =>
                    t.id === savedTemplate.id ? savedTemplate : t
                  )
                : [...prev, savedTemplate]
            );
          }}
        />
      )}

      {viewTemplate && (
        <TemplateViewModal
          template={viewTemplate}
          onClose={() => setViewTemplate(null)}
        />
      )}

      {populateTemplate && (
        <TemplatePopulateModal
          template={populateTemplate}
          calendars={calendars}
          onClose={() => setPopulateTemplate(null)}
          onPopulated={handlePopulated}
        />
      )}
    </div>
  );
}