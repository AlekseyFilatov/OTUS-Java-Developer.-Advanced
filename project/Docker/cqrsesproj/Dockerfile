FROM openjdk:21
VOLUME /tmp
EXPOSE 8087
EXPOSE 80
EXPOSE 30301
EXPOSE 9092
COPY target/cqrsesproject-application.jar cqrsesproject-application.jar
ENTRYPOINT ["java","-jar","/cqrsesproject-application.jar"]