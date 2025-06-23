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

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.float
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

internal object PreferencesJson {
    fun encodeToString(preferences: Preferences): String =
        Json.encodeToString(
            buildJsonObject {
                for ((key, value) in preferences.asMap()) {
                    val jsonValue =
                        when (value) {
                            is Boolean -> JsonPrimitive(value)
                            is Int -> JsonPrimitive(value)
                            is Float -> JsonPrimitive(value)
                            is String -> JsonPrimitive(value)
                            is Set<*> ->
                                @Suppress("UNCHECKED_CAST")
                                JsonArray((value as Set<String>).map { JsonPrimitive(it) })
                            else ->
                                throw IllegalArgumentException("Unsupported type for value $value")
                        }
                    put(key, jsonValue)
                }
            }
        )

    fun decodeFromString(string: String): Preferences =
        MapPreferences(
            buildMap {
                for ((key, jsonValue) in Json.parseToJsonElement(string).jsonObject) {
                    val value: Any =
                        when (jsonValue) {
                            is JsonPrimitive ->
                                when {
                                    jsonValue is JsonNull -> continue
                                    jsonValue.isString -> jsonValue.content
                                    else ->
                                        jsonValue.booleanOrNull
                                            ?: jsonValue.intOrNullStrict
                                            ?: jsonValue.float
                                }
                            is JsonArray ->
                                jsonValue.mapTo(mutableSetOf()) { it.jsonPrimitive.contentOrNull }
                            else -> error("Unsupported JSON value $jsonValue")
                        }
                    put(key, value)
                }
            }
        )

    private val JsonPrimitive.intOrNullStrict: Int?
        get() = content.toIntOrNull()
}
