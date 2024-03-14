@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.org.jetbrains.kotlin)
    alias(libs.plugins.com.google.dagger.hilt.android)
    alias(libs.plugins.org.jmailen.kotlinter)
    kotlin("kapt")
}

android {
    namespace = "dev.stupak.comparison"
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
    implementation(project(":core:ui"))
    implementation(project(":core:platform"))
    implementation(project(":core:navigation"))
    implementation(project(":domain"))
    implementation(libs.dagger.hilt.android)
    implementation(libs.viewpager2)
    kapt(libs.com.google.dagger.hilt.compiler)
    kapt(libs.androidx.hilt.compiler)
    implementation(libs.room.paging)
    implementation(libs.room)
    implementation(libs.paging)
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.android.material)
    implementation(libs.viewbindingpropertydelegate)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
}