package cloud.pablos.workouts.ui.modalbottomsheet

/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.R
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext

@Immutable
@JvmInline
value class Strings private constructor(private val value: Int) {
    companion object {
        val NavigationMenu = Strings(0)
        val CloseDrawer = Strings(1)
        val CloseSheet = Strings(2)
        val DefaultErrorMessage = Strings(3)
        val ExposedDropdownMenu = Strings(4)
        val SliderRangeStart = Strings(5)
        val SliderRangeEnd = Strings(6)
    }
}

@Composable
fun getString(string: Strings): String {
    LocalConfiguration.current
    val resources = LocalContext.current.resources
    return when (string) {
        Strings.NavigationMenu -> resources.getString(R.string.navigation_menu)
        Strings.CloseDrawer -> resources.getString(R.string.close_drawer)
        Strings.CloseSheet -> resources.getString(R.string.close_sheet)
        Strings.DefaultErrorMessage -> resources.getString(R.string.default_error_message)
        Strings.ExposedDropdownMenu -> resources.getString(R.string.dropdown_menu)
        Strings.SliderRangeStart -> resources.getString(R.string.range_start)
        Strings.SliderRangeEnd -> resources.getString(R.string.range_end)
        else -> ""
    }
}
