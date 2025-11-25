import { useState } from "react";
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  Image,
} from "react-native";
import { useForm, Controller } from "react-hook-form";
import { styles } from "../Styles/Register";

import perito from "../assets/ImgTop.png";
import Gtito from "../assets/imgBottom.png";
import Logo from "../assets/logo.png";

export default function SignUpScreen() {
  const [showPass, setShowPass] = useState(false);
  const [showConfirm, setShowConfirm] = useState(false);

  const {
    control,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm();

  const passwordValue = watch("password");

  const onSubmit = (data) => {
    console.log("datos del formualrio", data);
  };

  return (
    <View style={styles.container}>
      <Image source={perito} style={styles.topImage} />
      <Image source={Logo} style={styles.logo} resizeMode="contain" />
      <View style={styles.form}>
        <Text style={styles.title}>Create Account</Text>
        <Text style={styles.subtitle}>Join our pet-loving community!</Text>
        <Text style={styles.label}>Name</Text>
        <View style={styles.row}>
          <Controller
            control={control}
            name="firstName"
            rules={{ required: "First name is required" }}
            render={({ field: { onChange, value } }) => (
              <TextInput
                style={[
                  styles.input,
                  styles.halfInput,
                  errors.firstName && styles.errorInput,
                ]}
                placeholder="First name"
                placeholderTextColor="#8A8A8A"
                value={value}
                onChangeText={onChange}
              />
            )}
          />
          <Controller
            control={control}
            name="lastName"
            rules={{ required: "Last name is required" }}
            render={({ field: { onChange, value } }) => (
              <TextInput
                style={[
                  styles.input,
                  styles.halfInput,
                  errors.lastName && styles.errorInput,
                ]}
                placeholder="Last name"
                placeholderTextColor="#8A8A8A"
                value={value}
                onChangeText={onChange}
              />
            )}
          />
        </View>
        {(errors.firstName || errors.lastName) && (
          <Text style={styles.errorMsg}>
            {errors.firstName?.message || errors.lastName?.message}
          </Text>
        )}
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
                placeholder="Create a password"
                secureTextEntry={!showPass}
                placeholderTextColor="#8A8A8A"
                value={value}
                onChangeText={onChange}
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
        <Text style={styles.label}>Confirm Password</Text>
        <View
          style={[styles.inputRow, errors.confirmPassword && styles.errorInput]}
        >
          <Controller
            control={control}
            name="confirmPassword"
            rules={{
              required: "Confirm password",
              validate: (value) =>
                value === passwordValue || "Passwords do not match",
            }}
            render={({ field: { onChange, value } }) => (
              <TextInput
                style={styles.inputRowText}
                placeholder="Confirm your password"
                secureTextEntry={!showConfirm}
                placeholderTextColor="#8A8A8A"
                value={value}
                onChangeText={onChange}
              />
            )}
          />
          <TouchableOpacity onPress={() => setShowConfirm(!showConfirm)}>
            <Text style={styles.eye}>👁️</Text>
          </TouchableOpacity>
        </View>
        {errors.confirmPassword && (
          <Text style={styles.errorMsg}>{errors.confirmPassword.message}</Text>
        )}
        <TouchableOpacity
          style={styles.button}
          onPress={handleSubmit(onSubmit)}
        >
          <Text style={styles.buttonText}>Sign Up</Text>
        </TouchableOpacity>

        <Text style={styles.bottomText}>
          Already have an account?
          <Text style={styles.signIn}> Sign In</Text>
        </Text>
      </View>
      <Image source={Gtito} style={styles.bottomImage} resizeMode="contain" />
    </View>
  );
}
