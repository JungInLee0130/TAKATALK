# JWT 인증 흐름 정리 (Authentication Flow)

이 문서는 TakaTalk 프로젝트의 현재 JWT 기반 인증 시스템 작동 방식을 정리합니다.

## 1. 주요 구성 요소

### 🛡️ SecurityConfig (`global.security`)
- **Stateless 설정**: `SessionCreationPolicy.STATELESS`를 통해 서버에서 세션을 유지하지 않음.
- **필터 체인**: `JwtAuthenticationFilter`를 `UsernamePasswordAuthenticationFilter` 이전에 배치하여 모든 요청에서 토큰을 먼저 검사함.
- **권한 설정**: 특정 API(`api/v1/login/**`) 및 정적 리소스를 제외하고는 모두 인증(`authenticated`)이 필요함.

### 🔑 JwtProvider (`global.security.jwt`)
- **라이브러리**: `io.jsonwebtoken (jjwt) 0.12.6` 사용.
- **기능**:
  - `generateToken(email)`: 사용자 이메일을 기반으로 HS256 알고리즘을 사용해 토큰 생성.
  - `extractEmail(token)`: 토큰의 Payload(Claims)에서 사용자 식별값(Subject) 추출.
  - `isValid(token)`: 토큰의 서명 위변조 및 만료 여부 확인.

### 🔍 JwtAuthenticationFilter (`global.security.jwt`)
- **역할**: 클라이언트의 매 요청마다 Header를 확인하여 인증 상태를 구축함.
- **흐름**:
  1. Header에서 `Authorization: Bearer {token}` 패턴을 찾음.
  2. 토큰이 유효하면 `JwtProvider`를 통해 이메일을 추출.
  3. `UserDetailsService`를 통해 DB에서 사용자 정보를 로드.
  4. `UsernamePasswordAuthenticationToken`을 생성하여 `SecurityContextHolder`에 저장.

---

## 2. 인증 시나리오

### [Case 1] 로그인 시 (Login)
1. 사용자가 ID/PW로 `/api/v1/login` 호출.
2. 서버는 자격 증명을 확인한 후 `JwtProvider`를 통해 JWT 발급.
3. 클라이언트는 이 토큰을 저장(localStorage/Cookie)하고 이후 요청 시 헤더에 포함.

### [Case 2] API 요청 시 (Request)
1. 클라이언트: `Authorization: Bearer <JWT>` 헤더와 함께 요청.
2. `JwtAuthenticationFilter`:
   - 헤더에서 토큰 추출.
   - `isValid()`로 검증.
   - 유효하다면 `SecurityContext`에 인증 정보 저장.
3. Controller: `@AuthenticationPrincipal` 등을 통해 현재 로그인된 사용자 정보에 접근.

---

## 3. OAuth2 확장 계획 (Next Steps)
- **의존성**: `spring-boot-starter-oauth2-client` 추가 필요.
- **통합**: 소셜 로그인 성공 후, 기존 `JwtProvider`를 사용하여 JWT를 생성하고 클라이언트에게 전달하는 `OAuth2SuccessHandler` 구현 예정.
