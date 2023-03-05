![Banner.png](https://user-images.githubusercontent.com/80024278/222954613-48234ae8-5fe2-4e85-92d9-2a25358bb80a.png)
## “하루가 끝나고 사람들이 귀가를 서두를 무렵, 어서오세요 힐링 SNS 심야식당 입니다”

### 어떤 사람들을 위한 서비스일까요?
- SNS 피로를 느끼는 사람들
- 힐링과 위로가 필요한 사람들
- 새벽감성을 좋아하는 사람들
- 나와 비슷한 사람을 찾고 싶은 사람들

<br/>
힐링 SNS 심야식당은
10:00 Open - 05:00 Close, 제한적 저장이 가능한 휘발성 데이터와 강한 익명성, 그리고 유저들간 팔로우가 없는 위치기반의 운영과
이를 기반으로 하는 채팅방으로
나와 조금 더 비슷한 사람들과 조금 더 진실 된 소통을 할 수 있는 SNS입니다
힐링 SNS 심야식당을 통해
자극적이고, 나 빼고 모두가 행복해보이는, 사용할 수록 외롭게만 느껴졌던 SNS의 문제점을 해결하고자 합니다.


### 주요 기능
#### 회원
- 회원가입, 탈퇴
- 로그인, 로그아웃
- 소셜 로그인 (Kakao)
- JWT 토큰 인증
- 멀티 프로필 (한 개의 대표 프로필 지정)

#### 이야기 집
- 이야기 집 등록, 수정, 조회, 삭제하기
- 찜하기/취소 기능, 목록 보기
- 평점과 리뷰 작성, 조회, 삭제
- 이야기 집 Open/Close
- 오늘의 메뉴 선정하기, 바꾸기

#### 채팅
- 실시간 다중 채팅
- 전체/특정 손님 채팅창 얼리기
- 특정 손님
- 손님 목록, 프로필 정보 조회


<br/><br/>

## 🛠️ 개발 환경

| 프레임워크 | SpringBoot |
| --- | --- |
| 통합 개발 환경 | IntelliJ |
| 배포 | AWS EC2(Ubuntu), S3, CodeDepoly |
| 데이터베이스 | AWS RDS(MySQL) |
| Project 빌드 관리 도구 | Gradle |
| CI/CD 툴 | Github Actions |
| ERD 다이어그램 툴 | ERDCloud |
| Java version | Java 11  |
| 패키지 구조 | 도메인 패키지 구조 |
| 기타 | JPA, WebSocket, Redis |
| 버전 관리 | Git, Github |
| 협업 툴 | Notion, Discord, Figma |

### 시스템 아키텍처

![Service Architecture.png](https://user-images.githubusercontent.com/80024278/222954610-ca9ece89-a8b2-4a1f-bcc2-a1839304a7d8.png)

### 프로젝트 폴더 구조
<details>
<summary> 자세히 보기 👈🏻 </summary>
<div>


 ```
    src
        ├── main
        │   ├── java
        │   │   └── com
        │   │       └── backend
        │   │           └── simya
        │   │               ├── SimyaApplication.java
        │   │               ├── domain
        │   │               │   ├── chat
        │   │               │   │   ├── controller
        │   │               │   │   │   ├── ChatController.java
        │   │               │   │   │   ├── ChatRoomController.java
        │   │               │   │   │   └── IndexController.java
        │   │               │   │   ├── dto
        │   │               │   │   │   ├── ChatMessage.java
        │   │               │   │   │   ├── ChatMessageCustom.java
        │   │               │   │   │   ├── ChatRoom.java
        │   │               │   │   │   └── ChatRoomProfile.java
        │   │               │   │   ├── repository
        │   │               │   │   │   └── ChatRoomRepository.java
        │   │               │   │   └── service
        │   │               │   │       ├── ChatService.java
        │   │               │   │       ├── RedisPublisher.java
        │   │               │   │       └── RedisSubscriber.java
        │   │               │   ├── favorite
        │   │               │   │   ├── controller
        │   │               │   │   │   └── FavoriteController.java
        │   │               │   │   ├── dto
        │   │               │   │   │   └── MyFavoriteHouseResponseDto.java
        │   │               │   │   ├── entity
        │   │               │   │   │   └── Favorite.java
        │   │               │   │   ├── repository
        │   │               │   │   │   └── FavoriteRepository.java
        │   │               │   │   └── service
        │   │               │   │       └── FavoriteService.java
        │   │               │   ├── house
        │   │               │   │   ├── controller
        │   │               │   │   │   └── HouseController.java
        │   │               │   │   ├── dto
        │   │               │   │   │   ├── request
        │   │               │   │   │   │   ├── HouseCreateRequestDto.java
        │   │               │   │   │   │   ├── HouseOpenRequestDto.java
        │   │               │   │   │   │   ├── HouseUpdateRequestDto.java
        │   │               │   │   │   │   └── TopicRequestDto.java
        │   │               │   │   │   └── response
        │   │               │   │   │       ├── HouseIntroductionResponseDto.java
        │   │               │   │   │       ├── HouseResponseDto.java
        │   │               │   │   │       ├── HouseShowResponseDto.java
        │   │               │   │   │       ├── HouseSignboardResponseDto.java
        │   │               │   │   │       └── TopicResponseDto.java
        │   │               │   │   ├── entity
        │   │               │   │   │   ├── Category.java
        │   │               │   │   │   ├── House.java
        │   │               │   │   │   └── Topic.java
        │   │               │   │   ├── repository
        │   │               │   │   │   ├── HouseRepository.java
        │   │               │   │   │   └── TopicRepository.java
        │   │               │   │   └── service
        │   │               │   │       ├── HouseService.java
        │   │               │   │       └── TopicService.java
        │   │               │   ├── jwt
        │   │               │   │   ├── controller
        │   │               │   │   │   └── AuthController.java
        │   │               │   │   ├── dto
        │   │               │   │   │   ├── request
        │   │               │   │   │   │   └── TokenRequestDto.java
        │   │               │   │   │   └── response
        │   │               │   │   │       ├── RefreshResponseDto.java
        │   │               │   │   │       └── TokenDto.java
        │   │               │   │   ├── entity
        │   │               │   │   │   └── RefreshToken.java
        │   │               │   │   ├── repository
        │   │               │   │   │   └── RefreshTokenRepository.java
        │   │               │   │   └── service
        │   │               │   │       ├── AuthService.java
        │   │               │   │       └── TokenProvider.java
        │   │               │   ├── profile
        │   │               │   │   ├── controller
        │   │               │   │   │   └── ProfileController.java
        │   │               │   │   ├── dto
        │   │               │   │   │   ├── request
        │   │               │   │   │   │   ├── ProfileRequestDto.java
        │   │               │   │   │   │   └── ProfileUpdateDto.java
        │   │               │   │   │   └── response
        │   │               │   │   │       └── ProfileResponseDto.java
        │   │               │   │   ├── entity
        │   │               │   │   │   └── Profile.java
        │   │               │   │   ├── repository
        │   │               │   │   │   └── ProfileRepository.java
        │   │               │   │   └── service
        │   │               │   │       └── ProfileService.java
        │   │               │   ├── review
        │   │               │   │   ├── controller
        │   │               │   │   │   └── ReviewController.java
        │   │               │   │   ├── dto
        │   │               │   │   │   ├── MyReviewResponseDto.java
        │   │               │   │   │   ├── ReviewRequestDto.java
        │   │               │   │   │   └── ReviewResponseDto.java
        │   │               │   │   ├── entity
        │   │               │   │   │   └── Review.java
        │   │               │   │   ├── repository
        │   │               │   │   │   └── ReviewRepository.java
        │   │               │   │   └── service
        │   │               │   │       └── ReviewService.java
        │   │               │   └── user
        │   │               │       ├── controller
        │   │               │       │   ├── OauthController.java
        │   │               │       │   └── UserController.java
        │   │               │       ├── dto
        │   │               │       │   ├── request
        │   │               │       │   │   ├── FormSignupRequestDto.java
        │   │               │       │   │   └── LoginRequestDto.java
        │   │               │       │   └── response
        │   │               │       │       ├── ChatLoginInfo.java
        │   │               │       │       ├── FormLoginResponseDto.java
        │   │               │       │       ├── KakaoAccountDto.java
        │   │               │       │       └── KakaoTokenDto.java
        │   │               │       ├── entity
        │   │               │       │   ├── BaseTimeEntity.java
        │   │               │       │   ├── LoginType.java
        │   │               │       │   ├── Role.java
        │   │               │       │   └── User.java
        │   │               │       ├── repository
        │   │               │       │   └── UserRepository.java
        │   │               │       └── service
        │   │               │           ├── OauthService.java
        │   │               │           └── UserService.java
        │   │               └── global
        │   │                   ├── common
        │   │                   │   ├── BaseException.java
        │   │                   │   ├── BaseResponse.java
        │   │                   │   ├── BaseResponseStatus.java
        │   │                   │   └── ValidErrorDetails.java
        │   │                   ├── config
        │   │                   │   ├── AwsConfig.java
        │   │                   │   ├── CorsConfig.java
        │   │                   │   ├── SecurityConfig.java
        │   │                   │   ├── auth
        │   │                   │   │   └── CustomUserDetailsService.java
        │   │                   │   ├── jwt
        │   │                   │   │   ├── JwtAccessDeniedHandler.java
        │   │                   │   │   ├── JwtAuthenticationEntryPoint.java
        │   │                   │   │   ├── JwtFilter.java
        │   │                   │   │   └── JwtSecurityConfig.java
        │   │                   │   ├── redis
        │   │                   │   │   ├── EmbeddedRedisConfig.java
        │   │                   │   │   └── RedisConfig.java
        │   │                   │   └── websocket
        │   │                   │       ├── EmbeddedRedisConfig.java
        │   │                   │       ├── RedisConfig.java
        │   │                   │       ├── WebSockConfig.java
        │   │                   │       └── handler
        │   │                   │           └── StompHandler.java
        │   │                   └── util
        │   │                       ├── ChatUtils.java
        │   │                       ├── S3Uploader.java
        │   │                       ├── SecurityUtil.java
        │   │                       └── scheduler
        │   │                           ├── ChatCachingInRedisScheduling.java
        │   │                           └── ChatWriteBackScheduling.java
        │   └── resources
        │       ├── application.yml
        └── test
 ```
</div>
</details>

### API 명세서

📄 [API Docs](https://www.notion.so/1e03e34f94fa453e9091fd298e53319a)

### 데이터베이스 ERD

![심야식당 ERD.png](https://user-images.githubusercontent.com/80024278/222954607-54d96c60-bcbc-478f-8370-dc1df44190b1.png)

## 👥 Backend Convention

### 브랜치 운영 방식

> master, develop, feat, fix, test, study
>

`master`: 최최최최최최종본 - stable all the time

`develop`: 구현 완료된 코드를 합치는 공간 → 개발자들은 여기로 PR을 날릴 것!

`feat`: 이슈 별로 기능을 개발하면서 각자가 사용할 브랜치

Git flow 전략에 따라 feat과 fix 하위 → “**feat/#이슈번호-구현하려는기능**” 형식으로

git checkout -b ‘feat/#{이슈번호}-{기능단위?}’

ex. **feat/#3-header, feat/#56-login, feat/#32-profile_info_detail**

`fix`: 이슈 별 수정용 브랜치

`test`: 개인 연습 브랜치

`study` : 공부용 브랜치    ex. **study/wak**

### Commit Message

커밋 메시지 앞에 [ feat ] [ fix ]..  붙이기 → **[ 태그 ] 제목** 의 형태로

- `FEAT` : 새로운 기능 추가  ex. [ feat ] design_create
- `ADD`: FEAT 이외의 부수적인 코드 추가 및 라이브러리 추가, 새로운 파일 생성
- `FIX` : 버그 수정
- `MOD` : 코드 수정
- `REFACTOR` : 코드 리팩토링
- `DOCS` : 문서 수정(README) 또는 주석 관련 작업
- `TEST` : 테스트 코드 추가/작성
- `CHORE` : gradle 세팅, 기타 작업
- `ETC` : 파일명, 폴더명 수정 or 파일, 폴더 삭제 or 디렉토리 구조 변경

커밋 단위:

- 세부 기능 기준
- 기능 우선 순위 정리 파일 참고

[깃(Git) 커밋 가이드](https://tech.10000lab.xyz/git/git-commit-discipline.html)

### Code Review

1. 리뷰 시 확인해보면 좋을 것
    1. 시간복잡도
    2. 잠재적인 오류 케이스
    3. 테스트코드나 구조에 리뷰
    4. 해당 기술의 로직 리뷰
    5. 변수명과 같은 '코드 컨벤션' 대한 리뷰
    6. 이외에.. 추가하면 좋을 것 → 있다면 추가해주세요!
2. 리뷰 남기기
    1. OO 보다는 XX 가 더 나은 것 같아요.
    2. XX 는 OO 부분을 참고해서 이용하면 되요. ** 키워드로 찾아보시면 좋을 것 같아요.
    3. OO 는 XX 에 의해서 문제되지 않을까요?
    4. 제 생각은 지금 이름보다 이 이름이 더 명확할 것 같은데 어떤가요?
    5. 저 부분은 좀 이상한 것 같아요, 이거 틀렸어요 (X) → 리뷰시 구체적이고 명시적인 피드백을 제공하기
    6. 등등.. 자유롭게 남기기 피드백도 좋고, 좋은 코드에 대한 칭찬도 좋을 것 같다! (아래 링크 참고)

좋은 리뷰, 안좋은 리뷰 예시 :

[효과적인 코드리뷰를 위한 리뷰어의 자세](https://tech.kakao.com/2022/03/17/2022-newkrew-onboarding-codereview/)

## 🧑🏻‍💻 Team


|                                        **[왁/위진영](https://github.com/weejinyoung)**                                         |                                         **[쭈니/박예준](https://github.com/jun02160)**                                          |
|:------------------------------------------------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------------------------:|
| <img src="https://user-images.githubusercontent.com/80024278/222954620-02a4a226-d51a-4149-ae49-326d547ad146.jpeg" width=400 /> | <img src="https://user-images.githubusercontent.com/80024278/222954622-1c2da6cb-d1fe-4b0c-9eaa-cf5608d11e9c.jpeg" width=400 /> | 
|                                                           PM, 서버 개발자                                                           |                                                             서버 개발자                                                          |
|                                                   기획<br/>프로젝트 세팅<br />DB 설계                                                    |                                                 프로젝트 세팅<br />서버 배포<br />DB 설계                                               | 
