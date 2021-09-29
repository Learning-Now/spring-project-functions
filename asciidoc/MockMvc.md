
# Mock Mvc (Spring  공식  문서 해석)

> Mock Mvc로 도 알려진 스프링 MVC 테스트 프레임 워크는 Spring MVC 애플리케이션의 테스트를 도와준다.

Mock Mvc를 사용하면 애플리케이션을 실행시키지 않고도 테스트가 가능하다. 

의존성을 주입을 하면 메소드를 통해 Spring Mvc에 대한 일반 단위 테스트를 작성할수 있다. 그러나 이 테스트는 따로 매핑, 데이터 바인딩, 메세지 변환, 타입 변환, 검증을 해주지는 않는다.  @InitBinder, @ModelAttribute, or @ExceptionHandler method를 포함하고 있지도 않다.

## Mock Mvc 의 목표

Mock Mvc의 목표는 서버를 실행시키지 않고도 스프링 MVC의 컨트롤러에 보다 완벽한 테스트를 제공 하는 것이다. 이는 DispatcherServlet(프론트 컨트롤러)를 호출 그리고 mock을 통해 전반적인 SpringMvc 요청을 스프링 테스트 모듈로 부터 서버 구동 없이 처리 한다.

MockMvc는 경량 및 대상 테스트를 사용하여 Spring MVC 응용 프로그램의 대부분의 기능을 확인할 수 있는 서버 측 테스트 프레임워크입니다. 자체적으로 요청을 수행하고 응답을 확인하는 데 사용하거나 MockMvc가 서버로 연결된 WebTestClient API를 통해 요청을 처리할 수도 있습니다.

## Static Imports

- `MockMvcBuilders.*`
- `MockMvcRequestBuilders.*`
- `MockMvcResultMatchers.*`
- `MockMvcResultHandlers.*`

## Setup Choices

1. 테스트할 컨트롤러를 직접 지정

```jsx
class MyWebTests {

    MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new AccountController()).build();
    }

    // ...

}
```

1. 스프링 MVC 및 컨트롤러 인프라가 있는 곳을 가르키는 법

```jsx
@SpringJUnitWebConfig(locations = "my-servlet-context.xml")
class MyWebTests {

    MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    // ...

}
```

WebAppContextSetup은 실제 스프링 Mvc 구성을 로드해 보다 완벽한 통합테스트를 구현한다. 또한 Mock 서비스를 스프링 전반적인 컨트롤러에 배치하면 좀더 웹 계층에 집중이 가능하다.

다음 예제는 Mockito를 사용하여 Mock 서비스를 선언합니다.

```jsx
<bean id="accountService" class="org.mockito.Mockito" factory-method="mock">
    <constructor-arg value="org.example.AccountService"/>
</bean>
```

Mock 서비스를 테스트에 주입해 다음과 같은 기대치를 얻을 수 있다.

```jsx
@SpringJUnitWebConfig(locations = "test-servlet-context.xml")
class AccountTests {

    @Autowired
    AccountService accountService;

    MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    // ...

}
```

standaloneSetup 은 단위 테스트에 가깝다. 이것은 한번에 하나의 컨트롤러를 테스트 하며 수동으로 컨트롤러와 함께 Mock의 의존성을 주입이 가능하다. 그리고 이는 스프링을 실행시키지 않는다. 이는 어떤 컨트롤러가 테스트 중인지, 작동하기 위해 특정 스프링 MVC 구성이 필요한지 등을 보다 쉽게 확인할 수 있습니다. 

또한 standaloneSetup 은 특정 동작 확인, 문제 디버깅을 위한 임시 테스트 작성시 편리하다.

대부분의 통합테스트 vs 단위테스트의 논쟁은 정답이 없다. 하지만 StandAloneSetup방식을 사용하면 스프링 MVC 구성을 확인 하기 위해 추가적으로 WebAppContextSetUp 테스트가 필요하다. 그러나  WebAppContextSetUp방식을 사용하면 모든 테스트를 작성해 실제 스프링 MVC 구성에대해 테스트가 가능하다.

## Setup Features

어떤 MockMvc 빌더를 사용하든 상관없이 모든 MockMvcBuilder 구현은 몇 가지 공통적이고 매우 유용한 기능을 제공한다.  

예를 들어 다음과 같이 모든 요청에 대해 승인 머리글을 선언하고 모든 응답에서 200의 상태 및 내용 유형 머리글을 예상할 수 있습니다.

```jsx
// static import of MockMvcBuilders.standaloneSetup

MockMvc mockMvc = standaloneSetup(new MusicController())
    .defaultRequest(get("/").accept(MediaType.APPLICATION_JSON))
    .alwaysExpect(status().isOk())
    .alwaysExpect(content().contentType("application/json;charset=UTF-8"))
    .build();
```

Spring Framework에는 여러 요청 간에 HTTP 세션을 저장하고 재사용하는 데 도움이 되는 기본 제공 구현이 있습니다. 다음과 같이 사용할 수 있습니다.

```jsx
// static import of SharedHttpSessionConfigurer.sharedHttpSession

MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
        .apply(sharedHttpSession())
        .build();

// Use mockMvc to perform requests...
```

## Performing Requests

WebTestClient을 통해 Mock Mvc를 사용할 경우 다음을 참조

Http 메소드를 사용해 요청을 수행

```jsx
// static import of MockMvcRequestBuilders.*

mockMvc.perform(post("/hotels/{id}", 42).accept(MediaType.APPLICATION_JSON));
```

쿼리 매개변수를 Url스타일로 지정

```jsx
mockMvc.perform(get("/hotels?thing={thing}", "somewhere"));
```

응용 프로그램 코드가 서블릿 요청 매개 변수를 사용하고 쿼리 문자열을 명시적으로 검사하지 않는 경우(대부분의 경우처럼) 어떤 옵션을 사용하는지는 중요하지 않습니다. 

그러나 URI 템플릿과 함께 제공된 쿼리 파라미터는 디코딩되는 반면 매개 변수를 통해 제공된 요청 파라미터는 이미 디코딩되어있어야 한다는 점에 유의해야 한다.

대부분의 경우 요청 URI에서 컨텍스트 경로와 서블릿 경로를 제외하는 것이 좋습니다. 전체 요청 URI를 사용하여 테스트해야 하는 경우 다음 예와 같이 요청 매핑이 작동하도록 상황에 맞게 contextPath 및 servletPath를 설정하십시오.

```jsx
mockMvc.perform(get("/app/main/hotels/{id}").contextPath("/app").servletPath("/main"))
```

## Defining Expectations (기대값 정의)

Mock Mvc는 andExpect를 사용해 기다값을 정의할수 있다.

```jsx
// static import of MockMvcRequestBuilders.* and MockMvcResultMatchers.*

mockMvc.perform(get("/accounts/1")).andExpect(status().isOk());
```

위 코드는 기다값으로 isOk를 가진다는걸 표현한것이고 MockMvcResultMatchers.*는 많은 기대값을 제공해주며 일부는 더 디테일한 기대값을 제공해 준다.

기대치는 2가지로 나뉘는데

1. 응답의 속성 ( 응답 상태, 헤더 및 내용 )
2. 응답을 넘어서 요청을 처리한 컨트롤러 방법, 예외 발생 및 처리 여부, 모델의 콘텐츠, 선택한 뷰, 플래시 특성 추가 여부 등