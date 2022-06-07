FROM openjdk:8
VOLUME /tmp
EXPOSE 8002
ADD ./target/item-service-0.0.1-SNAPSHOT.jar item-service.jar
ENTRYPOINT ["java","-jar","/item-service.jar"]