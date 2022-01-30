object Libraries {

  // Support
  const val appCompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
  const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
  const val androidSupport = "androidx.legacy:legacy-support-v4:${Versions.supportVersion}"

  // Kotlin
  const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"

  // Java
  const val javaInject = "javax.inject:javax.inject:${Versions.javaInject}"

  // Arch Components
  const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
  const val lifeData = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
  const val lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
  const val viewModelState =
    "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.lifecycle}"
  const val paging_version = "androidx.paging:paging-runtime:${Versions.paging_version}"
  const val paging_version_ktx = "androidx.paging:paging-common-ktx:${Versions.paging_version}"
  const val roomVersion = "androidx.room:room-runtime:${Versions.roomVersion}"
  const val roomCompiler = "androidx.room:room-compiler:${Versions.roomVersion}"
  const val roomktx = "androidx.room:room-ktx:${Versions.roomVersion}"
  const val roomCommon = "androidx.room:room-common:${Versions.roomVersion}"

  // Kotlin Coroutines
  const val coroutinesCore =
    "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}"
  const val coroutinesAndroid =
    "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinCoroutines}"

  // DATA STORE
  const val datastore_preferences="androidx.datastore:datastore-preferences:${Versions.datastore_preferences}"
  // Networking
  const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
  const val retrofitConverter = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
  const val gson = "com.google.code.gson:gson:${Versions.gson}"
  const val interceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.interceptor}"
  const val chuckLogging = "com.readystatesoftware.chuck:library:${Versions.chuckLogging}"

  // UI
  const val materialDesign = "com.google.android.material:material:${Versions.materialDesign}"
  const val navigationFragment =
    "androidx.navigation:navigation-fragment-ktx:${Versions.androidNavigation}"
  const val navigationUI = "androidx.navigation:navigation-ui-ktx:${Versions.androidNavigation}"
  const val loadingAnimations = "com.airbnb.android:lottie:${Versions.loadingAnimations}"
  const val alerter = "com.github.tapadoo:alerter:${Versions.alerter}"
  const val coil = "io.coil-kt:coil:${Versions.coil}"

  // Utils
  const val playServices = "com.google.android.gms:play-services-auth:${Versions.playServices}"
  const val localization =
    "com.zeugmasolutions.localehelper:locale-helper-android:${Versions.localization}"
  const val multidex = "androidx.multidex:multidex:${Versions.multidex}"
  const val permissions = "com.afollestad.assent:core:${Versions.permissions}"

  // Hilt
  const val hilt = "com.google.dagger:hilt-android:${Versions.hiltVersion}"
  const val hiltDaggerCompiler = "com.google.dagger:hilt-android-compiler:${Versions.hiltVersion}"

  // Map
  const val map = "com.google.android.gms:play-services-maps:${Versions.map}"
  const val playServicesLocation =
    "com.google.android.gms:play-services-location:${Versions.playServicesLocation}"
  const val rxLocation = "com.github.ShabanKamell:RxLocation:${Versions.rxLocation}"

  //FireBase
//  implementation platform('com.google.firebase:firebase-bom:26.5.0')
//  implementation 'com.google.firebase:firebase-core'
//  implementation 'com.google.firebase:firebase-messaging'
  const val firebase_platform = "com.google.firebase:firebase-bom:${Versions.firebase}"
  const val firebase_core = "com.google.firebase:firebase-core"
  const val firebase_messaging = "com.google.firebase:firebase-messaging-ktx:${Versions.firebase}"

  //multi select media
  const val ted_bottom_picker = "gun0912.ted:tedbottompicker:${Versions.ted_bottom_picker}"

  //pin code
  const val pin_code = "com.chaos.view:pinview:${Versions.pin_code}"

  //smartteist
  const val smartteist = "com.github.smarteist:autoimageslider:${Versions.smarteist}"

  //Expandable
  const val expandable = "com.github.florent37:expansionpanel:${Versions.expandable}"

  //circularprogressbar
  const val circularprogressbar =
    "com.mikhaellopez:circularprogressbar:${Versions.circularprogressbar}"

}