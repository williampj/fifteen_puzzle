FROM eclipse-temurin:22-jdk-alpine
WORKDIR /app
COPY target/puzzle*.jar app.jar
COPY .env.properties .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
