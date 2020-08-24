# seomse-system

# 개발환경
- open jdk 1.8

# 개요
서버 프로세스는 물리 서버당 1개(단 네트워크 카드가 여러개면 여러개 가능) 엔진은 용도에 따라 여러개를 띄우는 구조적 환경에서의 사용을 위해 개발되었습니다.

이러한 방식에서의 in memory 병렬처리 엔진 구현이 가능 하며 엔진간의 통신, 방대한 데이터 전송을 지원하며 여러 노드를 활용하여 처리하는 구현을 할떄 적합합니다.

DB는 oracle 용으로 개발 되어 있고 maria는 지원 에정입니다.

maria 에서도 잘 동작 할거라고 생각 되지만 테스트가 충분 하지 못했습니다.

seomse-jdbc 활용

# 구성
### common 
- 공통 코드 관리
- 공통 설정 관리

### engine
- 엔진별 설정관리 (엔진설정이 최우선 순위)
- 엔진이 구동될때 시작되는 서비스 지원
- seomse-sync를 활용한 in memory 메타 관리기능 지원

### server
- 서버별 설정관리 (서버설정이 최우선 순위)
- 서버 프로세스가 구동될떄 시작되는 서비스 지원
- seomse-sync를 활용한 in memory 메타 관리기능 지원

# gradle
implementation 'com.seomse.system:seomse-system:0.9.0'

# etc
https://mvnrepository.com/artifact/com.seomse.system/seomse-system/0.9.0

# communication
### blog, homepage
- seomse.tistory.com
- www.seomse.com
- seomse.com

### 카카오톡 오픈톡
 - https://open.kakao.com/o/g6vzOKqb

### 슬랙 slack
- https://seomse.slack.com/

### email (협업, 외주)
 - comseomse@gmail.com