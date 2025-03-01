plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    // -- Spotless
    id("com.diffplug.spotless") version "7.0.2"
    // -- Hilt
    id("kotlin-kapt")
    alias(libs.plugins.hilt)
    // -- JUnit
    id("de.mannodermaus.android-junit5") version "1.11.3.0"
}

android {
    namespace = "io.rafaelribeiro.spendless"
    compileSdk = 35

    defaultConfig {
        applicationId = "io.rafaelribeiro.spendless"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.foundation.layout.android)
    // -- Hilt
    kapt(libs.hilt.android.compiler)
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)
    // -- Room
    implementation(libs.room.runtime)
    implementation(libs.room.core)
    ksp(libs.room.compiler)
    // -- Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.junit.jupiter.params)
    testImplementation(libs.assertk)
    testImplementation(libs.kotlinx.coroutines.test)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}

spotless {
    format("misc") {
        target(".gitignore", "*.md", "*.yml", "*.properties")

        trimTrailingWhitespace()
        leadingSpacesToTabs()
        endWithNewline()
    }

    kotlin {
        target("**/*.kt")
        ktlint("1.5.0")
        trimTrailingWhitespace()
        leadingSpacesToTabs()
        endWithNewline()
    }
}
