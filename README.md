# 동의대학교 수강신청 시스템

데이터베이스프로그래밍 과목에서 진행한 Oracle 기반 수강신청 웹 애플리케이션 팀 프로젝트입니다. 학생이 강좌를 검색하고 수강신청, 수강대기, 장바구니 기능을 사용할 수 있도록 Spring Boot와 Oracle DB를 연동했습니다.

## 프로젝트 개요

- 주제: 동의대학교 수강신청 시스템
- 목적: 수강신청 과정에서 발생할 수 있는 신청 기간 오류, 정원 초과, 수강대기 처리 등을 데이터베이스 로직과 웹 애플리케이션으로 관리

## 팀 구성 및 역할

| 이름 | 역할 | 주요 담당 |
| --- | --- | --- |
| 윤한울 | 팀장 | DB/WEB 개발환경 구축, 프론트 코드 작성, 백엔드 개발(JPA), 저장 프로시저 및 트리거 구현, 프론트/백엔드/DB 전체 연동, 개발 총괄, 이론적 DB 설계, 역할 분배, E-R 다이어그램 수정, 릴레이션 스키마 작성, 예외처리, 최종 문서 정리 |
| 권민정 | 팀원 | DB/WEB 개발환경 구축, 백엔드 개발(CallableStatement), 저장 프로시저 및 트리거 구현, 실질적 DB 설계, 주제 아이디어 제시, E-R 다이어그램 작성, 릴레이션 스키마 수정, 수강신청 기간 체크 프로시저/트리거 구현, 최종 보고서 작성 및 검토 |
| 박정현 | 팀원 | 프론트 디자인, 트리거 구현, 프론트/백엔드/DB 전체 연동, 실질적 DB 설계 및 검토, 요구사항 명세서 검토, E-R 다이어그램 검토, 릴레이션 스키마 검토, 수강신청 대기자 처리 및 수강인원 증감 관련 트리거 구현, 최종 보고서 작성 |

## 주요 기능

- 전체 강좌 목록 조회
- 교과목번호, 교과목명, 담당 교수, 개설학과, 학년 기준 강좌 검색
- 수강신청 등록 및 취소
- 장바구니 등록, 조회, 삭제
- 수강대기 내역 조회 및 취소
- Oracle 저장 프로시저를 통한 수강신청 기간 검증
- Oracle 트리거와 연계한 정원 초과, 수강인원 증감, 수강대기 처리

## 기술 스택

| 구분 | 사용 기술 |
| --- | --- |
| Backend | Java 17, Spring Boot 3.3.5 |
| View | Thymeleaf, HTML, CSS, JavaScript |
| Data Access | Spring Data JPA, Spring JDBC, JPQL |
| Database | Oracle 18c XE |
| Build | Gradle |
| IDE | IntelliJ IDEA |

## 프로젝트 구조

```text
src/main/java/org/example/deu_courseregistration
├── controller    # 화면 요청, 수강신청/장바구니/검색 요청 처리
├── dto           # 강좌 목록 화면 출력용 DTO
├── entity        # Oracle 테이블과 매핑되는 JPA 엔티티
├── exception     # 수강신청 예외 처리
├── repository    # JPA Repository, JPQL, 저장 프로시저 호출
└── service       # 수강신청, 장바구니, 검색, 대기내역 비즈니스 로직

src/main/resources
├── templates     # Thymeleaf 화면
├── static        # CSS, JavaScript
└── application.properties
```

## 데이터베이스 모델

애플리케이션은 아래 Oracle 테이블을 기준으로 동작합니다.

- `학생`
- `교수`
- `학과`
- `교과목`
- `강좌`
- `수강신청`
- `수강대기`
- `장바구니`

주요 관계는 학생-강좌의 수강신청, 수강대기, 장바구니 관계와 강좌-교과목-교수-학과 관계입니다. 화면에서는 여러 테이블을 조인해 개설학과, 교과목번호, 교과목명, 학점, 수강학년, 강의실, 강의시간, 담당교수, 수강인원, 제한인원 등을 조회합니다.

## DB 로직

프로젝트 보고서 기준으로 다음과 같은 DB 로직을 사용했습니다.

- `수강신청_기간_체크`: 수강신청 가능 기간 여부 확인
- `수강대기자_추가`: 정원 초과 시 수강대기 목록에 학생 추가
- `처리_수강대기`: 수강 취소로 자리가 생기면 대기자를 수강신청으로 이동
- 수강신청 삽입/삭제 트리거: 강좌의 수강인원 증가/감소
- 수강신청 기간 체크 트리거: 기간 외 신청 차단
- 수강대기 처리 트리거: 정원 초과 및 대기자 자동 처리

> 이 저장소에는 Spring Boot 애플리케이션 코드가 중심으로 포함되어 있습니다. Oracle 테이블, 초기 데이터, 저장 프로시저, 트리거 DDL은 실행 환경에 맞게 별도로 준비해야 합니다.

## 실행 방법

### 1. 요구 사항

- Java 17
- Oracle 18c XE 또는 호환 Oracle DB
- Gradle Wrapper 사용 가능 환경

### 2. DB 연결 정보 설정

`src/main/resources/application.properties`에서 로컬 Oracle 접속 정보를 환경에 맞게 수정합니다.

```properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:xe
spring.datasource.username=deu
spring.datasource.password=1234
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
```

JPA 설정은 `ddl-auto=validate`이므로 애플리케이션 실행 전에 Oracle 스키마와 DB 객체가 먼저 생성되어 있어야 합니다.

### 3. 애플리케이션 실행

```bash
./gradlew bootRun
```

실행 후 브라우저에서 아래 주소로 접속합니다.

```text
http://localhost:8080
```

## 주요 화면 경로

| 경로 | 설명 |
| --- | --- |
| `/` | 수강신청 메인 화면 |
| `/CourseCart` | 장바구니 담기 화면 |
| `/CourseRegistrationStatus?studentId=20203019` | 수강신청 내역 |
| `/CourseWaitlistStatus?studentId=20203019` | 수강대기 내역 |
| `/CourseCartStatus?studentId=20203019` | 장바구니 현황 |

## 구현 포인트

- Spring Data JPA를 이용해 수강신청, 수강대기, 장바구니의 복합키 테이블을 매핑했습니다.
- JPQL 생성자 표현식을 사용해 여러 테이블을 조인한 뒤 화면 출력용 DTO로 반환했습니다.
- Oracle 저장 프로시저 호출 결과를 기반으로 수강신청 가능 기간을 검증했습니다.
- Oracle 트리거에서 발생한 예외 메시지를 Spring 서비스 계층에서 파싱해 사용자 메시지로 변환했습니다.
- 정원 초과, 수강인원 증감, 수강대기 자동 처리처럼 데이터 변경 이벤트와 후속 상태 갱신을 DB 로직과 연계했습니다.

## 제한 사항 및 개선 방향

- 현재 저장소에는 Oracle DB 스키마, 초기 데이터, 프로시저, 트리거 생성 SQL이 포함되어 있지 않습니다.
- 화면 일부는 샘플 학번 `20203019`를 기준으로 작성되어 있어 사용자 로그인 기능으로 확장할 수 있습니다.
- 시간표 중복 검증, 학년 제한 검증, 교과목명 검색 고도화 등은 후속 개선 대상으로 정리했습니다.
