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

import kotlin.js.json

internal actual fun decodePreferencesFromString(string: String): Preferences =
    MapPreferences(
        buildMap {
            js("Object").entries(JSON.parse(string)).forEach { entry ->
                val key = entry[0].unsafeCast<String>()
                val jsonValue = entry[1]
                val value =
                    if (jsonValue is Array<String>) {
                        jsonValue.unsafeCast<Array<String>>().toSet()
                    } else {
                        jsonValue.unsafeCast<Any>()
                    }
                put(key, value)
            }
            Unit
        }
    )

internal actual fun encodePreferencesToString(preferences: Preferences): String =
    JSON.stringify(
        json().apply {
            preferences.asMap().map { (key, value) ->
                val jsonValue = if (value is Set<*>) value.toTypedArray() else value
                set(key, jsonValue)
            }
        }
    )
