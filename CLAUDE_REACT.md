# CLAUDE.md — Chat Frontend (React + TypeScript)

## 프로젝트 개요

Discord 클론 채팅 애플리케이션의 프론트엔드.
기존 Spring Boot 백엔드(`E:\chat\chat`)를 그대로 사용하고, Vanilla JS + Thymeleaf 프론트를 **React + TypeScript**로 마이그레이션.

## 기술 스택

- **Framework**: React 18+
- **Language**: TypeScript
- **Build Tool**: Vite
- **HTTP Client**: Axios
- **WebSocket**: STOMP.js + SockJS
- **State Management**: (선택 필요 — Zustand 또는 Jotai 권장)
- **Styling**: (선택 필요 — Tailwind CSS 또는 CSS Modules)

---

## 백엔드 연결 정보

- **Base URL**: `http://localhost:9000`
- **CORS 허용 Origin**: `http://localhost:5173`
- **인증 방식**: 세션 기반 (Cookie `JSESSIONID`)
- **CSRF**: 백엔드에서 활성화되어 있음 — 폼 전송 시 주의

### Axios 기본 설정

```typescript
// src/lib/axios.ts
import axios from 'axios';

const apiClient = axios.create({
  baseURL: 'http://localhost:9000',
  withCredentials: true, // 세션 쿠키 전송 필수
});
```

---

## REST API 명세

### 인증 (`/api/v1/login`)

| Method | Path | Body | 인증 필요 |
|--------|------|------|-----------|
| POST | `/api/v1/login` | `UserLoginForm` | No |
| POST | `/api/v1/login/logout` | - | No |
| POST | `/api/v1/login/send-change-password` | `MailRequest` | No |
| POST | `/api/v1/login/change-password` | `PasswordChangeRequest` | No |
| POST | `/api/v1/login/verify-resetToken` | `VerificationResetTokenRequest` | No |

### 그룹 (`/api/v1/group`)

| Method | Path | Body/Params | 응답 |
|--------|------|-------------|------|
| GET | `/api/v1/group/list` | - | `GroupResponse[]` |
| GET | `/api/v1/group/info/{groupId}` | - | `GroupResponse` |
| POST | `/api/v1/group/create` | `FormData` (name, profile) | `number` (groupId) |
| PATCH | `/api/v1/group/{groupId}` | `FormData` (name, profile) | `GroupEditResponse` |
| DELETE | `/api/v1/group/{groupId}` | - | - |
| GET | `/api/v1/group/{groupId}/invite-code` | - | `string` |
| GET | `/api/v1/group/{groupId}/invite-code/reset` | - | `string` |
| GET | `/api/v1/group/{groupId}` | - | `GroupChannelResponse` |

### 그룹 멤버 (`/api/v1/group-member`)

| Method | Path | Body | 응답 |
|--------|------|------|------|
| POST | `/api/v1/group-member/join` | `GroupJoinRequest` | - |

### 채널 (`/api/v1/group/{groupId}/channel`)

| Method | Path | Body | 응답 |
|--------|------|------|------|
| POST | `/api/v1/group/{groupId}/channel/create` | `CreateChannelRequest` | `ChannelCreateResponse` |
| PATCH | `/api/v1/group/{groupId}/channel/{channelId}` | `ChannelEditRequest` | `ChannelEditResponse` |
| DELETE | `/api/v1/group/{groupId}/channel/{channelId}` | - | - |

### 카테고리 (`/api/v1/group/{groupId}/category`)

| Method | Path | Body | 응답 |
|--------|------|------|------|
| POST | `/api/v1/group/{groupId}/category/create` | `CategoryCreateRequest` | `CategoryCreateResponse` |
| PATCH | `/api/v1/group/{groupId}/category/{categoryId}` | `CategoryUpdateRequest` | `CategoryEditResponse` |
| DELETE | `/api/v1/group/{groupId}/category/{categoryId}` | - | - |

### 채팅 메시지 (`/api/v1/channel/{channelId}/chatmessage`)

| Method | Path | Params | 응답 |
|--------|------|--------|------|
| GET | `/api/v1/channel/{channelId}/chatmessage/history` | `?lastMessageId=` | `ChatMessageResponse[]` |

### 유저 (`/api/v1/users`)

| Method | Path | Body | 응답 |
|--------|------|------|------|
| GET | `/api/v1/users/profile` | - | `UserProfileResponse` |

---

## TypeScript 타입 정의

```typescript
// src/types/index.ts

// --- Auth ---
export interface UserLoginForm {
  email: string;
  password: string;
}

export interface MailRequest {
  mail: string;
}

export interface PasswordChangeRequest {
  token: string;
  password: string;
}

export interface VerificationResetTokenRequest {
  resetToken: string;
}

// --- Group ---
export interface GroupResponse {
  id: number;
  name: string;
  profile: string;
}

export interface GroupEditResponse {
  groupId: number;
  name: string;
  profileUrl: string;
}

export interface GroupChannelResponse {
  categorizedChannels: CategoryResponse[];
  uncategorizedChannels: ChannelResponse[];
}

export interface GroupJoinRequest {
  inviteCode: string;
}

// --- Channel ---
export type ChannelType = 'TEXT' | 'VOICE';

export interface ChannelResponse {
  id: number;
  name: string;
  type: ChannelType;
  isSecret: boolean;
}

export interface CreateChannelRequest {
  channelName: string;
  channelType: ChannelType;
  isSecret: boolean;
}

export interface ChannelCreateResponse {
  channelId: number;
  groupId: number;
}

export interface ChannelEditRequest {
  name: string;
  type: ChannelType;
  isSecret: boolean;
}

export interface ChannelEditResponse {
  groupId: number;
  categoryId: number;
  channelId: number;
  name: string;
  type: ChannelType;
  isSecret: boolean;
}

// --- Category ---
export interface CategoryResponse {
  id: number;
  name: string;
  channels: ChannelResponse[];
}

export interface CategoryCreateRequest {
  name: string;
  isSecret: boolean;
}

export interface CategoryCreateResponse {
  groupId: number;
  categoryName: string;
  isSecret: boolean;
}

export interface CategoryUpdateRequest {
  name: string;
  isSecret: boolean;
}

export interface CategoryEditResponse {
  groupId: number;
  categoryId: number;
  name: string;
  isSecret: boolean;
}

// --- Chat Message ---
export type ChatMessageType = 'CHAT' | 'ENTER' | 'LEAVE';

export interface ChatMessageRequest {
  channelId: number;
  content: string;
}

export interface ChatMessageResponse {
  channelId: number;
  chatMessageId: number;
  profile: string;
  nickname: string;
  createdAt: string; // "HH:mm" 형식
  content: string;
  isModified: boolean;
  type: ChatMessageType;
}

// --- User ---
export interface UserProfileResponse {
  nickname: string;
  username: string;
  profile: string;
}

export interface GroupMemberResponse {
  siteUserId: number;
  nickname: string;
  profile: string;
}
```

---

## WebSocket (STOMP) 연결

### 설정

```
Endpoint:       ws://localhost:9000/ws-stomp (SockJS 폴백 지원)
Subscribe:      /sub/channel/{channelId}
Publish:        /pub/chatmessage/save
```

### 연결 코드 예시

```typescript
// src/lib/socket.ts
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import type { ChatMessageRequest, ChatMessageResponse } from '@/types';

export function createStompClient(
  channelId: number,
  onMessage: (msg: ChatMessageResponse) => void
): Client {
  const client = new Client({
    webSocketFactory: () => new SockJS('http://localhost:9000/ws-stomp'),
    onConnect: () => {
      client.subscribe(`/sub/channel/${channelId}`, (frame) => {
        const msg: ChatMessageResponse = JSON.parse(frame.body);
        onMessage(msg);
      });
    },
  });

  return client;
}

export function sendMessage(client: Client, payload: ChatMessageRequest) {
  client.publish({
    destination: '/pub/chatmessage/save',
    body: JSON.stringify(payload),
  });
}
```

### 패키지 설치

```bash
npm install @stomp/stompjs sockjs-client
npm install -D @types/sockjs-client
```

---

## 라우팅 구조 (권장)

```
/login              로그인 페이지
/signup             회원가입 페이지
/                   메인 (그룹 없을 때 빈 상태)
/group/:groupId     그룹 홈 (채널 목록)
/group/:groupId/channel/:channelId   채팅 화면
/profile            내 프로필
```

---

## 디렉토리 구조 (권장)

```
src/
├── api/                  # Axios API 호출 함수 (도메인별)
│   ├── auth.ts
│   ├── group.ts
│   ├── channel.ts
│   ├── category.ts
│   ├── chat.ts
│   └── user.ts
├── components/           # 재사용 가능한 UI 컴포넌트
│   ├── common/
│   ├── layout/
│   │   ├── Sidebar.tsx
│   │   ├── ChannelList.tsx
│   │   └── MemberList.tsx
│   ├── chat/
│   │   ├── ChatArea.tsx
│   │   └── ChatMessage.tsx
│   └── modals/
├── hooks/                # 커스텀 훅
│   ├── useAuth.ts
│   ├── useGroups.ts
│   └── useChat.ts
├── lib/
│   ├── axios.ts          # Axios 인스턴스
│   └── socket.ts         # STOMP 클라이언트
├── pages/                # 라우트별 페이지 컴포넌트
│   ├── LoginPage.tsx
│   ├── MainPage.tsx
│   └── ChannelPage.tsx
├── store/                # 전역 상태 (Zustand 등)
├── types/
│   └── index.ts          # 모든 TypeScript 타입
└── main.tsx
```

---

## 파일 업로드 주의사항

그룹 생성/수정, 프로필 이미지 업로드는 `multipart/form-data`를 사용합니다.

```typescript
const formData = new FormData();
formData.append('name', groupName);
if (profileFile) formData.append('profile', profileFile);

await apiClient.post('/api/v1/group/create', formData, {
  headers: { 'Content-Type': 'multipart/form-data' },
});
```

---

## 인증 처리

- 세션 기반이므로 로그인 후 `JSESSIONID` 쿠키가 자동으로 관리됨
- 모든 인증 필요 요청에 `withCredentials: true` 필수
- 401 응답 → 로그인 페이지로 리다이렉트 처리 필요
- AJAX 요청 헤더: `X-Requested-With: XMLHttpRequest` 추가 시 401 JSON 응답 반환

```typescript
// Axios 인터셉터로 401 처리
apiClient.interceptors.response.use(
  (res) => res,
  (error) => {
    if (error.response?.status === 401) {
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);
```

---

## 채팅 히스토리 페이지네이션

무한 스크롤 방식 (cursor-based):

```
GET /api/v1/channel/{channelId}/chatmessage/history?lastMessageId={id}
```

- 처음 로드: `lastMessageId` 생략 → 최신 메시지 반환
- 이전 메시지 로드: 현재 가장 오래된 메시지의 `chatMessageId` 전달

---

## 공개 접근 가능 경로 (인증 불필요)

```
/api/v1/login/**
/api/v1/users/test
```

나머지 모든 `/api/**` 경로는 인증 필요.

---

## 관련 문서

- 백엔드 소스: `E:\chat\chat`
- Swagger UI: `http://localhost:9000/swagger-ui/index.html`
- 백엔드 실행 프로파일: `application-dev.yml` (포트 9000)
