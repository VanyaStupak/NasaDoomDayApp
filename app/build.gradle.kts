plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin)
    alias(libs.plugins.com.google.dagger.hilt.android)
    alias(libs.plugins.com.google.firebase.crashlytics)
    alias(libs.plugins.org.jmailen.kotlinter)
    id("com.google.gms.google-services")
    kotlin("kapt")
}

android {
    namespace = "dev.stupak.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "dev.stupak.app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
    implementation(project(":core:ui"))
    implementation(project(":core:platform"))
    implementation(project(":core:navigation"))
    implementation(project(":feature:host"))
    implementation(project(":feature:asteroids"))
    implementation(project(":feature:favourites"))
    implementation(project(":feature:comparison"))
    implementation(project(":feature:details"))
    implementation(project(":feature:onboarding"))
    implementation(project(":feature:settings"))
    implementation(libs.dagger.hilt.android)
    kapt(libs.com.google.dagger.hilt.compiler)
    kapt(libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.work)
    implementation(libs.work.runtime.ktx)
    implementation(libs.firebase.crashlytics)

}