# TakaTalk 협업 가이드 (GIT_WORKFLOW.md)

이 문서는 TakaTalk 프로젝트의 엔지니어링 표준과 협업 프로세스를 정의합니다.

## 1. 브랜치 전략 (Context: MVC to REST Transition)

- **`main`**: 상용 배포용 안정 브랜치.
- **`develop`**: 개발 통합 브랜치. 현재 REST API 전환 작업의 기준점이 됩니다.
- **`feat/`**: 기능 단위 피처 브랜치.
    - `feat/rest-api`: MVC에서 REST API로의 전체적인 구조 전환 작업.
    - `feat/redis-online-status`: Redis 기반 실시간 상태 관리 고도화.

## 2. 작업 흐름 (Workflow)

1. 모든 피처는 `develop`에서 분기하는 것을 원칙으로 하되, 현재와 같은 대규모 전환기에는 `feat/rest-api`를 기준으로 작업할 수 있습니다.
2. 작업 완료 후 반드시 **Atomic Commit** 단위로 커밋을 쪼개어 가독성을 높입니다.
3. 원격 저장소 푸시 전 로컬 테스트(`gradlew test`) 통과 여부를 확인합니다.

## 3. 커밋 메시지 규격

- `feat`: 기능 추가
- `fix`: 버그 수정
- `docs`: 문서 (collaboration, project 등)
- `refactor`: 리팩토링 (기능 변화 없는 코드 구조 변경)
- `chore`: 설정, 의존성 관리
