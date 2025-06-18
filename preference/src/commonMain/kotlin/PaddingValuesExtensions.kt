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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Composable
internal fun PaddingValues.copy(
    horizontal: Dp = Dp.Unspecified,
    vertical: Dp = Dp.Unspecified,
): PaddingValues = copy(start = horizontal, top = vertical, end = horizontal, bottom = vertical)

@Composable
internal fun PaddingValues.copy(
    start: Dp = Dp.Unspecified,
    top: Dp = Dp.Unspecified,
    end: Dp = Dp.Unspecified,
    bottom: Dp = Dp.Unspecified,
): PaddingValues = CopiedPaddingValues(start, top, end, bottom, this)

@Stable
private class CopiedPaddingValues(
    private val start: Dp,
    private val top: Dp,
    private val end: Dp,
    private val bottom: Dp,
    private val paddingValues: PaddingValues,
) : PaddingValues {
    override fun calculateLeftPadding(layoutDirection: LayoutDirection): Dp =
        (if (layoutDirection == LayoutDirection.Ltr) start else end).takeIf { it != Dp.Unspecified }
            ?: paddingValues.calculateLeftPadding(layoutDirection)

    override fun calculateTopPadding(): Dp =
        top.takeIf { it != Dp.Unspecified } ?: paddingValues.calculateTopPadding()

    override fun calculateRightPadding(layoutDirection: LayoutDirection): Dp =
        (if (layoutDirection == LayoutDirection.Ltr) end else start).takeIf { it != Dp.Unspecified }
            ?: paddingValues.calculateRightPadding(layoutDirection)

    override fun calculateBottomPadding(): Dp =
        bottom.takeIf { it != Dp.Unspecified } ?: paddingValues.calculateBottomPadding()

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is CopiedPaddingValues) {
            return false
        }
        return start == other.start &&
            top == other.top &&
            end == other.end &&
            bottom == other.bottom &&
            paddingValues == other.paddingValues
    }

    override fun hashCode(): Int {
        var result = start.hashCode()
        result = 31 * result + top.hashCode()
        result = 31 * result + end.hashCode()
        result = 31 * result + bottom.hashCode()
        result = 31 * result + paddingValues.hashCode()
        return result
    }

    override fun toString(): String {
        return "Copied($start, $top, $end, $bottom, $paddingValues)"
    }
}

@Composable
internal fun PaddingValues.offset(all: Dp = 0.dp): PaddingValues = offset(all, all, all, all)

@Composable
internal fun PaddingValues.offset(horizontal: Dp = 0.dp, vertical: Dp = 0.dp): PaddingValues =
    offset(horizontal, vertical, horizontal, vertical)

@Composable
internal fun PaddingValues.offset(
    start: Dp = 0.dp,
    top: Dp = 0.dp,
    end: Dp = 0.dp,
    bottom: Dp = 0.dp,
): PaddingValues = OffsetPaddingValues(start, top, end, bottom, this)

@Stable
private class OffsetPaddingValues(
    private val start: Dp,
    private val top: Dp,
    private val end: Dp,
    private val bottom: Dp,
    private val paddingValues: PaddingValues,
) : PaddingValues {
    override fun calculateLeftPadding(layoutDirection: LayoutDirection): Dp =
        paddingValues.calculateLeftPadding(layoutDirection) +
            (if (layoutDirection == LayoutDirection.Ltr) start else end)

    override fun calculateTopPadding(): Dp = paddingValues.calculateTopPadding() + top

    override fun calculateRightPadding(layoutDirection: LayoutDirection): Dp =
        paddingValues.calculateRightPadding(layoutDirection) +
            (if (layoutDirection == LayoutDirection.Ltr) end else start)

    override fun calculateBottomPadding(): Dp = paddingValues.calculateBottomPadding() + bottom

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is OffsetPaddingValues) {
            return false
        }
        return start == other.start &&
            top == other.top &&
            end == other.end &&
            bottom == other.bottom &&
            paddingValues == other.paddingValues
    }

    override fun hashCode(): Int {
        var result = start.hashCode()
        result = 31 * result + top.hashCode()
        result = 31 * result + end.hashCode()
        result = 31 * result + bottom.hashCode()
        result = 31 * result + paddingValues.hashCode()
        return result
    }

    override fun toString(): String {
        return "Offset($start, $top, $end, $bottom, $paddingValues)"
    }
}
