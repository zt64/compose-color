package dev.zt64.compose.color

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

@Composable
public fun BrightnessSlider(
    brightness: Float,
    onBrightnessChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    onBrightnessChangeFinished: () -> Unit = {}
) {
    Canvas(modifier) {
        drawLine(
            color = Color.Black,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            strokeWidth = 10f
        )
    }
}