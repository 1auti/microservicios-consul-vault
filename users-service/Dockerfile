FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml ./pom.xml
COPY users-service/pom.xml ./users-service/pom.xml

WORKDIR /app/users-service
RUN mvn dependency:go-offline -B

COPY users-service/src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/users-service/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]