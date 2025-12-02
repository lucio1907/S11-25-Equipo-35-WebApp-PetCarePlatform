import { createNativeStackNavigator } from "@react-navigation/native-stack";
import { useAuth } from "../Context/AuthContext";

import Login from "../Screens/Login";
import Register from "../Screens/Register";
import Loading from "../Components/Loading";
import ForgotPassword from "../Screens/ForgotPassword";
import MyTabs from "./MyTabs";

const Stack = createNativeStackNavigator();

export default function RootStack() {
  const { token, loading } = useAuth();

  if (loading) return <Loading />;

  return (
    <Stack.Navigator screenOptions={{ headerShown: false }}>
      {token ? (
        <Stack.Screen name="MyTabs" component={MyTabs} />
      ) : (
        <>
          <Stack.Screen name="Login" component={Login} />
          <Stack.Screen name="ForgotPassword" component={ForgotPassword} />
          <Stack.Screen name="Register" component={Register} />
        </>
      )}
    </Stack.Navigator>
  );
}
