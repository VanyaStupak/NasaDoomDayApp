plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.org.jetbrains.kotlin)
    alias(libs.plugins.com.google.dagger.hilt.android)
    alias(libs.plugins.org.jmailen.kotlinter)
    kotlin("kapt")
}

android {
    namespace = "dev.stupak.domain"
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
    implementation(project(":data:repository"))
    implementation(project(":data:database"))
    implementation(project(":data:paging"))
    implementation(project(":data:network"))
    implementation(project(":data:source"))
    implementation(project(":data:local"))
    implementation(libs.dagger.hilt.android)
    implementation(libs.room.paging)
    implementation(libs.room)
    implementation(libs.paging)
    kapt(libs.com.google.dagger.hilt.compiler)
    kapt(libs.androidx.hilt.compiler)
}