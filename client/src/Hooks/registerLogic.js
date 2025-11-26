import { useToastMessages } from "../Utils/useToastMessages";
import { postRegister } from "../Services/postRegister";
import { useForm } from "react-hook-form";
import { useNavigation } from "@react-navigation/native";
import { useState } from "react";

export const registerLogic = () => {
  const [showPass, setShowPass] = useState(false);
  const navigation = useNavigation();
  const { registerSuccess, registerError, emailInUse } = useToastMessages();
  const {
    control,
    handleSubmit,
    formState: { errors },
  } = useForm();

  const onSubmit = async (data) => {
    try {
      const response = await postRegister(data);
      if (!response.ok) {
        if (response.message?.includes("email")) {
          emailInUse();
        } else {
          registerError(response.message);
        }
        return;
      }

      registerSuccess();

      setTimeout(() => {
        navigation.navigate("Login");
      }, 1200);
    } catch (error) {
      registerError(error.message || "Registration failed");
      console.log("error", error);
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
