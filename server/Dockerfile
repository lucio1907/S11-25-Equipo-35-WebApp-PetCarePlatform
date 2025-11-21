# 1. Etapa de construcción (Build)
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests


# 2. Etapa de ejecución (Runtime)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Copiamos el jar generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar


# Cloud Run inyecta la variable PORT
ENV PORT=8080


ENV JWT_SECRET="TuClaveSecretaMuySeguraYExtremadamenteLarga1234567890!@#$%^&*"
ENV JWT_EXPIRATION="259200000"

ENV DB_USERNAME="postgres.eyvuaavkahjzpaxgrdtk"
ENV DB_PASSWORD="91aeuTeFF1WcIEry"
ENV DB_HOST="aws-1-us-east-2.pooler.supabase.com"
ENV DB_PORT="6543"
ENV DB_DATABASE="postgres"

ENV EMAIL_USERNAME="luciogastellu.dev@gmail.com"
ENV EMAIL_PASSWORD="iggb luxd rsrn wolv"


EXPOSE 8080


ENTRYPOINT ["java", "-jar", "app.jar"]