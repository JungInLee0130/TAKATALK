# 💬 TAKATALK (타카톡)
> **Spring Boot 3.5.3 & Java 21** 기반의 실시간 커뮤니티 플랫폼(Discord 클론) 프로젝트입니다.  
> 단순한 채팅을 넘어 **데이터 정합성, 파일 시스템 설계, 실시간 동기화** 등 백엔드의 핵심 역량을 다지는 데 집중했습니다.

<br/>

## 🛠 Tech Stack

### Backend
![Java](https://img.shields.io/badge/Java_21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_3.5.3-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![H2](https://img.shields.io/badge/H2_Database-003545?style=for-the-badge&logo=sqlite&logoColor=white)
![WebSocket](https://img.shields.io/badge/WebSocket_STOMP-010101?style=for-the-badge&logo=socket.io&logoColor=white)

### Frontend
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)
![jQuery](https://img.shields.io/badge/jQuery-0769AD?style=for-the-badge&logo=jquery&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white)

<br/>

## 🚀 Key Features

### 🔐 인증 및 보안 (Auth)
- **Spring Security 기반 인증**: CustomUserDetails를 통한 세션 기반 로그인/회원가입.
- **비밀번호 재설정**: UUID 기반 리셋 토큰 및 SMTP 메일 발송을 통한 보안 로직 구현.

### 🖼 프로필 및 파일 시스템 (Profile & File)
- **이미지 미리보기**: `URL.createObjectURL`을 활용한 비동기 이미지 프리뷰.
- **스마트 파일 관리**: UUID 기반 중복 방지 저장 및 **정적 자원 보호 로직** 적용.
- **DTO 중심 경로 처리**: `FileUtil`을 통해 백엔드에서 이미지 경로를 확정하여 View의 복잡도 제거.

### 💬 실시간 소통 (Real-time)
- **STOMP 기반 채팅**: 채널별 메시지 발행/구독(Pub/Sub) 구조.
- **무한 스크롤**: AJAX를 활용하여 끊김 없는 과거 채팅 내역 조회.
- **입퇴장 알림**: 이벤트 리스너를 통한 실시간 입장/퇴장 시스템 메시지 전송.

### 층 계층형 커뮤니티 (Community)
- **서버 구조**: 서버(Group) → 카테고리 → 채널로 이어지는 디스코드식 계층 설계.
- **실시간 접속자 명단**: 서버별 온라인 유저를 실시간으로 추적 및 동기화.
- **동시성 제어**: `ConcurrentHashMap`을 활용한 스레드 안전성 확보.

<br/>

## 🏗 핵심 기술적 문제 해결 (Technical Highlights)

### 1. 정적 자원(Default Image) 보호 및 분리
- **문제**: 프로필 수정 시 기존 파일을 삭제하는 로직에서 시스템 기본 이미지까지 삭제되는 결함 발견.
- **해결**: DB에 기본 이미지명을 저장하지 않는 **'DB Null 전략'** 채택. 파일 삭제 전 `null` 여부를 검증하고, DTO 생성 시점에 기본 경로를 바인딩하도록 설계하여 정적 자원의 유실을 차단함.

### 2. View와 Data의 관심사 분리
- **문제**: Thymeleaf 템플릿 내에서 이미지 경로를 결정하는 삼항 연산자가 복잡해져 유지보수 저하.
- **해결**: `FileUtil` 도입 및 DTO 내부에서 경로를 확정하는 방식을 통해 HTML 코드를 단순화하고 재사용성을 높임.

### 3. JPA Auditing을 통한 데이터 관리 자동화
- **해결**: `BaseTimeEntity` 상속을 통해 모든 엔티티의 생성/수정 시간을 자동화하여 데이터 추적 가시성 확보.

<br/>

## 📊 Database Schema (ERD)
- **SiteUser**: 회원 정보 및 권한(Role).
- **Groups**: 다중 서버 관리.
- **GroupMembers**: 유저와 그룹 간의 N:M 관계 및 온라인 상태 관리.
- **Channels / Categories**: 서버 내 하위 계층 관리.
- **ChatMessages**: 채널별 메시지 영속화.
  ![ERD](https://github.com/user-attachments/assets/f9635d5d-dcca-4f41-9943-e811478dd29a)
  <img width="1333" height="693" alt="Image" src="" />


<br/>

## 🛣 Roadmap & Upcoming Features
- [ ] **Redis Migration**: 서버 분산 환경 대응을 위한 접속자 정보 저장소 이전.
- [ ] **Invite System**: UUID 기반 초대 링크 발급 및 만료 시간 관리.
- [ ] **Soft Delete**: 데이터 무결성 보존을 위한 삭제 상태값(`is_deleted`) 기반 로직.
- [ ] **React SPA**: 프론트엔드 Next.js/React 마이그레이션 및 RESTful API 전환.

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

