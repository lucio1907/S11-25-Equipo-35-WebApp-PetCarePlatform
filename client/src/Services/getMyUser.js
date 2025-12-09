import { apiUrl } from "../Api/apiUrl";

export const getMyUser = async (token) => {
  
  const response = await fetch(`${apiUrl}/users/me`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    credentials: "include",
  });

  return response.json();
};
