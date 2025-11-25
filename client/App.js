import Register from "./src/Screens/Register";
import LoginScreen from "./src/Screens/Login";
import { View } from "react-native";

export default function App() {
  return (
    <View style={styles.container}>
      <Register />
    </View>
  );
}

const styles = {
  container: {
    flex: 1,
  },
};
