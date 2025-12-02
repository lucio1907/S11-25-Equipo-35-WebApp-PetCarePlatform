import { View, StyleSheet } from "react-native";
import renderDiagonalColumn from "../Utils/renderDiagonalColumn";

export default function Layout({ children }) {
  return (
    <View style={styles.container}>
      <View style={styles.patternContainer}>{renderDiagonalColumn()}</View>
      <View style={styles.content}>{children}</View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#f5f5f5",
  },
  patternContainer: {
    position: "absolute",
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    zIndex: 0,
  },
  content: {
    flex: 1,
    zIndex: 1,
  },
});
