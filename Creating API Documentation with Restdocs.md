# Creating API Documentation with Restdocs

---

## 이 기술이 무엇인가?

Spring Rest Docs의 가장 큰 목적은 RESTful 서비스를 보다 간결하고 명확하며 읽기 쉬운 문서로 작성하는데 도움을 주는 것이다. 작성 도구로는 [Asciidoctor](https://donglee99.tistory.com/3)을 통해 필요에 따라 스타일링이 가능한 HTML 작성을 가능하게 해준다. (필요에 따라 마크다운을 사용해 구성이 가능하다.) 

- RESTful 서비스 문서화 → 해당 서비스의 리소스를 설명하는 것으로 Http 요청과 응답에 대한 정보를 기록하게 된다. 이때 Spring Rest Docs를 사용하게 된다면 Http 요청과 응답을 쉽게 기록 및 보호가 가능하다.

기본적으로 Spring MVC의 테스트 프레임워크에서 제공하는  [MockMvc](https://donglee99.tistory.com/2)로 테스트 코드를 작성하며 이 외에도 Spring WebFlux의 WebTestClient, Rest Assued 3에도 적용이 가능하다.  

---

## 이 기술은 어떠한 배경에서 나왔는가?

RESTful API에 대한 문서를 작성할때 테스트를 통해 검증을 받은 예제만 작성이 되면 얼마나 편할까?

개발자가 하나하나 양식을 맞춰가며 작성하는 것 보다 자동화를 해 테스트를 할때마다 작성되면 얼마나 편할까? 

테스트를 통해 검증을 받아야 요구 사항에 맞는 프로그램이 탄생하는 것이다 하지만 이 테스트와 API 문서에 명세 되어있는 내용이 불일치 한다면 어떻게 될것인가? 쉽게 말해서 우리가 사용하는 전자 제품에 설명서와 다른 버전의 모델을 받는 다고 생각하면 된다. 이는 결국 작업의 불일치성을 발생하게 할수 있고 넘어서 sideEffect를 야기 할 수도 있다. Spring Rest Doc은 이를 예방하기 위해서 나왔다 해도 무방하다.

---

## 사용 이유

![](https://images.velog.io/images/donglee99/post/12f7176a-6249-4991-ae14-26d589fccd0c/restdoc%20vs%20swagger.png)

Spring Rest Doc을 사용하는 가장 큰 이유는 API 문서의 목적인 개발하는 스펙을 정의 함에 있어 좀더 깔끔 하고 명료하게 작성하기 위함이라 할 수 있다. 

(Swagger 는 API 문서화를 위해 훌륭한 도구기는 하지만 사용하기위해 어노테이션을 삽입 해야하며 이로 인해 소스코드가 더러워 지는 단점이 있으며 API 동작을 테스트 하는 용도에 좀더 특화가 되어있다.)  

또한 위에서 말했듯이 Rest Doc은 swagger처럼 api를 직접 실행 시켜주지 않기 때문에 좀더 테스트에 의존하는 코드를 작성하게 되고 기존 소스와 API 분리 시키기 가능하다. (TDD에 적합한 방식이라고 생각함) 작업의 일치성을 보장 받기 위함과 테스트를 통해 작성된 문서로 이용자에게 좀더 신뢰도와  안정성을 줄 수 있다. 

---

## 사용방법 (Gradle)

### **요구 환경**

- Java 8 or later
- Spring Framework 5 (5.0.2 or later)
- Gradle 4+ or Maven 3.2+

### Spring Rest Docs 사용의 첫단계

프로젝트 빌드 구성 (의존성 추가)

```jsx
plugins {
	id 'org.springframework.boot' version '2.5.2'
	id 'io.spring.dependency-management' version '1.0.11.RELEASE'
	id 'java'
	id 'org.asciidoctor.jvm.convert' version '3.3.0'
}

group = 'com.example'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '1.8'

asciidoctor {
	sourceDir 'src/main/asciidoc'
	attributes \
		'snippets': file('target/snippets')
}

repositories {
	mavenCentral()
}

dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-web'
	// tag::test[]
	testImplementation 'org.springframework.restdocs:spring-restdocs-mockmvc'
	// end::test[]
	testImplementation('org.springframework.boot:spring-boot-starter-test')
}

test {
	useJUnitPlatform()
}
```

(AsciiDoc(명세 목적)과 MockMvc(테스트)를 사용하므로 의존성을 추가해준다.)

### Setting for JUnit4

새로운 테스트 케이스를 만들어 한번 주석 및 빈을 테스트 해준다.

```jsx
package com.example.testingrestdocs;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestingRestdocsApplicationTests {

	@Test
	public void contextLoads() throws Exception {
	}
}
```

이후 애플리케이션 동작 확인을 위해 몇가지 테스트를 작성해 주어야 한다. 주로 사용하는 방식은 MVC 계층만 테스트하는 것이다 이때 HTTP 요청을 처리해 컨트롤러에 넘겨 테스트를 진행한다. (@WebMvcTest 어노테이션을 이용해 컨트롤러를 주입해주어야함)

```jsx
@RunWith(SpringRunner.class)
@WebMvcTest(HomeController.class)
public class WebLayerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnDefaultMessage() throws Exception {
        this.mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Hello, World")));
    }
}
```

### Snippet 생성

앞의 테스트는 Http 요청을 만들고 응답을 한다. Http Api는 동적 컨텐츠를 가지고 테스트를 검증하고 문서로 옮기기 위해 스니펫을 생성한다.

```jsx
package com.example.testingrestdocs;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HomeController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class WebLayerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void shouldReturnDefaultMessage() throws Exception {
		this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("Hello, World")))
				.andDo(document("home"));
	}
}
```

@AutoConfigureRestDocs는 디렉토리 위치를 인자값으로 사용한다.

테스트를 실행하고 target/sinppets를 살펴보면 asciidoctor스니펫을 포함한 home을 찾을수 있을것이다.

![](https://images.velog.io/images/donglee99/post/3c224754-0fa1-45b4-a3b4-e273d1e03c29/snippet.png)

스니펫의 기본 형태는 Http 요청에 대한 응답을 나타낸 Asciidoctor 형식이다. 

테스트에 document() assertion을 추가해 추가 스니펫 생성이 가능하다. 

ex)

```jsx
this.mockMvc.perform(get("/"))
    ...
    .andDo(document("home", responseFields(
        fieldWithPath("message").description("The welcome message for the user.")
    ));
```

‼️ 필드를 생략하거나 필드 이름을 잘못 지정하면 테스트가 실패한다.

### Snippet 사용하기

생성된 스니펫을 사용하기위해서는 다음과 같이 진행하면된다.

```jsx
= Getting Started With Spring REST Docs

This is an example output for a service running at http://localhost:8080:

.request
include::{snippets}/home/http-request.adoc[]

.response
include::{snippets}/home/http-response.adoc[]

As you can see the format is very simple, and in fact you always get the same message.
```

Asciidoc 파일의 특징은 include::{} 를 사용해 파서에게 해당라인에 해당 스니펫을 표현하라는 뜻을 의미한다. 

포함된 스니펫의 경로는 {snippets}이라는 placeholder에 의해 표현된다.

= 은 마크다운에서의 # 와 같으며 .은 캡션을 의미한다.

---

## 기술의 장점

- Spring Rest Doc의 장점 이라하면 무엇보다 테스트가 성공해야만 문서 작성이 가능하다는 점이 가장 큰 장점이 될 수 있다. TDD를 진행 하다보면 자연스럽게 무수히 많은 테스트를 기반으로 서비스를 만들어 나가게 된다. 이때 TDD를 진행하는 이유도 개발자 스스로 검증을 통해 안정성을 확보 하기 위함인데 이를 문서로 작성하게 된다면 덩달아 문서에도 장점으로 신뢰도를 줄 수 있게 된다.
- 깔끔하고 명확하게 개발 스펙을 정의 할수 있기때문에 유용하다.
- 프로덕션 코드에 영향을 주지 않는다는 장점이 있다.

---

## 기술의 단점

- TDD도 그렇듯이 수많은 테스트를 기반으로 작성을 하기 때문에 테스트 케이스 작성을 위해 그만큼의 시간을 요구한다. 이는 결국 피로도를 증가시키게 되며 개발 과정의 비용을 증가 시킬수 있다.
- 초기에 설정이 어려워 적용을 하기가 힘들어 처음 사용하는 사용자는 어려움을 느낄수 있다. ( 러닝 커브다 높다.)

---

## Reference

[https://velog.io/@bread_dd/Spring-Rest-Docs와-Open-Api-Swagger](https://velog.io/@bread_dd/Spring-Rest-Docs%EC%99%80-Open-Api-Swagger)

[https://ykh6242.tistory.com/111](https://ykh6242.tistory.com/111)

[https://docs.spring.io/spring-restdocs/docs/current/reference/html5/](https://docs.spring.io/spring-restdocs/docs/current/reference/html5/)