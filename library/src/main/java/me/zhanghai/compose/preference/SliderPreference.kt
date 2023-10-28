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

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

inline fun LazyListScope.sliderPreference(
    key: String,
    defaultValue: Float,
    crossinline title: @Composable (Float) -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    crossinline rememberState: @Composable () -> MutableState<Float> = {
        rememberPreferenceState(key, defaultValue)
    },
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    valueSteps: Int = 0,
    crossinline rememberSliderState: @Composable (Float) -> MutableState<Float> = {
        remember { mutableFloatStateOf(it) }
    },
    crossinline enabled: (Float) -> Boolean = { true },
    noinline icon: @Composable ((Float) -> Unit)? = null,
    noinline summary: @Composable ((Float) -> Unit)? = null,
    noinline valueText: @Composable ((Float) -> Unit)? = null
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
            valueText = valueText?.let { { it(sliderValue) } }
        )
    }
}

@Composable
fun SliderPreference(
    state: MutableState<Float>,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    valueSteps: Int = 0,
    sliderState: MutableState<Float> = remember { mutableFloatStateOf(state.value) },
    enabled: Boolean = true,
    icon: @Composable (() -> Unit)? = null,
    summary: @Composable (() -> Unit)? = null,
    valueText: @Composable (() -> Unit)? = null
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
        valueText = valueText
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SliderPreference(
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
    valueText: @Composable (() -> Unit)? = null
) {
    var lastValue by remember { mutableFloatStateOf(value) }
    SideEffect {
        if (value != lastValue) {
            onSliderValueChange(value)
            lastValue = value
        }
    }
    BasicPreference(
        textContainer = {
            Column {
                val theme = LocalPreferenceTheme.current
                Column(
                    modifier =
                        Modifier.padding(
                            theme.padding.copy(
                                start = if (icon != null) 8.dp else Dp.Unspecified,
                                bottom = 0.dp
                            )
                        )
                ) {
                    PreferenceDefaults.TitleContainer(title = title, enabled = enabled)
                    PreferenceDefaults.SummaryContainer(summary = summary, enabled = enabled)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CompositionLocalProvider(
                        LocalMinimumInteractiveComponentEnforcement provides false
                    ) {
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
                            modifier =
                                Modifier.weight(1f)
                                    .padding(
                                        theme.padding
                                            .copy(
                                                start =
                                                    if (icon != null) {
                                                        8.dp
                                                    } else {
                                                        Dp.Unspecified
                                                    },
                                                top = theme.verticalSpacing,
                                                end =
                                                    if (valueText != null) {
                                                        theme.horizontalSpacing
                                                    } else {
                                                        Dp.Unspecified
                                                    }
                                            )
                                            .offset((-8).dp)
                                    ),
                            enabled = enabled,
                            valueRange = valueRange,
                            steps = valueSteps,
                            onValueChangeFinished = { onValueChange(latestSliderValue) }
                        )
                    }
                    if (valueText != null) {
                        Box(
                            modifier =
                                Modifier.padding(
                                    theme.padding.copy(start = 0.dp, top = 0.dp, bottom = 0.dp)
                                )
                        ) {
                            CompositionLocalProvider(
                                LocalContentColor provides
                                    theme.summaryColor.let {
                                        if (enabled) it else it.copy(alpha = theme.disabledOpacity)
                                    }
                            ) {
                                ProvideTextStyle(
                                    value = theme.summaryTextStyle,
                                    content = valueText
                                )
                            }
                        }
                    }
                }
            }
        },
        modifier = modifier,
        enabled = enabled,
        iconContainer = {
            PreferenceDefaults.IconContainer(
                icon = icon,
                enabled = enabled,
                excludedEndPadding = 8.dp
            )
        }
    )
}

@Composable
@Preview(showBackground = true)
@SuppressLint("AutoboxingStateCreation")
private fun SliderPreferencePreview() {
    ProvidePreferenceTheme {
        val state = remember { mutableStateOf(3.5f) }
        val sliderState = remember { mutableStateOf(state.value) }
        SliderPreference(
            state = state,
            title = { Text(text = "Slider preference") },
            modifier = Modifier.fillMaxWidth(),
            valueRange = 0f..5f,
            valueSteps = 9,
            sliderState = sliderState,
            icon = { Icon(imageVector = Icons.Outlined.Info, contentDescription = null) },
            summary = { Text(text = "Summary") },
            valueText = { Text(text = String.format("%.1f", sliderState.value)) }
        )
    }
}
