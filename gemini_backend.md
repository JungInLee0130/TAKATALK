# TakaTalk 백엔드 프로젝트 현황 (GEMINI_BACKEND.md)

이 파일은 백엔드 프로젝트의 진행 상황과 주요 설정을 기록하며, Gemini CLI가 컨텍스트를 유지하는 데 사용됩니다.

## 📂 프로젝트 경로
- **Backend:** `e:\chat\chat` (Spring Boot 3.5.5, Java 21, Gradle)
- **Frontend:** `E:\discord_clone\takatalk` (React 19 연동)

## 🚀 현재 구현 상태 (2026-04-11 기준)

### 1. 인증 및 사용자 (Auth & User) - [완료]
- [x] Spring Security + JWT 기반 인증 시스템 구축
- [x] 로그인 및 회원가입 API 구현 (`login`, `user` 패키지)
- [x] JWT 발급 및 검증 로직 (`global/security`)
- [x] **카카오 OAuth2 소셜 로그인 통합**:
    - `CustomOAuth2UserService`: 소셜 사용자 정보 DB 자동 저장/업데이트 로직 구현
    - `OAuth2SuccessHandler`: 소셜 로그인 성공 시 기존 `JwtProvider`를 통한 JWT 발급 및 프론트엔드 리다이렉트 구현
    - `SiteUser` 엔티티 확장 (`provider`, `providerId` 필드 추가)
    - `SecurityConfig`에 OAuth2 필터 및 설정 통합 완료

### 2. 그룹 및 채널 관리 - [완료]
- [x] 그룹(서버) 생성 및 조회 API (`group`)
- [x] 카테고리 및 채널 관리 API (`category`, `channel`)
- [x] 그룹 멤버 관리 (`groupmember`)

### 3. 실시간 채팅 (Real-time Chat) - [완료]
- [x] STOMP/WebSocket 기반 실시간 메시징 서버 설정 (`global/websocket`)
- [x] 전역 온라인 상태 추적 시스템 구축 (`WebSocketEventListener`)
- [x] 메시지 저장 및 전송 로직 구현 (`chatmessage`)
- [x] 채널별 실시간 방문자(Visitors) 추적 및 브로드캐스트 로직 구현

### 4. 기타 기능 및 테스트
- [x] **단위 테스트 및 검증 완료**:
    - `SiteUserTest`: 엔티티 생성 로직 검증
    - `OAuth2SuccessHandlerTest`: JWT 생성 및 리다이렉트 로직 검증
    - `CustomOAuth2UserServiceTest`: 사용자 저장/업데이트 분기 로직 검증
- [x] 공통 예외 처리 및 Swagger UI 통합
- [x] 데이터 초기화 (`InitData.java`): 테스트를 위한 샘플 데이터 생성

---

## 🛠 주요 아키텍처 패턴
- **Framework:** Spring Boot 3.5.5
- **Persistence:** Spring Data JPA (Hibernate)
- **Security:** Spring Security + JWT (Stateless) + OAuth2 Client
- **Messaging:** STOMP over WebSocket
- **Documentation:** Springdoc OpenAPI (Swagger UI)

---

## 📝 다음에 이어서 할 작업 (TODO)
- [ ] 메시지 읽음 처리 (Read Receipts) 로직 강화
- [ ] 실시간 온라인 상태(Online Status) 실시간 동기화 정교화
- [ ] 파일/이미지 업로드 API 구체화 및 스토리지 연동 (`global/file` 기반)
- [ ] 프론트엔드(`localhost:5173`) 소셜 로그인 리다이렉트 페이지 구현 및 연동 테스트

---

> **Gemini CLI에게:** 백엔드 작업 시작 전 항상 이 파일을 읽고 컨텍스트를 유지하세요.
