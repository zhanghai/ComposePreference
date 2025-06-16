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

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import composepreference.preference.generated.resources.Res
import composepreference.preference.generated.resources.cancel
import composepreference.preference.generated.resources.ok
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

inline fun <T> LazyListScope.multiSelectListPreference(
    key: String,
    defaultValue: Set<T>,
    values: List<T>,
    crossinline title: @Composable (Set<T>) -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    crossinline rememberState: @Composable () -> MutableState<Set<T>> = {
        rememberPreferenceState(key, defaultValue)
    },
    crossinline enabled: (Set<T>) -> Boolean = { true },
    noinline icon: @Composable ((Set<T>) -> Unit)? = null,
    noinline summary: @Composable ((Set<T>) -> Unit)? = null,
    noinline valueToText: (T) -> AnnotatedString = { AnnotatedString(it.toString()) },
    noinline item:
        @Composable
        (value: T, currentValues: Set<T>, onToggle: (Boolean) -> Unit) -> Unit =
        MultiSelectListPreferenceDefaults.item(valueToText),
) {
    item(key = key, contentType = "MultiSelectListPreference") {
        val state = rememberState()
        val value by state
        MultiSelectListPreference(
            state = state,
            values = values,
            title = { title(value) },
            modifier = modifier,
            enabled = enabled(value),
            icon = icon?.let { { it(value) } },
            summary = summary?.let { { it(value) } },
            valueToText = valueToText,
            item = item,
        )
    }
}

@Composable
fun <T> MultiSelectListPreference(
    state: MutableState<Set<T>>,
    values: List<T>,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable (() -> Unit)? = null,
    summary: @Composable (() -> Unit)? = null,
    valueToText: (T) -> AnnotatedString = { AnnotatedString(it.toString()) },
    item: @Composable (value: T, currentValues: Set<T>, onToggle: (Boolean) -> Unit) -> Unit =
        MultiSelectListPreferenceDefaults.item(valueToText),
) {
    var value by state
    MultiSelectListPreference(
        value = value,
        onValueChange = { value = it },
        values = values,
        title = title,
        modifier = modifier,
        enabled = enabled,
        icon = icon,
        summary = summary,
        valueToText = valueToText,
        item = item,
    )
}

@Composable
fun <T> MultiSelectListPreference(
    value: Set<T>,
    onValueChange: (Set<T>) -> Unit,
    values: List<T>,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable (() -> Unit)? = null,
    summary: @Composable (() -> Unit)? = null,
    valueToText: (T) -> AnnotatedString = { AnnotatedString(it.toString()) },
    item: @Composable (value: T, currentValues: Set<T>, onToggle: (Boolean) -> Unit) -> Unit =
        MultiSelectListPreferenceDefaults.item(valueToText),
) {
    var openDialog by rememberSaveable { mutableStateOf(false) }
    Preference(
        title = title,
        modifier = modifier,
        enabled = enabled,
        icon = icon,
        summary = summary,
    ) {
        openDialog = true
    }
    if (openDialog) {
        var dialogValue by rememberSaveable { mutableStateOf(value) }
        PreferenceAlertDialog(
            onDismissRequest = { openDialog = false },
            title = title,
            buttons = {
                TextButton(onClick = { openDialog = false }) {
                    Text(text = stringResource(Res.string.cancel))
                }
                TextButton(
                    onClick = {
                        onValueChange(dialogValue)
                        openDialog = false
                    }
                ) {
                    Text(text = stringResource(Res.string.ok))
                }
            },
        ) {
            val lazyListState = rememberLazyListState()
            LazyColumn(
                modifier = Modifier.fillMaxWidth().verticalScrollIndicators(lazyListState),
                state = lazyListState,
            ) {
                items(values) { itemValue ->
                    item(itemValue, dialogValue) {
                        dialogValue = if (it) dialogValue + itemValue else dialogValue - itemValue
                    }
                }
            }
        }
    }
}

@PublishedApi
internal object MultiSelectListPreferenceDefaults {
    fun <T> item(
        valueToText: (T) -> AnnotatedString
    ): @Composable (value: T, currentValues: Set<T>, onToggle: (Boolean) -> Unit) -> Unit =
        { value, currentValues, onToggle ->
            Item(value, currentValues, valueToText, onToggle)
        }

    @Composable
    private fun <T> Item(
        value: T,
        currentValues: Set<T>,
        valueToText: (T) -> AnnotatedString,
        onToggle: (Boolean) -> Unit,
    ) {
        val checked = value in currentValues
        Row(
            modifier =
                Modifier.fillMaxWidth()
                    .heightIn(min = 48.dp)
                    .toggleable(checked, true, Role.Checkbox, onToggle)
                    .padding(horizontal = 24.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(checked = checked, onCheckedChange = null)
            Spacer(modifier = Modifier.width(24.dp))
            Text(
                text = valueToText(value),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
@Preview
private fun MultiSelectListPreferencePreview() {
    ProvidePreferenceTheme {
        val state = remember { mutableStateOf(setOf("Alpha", "Beta")) }
        MultiSelectListPreference(
            state = state,
            values = listOf("Alpha", "Beta", "Canary"),
            title = { Text(text = "Multi-select list preference") },
            modifier = Modifier.fillMaxWidth(),
            icon = { Icon(imageVector = PreviewIcons.Info, contentDescription = null) },
            summary = { Text(text = state.value.joinToString(", ")) },
        )
    }
}
