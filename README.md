# Discord
## 기능
- 회원
  - 로그인, 회원가입
    - Spring Security : 세션 로그인
  - 비밀번호 찾기
    - SMTP 프로토콜을 활용한 이메일 전송
- 그룹
  - 그룹 생성
- 채널
  - 채널 생성, 입장
- 실시간 채팅
  - websocket + Stomp
- 실시간 방문자 반영
  - websocketEventListener 활용을 통한 감지
  - Subscribe시 방문자 추가, disconnect시 방문자 제거
## 기술스택
- 프론트
  - html, css, js
  - jquery (ajax)
  - thymeleaf
- 백엔드
  - java 21
  - spring boot 3.5.3
  - JPA
- DB
  - prod : mariadb, dev : h2-database
## ERD
<img width="1333" height="693" alt="Image" src="https://github.com/user-attachments/assets/f9635d5d-dcca-4f41-9943-e811478dd29a" />

## 아키텍쳐

 
## 문제해결

 
## 깃 컨벤션
- feat : 기능
- fix : 수정
- docs : 문서 작성, 수정
- style : html css 수정
- refactor : 리팩토링
- test : 테스트
- chore : 빌드 업무 수정
</ul>

