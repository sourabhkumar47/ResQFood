plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    //plugins for firebase
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    kotlin("kapt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.project.resqfood"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.project.resqfood"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }

    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
        }

        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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
    implementation(libs.firebase.auth)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.googleid)
    implementation(libs.androidx.material3.android)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.androidx.benchmark.traceprocessor.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    // LiveData
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    //Adding Firebase BOM
    implementation(platform(libs.firebase.bom))

    //Adding Firebase Analytics
    implementation(libs.google.firebase.analytics)

    //Adding dependencies for firebase authentication
    implementation(libs.firebase.auth.ktx)

    //Adding dependencies for google play sign in
    implementation(libs.play.services.auth)

    // Google Identity Services Library
    implementation(libs.play.services.identity)

    //Extended list of material 3 icons
    implementation(libs.androidx.material.icons.extended)
    //Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.work)
    kapt(libs.androidx.hilt.compiler)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.hilt.navigation.compose)


    //Dependencies for Google Sign In suing credential manager
    implementation(libs.androidx.credentials)

    // optional - needed for credentials support from play services, for devices running
    // Android 13 and below.
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.coil.compose)
    implementation(libs.firebase.firestore)
    // Timber for logging
    implementation(libs.timber)
//    Material 3 Core
    implementation(libs.core)
    // CLOCK
    implementation(libs.clock)
    //Lottie
    implementation( libs.lottie.compose)

    //navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    //pager
    implementation (libs.accompanist.pager)
    //google map
    implementation(libs.google.maps)
}