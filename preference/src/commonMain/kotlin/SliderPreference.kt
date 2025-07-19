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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

public inline fun LazyListScope.sliderPreference(
    key: String,
    defaultValue: Float,
    crossinline title: @Composable (Float) -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    crossinline rememberState: @Composable () -> MutableState<Float> = {
        rememberPreferenceState(key, defaultValue)
    },
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    valueSteps: Int = 0,
    crossinline rememberSliderState: @Composable (Float) -> MutableFloatState = {
        remember { mutableFloatStateOf(it) }
    },
    crossinline enabled: (Float) -> Boolean = { true },
    noinline icon: @Composable ((Float) -> Unit)? = null,
    noinline summary: @Composable ((Float) -> Unit)? = null,
    noinline valueText: @Composable ((Float) -> Unit)? = null,
) {
    item(key = key, contentType = "SliderPreference") {
        val state = rememberState()
        val value by state
        val sliderState = rememberSliderState(value)
        val sliderValue by sliderState
        SliderPreference(
            state = state,
            title = { title(sliderValue) },
            modifier = modifier,
            valueRange = valueRange,
            valueSteps = valueSteps,
            sliderState = sliderState,
            enabled = enabled(value),
            icon = icon?.let { { it(sliderValue) } },
            summary = summary?.let { { it(sliderValue) } },
            valueText = valueText?.let { { it(sliderValue) } },
        )
    }
}

@Composable
public fun SliderPreference(
    state: MutableState<Float>,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    valueSteps: Int = 0,
    sliderState: MutableFloatState = remember { mutableFloatStateOf(state.value) },
    enabled: Boolean = true,
    icon: @Composable (() -> Unit)? = null,
    summary: @Composable (() -> Unit)? = null,
    valueText: @Composable (() -> Unit)? = null,
) {
    var value by state
    var sliderValue by sliderState
    SliderPreference(
        value = value,
        onValueChange = { value = it },
        sliderValue = sliderValue,
        onSliderValueChange = { sliderValue = it },
        title = title,
        modifier = modifier,
        valueRange = valueRange,
        valueSteps = valueSteps,
        enabled = enabled,
        icon = icon,
        summary = summary,
        valueText = valueText,
    )
}

@Composable
public fun SliderPreference(
    value: Float,
    onValueChange: (Float) -> Unit,
    sliderValue: Float,
    onSliderValueChange: (Float) -> Unit,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    valueSteps: Int = 0,
    enabled: Boolean = true,
    icon: @Composable (() -> Unit)? = null,
    summary: @Composable (() -> Unit)? = null,
    valueText: @Composable (() -> Unit)? = null,
) {
    var lastValue by remember { mutableFloatStateOf(value) }
    SideEffect {
        if (value != lastValue) {
            onSliderValueChange(value)
            lastValue = value
        }
    }
    Preference(
        title = title,
        modifier = modifier,
        enabled = enabled,
        icon = icon,
        summary = {
            Column {
                summary?.invoke()
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // onValueChangeFinished() may be invoked before a recomposition has
                    // happened for onValueChange(), for example in the clicking case, so make
                    // onValueChange() share the latest value to onValueChangeFinished().
                    var latestSliderValue = sliderValue
                    Slider(
                        value = sliderValue,
                        onValueChange = {
                            onSliderValueChange(it)
                            latestSliderValue = it
                        },
                        modifier = Modifier.weight(1f),
                        enabled = enabled,
                        valueRange = valueRange,
                        steps = valueSteps,
                        onValueChangeFinished = { onValueChange(latestSliderValue) },
                    )
                    if (valueText != null) {
                        val theme = LocalPreferenceTheme.current
                        Box(modifier = Modifier.padding(start = theme.horizontalSpacing)) {
                            valueText()
                        }
                    }
                }
            }
        },
    )
}
