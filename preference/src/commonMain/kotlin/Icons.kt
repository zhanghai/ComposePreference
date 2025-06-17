/*
 * Copyright 2025 Google LLC
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

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.DefaultFillType
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

internal object Icons {
    val Info: ImageVector
        get() {
            if (_info != null) {
                return _info!!
            }
            _info =
                materialIcon(name = "Outlined.Info") {
                    materialPath {
                        moveTo(11.0f, 7.0f)
                        horizontalLineToRelative(2.0f)
                        verticalLineToRelative(2.0f)
                        horizontalLineToRelative(-2.0f)
                        close()
                        moveTo(11.0f, 11.0f)
                        horizontalLineToRelative(2.0f)
                        verticalLineToRelative(6.0f)
                        horizontalLineToRelative(-2.0f)
                        close()
                        moveTo(12.0f, 2.0f)
                        curveTo(6.48f, 2.0f, 2.0f, 6.48f, 2.0f, 12.0f)
                        reflectiveCurveToRelative(4.48f, 10.0f, 10.0f, 10.0f)
                        reflectiveCurveToRelative(10.0f, -4.48f, 10.0f, -10.0f)
                        reflectiveCurveTo(17.52f, 2.0f, 12.0f, 2.0f)
                        close()
                        moveTo(12.0f, 20.0f)
                        curveToRelative(-4.41f, 0.0f, -8.0f, -3.59f, -8.0f, -8.0f)
                        reflectiveCurveToRelative(3.59f, -8.0f, 8.0f, -8.0f)
                        reflectiveCurveToRelative(8.0f, 3.59f, 8.0f, 8.0f)
                        reflectiveCurveToRelative(-3.59f, 8.0f, -8.0f, 8.0f)
                        close()
                    }
                }
            return _info!!
        }

    private var _info: ImageVector? = null
}

private inline fun materialIcon(
    name: String,
    autoMirror: Boolean = false,
    block: ImageVector.Builder.() -> ImageVector.Builder,
): ImageVector =
    ImageVector.Builder(
            name = name,
            defaultWidth = MaterialIconDimension.dp,
            defaultHeight = MaterialIconDimension.dp,
            viewportWidth = MaterialIconDimension,
            viewportHeight = MaterialIconDimension,
            autoMirror = autoMirror,
        )
        .block()
        .build()

private inline fun ImageVector.Builder.materialPath(
    fillAlpha: Float = 1f,
    strokeAlpha: Float = 1f,
    pathFillType: PathFillType = DefaultFillType,
    pathBuilder: PathBuilder.() -> Unit,
) =
    path(
        fill = SolidColor(Color.Black),
        fillAlpha = fillAlpha,
        stroke = null,
        strokeAlpha = strokeAlpha,
        strokeLineWidth = 1f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1f,
        pathFillType = pathFillType,
        pathBuilder = pathBuilder,
    )

private const val MaterialIconDimension = 24f
