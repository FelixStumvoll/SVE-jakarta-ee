FROM gradle:jdk11 as build-step

RUN mkdir /app
WORKDIR /app
COPY . ./
RUN sed -i 's/spring.profiles.active=.*/spring.profiles.active=prod/' ./src/main/resources/application.properties
RUN gradle :bootJar

FROM openjdk:11-slim
COPY --from=build-step ./app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]