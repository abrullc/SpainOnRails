plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.abrullc.spainonrails"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.abrullc.spainonrails"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    viewBinding {
        enable  = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // LiveData
    implementation(libs.androidx.lifecycle.livedata.ktx)

    // Retrofit
    implementation(libs.retrofit)

    // Gson
    implementation(libs.converter.gson)

    // Lifecycle Runtime
    implementation(libs.androidx.lifecycle.runtime.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}