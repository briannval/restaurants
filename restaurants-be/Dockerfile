FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/restaurants-0.0.1-SNAPSHOT.jar /app/restaurants-0.0.1-SNAPSHOT.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/restaurants-0.0.1-SNAPSHOT.jar"]