# Start with a base image containing Java runtime
FROM openjdk:8-jdk-alpine

# Add Maintainer Info
LABEL maintainer="athul.ravindran87@gmail.com"

# Add a volume pointing to /tmp
VOLUME /usr/app

COPY /target/hazelcast-client-2-0.0.1-SNAPSHOT.jar /tmp/hazelcast-client-2.jar

WORKDIR /tmp

EXPOSE 8765

# Run the jar file
ENTRYPOINT ["java","-Deureka.client.props=eureka-client","-jar","hazelcast-client-2.jar"]