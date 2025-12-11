import { View, Image, Dimensions, StyleSheet } from "react-native";
import Group from "../assets/Group.png";

const { width, height } = Dimensions.get("window");
const IMAGE_WIDTH = 200;
const IMAGE_HEIGHT = 160;
const TOTAL_IMAGES = 5;
const VERTICAL_SPACING = 140;
const HORIZONTAL_OFFSET = 60;

const styles = StyleSheet.create({
  imageContainer: {
    position: "absolute",
    width: IMAGE_WIDTH,
    height: IMAGE_HEIGHT,
    justifyContent: "center",
    alignItems: "center",
  },
  groupImage: {
    width: "100%",
    height: "100%",
    resizeMode: "contain",
  },
});

export default function renderDiagonalColumn() {
  const images = [];

  const startX = width - IMAGE_WIDTH - 0;
  const startY = 60;

  const colorScheme = [
    { type: "original", opacity: 0.85 },
    { type: "green", opacity: 0.9 },
    { type: "original", opacity: 0.8 },
    { type: "green", opacity: 0.85 },
    { type: "original", opacity: 0.75 },
  ];

  for (let i = 0; i < TOTAL_IMAGES; i++) {
    const positionX = startX - i * HORIZONTAL_OFFSET;
    const positionY = startY + i * VERTICAL_SPACING;

    if (positionY > height - 150) break;

    const colorConfig = colorScheme[i] || colorScheme[colorScheme.length - 1];

    let tintColor = null;
    if (colorConfig.type === "green") {
      const greenShades = ["rgba(76, 175, 80, 0.85)", "rgba(56, 142, 60, 0.9)"];
      tintColor = greenShades[i % greenShades.length];
    }

    images.push(
      <View
        key={`image-container-${i}`}
        style={[
          styles.imageContainer,
          {
            left: positionX,
            top: positionY,
          },
        ]}
      >
        <Image
          source={Group}
          style={[
            styles.groupImage,
            {
              tintColor: tintColor,
              opacity: colorConfig.opacity,
              transform: [{ rotate: `${i % 2 === 0 ? -12 : 5}deg` }],
            },
          ]}
        />
      </View>
    );
  }

  return images;
}
