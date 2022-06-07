FROM openjdk:8
VOLUME /tmp
EXPOSE 8001
ADD ./target/product-service-0.0.1-SNAPSHOT.jar product-service.jar
ENTRYPOINT ["java","-jar","/product-service.jar"]