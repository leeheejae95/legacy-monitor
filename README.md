# 🖥 Legacy Monitor - 공공기관 시스템 운영 현황 관리

> 반복되던 일일점검 업무를 자동화하고, 그 결과를 한 화면에서 확인하는 운영 현황 대시보드

<br>

## 🔑 한눈에 보기

- **문제** : 배치 결과, 민원 처리 현황, 에러 로그를 매일 사람이 직접 화면을 열어 점검했음
- **해결** : Playwright로 점검을 자동화하고, Airflow로 점검 스케줄을 관리
- **결과물(이 저장소)** : 점검 결과를 한 화면에서 확인하고, 필요하면 엑셀로 내려받는 대시보드

<br>

## 📌 왜 만들었나

공공기관 시스템을 유지보수하면서, 매일 배치 결과·민원 처리 현황·에러 로그를 사람이 직접 화면에 들어가 확인하는 일을 반복했습니다.

이 작업을 자동화하고 싶었습니다. 그래서 **Playwright**로 화면 점검을 자동화하고, **Airflow**로 점검 스케줄을 관리하기로 했습니다. 그리고 그 점검 결과를 한 곳에서 볼 수 있도록 이 대시보드를 만들었습니다.

<br>

## 🔗 전체 그림 (Playwright + Airflow + 대시보드)

```
[Airflow] 매일 정해진 시간에 점검 실행
     │
     ▼
[Playwright] 배치 / 민원 / 에러로그 화면 자동 점검
     │
     ▼
[DB 저장] 점검 결과 저장
     │
     ▼
[Legacy Monitor 대시보드] ← 이 저장소
   담당자는 화면을 하나하나 열어보지 않고,
   대시보드 한 곳에서 전체 점검 결과를 확인 + 엑셀 다운로드
```

> ⚠️ 이 저장소는 이 전체 그림 중 **결과를 보여주는 대시보드** 부분입니다. 지금은 `DataInitializer`가 실제 점검 결과 대신 더미데이터를 넣어줍니다. Playwright 점검 스크립트와 Airflow 연동은 다음 개선 과제로 진행할 예정입니다.

<br>

## 🚀 주요 기능

| 기능 | 설명 |
|------|------|
| 배치 처리 결과 모니터링 | 배치명, 처리건수, 성공/실패 건수, 상태(정상/오류) 조회 |
| 민원 처리 현황 조회 | 접수번호, 민원종류, 접수일, 상태(접수/처리중/완료/반려) 조회 |
| 시스템 에러 로그 조회 | 서비스별·에러타입별 발생시간, 발생 횟수 확인 |
| 통계 카드 요약 | 화면 상단에 정상/오류, 완료/대기/반려 건수를 카드로 요약 |
| 엑셀 다운로드 | 배치/민원/에러로그 목록을 `.xlsx` 파일로 내려받기 |
| 관리자 로그인 | 로그인해야 대시보드에 들어올 수 있음 (Spring Security) |
| 더미데이터 자동 생성 | 실행하면 배치·민원·에러로그가 각 20건씩 자동으로 채워짐 |

<br>

## 🖥 화면 구성

| 화면 | 경로 | 요약 카드 | 엑셀 다운로드 |
|------|------|-----------|----------------|
| 배치 처리 결과 | `/batch` | 전체 / 정상 / 오류 건수 | `/batch/excel` |
| 민원 처리 현황 | `/civil` | 완료 / 대기(접수·처리중) / 반려 건수 | `/civil/excel` |
| 시스템 에러 로그 | `/error-log` | 전체 발생 횟수, 서비스 수, 최대 발생 횟수 | `/error-log/excel` |

<br>

## 🛠 기술 스택

| 분류 | 기술 | 왜 썼는지 |
|------|------|----------|
| Language | Java 21 | |
| Framework | Spring Boot 4.1.1 | |
| ORM | Spring Data JPA | |
| View | Thymeleaf | 서버에서 화면을 그리고, 사이드바 등 공통 부분을 재사용하기 좋음 |
| Security | Spring Security (Form Login) | 관리자만 대시보드에 들어올 수 있게 하려고 |
| Database | H2 (In-Memory) | 별도 설치 없이 바로 실행되는 포트폴리오용 환경 |
| Excel | Apache POI | 운영 현황을 엑셀 리포트로 내려받게 하려고 |
| Build | Gradle | |

<br>

## 🏗 동작 흐름

```
[관리자 브라우저]
    │
    ▼ 로그인
[Spring Security]
   - 로그인 화면, css, h2-console은 로그인 없이 접근 가능
   - 그 외 모든 화면은 로그인 필요
    ▼
[Controller] 배치 / 민원 / 에러로그 각각 담당
    │
    ├─ 목록 조회
    │    DB에서 데이터를 가져와 화면에 뿌려주고,
    │    정상/오류 같은 요약 통계도 함께 계산
    │
    └─ 엑셀 다운로드
         Apache POI로 엑셀 파일을 만들어 바로 다운로드
```

<br>

## 📁 프로젝트 구조

```
src/main/java/com/legacymonitor/
├── config/
│   └── SecurityConfig.java          # 로그인 설정, 관리자 계정
├── controller/
│   ├── LoginController.java         # 로그인 화면, 첫 진입 시 /batch로 이동
│   ├── BatchController.java         # 배치 결과 조회 + 엑셀 다운로드
│   ├── CivilController.java         # 민원 처리 현황 조회 + 엑셀 다운로드
│   └── ErrorLogController.java      # 에러 로그 조회 + 엑셀 다운로드
├── domain/
│   ├── BatchResult.java             # 배치일자, 배치명, 처리/성공/실패 건수, 상태
│   ├── CivilComplaint.java          # 접수번호, 민원종류, 접수일, 상태
│   └── ErrorLog.java                # 서비스명, 에러타입, 발생시간, 발생횟수
├── repository/                      # 각 도메인의 DB 조회 담당
├── init/
│   └── DataInitializer.java         # 실행할 때 더미데이터 자동 생성
├── util/
│   └── ExcelUtil.java               # 엑셀 파일 생성/다운로드 공통 처리
└── LegacyMonitorApplication.java

src/main/resources/
├── templates/
│   ├── layout.html                  # 사이드바 등 공통 화면 조각
│   ├── login.html                   # 로그인 화면
│   └── batch.html / civil.html / error-log.html
├── static/css/style.css
└── application.yaml                 # DB, 화면 관련 설정
```

<br>

## ⚙️ 로컬 실행 방법

### 사전 준비
- Java 21

### 1. 실행
```
./gradlew bootRun
```
또는 IntelliJ에서 `LegacyMonitorApplication` 실행

### 2. 접속
```
http://localhost:8080
```
- 아이디: `admin`
- 비밀번호: `admin1234`

로그인하면 배치 처리 결과 화면으로 이동합니다. 사이드바에서 배치 / 민원 / 에러로그 화면을 바꿔볼 수 있습니다.

### 3. H2 콘솔 (DB 데이터 직접 보고 싶을 때)
```
http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:legacydb
Username: sa / Password: (없음)
```

<br>

## 📈 만들면서 신경 쓴 부분

- **화면 공통 부분 재사용** : 사이드바를 한 번만 만들어두고 모든 화면에서 재사용, 현재 보고 있는 메뉴만 강조 표시
- **통계는 바로바로 계산** : 별도 서비스 레이어 없이 Controller에서 Stream API로 정상/오류 같은 요약 통계를 즉시 계산
- **엑셀 다운로드 로직 하나로 통일** : `ExcelUtil` 하나로 헤더 스타일, 파일명 인코딩, 다운로드 응답까지 배치/민원/에러로그가 공통으로 사용
- **예약어 충돌 방지** : DB의 `count`는 SQL 예약어라서 컬럼명을 `error_count`로 바꿔서 매핑
- **바로 실행되게 구성** : H2 인메모리 DB + 더미데이터 자동 생성으로, 클론받고 바로 실행해서 볼 수 있게 만듦
- **로그인 없이는 못 들어오게** : 로그인 화면, css, h2-console만 예외로 두고 나머지 화면은 모두 로그인 필수

<br>
