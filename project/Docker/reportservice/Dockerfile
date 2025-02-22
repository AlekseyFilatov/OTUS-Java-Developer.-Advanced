FROM openjdk:21
VOLUME /tmp
EXPOSE 8097
COPY target/reportrservice-application.jar reportrservice-application.jar
ENTRYPOINT ["java","-jar","/reportrservice-application.jar"]