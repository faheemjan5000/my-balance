FROM openjdk:17
EXPOSE 8080
ADD target/my-balance.jar my-balance.jar
ENTRYPOINT ["java","-jar","/my-balance.jar"]