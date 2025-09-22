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

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role

public fun LazyListScope.radioButtonPreference(
    key: String,
    selected: Boolean,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    summary: @Composable (() -> Unit)? = null,
    widgetContainer: @Composable (() -> Unit)? = null,
    onClick: () -> Unit,
) {
    item(key = key, contentType = "RadioButtonPreference") {
        RadioButtonPreference(
            selected = selected,
            title = title,
            modifier = modifier,
            enabled = enabled,
            summary = summary,
            widgetContainer = widgetContainer,
            onClick = onClick,
        )
    }
}

@Composable
public fun RadioButtonPreference(
    selected: Boolean,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    summary: @Composable (() -> Unit)? = null,
    widgetContainer: @Composable (() -> Unit)? = null,
    onClick: () -> Unit,
) {
    Preference(
        title = title,
        modifier = modifier.selectable(selected, enabled, Role.RadioButton, onClick = onClick),
        enabled = enabled,
        icon = { RadioButton(selected = selected, onClick = null, enabled = enabled) },
        summary = summary,
        widgetContainer = widgetContainer,
    )
}
