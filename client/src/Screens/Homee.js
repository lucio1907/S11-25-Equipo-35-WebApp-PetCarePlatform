import { View, Text, Button } from "react-native";

import { useAuth } from "../Context/AuthContext";

import Layout from "../Components/Layout";

export default function Homee() {
  const { logout } = useAuth();
  return (
    <Layout>
      <View style={{ flex: 1, marginTop: 20 }}>
        <Text>home</Text>
        <Button title="cerrar" onPress={() => logout()} />
      </View>
    </Layout>
  );
}
