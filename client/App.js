import { NavigationContainer } from "@react-navigation/native";
import RootStack from "./src/Navigation/RootStack";
import Toast from "react-native-toast-message";
import { toastConfig } from "./src/Config/toastConfig";

export default function App() {
  return (
    <>
      <NavigationContainer>
        <RootStack />
      </NavigationContainer>
      <Toast config={toastConfig} />
    </>
  );
}
