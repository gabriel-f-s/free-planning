FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests



FROM amazoncorretto:25-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

ENV JAVA_TOOL_OPTIONS="-Xmx256m"

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]