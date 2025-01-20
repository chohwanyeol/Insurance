

<img src="application/src/image/InsConfigPic/logo3_origin.png" style="width:50px" /> <strong style="font-size:40px" >BOHUMWANG</strong>


----
# 1. 개요
가상보험 사이트



## 1-1. 프로젝트 기술 스택
| | [백엔드](https://github.com/Grokeen/Insurance)|  [프론트엔드](https://github.com/Grokeen/Ourhome_React) | 
|:-----:|:-----:|:-----:|
|**IDLE**|IntelliJ| VScode|
|**Framwork**|Spring Boot|react|
|**Program** Language|JAVA / JDK 23|JavaScript / Node.js|
|**Markup** Lsnguage|-|HTML5|
||-|CSS3|
||-|Tailwind CSS|
|**Bulid Tool**|Grandle|npm|
|**Database**|JPA|-|
||Oracle SQL|-|
|**Project Connect**|-|Rest|
|**WAS**|Apache Tomcat|-|
||JWT|-|


<!-- -----------------------------------------------------------------
설명 : 
    <img src="https://img.shields.io/badge/
    Java-ED8B00 -> 이 부분이 문구와 색상 설정

    ?style=for-the-badge
    &logo=openjdk -> 이 부분은 로고 그림인데, 
                     여기서 찾아야 함 (https://simpleicons.org/ )

    &logoColor=white/"> -> 로고 컬러 설정
----------------------------------------------------------------- -->

<a><img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white/"></a>
<img src="https://img.shields.io/badge/Spring%20boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"/>
<img src="https://img.shields.io/badge/IntelliJ_IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white/">

<a><img src="https://img.shields.io/badge/node.js-FFFFFF?style=for-the-badge&logo=nodedotjs&logoColor=white/"></a>
<img src="https://img.shields.io/badge/React-0088CC?style=for-the-badge&logo=react&logoColor=white/">
<img src="https://img.shields.io/badge/css3-FC4C02?style=for-the-badge&logo=css3&logoColor=white/">
<img src="https://img.shields.io/badge/tailwind css-6935D3?style=for-the-badge&logo=tailwindcss&logoColor=white/">



## 1-2. 그 밖에 프로젝트 특징
1. 백앤드 
    - 아키텍쳐 패턴
        - MVC 패턴
        - MVVM 패턴(MV)
    - 디자인 패턴
        - Stratgy 패턴(DTO)
        - Factory Method 패턴(DTO)
2. 프론트엔드
    - react-rouete / session
    - 아키텍쳐 패턴
        - Flux 패턴(단방향 패턴 / 1 tear)
        - MVVM 패턴(VM)
    - 디자인 패턴
        - compotent 패턴
        - singletone 패턴
        - command 패턴
        - Observer 패턴
3. 웹디자인 
    - CSS 참고한 사이트 : 위택스 ,삼성화재 ,삼성화재 다이렉트 ,아워홈 ,겟앰프트
    - Tailwind CSS : 부족한 CSS에 보충 용도로 사용


----
# 2. 프로젝트
- 가상으로 만든 보험 사이트 
    


    ![Gif 오리는 방법](https://i.pinimg.com/originals/f8/1b/3d/f81b3d8c30943647c196391b7f94c65d.gif)



----
# 3. 프로젝트 실행

- [<strong style="color:white;"> 백엔드 </strong>](https://github.com/Grokeen/Insurance)프로젝트 실행방법
    - IntelliJ에서 scr 디렉토리로 가서, main 프로젝트을 찾아 실행

- [<strong style="color:white;"> 프론트엔드 </strong>](https://github.com/Grokeen/Ourhome_React) 프로젝트 실행방법
    - VS Code 하단에 Terminal(CLI)에서 아래 스크립트 실행
        1. application 디렉토리로 들어간다.
            ```linux
            cd application
            ```
        2. 프로젝트 빌드
            ```linux
            npm run build
            ```
        3. 정상 빌드가 됐다면, 프로젝트 시작
            ```linux
            npm start 
            ```

----
# 4. 프로젝트 관리
* CI/CD : Github 이용하여 형상관리
    1. [백엔드](https://github.com/Grokeen/Insurance)
    2. [프론트엔드](https://github.com/Grokeen/Ourhome_React)
## 4-1. 프로젝트 관리에 필요한 Git 스크립트
- 🐶 브런치 확인 / 생성 / 변경

    ```linux
    git branch 
    git branch (새로 만들 브런치명)
    git checkoput (새로 만들 브런치명)
    ```
    - 자신 만의 브런치를 만들어서, 서로의 개발공간을 침범하지 않아야 한다.

    - git 최신 버전에서는 ```git switch```로도 브런치 변경이 가능하다.

    - 브런치 삭제 : ```git branch -d (브런치명)```

- 🐱 깃서버에 올릴 때 스크립트

    - 특정 파일 추가
        ```linux
        git add /디렉토리명/파일명.js
        ```
        - 전체 파일 올리는 방법 : ```git add *```
            > <strong style="color:red">주의</strong> : 다른 사람 수정한 부분이 날라갈 수 있다.

    - add로 추가한 파일을 묶어서 전송준비
        ```linux
        git commit -m "message"
        ```
    - commit으로 포장한 걸 깃 서버에 전송
        ```linux
        git push
        ```

        - 특정 브런치에 올리려면 : ```git push --set-upstream origin master_mac```

- 🐭 최신 버전 받는 스크립트

    ```linux
    git pull
    ```
    - 특정 브런치에 받으려면 : ```git pull --set-upstream origin master_mac```

- 🐹 받은 버전을 패치

    
    ```linux
    git fetch
    ```
    - 일종의 동기화 단계로 최신버전에서는 ```pull``` 없이 ```fetch``` 만 으로도 최신버전을 가져오는게 가능하다고 한다.

    - 특정 버전으로 패치하기(master 브런치의 최신버전 가져오기)

        ```linux
        git fetch origin master
        ```
        
        > <strong style="color:red">주의</strong> : cli 창에 아래같은 문구가 떴다면, master 브런치에서 최신 정보를 가져오고 있다는 뜻으로 약간의 시간이 걸리니 기다려야 한다.
        "``` * branch            master     -> FETCH_HEAD```"

----
# 5. 개발자
> 백엔드 / 프론트엔드
- 조환열


> 웹디자인 / 프론트엔드
- 김용록





<!-- # 아워홈 사전과제
## 받은 자료
- ppt 예시
- 과제관련 사진 2장
- ppt ttf 파일 3개 -->

<!-- ![과제관련 사진 1](./markdown/img/(참고) 웹개발_사전과제 이미지 화면1.png) -->

<!-- 
<img src="/markdown/img/(참고) 웹개발_사전과제 이미지 화면1.png" />
<img src="/markdown/img/(참고) 웹개발_사전과제 이미지 화면2.png" />

ppt 내용에는 React와 Javascript로 개발하라는 필수 조건이 있었다. 나는 Spring 개발자다. 약간 막막했지만, 지난 회사에서 개발할 때, JSP에서 function으로 바인딩하여 계산기를 개발한 경험이 있어서, 딱히 걱정은 없었다. 오히려 코테보다 쉽다고 생각했다.

첫 날에 간단하게 했던 구상은
- 식품 정보를 가진 DB
- 로그인/비로그인 데이터 저장 방식
    로그인 시, 장바구니(DB)
    비로그인 시, 장바구니(session)
- 장바구니 페이지 자동 계산
- (가능하다면)식품 상세 페이지 -->


<!-- 
## 개발 기간 12월 29일 ~ 1월 3일
총 개발 기간은 6일 정도 받았다. 저녁에 문자를 확인하고, 간단하게 구상한 뒤에 다음 날 본격적으로 개발할 계획이었다. 그러나 다음 날 새벽에 안타깝게도 할머니 부고 소식이 전해졌다. 장례식 장에서 3일간 머물면서, React와 Spring의 차이를 분석하고 Github에 빈 repository를 만들어서, 브라우저 용 VSCode에 연결하여 실습해보았다.

<img src="/markdown/img/Screenshot 2024-01-09 at 14.15.25.png" />

### 참고) React와 Spring 차이
- 단방향 vs 양방향
- Flux패턴 vs MVC 패턴
- Dependentcy vs Route(상대경로 설정 방식)
- 디렉토리 구조, JS 구조
- 문법 차이
- 개발 사례로 보는 결과물 비교 -->


<!-- 
## 개발과정

<img src="/markdown/process/Screenshot 2024-01-03 at 23.18.44.png" style="height:60%" />
디렉토리 구성이다. img 디렉토리에 필요한 png 파일을 넣고, 내가 개발하려 하는 shop 디렉토리 명을 설정하고, js 파일을 넣었다.


<img src="/markdown/process/Screenshot 2024-01-03 at 23.16.13.png" style="height:60%" />
index.js에 import로 js 파일의 절대경로를 가져오고, 그걸 route로 상대경로로 설정하였다.
 
<img src="/markdown/process/Screenshot 2024-01-03 at 23.24.48.png" style="height:60%" />
css 같은 경우, 아워홈 mall에 실제 사용중인 부분에서 필요한 부분 만 가져와서 사용하였다.


<img src="/markdown/process/Screenshot 2024-01-03 at 23.33.14.png" style="height:60%" />
session에 넣는 방식이다. operationType 이나 conditionFlag 같은 경우는 실제로 사용 중인 아워홈 mall에서 가져오다 보니, 부분적으로 가져오게 되었는데 삭제하지 않고 단순히 저런 식(조건문)으로 사용될 거 같아 사용하였다. 실제 필요했던 정보는 상품의 ID와 가격 그리고 상품명이다.
이 부분이 아쉬웠다. 상품을 하드코딩하였기 때문에 이렇게 가져오지만 실제로는 상품 ID 값 만 있으면, 나머지 정보는 DB에서 가져왔을 것이다.


<img src="/markdown/process/Screenshot 2024-01-03 at 23.33.57.png" style="height:60%" />
특정 div 태그를 가려놓았다. 따라서 상품 장바구니를 선택 시, 화면이 보일 수 있게 해놓았다. Spring이었다면, JSTL에 none,block이나 JQuary에 hide, show를 사용했을 것이다. 그러나 JQuary를 import하고 문법을 사용해도 되는지 여부를 잘 모르겠어서, 최대한 인터넷에서 찾아서 적용하였다.

<img src="/markdown/process/Screenshot 2024-01-03 at 23.34.17.png" style="height:60%" />
마지막 사진은 위에서 가려 논 jsx 코드다. -->




<!-- ## 결과
아래는 결과 화면이다.
### 영상
[유튜브 올려논 테스트 영상](https://www.youtube.com/watch?v=J8UeHf1kJFk) -->

<!-- <iframe width="560" height="315" src="https://www.youtube.com/embed/J8UeHf1kJFk?si=NhWpjKxsnQL9Z7GG" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" allowfullscreen></iframe> -->



<!-- 
### 사진
<img src="/markdown/img/Screenshot 2024-01-03 at 22.57.18.png" />
<img src="/markdown/img/Screenshot 2024-01-03 at 23.34.35.png" />
<img src="/markdown/img/Screenshot 2024-01-03 at 22.57.09.png" /> -->

