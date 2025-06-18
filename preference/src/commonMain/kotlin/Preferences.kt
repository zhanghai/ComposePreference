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

interface Preferences {
    operator fun <T> get(key: String): T?

    fun asMap(): Map<String, Any>

    fun toPreferences(): Preferences = toMutablePreferences()

    fun toMutablePreferences(): MutablePreferences
}

internal class MapPreferences(private val map: Map<String, Any> = emptyMap()) : Preferences {
    @Suppress("UNCHECKED_CAST") override fun <T> get(key: String): T? = map[key] as T?

    override fun asMap(): Map<String, Any> = map

    override fun toMutablePreferences(): MutablePreferences =
        MapMutablePreferences(map.toMutableMap())
}

interface MutablePreferences : Preferences {
    operator fun <T> set(key: String, value: T?)

    fun remove(key: String) {
        set(key, null)
    }

    operator fun minusAssign(key: String) {
        remove(key)
    }

    fun clear()
}

internal class MapMutablePreferences(private val map: MutableMap<String, Any> = mutableMapOf()) :
    MutablePreferences {
    @Suppress("UNCHECKED_CAST") override fun <T> get(key: String): T? = map[key] as T?

    override fun asMap(): Map<String, Any> = map

    override fun toMutablePreferences(): MutablePreferences =
        MapMutablePreferences(map.toMutableMap())

    override fun <T> set(key: String, value: T?) {
        if (value != null) {
            map[key] = value
        } else {
            map -= key
        }
    }

    override fun clear() {
        map.clear()
    }
}
