import { useForm } from "react-hook-form";
import { useNavigation } from "@react-navigation/native";
import { useToastMessages } from "../Utils/useToastMessages";
import { forgotPasswordService } from "../Services/ForgotPassword";

export const useForgotPassword = () => {
  const navigation = useNavigation();

  const {
    control,
    handleSubmit,
    formState: { errors },
    reset,
  } = useForm({
    defaultValues: { email: "" },
  });

  const {
    invalidCredentials,
    loginSuccess,
    serverError,
  } = useToastMessages();

  const onSubmit = async ({ email }) => {
    try {
      const response = await forgotPasswordService(email.trim());

      
      if (response.success === false) {
        invalidCredentials(response?.message || "Email no encontrado");
        return;
      }

      
      loginSuccess(
        response?.message ||
          "Te enviamos un enlace para restablecer tu contraseña"
      );

      reset(); 
    } catch (error) {
      serverError();
    }
  };

  return {
    control,
    handleSubmit,
    onSubmit,
    errors,
    navigation,
  };
};
