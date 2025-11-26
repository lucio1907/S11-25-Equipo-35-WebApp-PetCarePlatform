import { useState } from "react";
import { useForm } from "react-hook-form";
import AsyncStorage from "@react-native-async-storage/async-storage";
import { useNavigation } from "@react-navigation/native";
import { useToastMessages } from "../Utils/useToastMessages";
import { login } from "../Services/Login";

export const logicLogin = () => {
  const [showPass, setShowPass] = useState(false);
  const navigation = useNavigation();

  const {
    control,
    handleSubmit,
    formState: { errors },
  } = useForm();

  const { invalidCredentials, noToken, loginSuccess, serverError } =
    useToastMessages();

  const onSubmit = async (data) => {
    try {
      const response = await login(data);

      if (!response.ok) {
        invalidCredentials(response?.message);
        return;
      }

      if (!response?.data?.accessToken) {
        noToken();
        return;
      }

      await AsyncStorage.setItem("token", response.data.accessToken);

      loginSuccess();
      navigation.navigate("Home");
    } catch (error) {
      serverError();
    }
  };

  return {
    control,
    handleSubmit,
    onSubmit,
    errors,
    showPass,
    setShowPass,
    navigation,
  };
};
