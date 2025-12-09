import { View, StyleSheet } from "react-native";
import { useSafeAreaInsets } from "react-native-safe-area-context";
import renderDiagonalColumn from "../Utils/renderDiagonalColumn";

export default function Layout({ children }) {

  const insets = useSafeAreaInsets();
  
  return (
    <View style={styles.container}>
      <View style={styles.patternContainer}>{renderDiagonalColumn()}</View>
      <View style={[styles.content, { paddingTop: insets.top }]}>
        {children}
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#f1d9b4ff",
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
