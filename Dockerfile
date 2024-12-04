FROM gcr.io/distroless/java21
COPY target/eux-avslutt-rinasaker.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
