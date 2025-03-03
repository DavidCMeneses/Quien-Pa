plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrainsKotlinSerialization)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "co.edu.unal.qnpa"
    compileSdk = 35

    defaultConfig {
        applicationId = "co.edu.unal.qnpa"
        minSdk = 25
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
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
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

    implementation (libs.retrofit)
    implementation (libs.converter.gson)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.maps.compose)
    implementation(libs.play.services.maps)

    implementation(libs.coil.compose)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.storage.ktx)

    implementation(libs.datetime)
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    implementation(libs.play.services.location)
    implementation(libs.androidx.runtime) // Para collectAsState

    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.runtime.livedata) // Si usas LiveData

    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}