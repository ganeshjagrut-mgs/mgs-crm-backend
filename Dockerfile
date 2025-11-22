FROM --platform=linux/amd64 eclipse-temurin:17-jdk AS build

WORKDIR /app

COPY . .

# Build the application, skip tests
RUN ./gradlew clean build -x test -x compileTestJava -x modernizer

# -------- Runtime stage --------
FROM --platform=linux/amd64 eclipse-temurin:17-jre

WORKDIR /app

# Safer: Only copy the Spring Boot fat JAR (not plain.jar or source jars)
COPY --from=build /app/build/libs/*-SNAPSHOT.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]
