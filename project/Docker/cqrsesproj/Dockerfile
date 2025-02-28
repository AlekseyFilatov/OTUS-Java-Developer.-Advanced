FROM eclipse-temurin:21-jre-alpine
VOLUME /tmp
EXPOSE 8087
EXPOSE 80
EXPOSE 30301
EXPOSE 9092
COPY target/cqrsesproject-application.jar cqrsesproject-application.jar
ENTRYPOINT ["java","-XX:MaxRAMPercentage=80.0","-jar","/cqrsesproject-application.jar"]