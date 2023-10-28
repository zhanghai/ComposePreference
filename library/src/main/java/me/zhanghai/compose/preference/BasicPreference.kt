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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

fun LazyListScope.basicPreference(
    key: String,
    textContainer: @Composable () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    enabled: Boolean = true,
    iconContainer: @Composable () -> Unit = {},
    widgetContainer: @Composable () -> Unit = {},
    onClick: (() -> Unit)? = null
) {
    item(key = key, contentType = "BasicPreference") {
        BasicPreference(
            textContainer = textContainer,
            modifier = modifier,
            enabled = enabled,
            iconContainer = iconContainer,
            widgetContainer = widgetContainer,
            onClick = onClick
        )
    }
}

@Composable
fun BasicPreference(
    textContainer: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    iconContainer: @Composable () -> Unit = {},
    widgetContainer: @Composable () -> Unit = {},
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier =
            modifier.then(
                if (onClick != null) {
                    Modifier.clickable(enabled, onClick = onClick)
                } else {
                    Modifier
                }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        iconContainer()
        Box(modifier = Modifier.weight(1f)) { textContainer() }
        widgetContainer()
    }
}
