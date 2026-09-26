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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import composepreference.preference.generated.resources.Res
import composepreference.preference.generated.resources.cancel
import composepreference.preference.generated.resources.ok
import org.jetbrains.compose.resources.stringResource

public inline fun <T> LazyListScope.multiSelectListPreference(
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
    noinline valueToText: @Composable (T) -> AnnotatedString = { AnnotatedString(it.toString()) },
    searchable: Boolean = false,
    noinline searchFilter: (value: T, text: String, query: String) -> Boolean =
        MultiSelectListPreferenceDefaults.searchFilter(),
    noinline searchPlaceholder: @Composable (() -> Unit)? = null,
    noinline searchLeadingIcon: @Composable (() -> Unit)? = null,
    noinline searchEmptyResult: @Composable (() -> Unit)? = null,
    pinSelectedValues: Boolean = false,
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
            searchable = searchable,
            searchFilter = searchFilter,
            searchPlaceholder = searchPlaceholder,
            searchLeadingIcon = searchLeadingIcon,
            searchEmptyResult = searchEmptyResult,
            pinSelectedValues = pinSelectedValues,
            item = item,
        )
    }
}

public fun <T> LazyListScope.multiSelectListPreference(
    key: String,
    value: Set<T>,
    onValueChange: (Set<T>) -> Unit,
    values: List<T>,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    enabled: Boolean = true,
    icon: @Composable (() -> Unit)? = null,
    summary: @Composable (() -> Unit)? = null,
    valueToText: @Composable (T) -> AnnotatedString = { AnnotatedString(it.toString()) },
    searchable: Boolean = false,
    searchFilter: (value: T, text: String, query: String) -> Boolean =
        MultiSelectListPreferenceDefaults.searchFilter(),
    searchPlaceholder: @Composable (() -> Unit)? = null,
    searchLeadingIcon: @Composable (() -> Unit)? = null,
    searchEmptyResult: @Composable (() -> Unit)? = null,
    pinSelectedValues: Boolean = false,
    item: @Composable (value: T, currentValues: Set<T>, onToggle: (Boolean) -> Unit) -> Unit =
        MultiSelectListPreferenceDefaults.item(valueToText),
) {
    item(key = key, contentType = "MultiSelectListPreference") {
        MultiSelectListPreference(
            value = value,
            onValueChange = onValueChange,
            values = values,
            title = title,
            modifier = modifier,
            enabled = enabled,
            icon = icon,
            summary = summary,
            valueToText = valueToText,
            searchable = searchable,
            searchFilter = searchFilter,
            searchPlaceholder = searchPlaceholder,
            searchLeadingIcon = searchLeadingIcon,
            searchEmptyResult = searchEmptyResult,
            pinSelectedValues = pinSelectedValues,
            item = item,
        )
    }
}

@Composable
public fun <T> MultiSelectListPreference(
    state: MutableState<Set<T>>,
    values: List<T>,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable (() -> Unit)? = null,
    summary: @Composable (() -> Unit)? = null,
    valueToText: @Composable (T) -> AnnotatedString = { AnnotatedString(it.toString()) },
    searchable: Boolean = false,
    searchFilter: (value: T, text: String, query: String) -> Boolean =
        MultiSelectListPreferenceDefaults.searchFilter(),
    searchPlaceholder: @Composable (() -> Unit)? = null,
    searchLeadingIcon: @Composable (() -> Unit)? = null,
    searchEmptyResult: @Composable (() -> Unit)? = null,
    pinSelectedValues: Boolean = false,
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
        searchable = searchable,
        searchFilter = searchFilter,
        searchPlaceholder = searchPlaceholder,
        searchLeadingIcon = searchLeadingIcon,
        searchEmptyResult = searchEmptyResult,
        pinSelectedValues = pinSelectedValues,
        item = item,
    )
}

@Composable
public fun <T> MultiSelectListPreference(
    value: Set<T>,
    onValueChange: (Set<T>) -> Unit,
    values: List<T>,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable (() -> Unit)? = null,
    summary: @Composable (() -> Unit)? = null,
    valueToText: @Composable (T) -> AnnotatedString = { AnnotatedString(it.toString()) },
    searchable: Boolean = false,
    searchFilter: (value: T, text: String, query: String) -> Boolean =
        MultiSelectListPreferenceDefaults.searchFilter(),
    searchPlaceholder: @Composable (() -> Unit)? = null,
    searchLeadingIcon: @Composable (() -> Unit)? = null,
    searchEmptyResult: @Composable (() -> Unit)? = null,
    pinSelectedValues: Boolean = false,
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
        // Snapshot of the selection when the dialog was opened, so that unchecking a pinned value
        // doesn't move it around until the dialog is reopened.
        val pinnedValues = rememberSaveable {
            if (pinSelectedValues) values.filter { it in value } else emptyList()
        }
        var query by rememberSaveable { mutableStateOf("") }
        val isFiltering = searchable && query.isNotEmpty()
        val filteredPinnedValues =
            if (isFiltering) {
                pinnedValues.filter { searchFilter(it, valueToText(it).text, query) }
            } else {
                pinnedValues
            }
        val filteredValues =
            if (isFiltering) {
                values.filter { searchFilter(it, valueToText(it).text, query) }
            } else {
                values
            }
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
            val onToggle: (T, Boolean) -> Unit = { itemValue, checked ->
                dialogValue = if (checked) dialogValue + itemValue else dialogValue - itemValue
            }
            val lazyListState = rememberLazyListState()
            val list: @Composable (Modifier) -> Unit = { modifier ->
                LazyColumn(
                    modifier = modifier.fillMaxWidth().verticalScrollIndicators(lazyListState),
                    state = lazyListState,
                ) {
                    // Pinned values are duplicates of their items in the full list below.
                    items(filteredPinnedValues) { itemValue ->
                        item(itemValue, dialogValue) { onToggle(itemValue, it) }
                    }
                    if (filteredPinnedValues.isNotEmpty()) {
                        item {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        }
                    }
                    items(filteredValues) { itemValue ->
                        item(itemValue, dialogValue) { onToggle(itemValue, it) }
                    }
                    if (filteredValues.isEmpty() && searchEmptyResult != null) {
                        item { MultiSelectListPreferenceSearchEmptyResult(searchEmptyResult) }
                    }
                }
            }
            if (searchable) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    MultiSelectListPreferenceSearchField(
                        query = query,
                        onQueryChange = { query = it },
                        placeholder = searchPlaceholder,
                        leadingIcon = searchLeadingIcon,
                    )
                    // Fill the available height so that the dialog doesn't resize while filtering.
                    list(Modifier.weight(1f))
                }
            } else {
                list(Modifier)
            }
        }
    }
}

@Composable
private fun MultiSelectListPreferenceSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: @Composable (() -> Unit)?,
    leadingIcon: @Composable (() -> Unit)?,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, bottom = 8.dp),
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
    )
}

@Composable
private fun MultiSelectListPreferenceSearchEmptyResult(content: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp)) {
        CompositionLocalProvider(
            LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant,
            LocalTextStyle provides MaterialTheme.typography.bodyLarge,
            content = content,
        )
    }
}

@PublishedApi
internal object MultiSelectListPreferenceDefaults {
    fun <T> searchFilter(): (value: T, text: String, query: String) -> Boolean =
        { _, text, query ->
            text.contains(query, ignoreCase = true)
        }

    fun <T> item(
        valueToText: @Composable (T) -> AnnotatedString
    ): @Composable (value: T, currentValues: Set<T>, onToggle: (Boolean) -> Unit) -> Unit =
        { value, currentValues, onToggle ->
            Item(value, currentValues, valueToText, onToggle)
        }

    @Composable
    private fun <T> Item(
        value: T,
        currentValues: Set<T>,
        valueToText: @Composable (T) -> AnnotatedString,
        onToggle: (Boolean) -> Unit,
    ) {
        val checked = value in currentValues
        Row(
            modifier =
                Modifier.fillMaxWidth()
                    .heightIn(min = 48.dp)
                    .toggleable(checked, true, Role.Checkbox, onValueChange = onToggle)
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
