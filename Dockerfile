# Etapa 1: Construcción (Maven + Java 21)
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
# Copiamos el pom y el código fuente
COPY pom.xml .
COPY src ./src
# Construimos el JAR saltando los tests para ir más rápido
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución (Solo el JRE 21 ligero)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Copiamos solo el JAR desde la etapa de construcción
# IMPORTANTE: Asegúrate de que el nombre coincida con tu pom.xml
COPY --from=build /app/target/BanckendKC-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto que usa Render
EXPOSE 8080

# Comando para ejecutar la app usando el puerto dinámico de Render
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=${PORT:8080}"]