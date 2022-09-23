#
# Build stage
#
FROM maven:3.8.3-openjdk-17 AS build
ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD pom.xml $HOME
RUN mvn verify --fail-never
ADD . $HOME
RUN mvn package

#
# Package stage
#
FROM openjdk:17.0.2-jdk
COPY --from=build /usr/app/target/app-0.0.1-SNAPSHOT.jar /usr/app/app-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/app/app-0.0.1-SNAPSHOT.jar"]