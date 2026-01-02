import api from "./axios";

export const createTemplate = async (userId, template) => {
  const res = await api.post(`/templates/user/${userId}`, template);
  return res.data;
};

export const getTemplateById = async (templateId) => {
  const res = await api.get(`/templates/${templateId}`);
  return res.data;
};

export const getTemplatesByUser = async (userId) => {
  const res = await api.get(`/templates/user/${userId}`);
  return res.data;
};

export const updateTemplate = async (templateId, template) => {
  const res = await api.put(`/templates/${templateId}`, template);
  return res.data;
};

export const deleteTemplate = async (templateId) => {
  await api.delete(`/templates/${templateId}`);
};
