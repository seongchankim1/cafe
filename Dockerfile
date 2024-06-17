# Base image로 OpenJDK 17을 사용
FROM openjdk:17-jdk

# 메인테이너 정보 추가
LABEL maintainer="minielec7@gmail.com"

# /tmp 디렉토리로의 볼륨 추가
VOLUME /tmp

# 8080 포트를 외부에 노출
EXPOSE 8080

WORKDIR /app

# 애플리케이션의 JAR 파일 경로 설정 (build.gradle에 설정된 경로와 일치해야 함)
COPY build/libs/cafe-0.0.1-SNAPSHOT.jar app.jar

# 애플리케이션의 JAR 파일을 컨테이너의 app.jar로 복사
COPY ${JAR_FILE} app.jar

# JAR 파일 실행
ENTRYPOINT ["java","-jar","/app.jar"]
