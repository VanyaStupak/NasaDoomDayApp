plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.org.jetbrains.kotlin)
    alias(libs.plugins.com.google.dagger.hilt.android)
    alias(libs.plugins.org.jmailen.kotlinter)
    kotlin("kapt")
}

android {
    namespace = "dev.stupak.network"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter)
    implementation(libs.okHttp)
    implementation(libs.dagger.hilt.android)
    kapt(libs.com.google.dagger.hilt.compiler)
    kapt(libs.androidx.hilt.compiler)
}