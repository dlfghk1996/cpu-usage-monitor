### '서버 CPU 사용률 모니터링 시스템 구현'

### A)  프로젝트 설정
1. 프로젝트 설정
   - Spring Boot
   - Gradle
   - Java 17
   - QueryDSL
2. 데이터베이스 설정
   - H2 (개발 및 테스트)
   - MariaDB (운영)
   - JPA
   - QueryDSL
3. 라이브러리
   - Spring Boot Actuator

----

### B) API 명세서 
http://localhost:8089/terra/swagger-ui/index.html


----

### C) 기능 상세 설명
- [x] 분 단위 조회: 지정한 시간 구간의 분 단위 CPU 사용률을 조회한다.
  - 지정한 시간 구간에 포함된 모든 분에 대한 데이터를 반환한다.
    - ex. 12시 ~ 13시 구간의 조회 요청시 13시 59분까지의 데이터를 포함한다.
    - 12시8분 ~ 13시35분 구간의 조회 요청시 12시 ~ 13시59분 까지의 데이터가 조회된다.


- [x] 시 단위 조회: 지정한 날짜의 시 단위 CPU 최소/최대/평균 사용률을 조회한다. 
  - 지정한 날짜 구간에 포함된 모든 시간에 대한 데이터를 반환한다.
    - ex. 18 ~ 19일 구간의 조회 요청시 19일 자정까지의 데이터를 포함한다.
    - 18일 12시 ~ 19시13시 구간의 조회 요청시 18일 0시부터 ~ 19일 24시까지의 데이터가 조회된다.
    

- [x] 일 단위 조회: 지정한 날짜 구간의 일 단위 CPU 최소/최대/평균 사용률을 조회한다. 
  - 지정한 날짜 구간에 포함된 모든 일자에 대한 데이터를 반환한다.
    - ex. 1일 ~ 3일 구간의 조회 요청시 1일,2일,3일까지의 데이터를 포함한다.

----

### D) API 사용 방법

**1. 분 단위 조회 API : 지정한 시간 구간의 분 단위 CPU 사용률을 조회합니다.**

■ Request
> http://localhost:8089/cpu-usage/list?startTime=2024-05-25T00:00:00&endTime=2024-05-25T23:00:00&dateType=MINUTE

■ Response
```
{
  "data": [
    {
      "id": 1500,
      "measurementValue": 0.12342555,
      "measurementTime": "2024-05-25T00:24:54"
    },
    {
      "id": 1501,
      "measurementValue": 0.22342555,
      "measurementTime": "2024-05-25T13:24:54"
    },
    {
      "id": 1502,
      "measurementValue": 0.32342555,
      "measurementTime": "2024-05-25T23:24:54"
    }
  ],
  "status": 2000,
  "message": "success"
}
```
 <br/> 

**2. 시 단위 조회 API : 지정한 날짜의 시 단위 CPU 최소/최대/평균 사용률을 조회한다.**

■ Request
> http://localhost:8089/cpu-usage/stats?startTime=2024-05-25T23:00:00&endTime=2024-05-26T16:00:00&dateType=HOURS

■ Response
```
{
    "data": [
        {
            "measurementTime": "2024-05-25 23:00",
            "maxUsage": 0.32342555,
            "minUsage": 0.32342555,
            "averageUsage": 0.32342555
        },
        {
            "measurementTime": "2024-05-26 00:00",
            "maxUsage": 0.42342555,
            "minUsage": 0.42342555,
            "averageUsage": 0.42342555
        },
        {
            "measurementTime": "2024-05-26 16:00",
            "maxUsage": 0.52342555,
            "minUsage": 0.52342555,
            "averageUsage": 0.52342555
        }
    ],
    "status": 2000,
    "message": "success"
```
 <br/> 

**3. 일 단위 조회 API :  지정한 날짜 구간의 일 단위 CPU 최소/최대/평균 사용률을 조회한다.**

■ Request
> http://localhost:8089/cpu-usage/stats?startTime=2024-05-25T00:00:00&endTime=2024-05-26T00:00:00&dateType=DAY

■ Response
```
{
    "data": [
        {
            "measurementTime": "2024-05-25",
            "maxUsage": 0.32342555,
            "minUsage": 0.12342555,
            "averageUsage": 0.22342555
        },
        {
            "measurementTime": "2024-05-26",
            "maxUsage": 0.62342555,
            "minUsage": 0.0,
            "averageUsage": 0.284425902415651
        }
    ],
    "status": 2000,
    "message": "success"
}
```

----

### E) 프로젝트 실행 방법
1. QClass 생성: compileJava 실행
2. CPU 사용량 통계 API 요청 중 SQL에러(함수지원문제) 발생 시 prod 환경으로 재실행


----


### F) 요구사항 해결 방법
1. CPU 사용률 수집: 스프링 스케줄러와 Actuator를 사용하여 CPU 사용량 수집
2. 데이터 조회:
   - 요청된 조회 시간을 가공하고 적절한 형식으로 변환
   - BETWEEN 함수를 활용하여 데이터를 특정 범위 내에서 조회
   - 데이터 그룹화 후 최댓값, 최솟값, 평균값을 구함    
3. 데이터 제공 기한:
   - 요청받은 startTime 기준으로 데이터 제공기한 검사 
   - DateType(분,시,일) enum의 메서드를 활용하여 주어진 시간 범위 내의 데이터 요청 검사 
   - DateUtil: 기준 범위 계산, 기준 범위 포함 여부 검사

----

### G) 정책
1. **[Requeset]** startTime 이 endTime 보다 클 경우 startTime 기준 으로 조회한다.
2. **[Data]** 서버의 CPU 사용률을 분 단위로 수집
3. **[API]** 분 단위 API : 최근 1주 데이터 제공
4. **[API]** 시 단위 API : 최근 3달 데이터 제공
5. **[API]** 일 단위 API : 최근 1년 데이터 제공

----
### H) Response Error
- 4XXX 사용자 요청 값 예외
- 5XXX Server error 
- 6XXX Service Error


|  Http <br/>Status  |   Code    | label                | 에러 메세지            | 설명                                 |
|:------------------:|:---------:|----------------------|-------------------|------------------------------------|
|        200         |   4000    | INVALID_REQUEST      | 잘못된 요청입니다.        | 필수 값이 없거나, 요청 값 타입 등이 잘못된 경우 발생한다. |
|        200         |   5000    | ERROR                | SERVER ERROR      | Server Error                       |
|        200         |   5001    | ERROR_SQL            | SQL ERROR         | DB 관련 Error                        |
|        200         |   6000    | INVALID_REQUEST_TIME | 최근 1주 데이터만 제공됩니다. | 최근 1주 이전 데이터 요청 시 발생한다.            |
|        200         |   6000    | INVALID_REQUEST_TIME | 최근 3달 데이터만 제공됩니다. | 최근 3달 이전 데이터 요청 시 발생한다.            |
|        200         |   6000    | INVALID_REQUEST_TIME | 최근 1년 데이터만 제공됩니다. | 최근 1년 이전 데이터 요청 시 발생한다.            |



----

### I) 테스트 시나리오
| No. | 시나리오                                                             | 결과                                                   | 성공 |
|:---:|------------------------------------------------------------------|------------------------------------------------------|:--:|
|  1  | 분 단위 조회 API:<br/>1:00~ 1:00 구간조회                                 | 1:00 ~ 1:59분까지의 데이터 반환                               |   Y |
|  2  | 분 단위 조회 API:<br/>최근 1주 이전 데이터 조회 요청                              | 6000 에러 발생                                           |   Y  |
|  3  | 시 단위 조회 API:<br/>2024-05-25T01:55:00 ~ 2024-05-25T02:55:00 구간 조회 | 2024-05-25 01:00:00 ~ 2024-05-25 03:00:00 까지의 데이터 반환 | Y  |
|  4  | 시 단위 조회 API:<br/>최근 3달 이전 데이터 조회 요청                              | 6000 에러 발생                                           | Y  |
|  5  | 일 단위 조회 API:<br/>2024-05-24T01:55:00 ~ 2024-05-25T02:55:00 구간 조회 | 2024-05-24 00:00 ~ 2022-05-26 00:00 까지의 데이터 반환       | Y  |
|  6  | 일 단위 조회 API:<br/>최근 1년 이전 데이터 조회 요청                              | 6000 에러 발생                                           | Y  |
|  7  | 시작 시간이 종료 시간 이후인 구간 조회 (ex.startTime > endTime)                  | 4000 에러 발생                                           | Y  |
|
