import { useEffect, useState } from "react";
import { getTemplatesByUser, createTemplate } from "../../api/templateApi";
import TemplateFormModal from "./TemplateFormModal";

export default function TemplatesPage({ userId }) {
  const [templates, setTemplates] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showCreateModal, setShowCreateModal] = useState(false);

  useEffect(() => {
    const loadTemplates = async () => {
      try {
        const data = await getTemplatesByUser(userId);
        setTemplates(data);
      } catch (err) {
        console.error("Failed to load templates", err);
      } finally {
        setLoading(false);
      }
    };

    loadTemplates();
  }, [userId]);

  const handleCreateTemplate = async () => {
    try {
      const newTemplate = {
        name: "New Template",
      };

      const created = await createTemplate(userId, newTemplate);
      setTemplates((prev) => [...prev, created]);
    } catch (err) {
      console.error("Failed to create template", err);
    }
  };

  if (loading) {
    return <div className="p-6">Loading templates…</div>;
  }

  return (
    <div className="p-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-semibold">Templates</h1>

        <button
          onClick={() => setShowCreateModal(true)}
          className="px-4 py-2 rounded bg-blue-500 text-white hover:bg-blue-400 transition cursor-pointer"
        >
          + New Template
        </button>
      </div>

      {templates.length === 0 ? (
        <div className="text-gray-500">
          You don’t have any templates yet.
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

              <div className="flex gap-2 mt-4">
                <button className="text-sm text-blue-600 hover:underline transition cursor-pointer">
                  Edit
                </button>
                <button className="text-sm text-red-600 hover:underline transition cursor-pointer">
                  Delete
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
      {showCreateModal && (
        <TemplateFormModal
          userId={userId}
          onClose={() => setShowCreateModal(false)}
          onCreated={(template) =>
            setTemplates((prev) => [...prev, template])
          }
        />
      )}
    </div>
  );
}
