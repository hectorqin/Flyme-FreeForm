// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath(libs.gradle.plugin)
        // 选用加解密算法库，默认实现了xor算法，也可以使用自己的加解密库。
        classpath(libs.stringfog.xor)
        classpath(libs.refine.gradle.plugin)
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ksp)  apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.refine) apply false
}

task<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}
