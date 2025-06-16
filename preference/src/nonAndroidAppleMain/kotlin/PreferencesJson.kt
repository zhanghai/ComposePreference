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
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.float
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put

internal object PreferencesJson {
    private const val KEY_TYPE = "type"
    private const val KEY_VALUE = "value"

    fun encodeToString(preferences: Preferences): String =
        Json.encodeToString(
            buildJsonObject {
                for ((key, value) in preferences.asMap()) {
                    val valueType: ValueType
                    val valueValue: JsonElement
                    when (value) {
                        is Boolean -> {
                            valueType = ValueType.Boolean
                            valueValue = JsonPrimitive(value)
                        }
                        // Put Float first so that we encode in a less confusing way on Kotlin/JS
                        // where both Int and Float are JsNumber.
                        is Float -> {
                            valueType = ValueType.Float
                            valueValue = JsonPrimitive(value)
                        }
                        is Int -> {
                            valueType = ValueType.Int
                            valueValue = JsonPrimitive(value)
                        }
                        is Long -> {
                            valueType = ValueType.Long
                            val highBits = (value ushr 32).toInt()
                            val lowBits = value.toInt()
                            valueValue =
                                JsonArray(listOf(JsonPrimitive(highBits), JsonPrimitive(lowBits)))
                        }
                        is String -> {
                            valueType = ValueType.String
                            valueValue = JsonPrimitive(value)
                        }
                        is Set<*> -> {
                            @Suppress("UNCHECKED_CAST")
                            value as Set<String>
                            valueType = ValueType.StringSet
                            valueValue = JsonArray(value.map { JsonPrimitive(it) })
                        }
                        else -> throw IllegalArgumentException("Unsupported type for value $value")
                    }
                    val valueElement = buildJsonObject {
                        put(KEY_TYPE, valueType.name)
                        put(KEY_VALUE, valueValue)
                    }
                    put(key, valueElement)
                }
            }
        )

    fun decodeFromString(string: String): Preferences =
        MapPreferences(
            buildMap {
                for ((key, jsonValue) in Json.parseToJsonElement(string).jsonObject) {
                    val valueElement = jsonValue.jsonObject
                    val valueType =
                        valueElement.require(KEY_TYPE).jsonPrimitive.string.let {
                            ValueType.valueOf(it)
                        }
                    val valueValue = valueElement.require(KEY_VALUE)
                    val value: Any =
                        when (valueType) {
                            ValueType.Boolean -> valueValue.jsonPrimitive.boolean
                            ValueType.Int -> valueValue.jsonPrimitive.int
                            ValueType.Long -> {
                                val valueArray = valueValue.jsonArray
                                val highBits = valueArray[0].jsonPrimitive.int
                                val lowBits = valueArray[1].jsonPrimitive.int
                                (highBits.toLong() shl 32) or (lowBits.toLong() and 0xFFFFFFFFL)
                            }
                            ValueType.Float -> valueValue.jsonPrimitive.float
                            ValueType.String -> valueValue.jsonPrimitive.string
                            ValueType.StringSet -> {
                                valueValue.jsonArray.mapTo(mutableSetOf()) {
                                    it.jsonPrimitive.string
                                }
                            }
                        }
                    put(key, value)
                }
            }
        )

    private fun JsonObject.require(key: String): JsonElement =
        requireNotNull(get(key)) { "Key \"$key\" is missing" }

    private val JsonPrimitive.string: String
        get() {
            require(isString) { "$this is not a string" }
            return content
        }

    enum class ValueType {
        Boolean,
        Int,
        Long,
        Float,
        String,
        StringSet,
    }
}
