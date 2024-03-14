plugins {
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.org.jetbrains.kotlin) apply false
    alias(libs.plugins.com.google.dagger.hilt.android) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.org.jmailen.kotlinter) apply false
    alias(libs.plugins.com.google.firebase.crashlytics) apply false
}
buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath(libs.navigation.safe.args.gradle.plugin)
        classpath("com.google.gms:google-services:4.4.1")
        classpath(libs.build.gradle)
        classpath(libs.firebase.crashlytics.gradle)
    }
}