FROM eclipse-temurin:21-jre-alpine
VOLUME /tmp
EXPOSE 8097
COPY target/reportrservice-application.jar reportrservice-application.jar
ENTRYPOINT ["java","-XX:MaxRAMPercentage=80.0", "-jar","/reportrservice-application.jar"]