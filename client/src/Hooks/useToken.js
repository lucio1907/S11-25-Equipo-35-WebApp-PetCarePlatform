import { useEffect, useState } from "react";
import AsyncStorage from "@react-native-async-storage/async-storage";

export function useToken() {
  const [token, setToken] = useState(null);
  console.log("useToken", token);
  useEffect(() => {
    const loadToken = async () => {
      try {
        const storedToken = await AsyncStorage.getItem("token");
        if (!storedToken) {
          console.log("No token found");
          return;
        }
        setToken(storedToken);
      } catch (error) {
        console.log("Error loading token:", error);
      }
    };

    loadToken();
  }, []);

  return token;
}
