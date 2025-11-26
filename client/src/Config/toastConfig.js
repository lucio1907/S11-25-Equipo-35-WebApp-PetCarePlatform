import { BaseToast, ErrorToast } from "react-native-toast-message";

export const toastConfig = {
  success: (props) => (
    <BaseToast
      {...props}
      style={{
        width: "100%",
        minHeight: 70,
        borderLeftColor: "#4CAF50",
        borderLeftWidth: 8,
        backgroundColor: "#ffffff",
      }}
      contentContainerStyle={{
        width: "100%",
        paddingHorizontal: 15,
      }}
      text1Style={{ fontSize: 16, fontWeight: "700", color: "#333" }}
      text2Style={{ fontSize: 14, color: "#555" }}
    />
  ),

  error: (props) => (
    <ErrorToast
      {...props}
      style={{
        width: "100%",
        minHeight: 70,
        borderLeftColor: "#FF3B30",
        borderLeftWidth: 8,
        backgroundColor: "#ffffff",
        paddingVertical: 10,
      }}
      contentContainerStyle={{
        width: "100%",
        paddingHorizontal: 15,
      }}
      text1Style={{ fontSize: 16, fontWeight: "700", color: "#333" }}
      text2Style={{ fontSize: 14, color: "#555" }}
    />
  ),
};
