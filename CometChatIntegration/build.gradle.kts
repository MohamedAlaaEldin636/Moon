plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
}

android {
	namespace = "ma.ya.cometchatintegration"
	compileSdk = 33

	defaultConfig {
		minSdk = 23
		targetSdk = 33

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		consumerProguardFiles("consumer-rules.pro")
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}
	kotlinOptions {
		jvmTarget = "11"
	}
	buildFeatures {
		viewBinding = true
		dataBinding = true
		//compose = true
	}
}

dependencies {
	implementation("com.cometchat:pro-android-chat-sdk:3.0.13")

	implementation("com.google.android.material:material:1.8.0")

	implementation("androidx.fragment:fragment-ktx:1.5.5")
	implementation("androidx.appcompat:appcompat:1.6.1")

	implementation("com.google.code.gson:gson:2.10.1")

	implementation("com.google.android.exoplayer:exoplayer:2.18.5")

	testImplementation("junit:junit:4.13.2")
	androidTestImplementation("androidx.test.ext:junit:1.1.5")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}