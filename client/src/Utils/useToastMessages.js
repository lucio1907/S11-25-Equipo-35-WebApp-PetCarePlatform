import Toast from "react-native-toast-message";

export const useToastMessages = () => {
  const invalidCredentials = (msg) => {
    Toast.show({
      type: "error",
      text1: "Credenciales incorrectas",
      text2: msg || "Revisa tu email y contraseña",
    });
  };

  const noToken = () => {
    Toast.show({
      type: "error",
      text1: "Error inesperado",
      text2: "No se recibió el token",
    });
  };

  const loginSuccess = () => {
    Toast.show({
      type: "success",
      text1: "Inicio de sesión correcto",
      text2: "Bienvenido nuevamente",
    });
  };

  const serverError = () => {
    Toast.show({
      type: "error",
      text1: "Error de servidor",
      text2: "Inténtalo nuevamente más tarde",
    });
  };

  const customSuccess = (title, message) => {
    Toast.show({
      type: "success",
      text1: title,
      text2: message,
    });
  };

  const customError = (title, message) => {
    Toast.show({
      type: "error",
      text1: title,
      text2: message,
    });
  };

  const registerSuccess = () => {
    Toast.show({
      type: "success",
      text1: "Registro exitoso",
      text2: "Tu cuenta fue creada correctamente",
    });
  };

  const registerError = (msg) => {
    Toast.show({
      type: "error",
      text1: "Error en el registro",
      text2: msg || "No se pudo crear la cuenta",
    });
  };

  const emailInUse = () => {
    Toast.show({
      type: "error",
      text1: "Email en uso",
      text2: "Ya existe una cuenta con este correo",
    });
  };

  return {
    invalidCredentials,
    noToken,
    loginSuccess,
    serverError,
    customSuccess,
    customError,
    registerSuccess,
    registerError,
    emailInUse,
  };
};
