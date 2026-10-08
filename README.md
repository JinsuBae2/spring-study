# spring-study

AI와 함께 만든 프로젝트의 코드를 이해하고 내 것으로 만들기 위해서,
Java와 Spring을 기초부터 직접 구현하며 공부하는 저장소 입니다.

## 지금 하는 것

`taskapi/`: 팀 업무 관리 API

- Java 21, Spring Boot, Gradle
- 업무 생성, 목록·단건 조회, 시작·완료 상태 전환
- 아직은 메모리 저장소를 쓴다. 다음 단계에서 JPA와 DB로 바꿀 예정이다.

## 공부하는 방식

1. 요구사항을 먼저 문서로 정한다. → [`taskapi/docs/task-api-requirements.md`](taskapi/docs/task-api-requirements.md)
2. AI는 과제를 내고 질문에 답하는 역할로 쓰고, 코드는 내가 직접 쓴다.
3. 단위 테스트와 curl 요청으로 동작을 확인한다.
4. 막힌 부분과 배운 내용을 기록한다. → [`taskapi/docs/learning/`](taskapi/docs/learning/)

## 실행

```bash
cd taskapi
./gradlew test
./gradlew bootRun
```
