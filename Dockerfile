FROM maven:3.9.8-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml ./
COPY gift-quiz-model/pom.xml gift-quiz-model/pom.xml
COPY gift-quiz-server/pom.xml gift-quiz-server/pom.xml

RUN mvn -pl gift-quiz-server -am dependency:go-offline

COPY gift-quiz-model/src gift-quiz-model/src
COPY gift-quiz-server/src gift-quiz-server/src

RUN mvn -pl gift-quiz-server -am package -Dmaven.test.skip=true

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/gift-quiz-server/target/gift-quiz-server-1.0.0-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
