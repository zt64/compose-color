package dev.zt64.compose.color.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.progressSemantics
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
public fun SaturationSlider(
    color: Color,
    onColorChange: (Color) -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onColorChangeFinished: () -> Unit = {}
) {
}

@Composable
public fun SaturationSlider(
    hue: Float,
    saturation: Float,
    onSaturationChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onSaturationChangeFinished: () -> Unit = {}
) {
    Canvas(
        modifier = modifier.progressSemantics(
            value = saturation
        )
    ) {
    }
}