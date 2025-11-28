import { createNativeStackNavigator } from "@react-navigation/native-stack";
import { useAuth } from "../Context/AuthContext";

import Login from "../Screens/Login";
import Register from "../Screens/Register";
import Home from "../Screens/Homee";
import Loading from "../Components/Loading";

const Stack = createNativeStackNavigator();

export default function RootStack() {
  const { token, loading } = useAuth();

  if (loading) return <Loading />;

  return (
    <Stack.Navigator screenOptions={{ headerShown: false }}>
      {token ? (
        <Stack.Screen name="Home" component={Home} />
      ) : (
        <>
          <Stack.Screen name="Login" component={Login} />
          <Stack.Screen name="Register" component={Register} />
        </>
      )}
    </Stack.Navigator>
  );
}
