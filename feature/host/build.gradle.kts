@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.org.jetbrains.kotlin)
    alias(libs.plugins.com.google.dagger.hilt.android)
    kotlin("kapt")
}

android {
    namespace = "dev.stupak.host"
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":core:platform"))
    implementation(project(":core:ui"))
    implementation(project(":core:navigation"))
    implementation(project(":data:worker"))
    implementation(project(":feature:favourites"))
    implementation(project(":feature:asteroids"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:details"))
    implementation(libs.core.ktx)
    implementation(libs.work.runtime.ktx)
    implementation(libs.appcompat)
    implementation(libs.android.material)
    implementation(libs.navigation.ui.ktx)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.viewbindingpropertydelegate)
    implementation(libs.dagger.hilt.android)
    kapt(libs.com.google.dagger.hilt.compiler)
    kapt(libs.androidx.hilt.compiler)
}