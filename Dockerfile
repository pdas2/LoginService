FROM java:8

ADD target/LoginService-0.0.1-SNAPSHOT.jar /LoginService.jar

RUN sh -c 'touch /LoginService.jar'

#EXPOSE 8140
EXPOSE 80

CMD ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/LoginService.jar"]