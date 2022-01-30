plugins {
  id(Config.Plugins.androidLibrary)
  id(Config.Plugins.kotlinAndroid)
  id(Config.Plugins.kotlinKapt)
}

android {
  compileSdk = Config.AppConfig.compileSdkVersion

  defaultConfig {
    minSdk = Config.AppConfig.minSdkVersion
    targetSdk = Config.AppConfig.compileSdkVersion
  }

  buildTypes {
    getByName("release") {
      proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
    }
  }
}

dependencies {
  //Room
  implementation(Libraries.roomVersion)
  kapt(Libraries.roomCompiler)

  // Kotlin Coroutines
  implementation(Libraries.coroutinesCore)
  implementation(Libraries.coroutinesAndroid)
//DATA STORE
  implementation(Libraries.datastore_preferences)

  // Networking
  implementation(Libraries.retrofit)
  implementation(Libraries.gson)

  implementation(Libraries.javaInject)
//  implementation(Libraries.paging_version)
//  implementation(Libraries.paging_version_ktx)

  // Project Modules
  implementation(project(Config.Modules.domain))
}