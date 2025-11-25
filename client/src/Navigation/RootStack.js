import { createNativeStackNavigator } from "@react-navigation/native-stack";

// SCREENS

import Login from "../Screens/Login";
import Register from "../Screens/Register";

const Stack = createNativeStackNavigator();

export default function RootStack() {
  return (
    <Stack.Navigator screenOptions={{ headerShown: false }}>
      <Stack.Screen name="Login" component={Login} headerShown={false} />
      <Stack.Screen name="Register" component={Register} />
    </Stack.Navigator>
  );
}
