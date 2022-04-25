object Config {
  object AppConfig {
    const val appId = "grand.app.moon"
    const val compileSdkVersion = 31
    const val minSdkVersion = 23
    const val versionCode = 1
    const val versionName = "0.0.1"
    const val testRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  object Dependencies {
    const val jitPackURL = "https://jitpack.io"
    const val gradleVersion = "com.android.tools.build:gradle:${Versions.gradleVersion}"
    const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val navigationSafeArgs =
      "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.androidNavigation}"
    const val hilt = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hiltVersion}"
    const val google_services = "com.google.gms:google-services:${Versions.google_services}"
  }

  object Plugins {
    const val androidApplication = "com.android.application"
    const val kotlinAndroid = "kotlin-android"
    const val kotlinAndroidExtensions = "kotlin-android-extensions"

    const val kotlinKapt = "kotlin-kapt"
    const val navigationSafeArgs = "androidx.navigation.safeargs"
    const val hilt = "dagger.hilt.android.plugin"
    const val androidLibrary = "com.android.library"
    const val kotlinJvm = "org.jetbrains.kotlin.jvm"
    const val ktLint = "org.jlleitschuh.gradle.ktlint"
    const val google_services = "com.google.gms.google-services"
  }

  object Modules {
    const val domain = ":domain"
    const val data = ":data"
    const val app = ":app"
    const val presentation = ":presentation"
    const val prettyPopUp = ":prettyPopUp"
    const val appTutorial = ":appTutorial"
    const val imagesSlider = ":imagesSlider"
  }

  object Environments {
    const val roomDb = "\"tafwk_db\""
    const val SERVER_CLIENT_ID = "\"AIzaSyApcEA5RXncL4762cObXGeBaE1x-nEZpOM\""
    const val debugBaseUrl = "\"https://moontest.my-staff.net/api/\""
    const val releaseBaseUrl = "\"https://moontest.my-staff.net/api/\""
  }
}