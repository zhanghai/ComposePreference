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

import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.android.kotlinMultiplatformLibrary)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.plugin.compose)
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
    androidLibrary {
        namespace = "me.zhanghai.compose.preference.sample"
        buildToolsVersion = libs.versions.android.buildTools.get()
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        androidResources { enable = true }
        optimization {
            consumerKeepRules.apply {
                publish = true
                file("consumer-proguard-rules.pro")
            }
        }
        packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }
    }
    iosArm64()
    iosSimulatorArm64()
    js {
        browser()
        binaries.executable()
    }
    jvm()
    // linuxArm64()
    // linuxX64()
    macosArm64()
    // mingwX64()
    // tvosArm64()
    // tvosSimulatorArm64()
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }
    // watchosArm32()
    // watchosArm64()
    // watchosDeviceArm64()
    // watchosSimulatorArm64()

    applyDefaultHierarchyTemplate()
    sourceSets {
        val commonJvmMain by creating { dependsOn(commonMain.get()) }
        val commonJvmTest by creating { dependsOn(commonTest.get()) }
        androidMain { dependsOn(commonJvmMain) }
        androidUnitTest { dependsOn(commonJvmTest) }
        jvmMain { dependsOn(commonJvmMain) }
        jvmTest { dependsOn(commonJvmTest) }

        commonMain {
            dependencies {
                implementation(project(":preference"))
                // TODO: Migrate away from deprecated dependency aliases once they have a BOM for
                //  compatible versions.
                implementation(compose.components.resources)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(compose.preview)
            }
        }
        commonTest { dependencies { implementation(libs.kotlin.test) } }
        androidMain {
            dependencies {
                implementation(libs.androidx.activity.compose)
                implementation(libs.kotlinx.coroutines.android)
            }
        }
        jvmMain {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.kotlinx.coroutines.swing)
            }
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.androidx.compose.ui.testManifest)
    androidRuntimeClasspath(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "me.zhanghai.compose.preference.sample.MainKt"
        nativeDistributions {
            packageName = "me.zhanghai.compose.preference.sample"
            packageVersion = providers.gradleProperty("VERSION_NAME").get()
            targetFormats(TargetFormat.Deb, TargetFormat.Dmg, TargetFormat.Msi)
        }
    }
}
