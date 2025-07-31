plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.freeform.hiddenapi"
    compileSdk = 36

    defaultConfig {
        minSdk = 30
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        aidl = true
    }
}

dependencies {
    annotationProcessor(libs.refine.annotation.processor)
    compileOnly(libs.androidx.annotation)
    compileOnly(libs.refine.annotation)
}