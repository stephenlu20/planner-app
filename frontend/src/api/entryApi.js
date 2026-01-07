import api from "./axios";

// CREATE ENTRY
export const createEntry = async (userId, { type, subjectType, subjectId, label, value, note }) => {
  const res = await api.post(`/entries/user/${userId}`, {
    type,
    subjectType,
    subjectId,
    label,
    value,
    note,
  });
  return res.data;
};

// GET ENTRY BY ID
export const getEntryById = async (entryId) => {
  const res = await api.get(`/entries/${entryId}`);
  return res.data;
};

// GET ENTRIES BY SUBJECT
export const getEntriesBySubject = async (subjectType, subjectId) => {
  const res = await api.get(`/entries/subject/${subjectType}/${subjectId}`);
  return res.data;
};

// UPDATE ENTRY (only editable fields)
export const updateEntry = async (entryId, { label, value, note }) => {
  const res = await api.put(`/entries/${entryId}`, {
    label,
    value,
    note,
  });
  return res.data;
};

// DELETE ENTRY
export const deleteEntry = async (entryId) => {
  await api.delete(`/entries/${entryId}`);
};
