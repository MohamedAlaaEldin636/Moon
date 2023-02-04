// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
  repositories {
    google()
    mavenCentral()
  }
  dependencies {
    classpath(Config.Dependencies.gradleVersion)
    classpath(Config.Dependencies.kotlin)
    classpath(Config.Dependencies.navigationSafeArgs)
    classpath(Config.Dependencies.hilt)
    classpath(Config.Dependencies.google_services)
    classpath ("gradle.plugin.com.onesignal:onesignal-gradle-plugin:[0.12.10, 0.99.99]")

  }
}

plugins {
  id(Config.Plugins.ktLint) version Versions.ktLint
	id("org.jetbrains.kotlin.android") version "1.7.20" apply false
}

subprojects {
  apply(plugin = Config.Plugins.ktLint) // To apply ktLint to all included modules

  repositories {
    mavenCentral()
  }

  configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    debug.set(true)
  }
}

allprojects {
  repositories {
    google()
    mavenCentral()
    jcenter()
    maven(url = Config.Dependencies.jitPackURL)
    maven(url = "https://dl.cloudsmith.io/public/cometchat/cometchat-pro-android/maven/")

  }
}

tasks.register("clean", Delete::class) {
  delete(rootProject.buildDir)
}

tasks.register("installGitHooks", Copy::class) {
  from("${rootProject.rootDir}/pre-commit")
  into("${rootProject.rootDir}/.git/hooks")
}