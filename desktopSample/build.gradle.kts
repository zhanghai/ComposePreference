/*
 * Copyright 2026 Google LLC
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

plugins {
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.compose)
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

dependencies {
    implementation(project(":sample"))
}
