import Register from "./src/Screens/Register";
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
