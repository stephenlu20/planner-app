import api from "./axios";

export const createEntry = async (userId, {
  type,
  subjectType,
  subjectId,
  label,
  value
}) => {
  const res = await api.post(`/records/user/${userId}`, {
    type,
    subjectType,
    subjectId,
    label,
    value
  });
  return res.data;
};

export const getEntryById = async (recordId) => {
  const res = await api.get(`/records/${recordId}`);
  return res.data;
};

export const getEntriesByUser = async (userId) => {
  const res = await api.get(`/records/user/${userId}`);
  return res.data;
};

export const getEntriesBySubject = async (subjectType, subjectId) => {
  const res = await api.get(`/records/subject/${subjectType}/${subjectId}`);
  return res.data;
};

export const updateEntry = async (recordId, {
  type,
  subjectType,
  subjectId,
  label,
  value
}) => {
  const res = await api.put(`/records/${recordId}`, {
    type,
    subjectType,
    subjectId,
    label,
    value
  });
  return res.data;
};

export const deleteEntry = async (recordId) => {
  await api.delete(`/records/${recordId}`);
};
