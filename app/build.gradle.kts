import com.github.megatronking.stringfog.plugin.StringFogExtension

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
    id("stringfog")
    id("dev.rikka.tools.refine")
}

android {
    namespace = "com.sunshine.freeform"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.sunshine.freeform"
        minSdk = 28
        targetSdk = 36
        versionCode = 3210
        versionName = "3.2.1"
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
    implementation("androidx.core:core-ktx:1.16.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.9.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.9.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.1")

    compileOnly("de.robv.android.xposed:api:82")

    implementation("dev.rikka.rikkax.appcompat:appcompat:1.5.0.1")
    implementation("dev.rikka.rikkax.widget:borderview:1.1.0")
    implementation("dev.rikka.rikkax.recyclerview:recyclerview-ktx:1.3.1")

    val shizukuVersion = "13.1.5"
    implementation("dev.rikka.shizuku:api:$shizukuVersion")
    implementation("dev.rikka.shizuku:provider:$shizukuVersion")

    implementation("androidx.room:room-runtime:2.7.1")
    implementation("androidx.room:room-ktx:2.7.1")
    ksp("androidx.room:room-compiler:2.7.1")

    implementation("dev.rikka.rikkax.preference:simplemenu-preference:1.0.3")

    implementation("com.airbnb.android:lottie:6.6.6")

    implementation("com.github.bumptech.glide:glide:4.16.0")

    implementation("org.lsposed.hiddenapibypass:hiddenapibypass:6.1")

    implementation("com.github.megatronking.stringfog:xor:5.0.0")

    implementation("dev.rikka.tools.refine:runtime:4.4.0")

    implementation("com.github.kyuubiran:EzXHelper:1.0.3")

    implementation("com.github.halifox:androidx.preference.material3:1.2.1-alpha07")

    implementation("com.github.promeg:tinypinyin:2.0.3")

    compileOnly(project(":hidden-api"))
}

configure<StringFogExtension> {
    implementation = "com.github.megatronking.stringfog.xor.StringFogImpl"
    enable = true
    fogPackages = arrayOf("com.sunshine.freeform")
}
