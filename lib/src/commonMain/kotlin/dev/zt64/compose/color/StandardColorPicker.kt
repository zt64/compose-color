package dev.zt64.compose.color

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
public fun StandardColorPicker(
    color: Color,
    onColorChange: (Color) -> Unit,
    modifier: Modifier = Modifier,
    onColorChangeFinished: () -> Unit = {}
) {
    Canvas(modifier) {
        drawRect(Color.White)
        drawRect(Brush.horizontalGradient(listOf(Color.Transparent, color)))
        drawRect(Brush.verticalGradient(listOf(Color.Transparent, Color.Black)))
    }
}