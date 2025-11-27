import { useState } from "react";
import { useForm } from "react-hook-form";
import { useNavigation } from "@react-navigation/native";
import { useToastMessages } from "../Utils/useToastMessages";
import { login as loginService } from "../Services/Login";
import { useAuth } from "../Context/AuthContext";

export const useLogicLogin = () => {
  const [showPass, setShowPass] = useState(false);
  const navigation = useNavigation();
  const { login } = useAuth();

  const {
    control,
    handleSubmit,
    formState: { errors },
  } = useForm();

  const { invalidCredentials, noToken, loginSuccess, serverError } =
    useToastMessages();

  const onSubmit = async (data) => {
    try {
      const response = await loginService(data);

      if (response.success === false) {
        invalidCredentials(response?.message);
        return;
      }
      await login(response.data.accessToken);

      loginSuccess();
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
