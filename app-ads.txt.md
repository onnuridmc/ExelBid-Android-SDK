
# App-ads.txt

## # App-ads.txt
- App-ads.txt는 Ads.txt와 마찬가지로 앱인벤토리의 경우 개발자가 자신의 개발자 웹 사이트에 업로드 하는 판매 권한 승인 목록 텍스트 파일 입니다.<br/>웹과 마찬가지로 IAB에서는 인앱 광고 인벤토리를 구매하고 판매할 권한이 있는 목록을 정의하였습니다.

- 모바일 앱에서 app-ads.txt는 어떻게 구현?
    * 기본적으로 스펙과 내용은 ads.txt와 같습니다.<br/>
    다만 인앱 특성상 OpenRTB 광고 요청시 웹 페이지 정보가 아닌 앱 정보가 전달되고 이때 전달된 스토어 정보를 기반으로 ads.txt 항목을 추적해야합니다. 다시 말해서, app-ads.txt의 경로는 앱스토에 등록한 개발자 웹 사이트 URL입니다.

- app-ads.txt는 다음 프로세스를 자세히 설명합니다.
	1. 앱 개발자는 앱의 스토어 메타 데이터에 웹 사이트 URL을 제공하고 해당 웹 사이트에 앱 광고 인벤토리의 승인 된 판매자를 나열하는 app-ads.txt 파일을 게시합니다.
	2. Ad network / SSP는 승인 된 판매자 상태를 쉽게 시행 할 수 있도록 입찰 요청에 대한 상점 목록 URL을 제공합니다.
	3. 앱 스토어는 앱 메타 데이터를 앱의 앱 목록 페이지에서 표준 HTML <meta> 태그로 게시하여 구매자가 구조화 된 데이터로 크롤링하고 구문 분석 할 수 있습니다.
	4. 개발자 웹 사이트 정보를 찾고 개발자 웹 사이트를 크롤링하여 app-ads.txt 파일을 가져 와서 해석하고 권한 부여 상태를 적용합니다.

## # 각 플레이어의 역할과 책임(Solution specification)
- 다음은 앱 개발자, Ad networks / SSP, DSP 및 앱 스토어에 대한 요구 사항을 지정합니다.



### 1. 앱 개발자(App developers)
- 앱을 배포하는 모든 앱 스토어의 메타 데이터에 개발자 웹사이트 URL이 있어야 합니다. 
- 관심있는 크롤러는이 웹 사이트 URL을 사용하여 경로를 도출하고 해당 도메인에서 appads.txt 파일을 크롤링하려고 시도합니다.

    - Publish an app-ads.txt file

		* 웹용 ads.txt 파일의 올바른 형식과 내용에 대한 자세한 내용은 https://iabtechlab.com/ads-txt/에있는 기본 ads.txt 표준을 참조하십시오. “subdomain”지시문 항목을 제외하고는 ads.txt 사양과 동일한 지침을 사용하십시오.
		* 파일 이름은 “ads.txt”와 달리 “app-ads.txt”이므로 앱과 웹 구성은 별도로 관리되며 서로 충돌하지 않습니다. 웹과는 다른 배포 모델을 사용하는 앱의 특성으로 인해서 하나의 통합 파일에 항목을 결합하는 것과 비교할 때 이러한 유연성으로 채택 및 유지 관리가 쉬워 질 것으로 예상됩니다.


    - 앱의 developer website URL 변경

		* developer website URL 변경시 인식하는데 다소 시간이 걸릴 수 있다고 예상해야 합니다. 이전 도메인에서 파일을 제거하거나 이전 위치에서 필요 항목을 제거하기 전에 특정 기간 동안 이전 도메인과 새 도메인에 관련 항목이 포함 된 app-ads.txt파일을 호스팅하는것이 좋습니다.


    - 승인 판매자 제거
		* app-ads.txt파일에서 고를 구매 및 판매 할 수있는 광고 시스템이 없음을 나타내는 빈 app-ads.txt 파일을 게시하여 광고 시스템을 승인하지 않을 수 있습니다.. 검증자가 빈 app-ads.txt파일을 읽고 해석하려면 ads.txt의 레코드가 없는 파일의 요구 사항을 설명하는 지침을 따라야합니다.

        * 인증 된 광고 시스템 레코드가 포함되지 않은 파일의 경우 다음 " placeholder "레코드를 사용하여 파일이 ads.txt 사양을 준수 함을 나타냅니다.
			- 승인 된 광고 시스템 레코드가없는 파일<br/>
                > placeholder.example.com, placeholder, DIRECT, placeholder
                - "example.com"도메인은 의미가 없습니다. ads.txt 사양의 이전 버전에서는 게시자가 단순히 빈 ads.txt 파일을 사용하여 웹 사이트에서 광고를 구매하거나 판매 할 수있는 광고 시스템이 없음을 나타냈었지만, 이 방법은 모호함으로 인해 더 이상 사용되지 않으며 2020 년 3 월 1 일 이후 무시해야합니다.      

    
### 2. Ad networks/sell-side platforms (aka “bid request issuers”)

이 사양에 따라 승인 된 인벤토리로 처리하기 위해 RTB 입찰 요청을 발행하는 광고 네트워크 및 판매 측 플랫폼 (SSP), Ad Exchange는 다음과 같이 앱의 배포 채널을 표시해야합니다.

- 입찰 요청에 storeurl 매개 변수 포함 - TAG 인벤토리 품질 가이드 라인( TAG Inventory Quality Guidelines )에서는 Ad networks, Ad Exchange 및 SSP에서는 app-ads.txt 요구 사항을 준수하려면 노출을 제공하는 앱에 해당하는 store URL을 제공해야합니다.현재 이 값을 제공하지 않는 모든 SSP 또는 Exchange등은 적용이 필요합니다. 


### 3. App stores
앱 스토어는 app-ads.txt 표준을 용이하게하기 위해 다음 기능을 지원해야합니다.
- 구조화된 앱 정보 게시 - App Store는 개별 앱의 페이지에서 3개의 HTML <meta>태그를 게시해야합니다.
	* 앱 개발자의 웹 사이트 URL (현재 상점 목록 페이지에서 사용자가 클릭 할 수있는 링크로 제공됨)
	* 앱의 bundle_id
	* 앱의 store_id


- HTML 메타 태그의 형식을 다음과 같이 HTML 문서 시작시 <head> HTML 태그에 삽입하고 content 속성에 적절한 값을 포함시켜야합니다.
```html
    <meta name="appstore:developer_url" content="https://www.path.to/page" />
    <meta name="appstore:bundle_id" content="com.example.myapp" />
    <meta name="appstore:store_id" content="SKU12345" />
```


- 이 방식은  [Open Graph protocol](http://ogp.me/) 또는 [Twitter markup tags](https://developer.twitter.com/en/docs/tweets/optimize-with-cards/overview/markup.html)와 유사합니다. 여기서 "appstore:" 접두어는 필드의 목적을 설명하는 데 사용됩니다. 이 이름은 [WHATWG Wiki MetaExtensions](https://wiki.whatwg.org/wiki/MetaExtensions) 페이지에 등록되어 있습니다. 
트위터 사양과 유사하게, 접두사에 공식적인 컴팩트 네임 스페이스 ([CURIE](https://en.wikipedia.org/wiki/CURIE))를 정의 할 필요는 없습니다. 앱 스토어는 [W3C standards for the <meta> tag](https://www.w3.org/TR/html5/document-metadata.html#the-meta-element) 및 관련 문서에 따라 유효한 HTML을 표시합니다. <br/>
정상적인 페이지로 인식 되려면 앱 스토어에 developer_url 메타 태그와 스토어에 적용 할 수있는 bundle_id, 추가로 store_id 메타 태그가 포함되어야합니다. 혹 앱에 URL이 제공되지 않은 경우 developer_url 메타 태그의 컨텐츠 속성에 빈 값을 사용합니다..

## # Implementer notes
- 공급측 플랫폼은 개발자에게 올바른 app-ads.txt 파일에 적절한 항목을 추가 할 수있는 방법을 알려주는 도움말 센터등을 제공해야합니다.
- 스토어에 적용된 개발자 웹사이트는 자주 변경이 되지 않아야 하므로 크롤러가 앱스토어 자체를 자주 크롤링해야 할 필요는 없습니다.
- app-ads.txt 파일을 호스팅하는 개발자 URL의 도메인 변경시 이전 도메인의 app-ads.txt파일 제거는 충분한 시간을 두고 진행해야 합니다(가능한 경우 30일 권장).


## # 참고) 뉴욕 타임즈 ads.txt와 app-ads.txt  <2019.10.01>
<table border=1>
    <tr align="center">
        <td>ads.txt<br/>(https://www.nytimes.com/ads.txt)</td>
        <td>app-ads.txt<br/>(https://www.nytimes.com/app-ads.txt)</td>
    </tr>
    <tr>
        <td>
        amazon-adsystem.com, 3030, DIRECT<br/>
        appnexus.com, 3661, DIRECT<br/>
        google.com, pub-4177862836555934, DIRECT<br/>
        google.com, pub-9542126426993714, DIRECT<br/>
        indexexchange.com, 184733, DIRECT<br/>
        liveintent.com, 130, DIRECT<br/>
        openx.com, 537145107, DIRECT<br/>
        openx.com, 539936340, DIRECT<br/>
        rubiconproject.com, 12330, DIRECT<br/>
        rubiconproject.com, 17470, DIRECT<br/>
        triplelift.com, 746, DIRECT<br/>
        pubmatic.com, 158573, DIRECT, 5d62403b186f2ace
        </td>
        <td>
        amazon-adsystem.com, 3030, DIRECT<br/>
        appnexus.com, 3661, DIRECT<br/>
        google.com, pub-4177862836555934, DIRECT<br/>
        google.com, pub-9542126426993714, DIRECT<br/>
        openx.com, 537145107, DIRECT<br/>
        openx.com, 539052954, DIRECT<br/>
        openx.com, 539936340, DIRECT<br/>
        openx.com, 540659133, DIRECT, 6a698e2ec38604c6<br/>
        rubiconproject.com, 12330, DIRECT<br/>
        rubiconproject.com, 17470, DIRECT<br/>
        triplelift.com, 746, DIRECT, 6c33edb13117fd86<br/>
        triplelift.com, 746-EB, DIRECT, 6c33edb13117fd86<br/>
        pubmatic.com, 158573, DIRECT, 5d62403b186f2ace<br/>
        indexexchange.com, 184733, DIRECT
        </td>
    </tr>
</table>