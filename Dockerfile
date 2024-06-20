# Etapa de construcción: Usa una imagen base con Maven y JDK 17
FROM maven:3.8.4-openjdk-17-slim AS build

# Establecer un directorio de trabajo
WORKDIR /gymapp

# Copiar archivos de tu proyecto al directorio de trabajo
COPY . /gymapp

# Ejecutar Maven para construir el proyecto en modo prod, para que admita las variables de entorno.
RUN mvn clean package -DskipTests

# Etapa de producción: Crear una nueva imagen basada en OpenJDK 17
FROM openjdk:17-jdk-slim

# Exponer el puerto que utilizará la aplicación
EXPOSE 8080

# Copiar el archivo JAR construido desde la etapa anterior
COPY --from=build /gymapp/target/gymapp-alpha-1.1.jar /gymapp/gymapp-alpha-1.1.jar

# Establecer el punto de entrada para ejecutar la aplicación en modo producción
ENTRYPOINT ["java", "-jar", "/gymapp/gymapp-alpha-1.1.jar"]
