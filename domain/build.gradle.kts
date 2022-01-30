plugins {
  id(Config.Plugins.kotlinJvm)
  id(Config.Plugins.kotlinKapt)
}

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
  // Kotlin Coroutines
  implementation(Libraries.coroutinesCore)
  implementation(Libraries.coroutinesAndroid)
  implementation(Libraries.retrofitConverter)
  implementation(Libraries.gson)
  implementation(Libraries.javaInject)
  implementation(Libraries.paging_version)
  implementation(Libraries.paging_version_ktx)
  implementation(Libraries.roomVersion)
  kapt(Libraries.roomCompiler)
  implementation(Libraries.roomCommon)
  implementation(Libraries.roomktx)


}