# ~~Mediation - Native 추가 지원 버전~~
- ~~Exelbid에서 '광고 없음'이 응답되는 경우, 간단한 설정 만으로 타사 SDK에 자동 광고 요청하여 Fill Rate를 증가시킬 수 있습니다.~~

## Kakao Adfit 추가하기
- ~~Exelbid SDK v 1.4.0 버전부터 지원~~<br>
- ~~Exelbid SDK v 1.4.4버전, adfit 버전 3.0.8 적용 필수(Adfit 정책상 구버전 지원 중단됨(2019년 9월 이후 예정)~~<br>
- Exelbid SDK v 1.6.2버전, adfit 버전 3.5.2 적용 필수 (Adfit Native Mediation 추가 적용)<br>
- Exelbid 운영팀에 사용 제안(아이디 발급등 처리 필요)<br>
- 디스플레이 배너 광고, 네이티브 기본(단독) 형태 (다이얼로그 광고 포함) 에서만 사용 가능
1. minSdkVersion 14 적용 
    - Adfit mediation 기능은 <a href="https://developer.android.com/about/versions/android-4.0">Android 4.0(Ice Cream Sandwich, API Level 14)</a> 이상 기기에서 동작합니다.

2. build.gradle 파일에 Maven repository를 추가합니다.
    ```java
    repositories {
        google()
        jcenter()
        maven { url 'http://devrepo.kakao.com:8088/nexus/content/groups/public/' }
    }
    ```
3. build.gradle 파일 dependencies에 Adfit SDK Library를 추가
    ```java
    dependencies {
        implementation "com.google.android.gms:play-services-ads-identifier:17.0.0"
        implementation "com.kakao.adfit:ads-base:3.5.2"
    }
    ```
    
4. ~~Android Gradle 플러그인 업데이트 권장~~
    - ~~플러그인 버전 4.0.0+, Gradle 버전 6.1.1+ 이상 사용 권장 <a href="https://developer.android.com/studio/releases/gradle-plugin?hl=ko#updating-plugin">(Android Gradle 플러그인 출시 노트)</a>~~





