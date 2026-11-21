import api from "./client";

export const getFoods = (nome) =>
  api.get("/foods", { params: nome ? { nome } : {} }).then((res) => res.data);