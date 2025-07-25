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
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import kotlinx.coroutines.flow.MutableStateFlow

@Composable public expect fun createDefaultPreferenceFlow(): MutableStateFlow<Preferences>

public val LocalPreferenceFlow: ProvidableCompositionLocal<MutableStateFlow<Preferences>> =
    compositionLocalOf {
        noLocalProvidedFor("LocalPreferenceFlow")
    }

@Composable
public fun ProvidePreferenceFlow(
    flow: MutableStateFlow<Preferences> = createDefaultPreferenceFlow(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalPreferenceFlow provides flow, content = content)
}
