FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml ./pom.xml
COPY cliente-service/pom.xml ./cliente-service/pom.xml

WORKDIR /app/cliente-service
RUN mvn dependency:go-offline -B

COPY cliente-service/src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/cliente-service/target/*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]