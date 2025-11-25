import { NavigationContainer } from "@react-navigation/native";
import RootStack from "./src/Navigation/RootStack";

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
