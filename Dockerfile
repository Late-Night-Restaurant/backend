FROM adoptopenjdk/openjdk11:latest
FROM ubuntu:22.04
MAINTAINER yejunpark1 <jun20020216@gmail.com>

#자동 실행할 명령어
#RUN ["/bin/bash", "-c", "sudo apt -y install nginx"]
RUN ["chmod","+x","${JAVA_HOME}"]

# Avoding user interaction with tzdata
ENV DEBIAN_FRONTEND=noninteractive

#호스트와 연결할 포트 번호 설정
EXPOSE 80

#호스트의 volume 설정 -> Docker Volume을 활용하여 Tomcat 로그 저장
VOLUME /tmp

#기본적으로 두 개의 jar 파일이 생성되어 하나로 특정해주어야 함
ARG JAR_FILE=./build/libs/simya-0.0.1-SNAPSHOT.jar

#JAR 파일 메인 디렉토리에 복사
COPY ${JAR_FILE} app.jar


#시스템 진입점 정의
ENTRYPOINT ["/Users/jun/Library/Java/JavaVirtualMachines/corretto-17.0.5/Contents/Home/bin/java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]
