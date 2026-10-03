# ---------- build stage ----------
FROM gradle:8.10.2-jdk17 AS build
WORKDIR /app

# Копируем сначала только файлы сборки для кэширования зависимостей
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY --chmod=755 gradlew ./
RUN ./gradlew dependencies --no-daemon || true

# Затем исходники и сборка jar
COPY src ./src
RUN ./gradlew clean bootJar --no-daemon -x test

# ---------- runtime stage ----------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
