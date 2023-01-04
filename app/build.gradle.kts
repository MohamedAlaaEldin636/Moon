import android.annotation.SuppressLint
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

buildscript {
	repositories {
		google()
		mavenCentral()
	}
	dependencies {
		classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20")
	}
}

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
      buildConfigField("String", "SERVER_CLIENT_ID", Config.Environments.SERVER_CLIENT_ID)
    }

    signingConfigs {
      create("releaseConfig") {
        /*storeFile = file(rootProject.file("key"))
        storePassword = "123456"
        keyAlias = "My-Key"
        keyPassword = "123456"*/

	      /*storeFile = file(rootProject.file("GrandKey.jks"))
	      storePassword = "grand2017"
	      keyAlias = "grand"
	      keyPassword = "grand2017"*/

	      storeFile = file(rootProject.file("encrypt-key\\GrandKey"))
	      storePassword = "grand2017"
	      keyAlias = "grand"
	      keyPassword = "grand2017"
      }
    }

    getByName("release") {
      signingConfig = signingConfigs.getByName("releaseConfig")

      isMinifyEnabled = false
      isShrinkResources = false

//      resValue("string", "google_api_key", gradleLocalProperties(rootDir).getProperty("GOOGLE_API_KEY"))
      manifestPlaceholders["appName"] = "@string/app_name"
      manifestPlaceholders["appIcon"] = "@mipmap/ic_launcher"
      manifestPlaceholders["appRoundIcon"] = "@mipmap/ic_launcher"

      buildConfigField("String", "API_BASE_URL", Config.Environments.releaseBaseUrl)
      buildConfigField("String", "ROOM_DB", Config.Environments.roomDb)
      buildConfigField("String", "SERVER_CLIENT_ID", Config.Environments.SERVER_CLIENT_ID)
    }



  }

  compileOptions {

    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  kotlinOptions {
    jvmTarget = "11"
  }

//  dataBinding {
//    isEnabled = true
//  }
  buildFeatures {
    viewBinding = true
    dataBinding = true
  }
}

dependencies {
  implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
  //Room
  implementation(Libraries.roomVersion)
  implementation("com.google.firebase:firebase-auth-ktx:21.0.3")
    implementation("com.google.android.play:app-update-ktx:+")
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

  implementation("com.squareup.picasso:picasso:2.8")

  //crop
  implementation("com.github.akshaaatt:Cropper:1.00")

//  //stories
  implementation ("com.github.TOrnelas:SegmentedProgressBar:0.0.3")

  //webView
  implementation ("com.github.delight-im:Android-AdvancedWebView:v3.2.1")


  //social-login
  /* Google sign in */
  implementation("com.google.android.gms:play-services-auth:20.1.0")
  /* For Facebook Sign in */
  implementation("com.facebook.android:facebook-login:13.0.0")

  //map
//  implementation("com.google.android.gms:play-services-maps:18.0.2")
  implementation("com.google.android.gms:play-services-location:19.0.1")
  implementation("com.google.android.gms:play-services-places:17.0.0")
  implementation("com.google.maps:google-maps-services:0.2.5")
  implementation("com.google.android.libraries.places:places:2.6.0")
  implementation("com.google.maps.android:android-maps-utils:2.3.0")

  //firebase - signIn
  // Import the BoM for the Firebase platform
  implementation ("com.google.firebase:firebase-bom:29.3.1")
  // Declare the dependency for the Firebase Authentication library
  // When using the BoM, you don't specify versions in Firebase library dependencies
  implementation ("com.google.firebase:firebase-auth")
  // Also declare the dependency for the Google Play services library and specify its version
  implementation ("com.google.android.gms:play-services-auth:20.1.0")

  //exoPlayer
  implementation("com.google.android.exoplayer:exoplayer:2.15.1")
  implementation("com.google.android.exoplayer:exoplayer-core:2.15.1")
  implementation("com.google.android.exoplayer:exoplayer-dash:2.15.1")
  implementation("com.google.android.exoplayer:exoplayer-ui:2.15.1")


//  implementation ("com.github.MohammedAlaaMorsi:RangeSeekBar:1.0.6")
  implementation ("com.github.sephiroth74:RangeSeekBar:1.1.0")
  implementation ("com.crystal:crystalrangeseekbar:1.1.3")
  implementation ("com.github.Jay-Goo:RangeSeekBar:v2.0.1")

//  implementation ("com.github.Innovattic:range-seek-bar:1.0.0")
  implementation ("com.rizlee.view:rangeseekbar:1.0.0")

  implementation ("com.android.support:exifinterface:28.0.0")

  //eventBus
  implementation("org.greenrobot:eventbus:3.3.1")

  //oneSignal
  implementation ("com.onesignal:OneSignal:[4.0.0, 4.99.99]")


  // Project Modules
  implementation(project(Config.Modules.prettyPopUp))
  implementation(project(":uikit-kotlin"))

}