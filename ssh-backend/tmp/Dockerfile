FROM centos

RUN yum install -y java

VOLUME /tmp

ADD /ssh-0.0.1-SNAPSHOT.jar myapp.jar

RUN sh -c 'touch ./myapp.jar'

EXPOSE 8080

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/myapp.jar"]