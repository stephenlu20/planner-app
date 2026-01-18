import api from "./axios";

// Create schedule rule
export const createScheduleRule = async (ruleData) => {
  const res = await api.post("/schedule-rules", ruleData);
  return res.data;
};

// Get schedule rules by template
export const getScheduleRulesByTemplate = async (templateId) => {
  const res = await api.get(`/schedule-rules/template/${templateId}`);
  return res.data;
};

// Update schedule rule
export const updateScheduleRule = async (ruleId, ruleData) => {
  const res = await api.put(`/schedule-rules/${ruleId}`, ruleData);
  return res.data;
};

// Delete schedule rule
export const deleteScheduleRule = async (ruleId) => {
  await api.delete(`/schedule-rules/${ruleId}`);
};