# Dockerfile
FROM openjdk:17

MAINTAINER nju05

VOLUME /tmp

ADD target/demo1-0.0.1-SNAPSHOT.jar cloud-native.jar

RUN bash -c 'touch /cloud-native.jar'

ENTRYPOINT ["java", "-jar", "/cloud-native.jar"]


#VOLUME /tmp
#
#ADD target/demo1-0.0.1-SNAPSHOT.jar .
#
#ENTRYPOINT ["java", "-jar", "demo1-0.0.1-SNAPSHOT.jar"]

EXPOSE 8905