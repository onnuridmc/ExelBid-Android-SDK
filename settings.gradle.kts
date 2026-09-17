rootProject.name = "exelbid-android"

// 연동 시나리오별 샘플 모듈 — 각 모듈의 build.gradle.kts가 해당 시나리오의 의존성 문서 역할을 한다
include(":exelbid-sample-common")
include(":exelbid-sample-basic")
include(":exelbid-sample-mp")
include(":exelbid-sample-mediation")
include(":exelbid-sample-admob")
include(":exelbid-sample-admob-nextgen")
