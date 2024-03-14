@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.org.jetbrains.kotlin)
    alias(libs.plugins.org.jmailen.kotlinter)
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "dev.stupak.navigation"
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
    implementation(libs.navigation.ui.ktx)
    implementation(libs.navigation.fragment.ktx)
}