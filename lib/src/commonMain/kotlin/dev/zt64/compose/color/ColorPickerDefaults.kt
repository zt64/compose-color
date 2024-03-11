package dev.zt64.compose.color

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

public object ColorPickerDefaults {
    @Composable
    public fun Magnifier(
        color: Color,
        interactionSource: InteractionSource,
        modifier: Modifier = Modifier
    ) {
        Canvas(modifier = modifier) {
            drawCircle(color, radius = 20f)
        }
    }
}