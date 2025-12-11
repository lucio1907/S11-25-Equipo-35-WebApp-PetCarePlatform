import { useState, useEffect } from "react";
import { getMyUser } from "../Services/getMyUser";
import { getPetsUser } from "../Services/getPetsUser";
import { useToken } from "./useToken";
import { useNavigation } from "@react-navigation/native";

export const useProfile = () => {
    
  const [user, setUser] = useState(null);
  const [pets, setPets] = useState([]);
  const [loading, setLoading] = useState(true);
  const token = useToken();
  const navigation = useNavigation();

  useEffect(() => {
    const fetchData = async () => {
      try {
        if (token) {
          const userData = await getMyUser(token);

          if (userData.success === true) {
            setUser(userData);
            const petsData = await getPetsUser(token, userData.data.id);

            if (petsData.length > 0) {
              setPets(petsData);
            } else {
              setPets([]);
            }
          }
        }
      } catch (error) {
        console.log(error);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, [token]);

  const userData = user?.data;
  const hasPets = pets && pets?.length > 0;

  return { user, pets, loading, userData, hasPets, navigation };
};
