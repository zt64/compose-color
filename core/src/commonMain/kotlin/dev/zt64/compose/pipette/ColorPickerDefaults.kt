package dev.zt64.compose.pipette

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

internal val ThumbRadiusPressed = 14.dp
internal val ThumbRadius = 10.dp

public object ColorPickerDefaults {
    /**
     * Default implementation of the thumb component for the color pickers.
     *
     * @param color The color of the thumb
     * @param interactionSource The interaction source for the thumb
     * @param modifier The modifier for the thumb
     */
    @Composable
    public fun Thumb(color: Color, interactionSource: MutableInteractionSource, modifier: Modifier = Modifier) {
        val isPressed by interactionSource.collectIsPressedAsState()
        val isDragged by interactionSource.collectIsDraggedAsState()
        val radius by animateDpAsState(
            targetValue = if (isPressed || isDragged) ThumbRadiusPressed else ThumbRadius
        )

        Canvas(modifier = modifier) {
            drawCircle(color, radius = radius.toPx())

            // Draw the border
            drawCircle(
                color = Color.Black,
                radius = radius.toPx(),
                alpha = 0.5f,
                style = Stroke(1.dp.toPx())
            )
        }
    }
}