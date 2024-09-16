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

package me.zhanghai.compose.preference.sample

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import me.zhanghai.compose.preference.ListPreferenceType
import me.zhanghai.compose.preference.ProvidePreferenceLocals
import me.zhanghai.compose.preference.checkboxPreference
import me.zhanghai.compose.preference.footerPreference
import me.zhanghai.compose.preference.listPreference
import me.zhanghai.compose.preference.multiSelectListPreference
import me.zhanghai.compose.preference.preference
import me.zhanghai.compose.preference.radioButtonPreference
import me.zhanghai.compose.preference.sliderPreference
import me.zhanghai.compose.preference.switchPreference
import me.zhanghai.compose.preference.textFieldPreference
import me.zhanghai.compose.preference.twoTargetIconButtonPreference
import me.zhanghai.compose.preference.twoTargetSwitchPreference

@Composable
fun SampleApp() {
    SampleTheme { ProvidePreferenceLocals { SampleScreen() } }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SampleScreen() {
    val windowInsets = WindowInsets.safeDrawing
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            val context = LocalContext.current
            val appLabel =
                if (LocalView.current.isInEditMode) {
                    "Sample"
                } else {
                    context.applicationInfo.loadLabel(context.packageManager).toString()
                }
            TopAppBar(
                title = { Text(text = appLabel) },
                modifier = Modifier.fillMaxWidth(),
                windowInsets =
                    windowInsets.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top),
                scrollBehavior = scrollBehavior,
            )
        },
        containerColor = Color.Transparent,
        contentColor = contentColorFor(MaterialTheme.colorScheme.background),
        contentWindowInsets = windowInsets,
    ) { contentPadding ->
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = contentPadding) {
            preference(
                key = "preference",
                title = { Text(text = "Preference") },
                summary = { Text(text = "Summary") },
            ) {}
            checkboxPreference(
                key = "checkbox_preference",
                defaultValue = false,
                title = { Text(text = "Checkbox preference") },
                summary = { Text(text = if (it) "On" else "Off") },
            )
            preferenceCategory(
                key = "category",
                title = { Text(text = "Switch type preferences") }
            )
            switchPreference(
                key = "switch_preference",
                defaultValue = false,
                title = { Text(text = "Switch preference") },
                summary = { Text(text = if (it) "On" else "Off") },
            )
            twoTargetSwitchPreference(
                key = "two_target_switch_preference",
                defaultValue = false,
                title = { Text(text = "Two target switch preference") },
                summary = { Text(text = if (it) "On" else "Off") },
            ) {}
            preferenceCategory(
                key = "category2",
                title = { Text(text = "More preferences") }
            )
            twoTargetIconButtonPreference(
                key = "two_target_icon_button_preference",
                title = { Text(text = "Two target icon button preference") },
                summary = { Text(text = "Summary") },
                onClick = {},
                iconButtonIcon = {
                    Icon(imageVector = Icons.Outlined.Settings, contentDescription = "Settings")
                },
            ) {}
            sliderPreference(
                key = "slider_preference",
                defaultValue = 0f,
                title = { Text(text = "Slider preference") },
                valueRange = 0f..5f,
                valueSteps = 9,
                summary = { Text(text = "Summary") },
                valueText = { Text(text = "%.1f".format(it)) },
            )
            listPreference(
                key = "list_alert_dialog_preference",
                defaultValue = "Alpha",
                values = listOf("Alpha", "Beta", "Canary"),
                title = { Text(text = "List preference (alert dialog)") },
                summary = { Text(text = it) },
            )
            listPreference(
                key = "list_dropdown_menu_preference",
                defaultValue = "Alpha",
                values = listOf("Alpha", "Beta", "Canary"),
                title = { Text(text = "List preference (dropdown menu)") },
                summary = { Text(text = it) },
                type = ListPreferenceType.DROPDOWN_MENU,
            )
            multiSelectListPreference(
                key = "multi_select_list_preference",
                defaultValue = setOf("Alpha", "Beta"),
                values = listOf("Alpha", "Beta", "Canary"),
                title = { Text(text = "Multi-select list preference") },
                summary = { Text(text = it.sorted().joinToString(", ")) },
            )
            textFieldPreference(
                key = "text_field_preference",
                defaultValue = "Value",
                title = { Text(text = "Text field preference") },
                textToValue = { it },
                summary = { Text(text = it) },
            )
            radioButtonPreference(
                key = "radio_button_preference",
                selected = true,
                title = { Text(text = "Radio button preference") },
                summary = { Text(text = "Summary") },
            ) {}
            footerPreference(
                key = "footer_preference",
                summary = { Text(text = "Footer preference summary") },
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SampleAppPreview() {
    SampleApp()
}
