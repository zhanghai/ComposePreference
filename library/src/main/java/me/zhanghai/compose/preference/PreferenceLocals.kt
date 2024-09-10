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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun ProvidePreferenceLocals(
    flow: MutableStateFlow<Preferences> = defaultPreferenceFlow(),
    theme: PreferenceTheme = preferenceTheme(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalPreferenceFlow provides flow,
        LocalPreferenceTheme provides theme,
        content = content,
    )
}

internal fun noLocalProvidedFor(name: String): Nothing {
    error("CompositionLocal $name not present")
}
