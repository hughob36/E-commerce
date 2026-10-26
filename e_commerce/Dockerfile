FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copiamos el pom.xml y descargamos las dependencias (esto acelera futuros builds)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiamos el código fuente y generamos el JAR
COPY src ./src
RUN mvn -B -DskipTests=false clean package

# ETAPA 2: Ejecución (Runtime)
# Usamos una imagen mucho más ligera (JRE) para correr la app
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiamos solo el JAR resultante de la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# EXTREMADAMENTE IMPORTANTE PARA RENDER:
# Limitamos la memoria de la JVM para que no exceda los 512MB totales del contenedor.
# -Xmx380m le dice a Java que use máximo 380MB, dejando el resto para el sistema.
ENV JAVA_TOOL_OPTIONS="-Xmx380m -Xms200m"

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]