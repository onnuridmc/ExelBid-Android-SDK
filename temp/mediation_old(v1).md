# Mediation v1 (legacy 버전)
- Exelbid에서 '광고 없음'이 응답되는 경우, 간단한 설정 만으로 타사 SDK에 자동 광고 요청하여 Fill Rate를 증가시킬 수 있습니다.

## Kakao Adfit 추가하기
- Exelbid SDK v 1.9.5버전, adfit 버전 3.5.2 이상 적용 필수<br>
- Exelbid 운영팀에 사용 제안(아이디 발급등 처리 필요)<br>
- 디스플레이 배너 광고 사용 가능
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
    
