import { NavigationContainer } from "@react-navigation/native";
import RootStack from "./src/Navigation/RootStack";

export default function App() {
  return (
    <NavigationContainer>
      <RootStack />
    </NavigationContainer>
  );
}
