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

package me.zhanghai.compose.preference

import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

@Composable
public actual fun createDefaultPreferenceFlow(): MutableStateFlow<Preferences> {
    val view = LocalView.current
    if (view.isInEditMode) {
        return MutableStateFlow(MapPreferences())
    }
    val context = LocalContext.current
    @Suppress("DEPRECATION")
    return createPreferenceFlow(
        android.preference.PreferenceManager.getDefaultSharedPreferences(context)
    )
}

public fun createPreferenceFlow(
    sharedPreferences: SharedPreferences
): MutableStateFlow<Preferences> =
    MutableStateFlow(sharedPreferences.preferences).also {
        @OptIn(DelicateCoroutinesApi::class)
        GlobalScope.launch(Dispatchers.Main.immediate) {
            it.drop(1).collect { sharedPreferences.preferences = it }
        }
    }

private var SharedPreferences.preferences: Preferences
    get() = @Suppress("UNCHECKED_CAST") MapPreferences(all as Map<String, Any>)
    set(value) {
        edit {
            clear()
            for ((key, mapValue) in value.asMap()) {
                when (mapValue) {
                    is Boolean -> putBoolean(key, mapValue)
                    is Int -> putInt(key, mapValue)
                    is Long -> putLong(key, mapValue)
                    is Float -> putFloat(key, mapValue)
                    is String -> putString(key, mapValue)
                    is Set<*> ->
                        @Suppress("UNCHECKED_CAST") putStringSet(key, mapValue as Set<String>)
                    else -> throw IllegalArgumentException("Unsupported type for value $mapValue")
                }
            }
        }
    }

private inline fun SharedPreferences.edit(action: SharedPreferences.Editor.() -> Unit) {
    edit().apply {
        action()
        apply()
    }
}
