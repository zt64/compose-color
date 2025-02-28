package dev.zt64.compose.color

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

internal val MagnifierRadiusPressed = 20.dp
internal val MagnifierRadius = 10.dp

public object ColorPickerDefaults {
    @Composable
    public fun Magnifier(color: Color, interactionSource: MutableInteractionSource, modifier: Modifier = Modifier) {
        val isPressed by interactionSource.collectIsPressedAsState()
        val radius by animateDpAsState(
            targetValue = if (isPressed) MagnifierRadiusPressed else MagnifierRadius
        )

        Canvas(modifier = modifier) {
            drawCircle(color, radius = radius.toPx())

            // Draw the border
            drawCircle(
                color = Color.Black,
                radius = radius.toPx(),
                alpha = 0.5f,
                style = androidx.compose.ui.graphics.drawscope.Stroke(1.dp.toPx())
            )
        }
    }
}