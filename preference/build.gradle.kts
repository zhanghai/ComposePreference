/*
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose)
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.plugin.compose)
    alias(libs.plugins.mavenPublish)
}

kotlin {
    // From AndroidX and Compose Multiplatform
    // @see
    // https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:collection/collection/build.gradle
    // @see
    // https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:datastore/datastore/build.gradle
    // @see
    // https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:buildSrc/private/src/main/kotlin/androidx/build/AndroidXMultiplatformExtension.kt
    // @see
    // https://github.com/JetBrains/compose-multiplatform/blob/master/components/resources/library/build.gradle.kts
    androidTarget { publishLibraryVariants("release") }
    iosArm64()
    iosSimulatorArm64()
    iosX64()
    js { browser() }
    jvm()
    // linuxArm64()
    // linuxX64()
    macosArm64()
    macosX64()
    // mingwX64()
    // tvosArm64()
    // tvosSimulatorArm64()
    // tvosX64()
    @OptIn(ExperimentalWasmDsl::class) wasmJs { browser() }
    // watchosArm32()
    // watchosArm64()
    // watchosDeviceArm64()
    // watchosSimulatorArm64()
    // watchosX64()

    applyDefaultHierarchyTemplate()
    sourceSets {
        val commonJvmMain by creating { dependsOn(commonMain.get()) }
        val commonJvmTest by creating { dependsOn(commonTest.get()) }
        androidMain { dependsOn(commonJvmMain) }
        androidUnitTest { dependsOn(commonJvmTest) }
        jvmMain { dependsOn(commonJvmMain) }
        jvmTest { dependsOn(commonJvmTest) }

        val commonJsMain by creating { dependsOn(commonMain.get()) }
        val commonJsTest by creating { dependsOn(commonTest.get()) }
        jsMain { dependsOn(commonJsMain) }
        jsTest { dependsOn(commonJsTest) }
        wasmJsMain { dependsOn(commonJsMain) }
        wasmJsTest { dependsOn(commonJsTest) }

        val nonAndroidAppleMain by creating { dependsOn(commonMain.get()) }
        val nonAndroidAppleTest by creating { dependsOn(commonTest.get()) }
        jvmMain { dependsOn(nonAndroidAppleMain) }
        jvmTest { dependsOn(nonAndroidAppleTest) }
        commonJsMain.dependsOn(nonAndroidAppleMain)
        commonJsTest.dependsOn(nonAndroidAppleTest)

        commonMain {
            dependencies {
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation(compose.material3)
                implementation(libs.jetbrains.androidx.lifecycle.runtimeCompose)
            }
        }
        commonTest { dependencies { implementation(libs.kotlin.test) } }
        androidMain { dependencies { implementation(libs.androidx.compose.ui.toolingPreview) } }
        nonAndroidAppleMain.dependencies { implementation(libs.kotlinx.serialization.json) }
    }
}

android {
    namespace = "me.zhanghai.compose.preference"
    buildToolsVersion = libs.versions.android.buildTools.get()
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        consumerProguardFiles("proguard-rules.pro")
    }
    buildFeatures { compose = true }
    packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }
    buildTypes { release { isMinifyEnabled = false } }
}

dependencies {
    debugImplementation(libs.androidx.compose.ui.testManifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
