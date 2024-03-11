package dev.zt64.compose.color

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * TODO: Add documentation
 *
 * @param color
 * @param onColorChange
 * @param modifier
 * @param interactionSource
 * @param onColorChangeFinished
 */
@Composable
public fun ColorWell(
    color: Color,
    onColorChange: (Color) -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onColorChangeFinished: () -> Unit = {}
) {
}