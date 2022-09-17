#!/bin/bash
# build script
JAR_FILE=target/demo-0.0.1-SNAPSHOT.jar

./mvnw install
docker build --build-arg JAR_FILE=$JAR_FILE -t demo/myapp .