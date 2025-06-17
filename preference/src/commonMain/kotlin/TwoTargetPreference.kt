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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.DividerDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

public fun LazyListScope.twoTargetPreference(
    key: String,
    title: @Composable () -> Unit,
    secondTarget: @Composable () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    enabled: Boolean = true,
    icon: @Composable (() -> Unit)? = null,
    summary: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    item(key = key, contentType = "TwoTargetPreference") {
        TwoTargetPreference(
            title = title,
            secondTarget = secondTarget,
            modifier = modifier,
            enabled = enabled,
            icon = icon,
            summary = summary,
            onClick = onClick,
        )
    }
}

@Composable
public fun TwoTargetPreference(
    title: @Composable () -> Unit,
    secondTarget: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable (() -> Unit)? = null,
    summary: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    Preference(
        title = title,
        modifier = modifier,
        enabled = enabled,
        icon = icon,
        summary = summary,
        widgetContainer = {
            val theme = LocalPreferenceTheme.current
            Row(
                modifier = Modifier.padding(start = theme.horizontalSpacing),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier =
                        Modifier.size(DividerDefaults.Thickness, theme.dividerHeight)
                            .background(
                                DividerDefaults.color.let {
                                    if (enabled) it else it.copy(alpha = theme.disabledOpacity)
                                }
                            )
                )
                secondTarget()
            }
        },
        onClick = onClick,
    )
}
