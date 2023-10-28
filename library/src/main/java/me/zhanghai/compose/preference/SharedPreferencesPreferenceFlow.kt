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
import androidx.core.content.edit
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
fun SharedPreferences.getPreferenceFlow(): MutableStateFlow<Preferences> =
    MutableStateFlow(preferences).also {
        GlobalScope.launch { it.drop(1).collect { preferences = it } }
    }

private var SharedPreferences.preferences: Preferences
    get() = @Suppress("UNCHECKED_CAST") MapPreferences(all as Map<String, Any>)
    set(value) {
        edit {
            clear()
            value.asMap().forEach { (key, value) ->
                when (value) {
                    is Boolean -> putBoolean(key, value)
                    is Int -> putInt(key, value)
                    is Long -> putLong(key, value)
                    is Float -> putFloat(key, value)
                    is String -> putString(key, value)
                    is Set<*> -> @Suppress("UNCHECKED_CAST") putStringSet(key, value as Set<String>)
                    else -> throw IllegalArgumentException("Unsupported type for value $value")
                }
            }
        }
    }
