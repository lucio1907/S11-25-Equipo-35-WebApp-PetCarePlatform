import { createNativeStackNavigator } from "@react-navigation/native-stack";

import Login from "../Screens/Login";
import Register from "../Screens/Register";
import Home from "../Screens/Homee";

const Stack = createNativeStackNavigator();

export default function RootStack() {
  return (
    <Stack.Navigator screenOptions={{ headerShown: false }}>
      <Stack.Screen name="Login" component={Login} headerShown={false} />
      <Stack.Screen name="Register" component={Register} />
      <Stack.Screen name="Home" component={Home} />
    </Stack.Navigator>
  );
}
