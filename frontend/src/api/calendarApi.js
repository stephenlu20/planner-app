import api from "./axios";

export const createCalendar = async (userId, name) => {
  const res = await api.post("/calendars", {
    userId,
    name
  });
  return res.data;
};

export const getCalendarsForUser = async (userId) => {
  const res = await api.get(`/calendars/user/${userId}`);
  return res.data;
};

export const reorderCalendars = async (userId, orderedCalendarIds) => {
  const res = await api.put("/calendars/reorder", {
    userId,
    orderedCalendarIds
  });
  return res.data;
};

export const deleteCalendar = async (calendarId) => {
  await api.delete(`/calendars/${calendarId}`);
};
