/*
 *     Dooz
 *     Constant.kt Created/Updated by Yamin Siahmargooei at 2022/9/6
 *     This file is part of Dooz.
 *     Copyright (C) 2022  Yamin Siahmargooei
 *
 *     Dooz is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Dooz is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Dooz.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.dooz.util

import io.github.yamin8000.dooz.model.AiDifficulty

object Constants {
    const val firstPlayerPolicy = "firstPlayerPolicy"
    const val theme = "theme"

    const val gameSize = "gameSize"
    val gameSizeRange = 3..7

    const val gamePlayersType = "gamePlayersType"
    const val firstPlayerName = "firstPlayerName"

    const val secondPlayerName = "secondPlayerName"
    const val firstPlayerShape = "firstPlayerShape"

    const val secondPlayerShape = "secondPlayerShape"
    const val aiDifficulty = "aiDifficulty"

    const val isSoundOn = "isSoundOn"
    const val isVibrationOn = "isVibrationOn"

    object Shapes {
        const val ringShape = "ringShape"
        const val circleShape = "circleShape"
        const val xShape = "xShape"
        const val triangleShape = "triangleShape"
        const val rectangleShape = "rectangleShape"
    }

    val difficulties = listOf(AiDifficulty.Hard, AiDifficulty.Medium, AiDifficulty.Easy)

    val aiPlayDelayRange = 250L..750L

    val diceRange = 1..6
}