/*
 * Copyright 2025 Google LLC
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

package me.zhanghai.compose.preference

import androidx.compose.runtime.Composable
import java.util.prefs.Preferences as JavaPreferences
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

private const val PreferenceNodePropertyName = "me.zhanghai.compose.preference.node"
private const val SunJavaCommandPropertyName = "sun.java.command"
private const val PackagePreferenceNodeName = "preference"

@Composable
actual fun createDefaultPreferenceFlow(): MutableStateFlow<Preferences> {
    val nodeName = System.getProperty(PreferenceNodePropertyName)
    val javaPreferences =
        if (nodeName != null) {
            JavaPreferences.userRoot().node(nodeName)
        } else {
            val command = System.getProperty(SunJavaCommandPropertyName)
            checkNotNull(command) {
                "Missing system property $SunJavaCommandPropertyName for inferring main class name -" +
                    " or please set $PreferenceNodePropertyName instead"
            }
            val mainClass = Class.forName(command.split(' ')[0])
            JavaPreferences.userNodeForPackage(mainClass).node(PackagePreferenceNodeName)
        }
    return javaPreferences.createPreferenceFlow()
}

private fun JavaPreferences.createPreferenceFlow(): MutableStateFlow<Preferences> =
    MutableStateFlow(preferences).also {
        @OptIn(DelicateCoroutinesApi::class)
        GlobalScope.launch(Dispatchers.Main.immediate) { it.drop(1).collect { preferences = it } }
    }

private const val PreferencesKey = "preferences"

private var JavaPreferences.preferences: Preferences
    get() =
        get(PreferencesKey, null)?.let { PreferencesJson.decodeFromString(it) } ?: MapPreferences()
    set(value) {
        put(PreferencesKey, PreferencesJson.encodeToString(value))
    }
