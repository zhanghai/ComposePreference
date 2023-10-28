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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

fun LazyListScope.footerPreference(
    key: String,
    summary: @Composable () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    icon: @Composable () -> Unit = FooterPreferenceDefaults.Icon
) {
    item(key = key, contentType = "FooterPreference") {
        FooterPreference(summary = summary, modifier = modifier, icon = icon)
    }
}

@Composable
fun FooterPreference(
    summary: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit = FooterPreferenceDefaults.Icon
) {
    Preference(
        title = {
            val theme = LocalPreferenceTheme.current
            Box(modifier = Modifier.padding(bottom = theme.verticalSpacing)) {
                CompositionLocalProvider(LocalContentColor provides theme.iconColor, content = icon)
            }
        },
        modifier = modifier,
        summary = summary,
    )
}

private object FooterPreferenceDefaults {
    val Icon: @Composable () -> Unit = {
        Icon(imageVector = Icons.Outlined.Info, contentDescription = null)
    }
}

@Composable
@Preview(showBackground = true)
private fun FooterPreferencePreview() {
    ProvidePreferenceTheme {
        FooterPreference(
            modifier = Modifier.fillMaxWidth(),
            summary = { Text(text = "Footer preference summary") }
        )
    }
}
