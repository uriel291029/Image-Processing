FROM openjdk:8
ADD target/sb-app.jar app.jar
ADD tokens/StoredCredential tokens/StoredCredential
ENTRYPOINT ["java", "-jar", "app.jar"]
