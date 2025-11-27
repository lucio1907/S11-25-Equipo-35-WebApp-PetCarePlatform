import { createContext, useContext, useEffect, useState } from "react";
import AsyncStorage from "@react-native-async-storage/async-storage";

const AuthContext = createContext();

export function AuthProvider({ children }) {
  const [loading, setLoading] = useState(true);
  const [token, setToken] = useState(null);

  useEffect(() => {
    const loadToken = async () => {
      const stored = await AsyncStorage.getItem("token");
      setToken(stored);
      setLoading(false);
    };

    loadToken();
  }, []);

  const login = async (tokenValue) => {
    console.log("login", tokenValue);
    await AsyncStorage.setItem("token", tokenValue);
    setToken(tokenValue);
  };

  const logout = async () => {
    await AsyncStorage.removeItem("token");
    setToken(null);
  };

  return (
    <AuthContext.Provider value={{ token, login, logout, loading }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
