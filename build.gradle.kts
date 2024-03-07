// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.org.jetbrains.kotlin) apply false
    alias(libs.plugins.com.google.dagger.hilt.android) apply false
    id("com.android.library") version "8.2.2" apply false
}
buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath(libs.navigation.safe.args.gradle.plugin)
    }
}