FROM openjdk:21
VOLUME /tmp
EXPOSE 8089
COPY target/webfluxclient-application.jar webfluxclient-application.jar
ENTRYPOINT ["java","-jar","/webfluxclient-application.jar"]