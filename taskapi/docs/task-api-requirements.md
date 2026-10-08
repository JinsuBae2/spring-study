# 팀 업무 관리 API 요구사항 명세서

- 작성일: 2026-10-08
- 범위: 학습 대화에서 배정한 TASK-001 ~ TASK-009
- 목적: Java·Spring을 직접 구현하고 테스트·HTTP 요청으로 검증한다.
- 기준: 단계별로 바뀐 요구사항은 아래 최종 API 계약과 최신 과제의 요구사항을 따른다.
- 이 문서는 요구사항이다. 기재된 모든 항목의 구현·검증 완료를 의미하지 않는다.

## 1. 범위와 환경

- Java 21, Spring Boot, Gradle 사용
- HTTP 요청·응답 본문은 JSON 사용
- 메모리 저장소 사용. 현 단계에는 JPA 및 DB 연결 없음
- 서버 재시작 시 저장 데이터와 ID 발급 상태가 초기화될 수 있음
- 단일 요청 흐름을 기준으로 학습. 동시성 보장, 인증, 화면, 검색, 페이징, 삭제는 이번 배정 범위에 포함하지 않음

## 2. 업무 데이터와 규칙

| 항목 | 타입·의미 | 규칙 |
|---|---|---|
| id | 정수, 저장된 업무의 식별자 | 서버가 저장 시 부여하는 양의 정수. 같은 서버 실행 중 서로 다른 업무의 ID는 중복되지 않음 |
| title | 문자열, 제목 | null, 빈 문자열, 공백만 있는 문자열 금지 |
| description | 문자열, 설명 | 비어 있어도 됨. 별도 필수 검증은 배정하지 않음 |
| status | 업무 상태 | TODO, IN_PROGRESS, DONE |

- 신규 업무의 초기 상태는 TODO다.
- 상태 전환은 TODO → IN_PROGRESS → DONE만 허용한다.
- 단계 건너뛰기, 되돌리기, 동일 상태로 변경은 금지한다.
- 허용하지 않는 전환 요청은 업무 상태를 변경하지 않는다.
- 사용자는 생성 요청에서 제목·설명을 제공한다. ID와 초기 상태는 서버가 정한다.
- 필드를 외부에서 직접 수정하지 못하도록 한다.
- 업무 ID와 목록 인덱스는 독립적이다. 단건 조회는 업무 ID를 비교해 찾는다.

## 3. 최종 API 계약 — TASK-009까지의 목표

| 메서드 | 경로 | 동작 | 성공 | 실패 |
|---|---|---|---|---|
| GET | /tasks/sample | 학습용 고정 업무 반환 | 200 | 별도 실패 요구사항 없음 |
| POST | /tasks | 업무 생성·저장 | 200 + 업무 | 제목 검증 실패 400 |
| GET | /tasks | 저장 업무 전체 조회 | 200 + 배열 | 별도 실패 요구사항 없음 |
| GET | /tasks/{id} | ID로 업무 조회 | 200 + 업무 | 없는 ID 404 |
| PATCH | /tasks/{id}/start | 업무 시작 | 200 + 변경된 업무 | 없는 ID 404, 잘못된 상태 409 |
| PATCH | /tasks/{id}/complete | 업무 완료 | 200 + 변경된 업무 | 없는 ID 404, 잘못된 상태 409 |

### 생성 요청

```json
{"title":"POST API 공부","description":"요청 본문 받아보기"}
```

### 저장된 업무 응답 예시

```json
{"id":1,"title":"POST API 공부","description":"요청 본문 받아보기","status":"TODO"}
```

- JSON 속성 순서는 검증 대상이 아니다.
- 목록은 생성 순서로 반환한다. 데이터가 없으면 []다.
- 단건 조회와 상태 변경의 없는 ID 응답은 404다. 이 단계에서는 오류 본문을 별도로 정의하지 않으며 빈 본문을 허용한다.
- 고정 샘플 업무의 제목은 ‘스프링 학습’, 설명은 ‘첫 조회 API 구현’, 상태는 TODO다. 저장하지 않으며 저장된 업무용 양의 ID 요구사항의 대상이 아니다.

### 제목 검증 오류

상태 코드: 400

```json
{"code":"INVALID_REQUEST","message":"제목은 비어 있을 수 없습니다."}
```

### 상태 전환 오류 — TASK-009 목표

상태 코드: 409

```json
{"code":"INVALID_STATE","message":"Task에서 발생한 예외의 메시지"}
```

- message는 위 예시 문구 자체가 아니라 실제 IllegalStateException의 메시지를 담는다.
- 오류 응답은 ErrorResponse의 code, message 필드로 표현한다.
- 제목 이외의 검증, 잘못된 JSON 및 잘못된 경로 변수 형식의 공통 오류 계약은 아직 배정하지 않았다.

## 4. 단계별 업무와 완료 기준

### TASK-001 — 업무 생성과 제목 검증

- Task와 TaskStatus를 구현한다.
- 생성 시 TODO이며, 잘못된 제목은 IllegalArgumentException으로 거부한다.
- 정상 생성, null·빈 문자열·공백 제목을 단위 테스트로 검증한다.
- 상태 변경 요구사항은 아래 TASK-002로 나누어 진행했다.
- 작업 파일: Task.java, TaskStatus.java, TaskTest.java

### TASK-002 — 시작과 완료

- start()는 TODO에서 IN_PROGRESS로만 변경한다.
- complete()는 IN_PROGRESS에서 DONE으로만 변경한다.
- 잘못된 상태에서 호출하면 IllegalStateException을 던지고 상태를 유지한다.
- 정상 시작·중복 시작 거부를 테스트한다.
- 완료 관련 테스트: 정상 완료, 시작 전 완료 거부, 중복 완료 거부를 제시했다. 학습자 요청으로 유사 테스트 작성 연습은 생략했으며, 기능 요구사항은 유지한다.
- 작업 파일: Task.java, TaskTest.java

### TASK-003 — 첫 조회 API

- GET /tasks/sample에서 고정 업무를 200과 JSON으로 반환한다.
- 서버 실행 후 HTTP 요청으로 제목·설명·상태를 확인한다.
- 작업 파일: TaskController.java

### TASK-004 — 생성 요청 처리

- POST /tasks에서 요청 제목·설명으로 업무를 생성해 반환한다.
- 요청 DTO와 도메인 객체를 분리한다.
- 이 단계에서는 저장하지 않으며, 저장 요구사항은 TASK-005에서 추가했다.
- 작업 파일: TaskCreateRequest.java, TaskController.java

### TASK-005 — 메모리 저장과 목록 조회

- 생성한 업무를 메모리에 저장한다.
- GET /tasks는 전체 목록을 생성 순서로 반환한다.
- HTTP 처리와 저장·조회 역할을 분리한다.
- 빈 목록, 두 업무 생성 후 순서·입력값·TODO 유지, 재시작 후 빈 목록을 확인한다.
- 작업 파일: TaskRepository.java, TaskController.java

### TASK-006 — ID와 단건 조회

- 저장 시 서버가 고유한 양의 정수 ID를 부여한다.
- 생성·목록·단건 조회 응답에 ID를 포함한다.
- ID로 찾은 업무는 200, 없는 ID는 404로 반환한다.
- Repository의 없는 ID 조회는 null 반환으로 계약을 선택했다. Controller가 이를 HTTP 응답으로 변환한다.
- Repository 단위 테스트: 서로 다른 양의 ID 부여, 저장한 두 ID 조회, 없는 ID 조회
- HTTP 검증: 존재하는 ID의 업무 일치, 없는 ID 및 빈 저장소에서 404
- 작업 파일: Task.java, TaskRepository.java, TaskController.java, TaskRepositoryTest.java

### TASK-007 — 상태 변경 API

- 시작·완료 PATCH API를 제공한다.
- 기존 Task.start(), Task.complete()를 사용한다.
- 정상 전환 200, 없는 ID 404, 잘못된 전환 409
- 생성 → 시작 → 완료의 상태 흐름을 확인한다.
- 시작 전 완료, 중복 시작, 중복 완료 및 실패 후 상태 유지를 확인한다.
- 작업 파일: TaskController.java

### TASK-008 — 생성 요청 검증과 공통 오류 응답

- Validation을 사용하고 Controller에 제목 검사 조건을 직접 작성하지 않는다.
- 잘못된 제목은 Controller 본문 실행 전에 거부한다.
- Task 생성자의 제목 검증은 유지한다.
- 400과 INVALID_REQUEST 오류 본문을 반환한다.
- 정상 제목의 생성·저장, 잘못된 제목 세 종류의 400·본문 일치·저장 안 됨을 확인한다.
- 작업 파일: build.gradle, TaskCreateRequest.java, TaskController.java, ErrorResponse.java, GlobalExceptionHandler.java

### TASK-009 — 상태 규칙과 예외 응답 분리 — 현재 진행 과제

- Controller에서 상태 비교 조건을 제거한다.
- 전환 가능 여부는 Task.start(), Task.complete()가 판단한다.
- IllegalStateException을 공통 예외 처리에서 409로 변환한다.
- INVALID_STATE와 실제 예외 메시지를 응답한다.
- 정상 전환 200, 없는 ID 404 및 실패 후 기존 상태 유지 요구사항을 유지한다.
- 작업 파일: TaskController.java, GlobalExceptionHandler.java

## 5. 검증 현황 — 2026-10-08 기록 시점

| 항목 | 확인된 증거와 남은 범위 |
|---|---|
| 기본 프로젝트 | 기본 테스트 BUILD SUCCESSFUL 확인 |
| Task 테스트 | 학습자 통과 보고 있음. null·공백 제목 테스트 전체 완료는 미확인. 완료 전환 단위 테스트 작성은 생략 |
| 생성·목록 | 공유된 HTTP 결과로 입력값·두 업무 순서 확인. 재시작 후 빈 목록은 명시적 실행 증거 미확인 |
| Repository 테스트 | 학습자 통과 보고 있음 |
| 단건 조회 | 조력자가 생성한 업무 ID의 200·본문 일치 및 없는 ID의 404 직접 확인 |
| 상태 API | 조력자가 정상 전환, 잘못된 전환, 없는 ID, 실패 후 상태 유지 직접 확인 |
| 제목 검증 | 세 종류 모두 400 및 INVALID_REQUEST 확인. 당시 메시지 띄어쓰기 불일치 발견 |
| 제목 오류 메시지 수정 | 최신 소스에서 요구 문구와 일치 확인. 수정 후 실행 응답은 미확인 |
| 저장 영향 | 공백·null 요청은 전후 목록 동일 확인. 빈 문자열은 본문 비교 실패로 후속 저장 여부 검증이 중단됨 |
| 정상 제목 | 생성 200·TODO·목록 저장 직접 확인 |
| TASK-009 | 배정됨. 구현·실행 검증 미확인 |

## 6. 학습 진행 원칙

- 기본 과제 안내는 요구사항, 완료 기준, 생성·수정 파일 중심으로 한다.
- 학습자가 도움을 요청할 때 힌트와 코드 예시를 제공한다.
- 학습자가 직접 구현하며, 리뷰와 테스트로 다음 단계 진입 여부를 판단한다.
- 실습 기록은 docs/learning/에 남긴다. ‘오늘 공부 끝’ 요청 시 기록을 작성하거나 갱신한다.
- 완료 보고 시 직접 실행한 결과, 학습자 공유 결과, 미검증 범위를 구분한다.
