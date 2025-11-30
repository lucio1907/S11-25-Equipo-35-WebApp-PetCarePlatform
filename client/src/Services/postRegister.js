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

  let json;
  try {
    json = await response.json();
  } catch (e) {
    json = { message: "Invalid JSON response", raw: await response.text() };
  }

  if (!response.ok) {
    throw json;
  }

  return json;
};
