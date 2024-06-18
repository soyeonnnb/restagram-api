# 이미지 지정
FROM openjdk:17

#ARG : docker build 명렁어를 사용할 때 입력받을 수 있는 인자 선언
ARG JAR_FILE=build/libs/*.jar

# copy 이미지에 파일이나 폴더 추가
COPY ${JAR_FILE} app.jar

# EntryPoint 컨테이너를실행할 때, 실행할 명령어를 지정해준다.
ENTRYPOINT [ "java" ,"-jar", "app.jar"]