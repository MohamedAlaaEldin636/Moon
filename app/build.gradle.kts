import android.annotation.SuppressLint
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
  id(Config.Plugins.androidApplication)
  id(Config.Plugins.kotlinAndroid)
  id(Config.Plugins.kotlinAndroidExtensions)

  id(Config.Plugins.kotlinKapt)
  id(Config.Plugins.navigationSafeArgs)
  id(Config.Plugins.hilt)
  id(Config.Plugins.google_services)
  id ("com.onesignal.androidsdk.onesignal-gradle-plugin")


}

android {
  compileSdk = Config.AppConfig.compileSdkVersion

  defaultConfig {
    applicationId = Config.AppConfig.appId
    minSdk = Config.AppConfig.minSdkVersion
    targetSdk = Config.AppConfig.compileSdkVersion
    versionCode = Config.AppConfig.versionCode
    versionName = Config.AppConfig.versionName

    vectorDrawables.useSupportLibrary = true
    multiDexEnabled = true
    testInstrumentationRunner = Config.AppConfig.testRunner

  }

  buildTypes {
    getByName("debug") {
      manifestPlaceholders["appName"] = "@string/ic_launcher"
      manifestPlaceholders["appIcon"] = "@mipmap/ic_launcher"
      manifestPlaceholders["appRoundIcon"] = "@mipmap/ic_launcher"
      buildConfigField("String", "API_BASE_URL", Config.Environments.debugBaseUrl)
      buildConfigField("String", "ROOM_DB", Config.Environments.roomDb)
    }

    signingConfigs {
      create("releaseConfig") {
        storeFile = file(rootProject.file("key"))
        storePassword = "123456"
        keyAlias = "My-Key"
        keyPassword = "123456"
      }
    }

    getByName("release") {
      signingConfig = signingConfigs.getByName("releaseConfig")

      isMinifyEnabled = true
      isShrinkResources = true

//      resValue("string", "google_api_key", gradleLocalProperties(rootDir).getProperty("GOOGLE_API_KEY"))
      manifestPlaceholders["appName"] = "@string/app_name"
      manifestPlaceholders["appIcon"] = "@mipmap/ic_launcher"
      manifestPlaceholders["appRoundIcon"] = "@mipmap/ic_launcher"

      buildConfigField("String", "API_BASE_URL", Config.Environments.releaseBaseUrl)
      buildConfigField("String", "ROOM_DB", Config.Environments.roomDb)
    }
  }

  compileOptions {

    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  kotlinOptions {
    jvmTarget = "11"
  }

  dataBinding {
    isEnabled = true
  }
}

dependencies {
  implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
  //Room
  implementation(Libraries.roomVersion)
  kapt(Libraries.roomCompiler)
  implementation(Libraries.roomktx)
  implementation(Libraries.roomCommon)

  // Networking
  implementation(Libraries.retrofit)
  implementation(Libraries.retrofitConverter)
  implementation(Libraries.gson)
  implementation(Libraries.interceptor)
  implementation(Libraries.chuckLogging)

  // Utils
  implementation(Libraries.playServices)
  implementation(Libraries.localization)
  implementation(Libraries.multidex)
  implementation(Libraries.permissions)
  implementation(Libraries.gson)

// paging
  implementation(Libraries.paging_version)
  implementation(Libraries.paging_version_ktx)

  // Hilt
  implementation(Libraries.hilt)
  implementation(Libraries.firebase_platform)
  implementation(Libraries.firebase_messaging)
  kapt(Libraries.hiltDaggerCompiler)
  // Support
  implementation(Libraries.appCompat)
  implementation(Libraries.coreKtx)
  implementation(Libraries.androidSupport)

  // Arch Components
  implementation(Libraries.viewModel)
  implementation(Libraries.lifeData)
  implementation(Libraries.lifecycle)
  implementation(Libraries.viewModelState)

  // Kotlin Coroutines
  implementation(Libraries.coroutinesCore)
  implementation(Libraries.coroutinesAndroid)
//DATA STORE
  implementation(Libraries.datastore_preferences)

  // UI
  implementation(Libraries.materialDesign)
  implementation(Libraries.navigationFragment)
  implementation(Libraries.navigationUI)
  implementation(Libraries.loadingAnimations)
  implementation(Libraries.alerter)
  implementation(Libraries.coil)

  // Map
  implementation(Libraries.map)
  implementation(Libraries.playServicesLocation)
  implementation(Libraries.rxLocation)
  implementation(Libraries.firebase_messaging)

  //Ted bottom picker
  implementation(Libraries.ted_bottom_picker)

  //Pin code
  implementation(Libraries.pin_code)
  //smarteist
  implementation(Libraries.smartteist)
  //expandable
  implementation(Libraries.expandable)
  //circularprogressbar
  implementation(Libraries.circularprogressbar)

  //bottom Naviation
  implementation("com.zagori:bottomnavbar:1.0.2")

  //toasty
  implementation("com.github.GrenderG:Toasty:1.5.2")

  //country
  implementation("com.hbb20:ccp:2.6.0")

  //slider
  implementation("com.github.denzcoskun:ImageSlideshow:0.1.0")
  //chat
  implementation("com.cometchat:pro-android-chat-sdk:3.0.4")

  //exo-player
  implementation("com.google.android.exoplayer:exoplayer:2.10.0")

  //glide
  implementation("com.github.bumptech.glide:glide:4.11.0")
  annotationProcessor("com.github.bumptech.glide:compiler:4.11.0")

//  //stories
  implementation("com.github.bolaware:momentz:v2.0")

  //social-login
  /* Google sign in */
  implementation("com.google.android.gms:play-services-auth:20.1.0")
  /* For Facebook Sign in */
  implementation("com.facebook.android:facebook-login:13.0.0")

  //map
  implementation("com.google.android.gms:play-services-maps:18.0.2")
  implementation("com.google.android.gms:play-services-location:19.0.1")
  implementation("com.google.android.gms:play-services-places:17.0.0")
  implementation("com.google.maps:google-maps-services:0.2.5")
  implementation("com.google.android.libraries.places:places:2.6.0")
  implementation("com.google.maps.android:android-maps-utils:2.3.0")


  //exoPlayer
  implementation("com.google.android.exoplayer:exoplayer:2.15.1")
  implementation("com.google.android.exoplayer:exoplayer-core:2.15.1")
  implementation("com.google.android.exoplayer:exoplayer-dash:2.15.1")
  implementation("com.google.android.exoplayer:exoplayer-ui:2.15.1")

  //eventBus
  implementation("org.greenrobot:eventbus:3.3.1")

  //oneSignal
  implementation ("com.onesignal:OneSignal:[4.0.0, 4.99.99]")


  // Project Modules
  implementation(project(Config.Modules.prettyPopUp))
  implementation(project(":uikit-kotlin"))

}