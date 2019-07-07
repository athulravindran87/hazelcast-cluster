# Start with a base image containing Java runtime
FROM openjdk:8-jdk-alpine

# Add Maintainer Info
LABEL maintainer="athul.ravindran87@gmail.com"

# Add a volume pointing to /tmp
VOLUME /usr/app

COPY /target/hazelcast-server-2-0.0.1-SNAPSHOT.jar /tmp/hazelcast-server-2.jar

WORKDIR /tmp

EXPOSE 8763

# Run the jar file
ENTRYPOINT ["java","-Deureka.client.props=eureka-client", "-Dhazelcast.port=5702","-jar","hazelcast-server-2.jar"]