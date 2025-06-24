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
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

@Composable
public actual fun createDefaultPreferenceFlow(): MutableStateFlow<Preferences> =
    localStorage.createPreferenceFlow()

private fun Storage.createPreferenceFlow(): MutableStateFlow<Preferences> =
    MutableStateFlow(preferences).also {
        @OptIn(DelicateCoroutinesApi::class)
        GlobalScope.launch(Dispatchers.Main.immediate) { it.drop(1).collect { preferences = it } }
    }

private const val PreferencesKey = "me.zhanghai.compose.preference"

private var Storage.preferences: Preferences
    get() =
        localStorage.getItem(PreferencesKey)?.let { decodePreferencesFromString(it) }
            ?: MapPreferences()
    set(value) {
        localStorage.setItem(PreferencesKey, encodePreferencesToString(value))
    }

internal expect fun decodePreferencesFromString(string: String): Preferences

internal expect fun encodePreferencesToString(preferences: Preferences): String
