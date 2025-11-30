import { View, Text, TextInput, TouchableOpacity, Image } from "react-native";
import { Controller } from "react-hook-form";
import { styles } from "../Styles/Register";
import { stylesForgot } from "../Styles/ForgotPassword";

import dog from "../assets/dog.png";
import Gtito from "../assets/imgBottom.png";
import paw from "../assets/paw.png";

import { useForgotPassword } from "../Hooks/useForgotPassword";

export default function ForgotPassword() {
  const {
   onSubmit, errors, control, handleSubmit, navigation
  } = useForgotPassword();

  return (
    <View style={styles.container}>
      <Image source={dog} style={styles.topImage} />
      <Image source={paw} style={styles.logo} resizeMode="contain" />

      <View style={styles.form}>
        <Text style={styles.title}>Forgot Password</Text>
        <Text style={styles.subtitle}>
          Enter your email and we will send you a reset link
        </Text>

        <Text style={styles.label}>Email</Text>
        <Controller
          control={control}
          name="email"
          rules={{
            required: 'Email is required',
            pattern: { value: /\S+@\S+\.\S+/, message: 'Invalid email format' },
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

        <TouchableOpacity
          style={stylesForgot.btn}
          onPress={handleSubmit(onSubmit)}
        >
          <Text style={styles.buttonText}>Send link</Text>
        </TouchableOpacity>

        <Text
          onPress={() => navigation.navigate('Login')}
          style={stylesForgot.signInText}
        >
          Back to Login
        </Text>
      </View>

      <Image source={Gtito} style={styles.bottomImage} resizeMode="contain" />
    </View>
  )
}
