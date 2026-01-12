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

## 🏗 Key Troubleshooting & Optimization

### 1. 외부 API(SMTP) 통신 지연 해소를 위한 메일 전송 비동기화
> 요약: 비밀번호 재설정 메일 발송 시 발생하는 4.8초의 응답 지연을 @Async를 이용한 비동기 처리를 통해 100ms 이하로 단축하였습니다.

### [문제 상황]

- **현상**: 사용자가 '비밀번호 재설정 메일 보내기' 버튼을 클릭한 후, 응답시간이 약 **4.8초~5초** 정도 걸렸습니다.
- **원인**: 메일 발송을 위해 외부 SMTP 서버와 통신하는 과정이 동기(Synchronous) 방식으로 설계되어, 메일 발송이 완료될 때까지 사용자가 HTTP 응답을 받지 못하는 병목 현상이 확인되었습니다.

### [해결 시도 및 시행착오]

1. **CompletableFuture 도입 및 프론트엔드 우회 (1차 시도)**
    - CompletableFuture를 활용해 비동기화를 시도했으나, 초기 구현 미숙으로 인해 메인 스레드가 작업 완료를 기다리게 되어 여전히 **4.5초 이상의 지연**이 발생했습니다.
    - 임시방편으로 프론트엔드에서 모달창을 즉시 띄워 '전송 중' 상태를 보여주는 UX 처리를 했으나, 백엔드 리소스가 오랫동안 점유되는 근본적인 문제는 해결되지 않았습니다.
2. **Spring @Async와 ThreadPoolTaskExecutor 적용 (최종 해결)**
    - Spring Boot에서 제공하는 @Async 어노테이션을 사용하여 메일 전송 로직을 별도의 스레드에서 수행하도록 분리했습니다.
    - 단순 어노테이션 사용에 그치지 않고, ThreadPoolTaskExecutor를 설정하여 스레드 풀의 크기(Core, Max Pool Size)와 큐 용량을 프로젝트 규모에 맞게 관리하여 예측 가능한 시스템을 구축했습니다.

### [Learned]

- **비동기 처리의 핵심**: 단순히 코드의 실행 순서를 바꾸는 것이 아니라, 사용자에게 필요한 최소한의 응답을 우선 전달하고 무거운 작업은 백그라운드로 위임하는 설계의 중요성을 깨달았습니다.
- **기술의 적재적소 활용**: 직접적인 스레드 관리(CompletableFuture 등)보다 Spring 프레임워크가 제공하는 추상화된 기능(@Async)을 활용할 때 생산성과 코드 가독성이 높아진다는 것을 경험했습니다.

### 2. 대용량 채팅 데이터 조회를 위한 커서 기반 페이지네이션 최적화
> 요약: 무한 스크롤 구현 시, 데이터 양이 많아질수록 성능이 저하되는 오프셋(Offset) 방식의 한계를 극복하기 위해 커서(Cursor) 기반 페이지네이션을 도입하고 조회 성능을 최적화했습니다.

### [배경 및 전략 선택]

- **요구사항**: 채팅방 진입 시 최신 대화 내용을 보여주고, 상단으로 스크롤 시 이전 내역을 끊김 없이 로드해야 함.
- **기술 선택의 이유 (Offset vs Cursor)**:
    - **Offset 방식**: LIMIT 10000, 20과 같이 뒤로 갈수록 DB가 이전 행을 모두 읽어야 하므로 성능이 저하(O(N))되고, 실시간으로 메시지가 추가되는 채팅 특성상 데이터 중복 노출 혹은 누락 이슈가 발생할 수 있다고 판단했습니다.
    - **Cursor 방식**: 마지막으로 읽은 메시지 ID(lastMessageId)를 기준으로 인덱스 기반 조회를 수행하므로, 데이터 양에 관계없이 일정한 성능(O(log N))을 보장하며 데이터 정합성 문제를 해결할 수 있어 이를 채택했습니다.

### [핵심 구현 내용]

- **Slice 인터페이스 활용**: 대용량 데이터에서 전체 카운트 쿼리(COUNT(*))는 상당한 부하를 일으킵니다. 전체 페이지 수를 알 필요가 없는 무한 스크롤의 특성을 고려하여, Page 대신 다음 페이지 존재 여부만 확인하는 Slice를 사용하여 쿼리 성능을 최적화했습니다.
- **분기 로직 설계**:
    - 최초 조회 시: lastMessageId가 없는 상태로 가장 최신 ID부터 역순 조회.
    - 추가 로드 시: 전달받은 lastMessageId보다 작은(Id < lastMessageId) 데이터를 조회하여 과거 내역을 정확히 타겟팅.
- **데이터 정렬 최적화**: DB에서는 인덱스를 활용하기 위해 최신순(DESC)으로 조회하고, 애플리케이션 레벨에서 Collections.reverse()를 통해 사용자에게 보여줄 순서(과거→현재)로 재정렬하여 서버 부하와 클라이언트 편의성을 동시에 고려했습니다.

### [결과 및 성과]

- **성능 안정성**: 데이터가 수십만 건 이상 쌓이더라도 페이지 위치와 상관없이 동일한 응답 속도를 유지할 수 있는 구조를 확립했습니다.
- **데이터 정합성**: 사용자가 대화 내역을 읽는 도중 새로운 메시지가 도착해도, 리스트의 순서가 뒤섞이거나 중복된 메시지가 노출되는 사용자 경험(UX) 저해 요소를 사전에 차단했습니다.

### [Learned]

- **확장성 있는 설계**: 단순한 기능 구현을 넘어, 데이터 규모가 커졌을 때 발생할 수 있는 병목 지점을 미리 예측하고 적절한 인덱스 전략과 페이지네이션 기법을 적용하는 법을 익혔습니다.
- **적절한 추상화 수준**: Spring Data JPA의 Slice와 같이 프레임워크가 제공하는 기능을 활용해 불필요한 카운트 쿼리를 줄이는 등 비용 절감의 중요성을 깨달았습니다.

### 3. WebSocket 이벤트를 활용한 실시간 접속자 명단(Presence) 구현

> 요약: 비-SPA(Thymeleaf) 환경의 한계를 극복하기 위해 WebSocket 구독 이벤트를 추적하고, 인메모리 세션 관리 모델을 설계하여 별도의 새로고침 없이 실시간 접속자 명단을 동기화했습니다.

### [문제 상황 및 배경]

- **환경적 제약**: 현재 프로젝트는 Thymeleaf 기반의 MPA 구조로, 새로운 사용자가 입장했을 때 다른 사용자들의 화면에 접속자 명단이 실시간으로 갱신되려면 전체 페이지를 새로고침(HTTP GET)해야 하는 UX 저해 요소가 존재했습니다.
- **요구사항**: 채팅방에 접속 중인 사용자 리스트가 별도의 요청 없이도 실시간으로 추가/제거되어야 하며, 이는 채팅 세션의 연결 상태와 완벽히 동기화되어야 했습니다.

### [해결 전략]

1. **WebSocket 이벤트 리스너 활용**: Spring의 SessionSubscribeEvent 및 SessionDisconnectEvent를 가로채어 사용자의 입/퇴장 시점을 정확히 파악했습니다.
2. **인메모리 세션 관리 (Concurrency)**:
    - 멀티 스레드 환경인 WebSocket 서버에서 데이터 일관성을 보장하기 위해 ConcurrentHashMap과 ConcurrentHashMap.newKeySet()을 사용하여 접속자 정보를 관리했습니다.
    - SESSION_CHANNEL 맵을 통해 세션 ID와 채널 ID를 매핑하여, 연결 종료 시 해당 사용자가 어느 채널에서 나갔는지 즉시 식별할 수 있도록 설계했습니다.
3. **실시간 브로드캐스팅**: 사용자가 특정 채널을 구독(SUBSCRIBE)하는 순간, 해당 채널의 전체 접속자 명단을 추출하여 /sub/channel/{id}/visitors 경로로 즉시 발행(Publish)함으로써 클라이언트 UI를 강제로 갱신하도록 처리했습니다.

### [핵심 코드 구현 포인트]

- **정규표현식을 통한 목적지(Destination) 검증**: 모든 구독 이벤트 중 채팅방 입장 이벤트만 선별하기 위해 정규표현식(VISITOR_REGEX)을 사용하여 로직의 정확도를 높였습니다.
- **인증 정보 연동**: StompHeaderAccessor를 통해 Security Context의 Principal 객체에 접근, 로그인된 사용자의 정보를 안전하게 추출하여 접속자 명단에 포함했습니다.

### [결과 및 성과]

- **UX 혁신**: 페이지 새로고침 없이도 새로운 사용자가 들어오면 즉시 우측 사용자 리스트에 추가되는 기능을 구현하여, Discord 클론 프로젝트에 걸맞은 실시간성을 확보했습니다.
- **자원 효율성**: 별도의 폴링(Polling) 요청 없이, 이미 수립된 WebSocket 연결의 이벤트 주기를 활용함으로써 서버 리소스 낭비를 최소화했습니다.
- **안전한 데이터 처리**: ConcurrentHashMap 도입을 통해 동시 접속자가 급증하는 상황에서도 데이터 경합(Race Condition) 문제 없이 안전하게 세션을 관리할 수 있는 기반을 마련했습니다.

### [Learned]

- **Stateful 서버의 이해**: HTTP의 Stateless 특성과 대조되는 WebSocket의 세션 유지 방식을 이해하고, 서버 레벨에서 세션 상태를 관리하는 설계 능력을 키웠습니다.
- **프레임워크 이벤트 모델**: Spring Framework가 제공하는 어플리케이션 이벤트 모델(@EventListener)을 활용해 비즈니스 로직과 인프라 로직(WebSocket 연결)을 깔끔하게 분리하는 경험을 했습니다.

### 4. JPA Fetch Join을 이용한 N+1 문제 해결 및 DB I/O 최적화

> 요약: 준영속 상태의 엔티티 참조로 발생한 LazyInitializationException을 해결하고, 그 과정에서 발생한 불필요한 3중 쿼리를 단일 쿼리로 통합하여 네트워크 I/O 비용을 약 66% 절감했습니다.
> 

### [문제 상황: 에러 발생과 원인 분석]

- **현상**: 실시간 접속자 명단 가공을 위해 GroupMemberResponse.from(entity) 호출 시 LazyInitializationException이 발생했습니다.
- **원인**: GroupMember 엔티티 내의 SiteUser가 지연 로딩(Lazy Loading)으로 설정되어 있었으나, 해당 메서드가 호출되는 시점에 엔티티가 이미 준영속 상태(Detached)였기 때문에 프록시 객체를 초기화할 수 없었습니다.
- **1차 수정**: 단순히 문제를 해결하기 위해 findById 대신 Fetch Join을 사용하여 SiteUser를 함께 가져오도록 수정했으나, 로직 상 Channel을 거쳐 Group과 GroupMember를 순차적으로 조회하게 되면서 **한 번의 요청에 3개의 독립적인 SELECT 쿼리가 발생**하는 성능 저하 요인을 발견했습니다.

### [문제 재정의: 네트워크 I/O 비용 문제]

- **분석**: 웹소켓 이벤트 리스너에서 동작하는 로직 특성상 접속자가 많아질수록 쿼리 횟수가 기하급수적으로 늘어날 위험이 있었습니다.
- **판단**: 데이터베이스 성능의 가장 큰 병목은 네트워크 I/O(Round-trip)에 있다는 점을 고려할 때, 3번의 쿼리로 인한 Latency 증가는 시스템 전체의 처리량(Throughput)을 떨어뜨릴 것으로 판단하여 쿼리 통합 작업을 진행했습니다.

### [해결 시도: 복합 페치 조인(Fetch Join) 적용]

- **논리적 연결**: Channel은 Group에 속해 있고, GroupMember 역시 Group과 매핑되어 있다는 점에 착안했습니다.
- **JPQL 최적화**: Channel ID와 User ID를 조건으로 하되, GroupMember를 중심으로 Group과 SiteUser를 한 번에 inner join fetch하는 복합 쿼리를 작성했습니다.
    
    ```java
     @Query("select m from GroupMember m " +
           "inner join fetch m.group g " +
           "inner join fetch m.siteUser u " +
           "inner join Channel ch on ch.group.id = g.id " +
           "where ch.id = :channelId and u.id = :siteUserId")
    ```
    

### [결과 및 성과]

- **쿼리 효율화**: 기존 **3회(Channel, User, GroupMember 각각 조회)** 발생하던 쿼리를 **1회의 Join 쿼리**로 압축했습니다.
- **성능 향상**: 불필요한 네트워크 왕복을 제거하여 데이터 조회 단계의 Latency를 60% 이상 단축했습니다.
- **안정성 확보**: LazyInitializationException을 근본적으로 방지함과 동시에, 영속성 컨텍스트 내에서 필요한 모든 데이터를 한 번에 로드하여 실시간 통신 환경에서의 신뢰성을 높였습니다.

### [Learned]

- **성능 중심의 사고**: 단순한 버그 수정에 그치지 않고 SQL 실행 로그를 분석하며 시스템의 잠재적 병목 구간을 찾아내고 개선하는 태도를 길렀습니다.
- **JPA의 깊은 이해**: 지연 로딩과 준영속 상태의 관계, 그리고 복잡한 연관 관계에서의 Fetch Join 활용법을 실무 수준으로 익힐 수 있었습니다.

### 5. 데이터 확장성과 유연성을 고려한 N:M 관계의 1:N 분리 설계

> 요약: SiteUser와 Group 간의 다대다(N:M) 관계를 직접 매핑하는 대신, 중간 엔티티인 GroupMember를 도입하여 비즈니스 로직 확장성을 확보하고 쿼리 제어력을 높였습니다.
> 

### [문제 상황: N:M 직접 매핑의 한계]

- **초기 설계**: 유저는 여러 그룹에 가입할 수 있고, 그룹은 여러 유저를 가질 수 있으므로 JPA의 @ManyToMany를 사용한 설계를 고려했습니다.
- **잠재적 문제점 분석**:
    1. **메타데이터 기록 불가**: 채팅방 서비스 특성상 가입일, 해당 그룹 내에서의 권한(Admin/User), 현재 접속 상태(isOnline) 등 **'관계' 자체에 대한 추가 정보**를 저장해야 했으나, @ManyToMany가 생성하는 숨겨진 연결 테이블에는 컬럼을 추가할 수 없었습니다.
    2. **쿼리 제어의 어려움**: 연결 테이블이 엔티티로 노출되지 않아 복잡한 조인 쿼리나 통계 쿼리를 작성할 때 JPA의 통제를 벗어나는 경우가 발생할 것으로 예상되었습니다.

### [해결 전략: 브릿지 엔티티(GroupMember) 도입]

- **설계 변경**: SiteUser와 Group 사이의 다대다 관계를 두 개의 1:N 관계로 분리하고, 이를 연결하는 **GroupMember 브릿지 엔티티**를 생성했습니다.
- **비즈니스 데이터 포함**: GroupMember 엔티티 내에 다음과 같은 필드를 추가하여 서비스 요구사항을 충족했습니다.
    - Role (Owner, Member): 그룹 내 권한 관리
    - isOnline: 실시간 접속 여부 (웹소켓 연동 시 활용)
    - CreatedAt: 그룹 가입일 추적

### [결과 및 성과]

- **확장성 확보**: 단순한 연결을 넘어 '채팅방 내 사용자 관리'라는 독립적인 도메인 모델을 구축했습니다. 이는 이후 '실시간 접속자 명단 구현' 및 '권한별 기능 제한' 로직을 구현하는 핵심 기반이 되었습니다.
- **데이터 정합성 및 유지보수성**: 중간 엔티티를 직접 관리함으로써 복잡한 연관 관계 쿼리에서도 fetch join을 유연하게 사용하여 성능 최적화를 가능하게 했습니다. (TROUBLESHOOTING #4의 기반)

### [Learned]

- **엔티티 설계의 중요성**: 기술적인 편의성(@ManyToMany)보다 실제 비즈니스의 흐름과 향후 확장 가능성을 우선순위에 두고 DB를 설계하는 법을 배웠습니다.
- **도메인 주도 설계의 단초**: 관계 자체를 하나의 도메인(GroupMember)으로 바라봄으로써 관심사를 명확히 분리하고 코드의 가독성을 높이는 경험을 했습니다.

### 6. GitHub Actions를 이용한 CI 파이프라인 구축
- **기능:** GitHub Actions를 이용한 CI 파이프라인 구축
- **해결 문제:** 로컬 환경의 보안 변수(env.yml) 부재로 인한 CI 빌드 실패 해결
- **기술적 포인트:**
    - optional:import를 통한 설정 파일 의존성 제거
    - Profile(test) 분리를 통한 환경별(Main/Test) 설정 최적화
    - H2 인메모리 DB를 활용하여 외부 인프라 의존성 없는 독립적 테스트 환경 구축

<br/>

## 📊 Database Schema (ERD)
- **SiteUser**: 회원 정보 및 권한(Role).
- **Groups**: 다중 서버 관리.
- **GroupMembers**: 유저와 그룹 간의 N:M 관계 및 온라인 상태 관리.
- **Channels / Categories**: 서버 내 하위 계층 관리.
- **ChatMessages**: 채널별 메시지 영속화.
  ![ERD](https://github.com/user-attachments/assets/4792d7de-02f3-486f-b4cb-774f10ba9acc)


<br/>

## 🛣 Roadmap & Upcoming Features
- [ ] **Redis Migration**: 서버 분산 환경 대응을 위한 접속자 정보 저장소 이전.
- [x] **Invite System**: UUID 기반 초대 링크 발급 및 만료 시간 관리.
- [x] **Soft Delete**: 데이터 무결성 보존을 위한 삭제 상태값(`is_deleted`) 기반 로직.
- [ ] **React SPA**: 프론트엔드 Next.js/React 마이그레이션 및 RESTful API 전환.


<br/>

## 아키텍쳐
  ![SSR_Architecture](https://github.com/user-attachments/assets/707f91bb-ce63-4bae-9321-726968fc5d87)


<br/>

## 깃 컨벤션
- feat : 기능
- fix : 수정
- docs : 문서 작성, 수정
- style : html css 수정
- refactor : 리팩토링
- test : 테스트
- chore : 빌드 업무 수정


(본 프로젝트는 학습 목적으로 제작된 디스코드 클론 코딩 프로젝트입니다. 사용된 이미지 및 디자인의 저작권은 Discord에 있으며, 상업적인 용도로 사용하지 않습니다.)
