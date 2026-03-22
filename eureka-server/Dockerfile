FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copiamos el pom padre en su propia carpeta para que relativePath lo encuentre
COPY pom.xml ./pom.xml
COPY eureka-server/pom.xml ./eureka-server/pom.xml

# Bajamos dependencias desde la carpeta del módulo
WORKDIR /app/eureka-server
RUN mvn dependency:go-offline -B

# Copiamos el código fuente y compilamos
COPY eureka-server/src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/eureka-server/target/*.jar app.jar

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "app.jar"]