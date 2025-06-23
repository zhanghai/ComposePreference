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
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import platform.Foundation.NSArray
import platform.Foundation.NSBundle
import platform.Foundation.NSNumber
import platform.Foundation.NSString
import platform.Foundation.NSUserDefaults

@Composable
public actual fun createDefaultPreferenceFlow(): MutableStateFlow<Preferences> =
    NSUserDefaults.standardUserDefaults.createPreferenceFlow()

private fun NSUserDefaults.createPreferenceFlow(): MutableStateFlow<Preferences> =
    MutableStateFlow(preferences).also {
        @OptIn(DelicateCoroutinesApi::class)
        GlobalScope.launch(Dispatchers.Main.immediate) { it.drop(1).collect { preferences = it } }
    }

private var NSUserDefaults.preferences: Preferences
    get() {
        @Suppress("UNCHECKED_CAST")
        val dictionary =
            persistentDomainForName(NSBundle.mainBundle.bundleIdentifier!!) as Map<String, Any>
        val map =
            dictionary.mapValues { (_, value) ->
                when (value) {
                    is NSNumber ->
                        // @see
                        // https://developer.apple.com/library/archive/documentation/Cocoa/Conceptual/ObjCRuntimeGuide/Articles/ocrtTypeEncodings.html
                        @OptIn(ExperimentalForeignApi::class)
                        when (val objCType = value.objCType?.toKString()) {
                            "c", "C", "B" -> value as Boolean
                            "i", "s", "l", "q", "I", "S", "Q" -> value as Int
                            "f", "d" -> value as Float
                            else ->
                                throw IllegalArgumentException(
                                    "Unsupported objCType \"$objCType\" for value $value"
                                )
                        }
                    is NSString -> value as String
                    is NSArray ->
                        @Suppress("CAST_NEVER_SUCCEEDS", "UNCHECKED_CAST")
                        (value as List<NSString>).mapTo(mutableSetOf()) { it as String }
                    else -> throw IllegalArgumentException("Unsupported type for value $value")
                }
            }
        return MapPreferences(map)
    }
    set(value) {
        val dictionary: Map<Any?, *> =
            value.asMap().mapValues { (_, mapValue) ->
                when (mapValue) {
                    is Boolean -> mapValue as NSNumber
                    is Int -> mapValue as NSNumber
                    is Float -> mapValue as NSNumber
                    is String -> mapValue as NSString
                    is Set<*> ->
                        @Suppress("CAST_NEVER_SUCCEEDS", "UNCHECKED_CAST")
                        (mapValue as Set<String>).map { it as NSString } as NSArray
                    else -> throw IllegalArgumentException("Unsupported type for value $mapValue")
                }
            }
        setPersistentDomain(dictionary, NSBundle.mainBundle.bundleIdentifier!!)
    }
