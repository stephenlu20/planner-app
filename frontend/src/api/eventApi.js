import api from "./axios";

export const createEvent = async ({
  note,
  orderIndex,
  userId,
  calendarId
}) => {
  const res = await api.post("/events", {
    note,
    orderIndex,
    userId,
    calendarId
  });
  return res.data;
};

export const getEventsByUser = async (userId) => {
  const res = await api.get(`/events/user/${userId}`);
  return res.data;
};

export const getEventsByCalendar = async (calendarId) => {
  const res = await api.get(`/events/calendar/${calendarId}`);
  return res.data;
};

export const toggleEventCompleted = async (eventId) => {
  const res = await api.put(`/events/${eventId}/toggle`);
  return res.data;
};

export const updateEvent = async (eventId, note) => {
  const res = await api.put(`/events/${eventId}`, { note });
  return res.data;
};

export const reorderEvents = async ({
  calendarId,
  orderedEventIds
}) => {
  const res = await api.put("/events/reorder", {
    calendarId,
    orderedEventIds
  });
  return res.data;
};

export const deleteEvent = async (eventId) => {
  await api.delete(`/events/${eventId}`);
};