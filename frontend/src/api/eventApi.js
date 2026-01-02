import api from "./axios";

export const createEvent = async (note, orderIndex, userId, calendarId) => {
  const res = await api.post("/events", {
    note,
    orderIndex,
    userId,
    calendarId
  });
  return res.data;
};

export const toggleEvent = async (eventId) => {
  const res = await api.put(`/events/${eventId}/toggle`);
  return res.data;
};

export const reorderEvents = async (userId, orderedEventIds) => {
  const res = await api.put("/events/reorder", {
    userId,
    orderedEventIds
  });
  return res.data;
};

export const deleteEvent = async (eventId) => {
  await api.delete(`/events/${eventId}`);
};
