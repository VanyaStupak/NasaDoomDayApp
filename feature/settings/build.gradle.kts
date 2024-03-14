plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.org.jetbrains.kotlin)
    alias(libs.plugins.com.google.dagger.hilt.android)
    alias(libs.plugins.org.jmailen.kotlinter)
    kotlin("kapt")
}

android {
    namespace = "dev.stupak.settings"
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
    buildFeatures{
        viewBinding = true
    }
}

dependencies {
    implementation(project(":core:platform"))
    implementation(project(":core:ui"))
    implementation(project(":core:navigation"))
    implementation(project(":domain"))
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.android.material)
    implementation(libs.dagger.hilt.android)
    kapt(libs.com.google.dagger.hilt.compiler)
    kapt(libs.androidx.hilt.compiler)
    implementation(libs.viewbindingpropertydelegate)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    implementation(libs.datastore)
}