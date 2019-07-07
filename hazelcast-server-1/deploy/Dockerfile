# Start with a base image containing Java runtime
FROM openjdk:8-jdk-alpine

# Add Maintainer Info
LABEL maintainer="athul.ravindran87@gmail.com"

# Add a volume pointing to /tmp
VOLUME /usr/app

COPY /target/hazelcast-server-1-0.0.1-SNAPSHOT.jar /tmp/hazelcast-server-1.jar

WORKDIR /tmp

EXPOSE 8762

# Run the jar file
ENTRYPOINT ["java","-jar","hazelcast-server-1.jar"]