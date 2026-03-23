FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml ./pom.xml
COPY gateway-service/pom.xml ./gateway-service/pom.xml

WORKDIR /app/gateway-service
RUN mvn dependency:go-offline -B

COPY gateway-service/src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/gateway-service/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]