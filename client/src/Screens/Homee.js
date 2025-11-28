import { View, Text, Button } from "react-native";
Button;
import { useAuth } from "../Context/AuthContext";

export default function Homee() {
  const { logout } = useAuth();
  return (
    <View style={{ flex: 1, marginTop: 20 }}>
      <Text>home</Text>
      <Button title="cerrar" onPress={() => logout()} />
    </View>
  );
}
