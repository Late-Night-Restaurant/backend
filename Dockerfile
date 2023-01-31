FROM openjdk:11-jdk

#기본적으로 두 개의 jar 파일이 생성되어 하나로 특정해주어야 함
ARG JAR_FILE=./build/libs/simya-0.0.1-SNAPSHOT.jar

#JAR 파일 메인 디렉토리에 복사
COPY ${JAR_FILE} app.jar

#시스템 진입점 정의
ENTRYPOINT ["java", "-jar", "/app.jar"]