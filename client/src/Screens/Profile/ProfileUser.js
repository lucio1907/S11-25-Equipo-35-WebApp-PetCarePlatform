import {
  View,
  Text,
  Image,
  TouchableOpacity,
  ActivityIndicator,
} from "react-native";
import Layout from "../../Components/Layout";
import Ionicons from "@expo/vector-icons/Ionicons";
import FontAwesome6 from "@expo/vector-icons/FontAwesome6";

import { useProfile } from "../../Hooks/useProfile";
import { styles } from "../../Styles/ProfileUser";

export default function ProfileUser() {
  
  const { user, pets, loading, userData, hasPets, navigation } = useProfile();
  
  if (loading || !user) {
    return (
      <Layout>
        <View style={styles.loadingContainer}>
          <ActivityIndicator size="large" color="#ea9b56" />
          <Text style={styles.loadingText}>Loading...</Text>
        </View>
      </Layout>
    );
  }

  return (
    <Layout>
      <View style={styles.container}>
        <View style={styles.header}>
          <Ionicons
            name="chevron-back"
            size={24}
            color="black"
            onPress={() => navigation.navigate("Home")}
          />
          <Text>
            <Text style={styles.name}>{userData.firstName}</Text> Profile
          </Text>
          <FontAwesome6
            name="edit"
            size={24}
            color="black"
            onPress={() => navigation.navigate("EditProfile")}
          />
        </View>

        <View style={styles.sectionImage}>
          <Image
            source={{
              uri: "https://wallpapers.com/images/featured/imagenes-de-perfil-geniales-4co57dtwk64fb7lv.jpg",
            }}
            style={styles.profileImage}
            resizeMode="cover"
          />
          <Text style={styles.nameImage}>
            {userData.firstName} {userData.lastName}
          </Text>
        </View>

        <View style={styles.sectionInfo}>
          <View style={styles.infoItem}>
            <Text style={styles.label}>Email</Text>
            <Text style={styles.value}>{userData.email}</Text>
          </View>
          <View>
            <Text style={styles.label}>Phone</Text>
            <Text style={styles.value}>{userData.phone || "No phone"}</Text>
          </View>
        </View>

        <View style={styles.sectionPets}>
          <Text style={styles.textPets}>My pets</Text>

          <View style={styles.petsContainer}>
            {hasPets
              ? pets.map((pet, index) => (
                  <View key={index} style={styles.petItem}>
                    <Image
                      source={{
                        uri:
                          pet.imageUrl ||
                          "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRPpl5XpGvsbCgINnrVk9m9UIVJcqUWQuchIA&s",
                      }}
                      style={styles.profilePets}
                      resizeMode="cover"
                    />
                    <Text style={styles.namePets}>{pet.name}</Text>
                  </View>
                ))
              : null}
            {(!hasPets || pets.length < 3) && (
              <TouchableOpacity style={styles.addPetItem}>
                <View style={styles.addPets}>
                  <FontAwesome6 name="add" size={24} color="#628141" />
                </View>
                <Text style={styles.addPetText}>Add pet</Text>
              </TouchableOpacity>
            )}
          </View>
        </View>
      </View>
    </Layout>
  );
}


