import api from "./axios";

export const createUser = async (username) => {
  const res = await api.post("/users", {
    username
  });
  return res.data;
};

export const getUserById = async (userId) => {
  const res = await api.get(`/users/${userId}`);
  return res.data;
};

export const getUserByUsername = async (username) => {
  const res = await api.get(`/users/username/${username}`);
  return res.data;
};

export const deleteUser = async (userId) => {
  await api.delete(`/users/${userId}`);
};
