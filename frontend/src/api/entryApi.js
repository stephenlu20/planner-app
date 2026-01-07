import api from "./axios";

export const createEntry = async (userId, {
  type,
  subjectType,
  subjectId,
  label,
  value
}) => {
  const res = await api.post(`/entries/user/${userId}`, {
    type,
    subjectType,
    subjectId,
    label,
    value
  });
  return res.data;
};

export const getEntryById = async (entryId) => {
  const res = await api.get(`/entries/${entryId}`);
  return res.data;
};

export const getEntriesByUser = async (userId) => {
  const res = await api.get(`/entries/user/${userId}`);
  return res.data;
};

export const getEntriesBySubject = async (subjectType, subjectId) => {
  const res = await api.get(`/entries/subject/${subjectType}/${subjectId}`);
  return res.data;
};

export const updateEntry = async (entryId, {
  type,
  subjectType,
  subjectId,
  label,
  value
}) => {
  const res = await api.put(`/entries/${entryId}`, {
    type,
    subjectType,
    subjectId,
    label,
    value
  });
  return res.data;
};

export const deleteEntry = async (entryId) => {
  await api.delete(`/entries/${entryId}`);
};
