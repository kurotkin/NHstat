FROM anapsix/alpine-java
MAINTAINER Vitaly
COPY NHstat-1.0-SNAPSHOT.jar /NHstat-1.0-SNAPSHOT.jar
COPY settings.yml /settings.yml
COPY lib /lib
CMD ["java","-jar","/NHstat-1.0-SNAPSHOT.jar"]
