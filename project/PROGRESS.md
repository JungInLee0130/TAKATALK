# TakaTalk 백엔드 개발 진행 상황 (PROGRESS.md)

이 문서는 프로젝트의 마일스톤과 현재 상태를 관리합니다.

## 🚀 주요 마일스톤

### [Phase 1] 기본 기반 및 REST 전환 (진행 중)
- [x] JWT 기반 인증 시스템
- [x] 카카오 OAuth2 소셜 로그인 통합
- [ ] MVC 구조에서 REST API로 전체 전환 (`feat/rest-api`)

### [Phase 2] 대규모 트래픽 대비 고도화 (진행 중)
- [ ] Redis 기반 실시간 상태 관리 (`feat/redis-online-status`)
    - [x] Redis Pub/Sub 인프라 구축
    - [x] Redis 메시지 발행/구독 로직 구현
    - [ ] WebSocketEventListener 연동 (메모리 -> Redis 이전)
- [ ] 데이터베이스 인덱스 최적화 및 QueryDSL 도입 예정

---

## 🛠 현재 작업 컨텍스트 (2026-04-11)
- **현재 브랜치**: `feat/redis-online-status`
- **최근 작업**: Redis 인프라 구축 및 Pub/Sub 통신 클래스 구현 완료.
- **다음 할 일**: `RedisService`를 통한 `WebSocketEventListener`의 상태 관리 로직 이전.

---

## 📂 프로젝트 환경
- **Framework**: Spring Boot 3.5.5, Java 21
- **Storage**: MariaDB, Redis (Pub/Sub & Cache)
- **Auth**: JWT, OAuth2 (Kakao)
