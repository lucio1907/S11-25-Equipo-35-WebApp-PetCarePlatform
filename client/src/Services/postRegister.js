import { apiUrl } from "../Api/apiUrl";

export const postRegister = async (data) => {
  console.log("data", data);
  const response = await fetch(`${apiUrl}/auth/register`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });

  const result = await response.json();

  return result;
};
