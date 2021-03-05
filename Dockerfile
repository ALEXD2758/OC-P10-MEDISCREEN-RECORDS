FROM adoptopenjdk/openjdk11:alpine-jre
WORKDIR /usr/app
COPY build/libs/records-0.0.1-SNAPSHOT.jar mediscreen-records.jar
CMD ["java", "-jar", "mediscreen-records.jar"]