import Register from "./src/Screens/Register";
import LoginScreen from "./src/Screens/Login";
import { View } from "react-native";

export default function App() {
  return (
    <AuthProvider>
      <NavigationContainer>
        <RootStack />
      </NavigationContainer>
      <Toast config={toastConfig} />
    </AuthProvider>
  );
}
