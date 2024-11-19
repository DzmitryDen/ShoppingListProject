package com.hfad.shoppinglist.settings_screen

import androidx.compose.ui.graphics.Color
import com.hfad.shoppinglist.ui.theme.Green
import com.hfad.shoppinglist.ui.theme.Red
import com.hfad.shoppinglist.ui.theme.Yellow

// список с цветами
object ColorUtils {
    val colorList = listOf(
        "#FF000000",
        "#487242",
        "#22b9a8",
        "#452e52",
        "#f28f93",
        "#ff00a1",
        "#041cf6",
        "#532a4a",
        "#774084",
        "#09cf6a",
        "#668096",
        "#E31FC2",
        "#0BEC6F"
    )

    fun getProgressColor(progress: Float): Color {
        return when (progress) {
            in 0.0..0.339 -> Red
            in 0.34..0.669 -> Yellow
            in 0.67..1.0 -> Green
            else -> Red
        }
    }
}