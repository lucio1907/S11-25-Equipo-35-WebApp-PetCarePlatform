import { useState } from "react";
import { View, Text, TextInput, TouchableOpacity, Image } from "react-native";
import { useForm, Controller } from "react-hook-form";
import { useNavigation } from "@react-navigation/native";

import { styles } from "../Styles/Register";

import perito from "../assets/ImgTop.png";
import Gtito from "../assets/imgBottom.png";
import Logo from "../assets/logo.png";

export default function LoginScreen() {
  const [showPass, setShowPass] = useState(false);
  const navigation = useNavigation();
  const {
    control,
    handleSubmit,
    formState: { errors },
  } = useForm();

  const onSubmit = async (data) => {
    try {
      console.log("login data:", data);
    } catch (error) {
      console.log("error", error);
    }
  };

  return (
    <View style={styles.container}>
      <Image source={perito} style={styles.topImage} />
      <Image source={Logo} style={styles.logo} resizeMode="contain" />

      <View style={styles.form}>
        <Text style={styles.title}>Welcome Back</Text>
        <Text style={styles.subtitle}>Sign in to continue</Text>
        <Text style={styles.label}>Email</Text>
        <Controller
          control={control}
          name="email"
          rules={{
            required: "Email is required",
            pattern: {
              value: /\S+@\S+\.\S+/,
              message: "Invalid email format",
            },
          }}
          render={({ field: { onChange, value } }) => (
            <TextInput
              style={[styles.input, errors.email && styles.errorInput]}
              placeholder="Enter your email"
              placeholderTextColor="#8A8A8A"
              value={value}
              onChangeText={onChange}
              keyboardType="email-address"
              autoCapitalize="none"
            />
          )}
        />
        {errors.email && (
          <Text style={styles.errorMsg}>{errors.email.message}</Text>
        )}
        <Text style={styles.label}>Password</Text>
        <View style={[styles.inputRow, errors.password && styles.errorInput]}>
          <Controller
            control={control}
            name="password"
            rules={{
              required: "Password is required",
              minLength: { value: 6, message: "Min 6 characters" },
            }}
            render={({ field: { onChange, value } }) => (
              <TextInput
                style={styles.inputRowText}
                placeholder="Enter your password"
                secureTextEntry={!showPass}
                placeholderTextColor="#8A8A8A"
                value={value}
                onChangeText={onChange}
                autoCapitalize="none"
              />
            )}
          />
          <TouchableOpacity onPress={() => setShowPass(!showPass)}>
            <Text style={styles.eye}>👁️</Text>
          </TouchableOpacity>
        </View>
        {errors.password && (
          <Text style={styles.errorMsg}>{errors.password.message}</Text>
        )}
        <TouchableOpacity
          style={styles.button}
          onPress={handleSubmit(onSubmit)}
        >
          <Text style={styles.buttonText}>Sign In</Text>
        </TouchableOpacity>
        <Text style={styles.bottomText}>
          Don’t have an account?
          <Text
            onPress={() => navigation.navigate("Register")}
            style={styles.signIn}
          >
            Create Account
          </Text>
        </Text>
      </View>
      <Image source={Gtito} style={styles.bottomImage} resizeMode="contain" />
    </View>
  );
}
