

## AsciiDoc이 무엇이며 왜 필요한가?

> 작성의 어려움

작성한 코드에 대해 설명을 하는 글을 쓸때 어려움을 겪어 본적이 있을 것이다. 

아무리 가독성이 좋은 코드, API여도 제대로된 문서화 없이 과연 제 3자의 입장에서 원활하게 쓰일수 있을까? 

유지 보수성이 보장이 될까? 

사용자나 타인이 사용할 수가 없다면 아무리 코드 작성을 잘했다 할지라도 좋은 프로그램이라 할수 있을까?

그렇다면 설명서가 중요하다는 것인데 설명서 작성에 불편함을 느끼게 된다면 작성자도 힘들고 그로인해 읽는 사람도 힘들어지는 경우가 생길수 있다. 

 AsciiDoc은 이를 좀더 편리하게 작성할수 있게 도와준다.

---

## What is AsciiDoc?

- AsciiDoc은 일반 텍스트 형식이며 블로그, 게시글, 웹페이지 등을 작성하기 위해 사용된다.
    - AsciiDoc은 경량 마크업 언어에 속하며 같은 종류로는 MarkDown이 있다.
- AsciiDoc 문서를 다양한 형식으로 변환하기 위한 텍스트 프로세서 및 툴체인 이다.

## Why AsciiDoc?

> 그렇다면 왜 아스키 독을 사용해야 할까?

우리는 말하거나 생각은 어렵지 않게 하지만 이를 글로 옮기는데는 어려움을 느낀다. 

어떤 형식, 어떤 단어를 사용 할지 고민하며 과하면 이를 고민하다가 시작을 늦게해 데드라인에 못 맞추는 불상사도 발생하게 된다. 

글을 작성할때 사람들은 사용하고있는 툴에 최소한의 방해를 받으며 자신이 원하는 방식으로 작성하고 싶어한다. 하지만 워드 프로세서등의 편집기를 사용하면 글꼴, 줄간격등 다양한 선택지로 인해 고민이 늘어간다.  

이는 인터페이스와 싸우며 고민하며 시간 낭비를 유발 할수 있다.

만약 문서를 작성할때 이메일 처럼 작성할수 있다면 얼마나 편리 할까?

레이아웃, 스타일링 을 신경쓰지 않고 작성한다면 정말 편리 할 것이다. 고맙게도 이를 asciidoc은 가능하게 해준다. 일반 텍스트 문서와 동일한 방식으로 문서를 작성할수 있게 해주며 마크업 태그나 이상한 형식 표시가 없다.

markdown이 상대적으로 단순한 표현력만 가지고 있는 반면 AsciiDoc은 SGML, Dockbook 처럼 전문적인 문서 작서에 용이하다.

---

## How to use

### 문단

빈줄로 구분되며 빈줄이 없이 쓰여진 문장은 한 문단으로 인식됨

```
This journey begins one late Monday afternoon in Antwerp.
Our team desperately needs coffee, but none of us dare open the office door.

To leave means code dismemberment and certain death.
```

→

```
This journey begins one late Monday afternoon in Antwerp.Our team desperately needs coffee, but none of us dare open the office door.

To leave means code dismemberment and certain death.
```

### 줄바꿈

줄 끝에 **+** 를 붙여 주면 줄바꿈이 된다.

```jsx
Rubies are red, +
Topazes are blue.
```

->

```jsx
Rubies are red,
Topazes are blue.
```

or 문단 앞에 [%hardbreaks] 를 붙여주면 줄바꿈이 된다.

```jsx
[%hardbreaks]
Rubies are red,
Topazes are blue.
```

->

```jsx
Rubies are red,
Topazes are blue.
```

### 경고

- `NOTE`
- `TIP`
- `IMPORTANT`
- `CAUTION`
- `WARNING`

경고를 주고 싶을때는 위의 단어를 대문자로 작성후 **:** 를 붙이고 한칸을 띄고 내용을 작성하면 된다.

```jsx
NOTE: 참고하고 하세요

TIP: 팁입니다

IMPORTANT: 중요합니다.

CAUTION: 주의 하세요

WARNING: 위험합니다.
```

( 대체적으로 MarkDown 하고 유사하다 )

---

## Reference

[https://docs.asciidoctor.org/asciidoctor/latest/](https://docs.asciidoctor.org/asciidoctor/latest/)

[https://asciidoctor.org/docs/what-is-asciidoc/](https://asciidoctor.org/docs/what-is-asciidoc/)

[https://narusas.github.io/2018/03/21/Asciidoc-basic.html](https://narusas.github.io/2018/03/21/Asciidoc-basic.html)