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

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
class PreferenceTheme(
    val categoryPadding: PaddingValues,
    val categoryColor: Color,
    val categoryTextStyle: TextStyle,
    val padding: PaddingValues,
    val horizontalSpacing: Dp,
    val verticalSpacing: Dp,
    val disabledOpacity: Float,
    val iconContainerMinWidth: Dp,
    val iconColor: Color,
    val titleColor: Color,
    val titleTextStyle: TextStyle,
    val summaryColor: Color,
    val summaryTextStyle: TextStyle,
    val dividerHeight: Dp,
)

@Composable
fun preferenceTheme(
    categoryPadding: PaddingValues =
        PaddingValues(start = 16.dp, top = 24.dp, end = 16.dp, bottom = 8.dp),
    categoryColor: Color = MaterialTheme.colorScheme.secondary,
    categoryTextStyle: TextStyle = MaterialTheme.typography.labelLarge,
    padding: PaddingValues = PaddingValues(16.dp),
    horizontalSpacing: Dp = 16.dp,
    verticalSpacing: Dp = 16.dp,
    disabledOpacity: Float = 0.38f,
    iconContainerMinWidth: Dp = 56.dp,
    iconColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    titleTextStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    summaryColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    summaryTextStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    dividerHeight: Dp = 32.dp,
) =
    PreferenceTheme(
        categoryPadding = categoryPadding,
        categoryColor = categoryColor,
        categoryTextStyle = categoryTextStyle,
        padding = padding,
        horizontalSpacing = horizontalSpacing,
        verticalSpacing = verticalSpacing,
        disabledOpacity = disabledOpacity,
        iconContainerMinWidth = iconContainerMinWidth,
        iconColor = iconColor,
        titleColor = titleColor,
        titleTextStyle = titleTextStyle,
        summaryColor = summaryColor,
        summaryTextStyle = summaryTextStyle,
        dividerHeight = dividerHeight,
    )

val LocalPreferenceTheme =
    compositionLocalOf<PreferenceTheme> { noLocalProvidedFor("LocalPreferenceTheme") }

@Composable
fun ProvidePreferenceTheme(
    theme: PreferenceTheme = preferenceTheme(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalPreferenceTheme provides theme, content = content)
}
