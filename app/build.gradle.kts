import com.github.megatronking.stringfog.plugin.StringFogExtension

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.refine)
    id("stringfog")
}

android {
    namespace = "com.sunshine.freeform"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.sunshine.freeform"
        minSdk = 30
        targetSdk = 36
        versionCode = 4000
        versionName = "4.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    
    buildFeatures {
        buildConfig = true
        viewBinding = true
        aidl = true
    }

    buildTypes {
        release {
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    kotlinOptions {
        jvmTarget = "17"
    }
}

configurations.all {
    exclude(group = "androidx.appcompat", module = "appcompat")
    exclude(group = "androidx.preference", module = "preference")
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit.ext)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.androidx.lifecycle.viewmodel)

    compileOnly(libs.xposed.api)

    implementation(libs.rikkax.appcompat)
    implementation(libs.rikkax.borderview)
    implementation(libs.rikkax.recyclerview)

    implementation(libs.shizuku.api)
    implementation(libs.shizuku.provider)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    implementation(libs.rikkax.preference.simplemenu)

    implementation(libs.lottie)

    implementation(libs.glide)

    implementation(libs.hiddenapibypass)

    implementation(libs.stringfog.xor)

    implementation(libs.refine.runtime)

    implementation(libs.ezxhelper)

    implementation(libs.preference.material3)

    implementation(libs.tinypinyin)

    compileOnly(project(":hidden-api"))
}

configure<StringFogExtension> {
    implementation = "com.github.megatronking.stringfog.xor.StringFogImpl"
    enable = true
    fogPackages = arrayOf("com.sunshine.freeform")
}
