# 2024_DANPOONG_TEAM_50_BE
![KakaoTalk_20241124_035709129](https://github.com/user-attachments/assets/64dca812-5181-498f-b8ed-ef0f64fa818e)


2024 단풍톤 50팀 결식아동에게 따듯한 한끼를 - 마이무 프로젝트를 위한 BE API SERVER 입니다.

## 기능 설명 및 배포 URL - Swagger Page
API 상세 명세서는 Swagger 를 통해 배포되었습니다.

배포 URL : https://api.mymoo.site/swagger-ui/index.html

## 시작 가이드
![KakaoTalk_20241124_031116284](https://github.com/user-attachments/assets/28dc6267-d21f-406d-9933-1d47104ebe58)


본 프로젝트는 Docker, Docker Compose 를 통해 배포하였습니다.
SpringBoot, MariaDB, Redis 총 3개의 컨테이너로 구성되어있습니다.

먼저 본 레포지토리를 clone 합니다.
```
git clone https://github.com/9oormthon-univ/2024_DANPOONG_TEAM_50_BE.git

```
application-dev.yml 을 작성하여 환경 변수를 설정합니다.

gradle 을 통해 jar 파일을 build 합니다.
```
chmod +x ./gradlew
clean bootJar
```
서버에 Docker 를 설치하고
Docker 를 사용하여 이미지를 만듭니다.
```
docker build -t ${{ IMAGEName_YOU_WANT }} .
```
Docker Compose 를 실행시킵니다.
```
docker compose up 
```
Docker 명령어를 통해 컨테이너가 잘 실행되었는지 확인합니다.
```
docker ps
```


