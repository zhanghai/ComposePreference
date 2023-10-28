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

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

inline fun LazyListScope.twoTargetSwitchPreference(
    key: String,
    defaultValue: Boolean,
    crossinline title: @Composable (Boolean) -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    crossinline rememberState: @Composable () -> MutableState<Boolean> = {
        rememberPreferenceState(key, defaultValue)
    },
    noinline enabled: (Boolean) -> Boolean = { true },
    noinline icon: @Composable ((Boolean) -> Unit)? = null,
    noinline summary: @Composable ((Boolean) -> Unit)? = null,
    noinline switchEnabled: (Boolean) -> Boolean = enabled,
    noinline onClick: ((Boolean) -> Unit)? = null
) {
    item(key = key, contentType = "TwoTargetSwitchPreference") {
        val state = rememberState()
        val value by state
        TwoTargetSwitchPreference(
            state = state,
            title = { title(value) },
            modifier = modifier,
            enabled = enabled(value),
            icon = icon?.let { { it(value) } },
            summary = summary?.let { { it(value) } },
            switchEnabled = switchEnabled(value),
            onClick = onClick?.let { { it(value) } }
        )
    }
}

@Composable
fun TwoTargetSwitchPreference(
    state: MutableState<Boolean>,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable (() -> Unit)? = null,
    summary: @Composable (() -> Unit)? = null,
    switchEnabled: Boolean = enabled,
    onClick: (() -> Unit)? = null
) {
    var value by state
    TwoTargetSwitchPreference(
        value = value,
        onValueChange = { value = it },
        title = title,
        modifier = modifier,
        enabled = enabled,
        icon = icon,
        summary = summary,
        switchEnabled = switchEnabled,
        onClick = onClick
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TwoTargetSwitchPreference(
    value: Boolean,
    onValueChange: (Boolean) -> Unit,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable (() -> Unit)? = null,
    summary: @Composable (() -> Unit)? = null,
    switchEnabled: Boolean = enabled,
    onClick: (() -> Unit)? = null
) {
    TwoTargetPreference(
        title = title,
        secondTarget = {
            val theme = LocalPreferenceTheme.current
            Switch(
                checked = value,
                onCheckedChange = onValueChange,
                modifier =
                    Modifier.padding(
                        theme.padding
                            .copy(start = theme.horizontalSpacing)
                            .offset(vertical = (-8).dp)
                    ),
                enabled = switchEnabled
            )
        },
        modifier = modifier,
        enabled = enabled,
        icon = icon,
        summary = summary,
        onClick = onClick
    )
}

@Composable
@Preview(showBackground = true)
private fun TwoTargetSwitchPreferencePreview() {
    ProvidePreferenceTheme {
        TwoTargetSwitchPreference(
            state = remember { mutableStateOf(true) },
            title = { Text(text = "Two target switch preference") },
            modifier = Modifier.fillMaxWidth(),
            icon = { Icon(imageVector = Icons.Outlined.Info, contentDescription = null) },
            summary = { Text(text = "Summary") }
        )
    }
}
