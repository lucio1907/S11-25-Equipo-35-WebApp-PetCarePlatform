import { apiUrl } from "../Api/apiUrl";

export const getPetsUser = async (token, userId) => {
  
  const response = await fetch(`${apiUrl}/user/${userId}/pets`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    credentials: "include",
  });

  return response.json();
};
