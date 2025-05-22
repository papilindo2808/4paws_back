# Etapa 1: Build de la aplicación
FROM maven:3.9.2-eclipse-temurin-17 AS build

WORKDIR /app

# Copia archivos de configuración y código
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
COPY src src

# Da permisos de ejecución a mvnw
RUN chmod +x mvnw

# Construye el proyecto y empaqueta el jar
RUN ./mvnw clean package -DskipTests

# Etapa 2: Imagen para ejecutar el jar
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copia el jar construido desde la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Expone el puerto que usará la app (Render usa $PORT automáticamente)
EXPOSE 8080

# Comando para ejecutar la app
ENTRYPOINT ["java", "-jar", "app.jar"]
