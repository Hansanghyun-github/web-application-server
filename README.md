# 실습을 위한 개발 환경 세팅
* https://github.com/slipp/web-application-server 프로젝트를 자신의 계정으로 Fork한다. Github 우측 상단의 Fork 버튼을 클릭하면 자신의 계정으로 Fork된다.
* Fork한 프로젝트를 eclipse 또는 터미널에서 clone 한다.
* Fork한 프로젝트를 eclipse로 import한 후에 Maven 빌드 도구를 활용해 eclipse 프로젝트로 변환한다.(mvn eclipse:clean eclipse:eclipse)
* 빌드가 성공하면 반드시 refresh(fn + f5)를 실행해야 한다.

# 웹 서버 시작 및 테스트
* webserver.WebServer 는 사용자의 요청을 받아 RequestHandler에 작업을 위임하는 클래스이다.
* 사용자 요청에 대한 모든 처리는 RequestHandler 클래스의 run() 메서드가 담당한다.
* WebServer를 실행한 후 브라우저에서 http://localhost:8080으로 접속해 "Hello World" 메시지가 출력되는지 확인한다.

# 각 요구사항별 학습 내용 정리
* 구현 단계에서는 각 요구사항을 구현하는데 집중한다. 
* 구현을 완료한 후 구현 과정에서 새롭게 알게된 내용, 궁금한 내용을 기록한다.
* 각 요구사항을 구현하는 것이 중요한 것이 아니라 구현 과정을 통해 학습한 내용을 인식하는 것이 배움에 중요하다. 

### 요구사항 1 - http://localhost:8080/index.html로 접속시 응답

응답 내용을 테스트할 때, 응답 헤더 사이의 순서는 의미가 없는데
응답도 문자열이라, 순서가 다르면 이를 실패했다고 인텔리제이가 판단한다.
헤더만 테스트할 수 있는 메소드가 필요할 듯

문자열에서 딱 한글자(개행이 있냐 없냐) 차이로 테스트가 실패하기 때문에 이를 관리해주는게 귀찮았다.

처음에는 RequestHandler에서 직접 request를 파싱해줬는데  
파싱하는 메소드가 private해서 단위 테스트가 불가능했다.  
예전 테스팅 관련 공부 자료를 보니,  
private 메소드를 테스트하고 싶다는건 객체의 기능 분리 타이밍이라는 걸 알게 됐다.  
따라서 request 객체를 생성해주는 Factory 클래스를 만들고,  
여기서만 request를 생성하도록 만들었다.

그리고 Factory 클래스에서만 request를 생성하게 만들고 싶어  
request 클래스의 생성자는 default로 선언했다.  
(Factory에서는 접근 가능하고, 다른 패키지에서는 접근 불가능하게)

> 신기했던 점은  
> 만약 웹 브라우저(크롬)에 입력한 url이 캐싱되어 있다면  
> 웹 브라우저의 url 창에 `localhost:8080/index.html`의 l을 입력했을 때  
> (첫 글자만 입력했을 때)  
> `localhost:8080/index.html`가 다음 예상 url로 띄워지면서  
> 웹 서버에 요청을 보낸다는 점이다.  
> (개발자 도구를 열고 입력했을 때는 이런 일이 발생하지 않는다)
> 
> 아마 크롬 자체에서 최적화를 위해 미리 요청을 보낸 것 같다.

### 요구사항 2 - get 방식으로 회원가입
* 

### 요구사항 3 - post 방식으로 회원가입
* 

### 요구사항 4 - redirect 방식으로 이동
* 

### 요구사항 5 - cookie
* 

### 요구사항 6 - stylesheet 적용
* 

### heroku 서버에 배포 후
* 