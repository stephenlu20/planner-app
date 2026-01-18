import api from "./axios";

// Preview schedule dates
export const previewTemplateSchedule = async (templateId) => {
  const res = await api.get(`/templates/${templateId}/preview`);
  return res.data;
};

// Populate calendar from template
export const populateCalendar = async (templateId, calendarId, deleteStrategy) => {
  const res = await api.post("/templates/populate", {
    templateId,
    calendarId,
    deleteStrategy
  });
  return res.data;
};