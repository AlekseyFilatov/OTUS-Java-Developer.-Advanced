FROM eclipse-temurin:21-jre-alpine
VOLUME /tmp
EXPOSE 8089
COPY target/webfluxclient-application.jar webfluxclient-application.jar
ENTRYPOINT ["java","-XX:MaxRAMPercentage=80.0","-jar","/webfluxclient-application.jar"]