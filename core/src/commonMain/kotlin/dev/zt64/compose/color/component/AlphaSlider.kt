package dev.zt64.compose.color.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.progressSemantics
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
public fun AlphaSlider(
    alpha: Float,
    onAlphaChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onAlphaChangeFinished: () -> Unit = {}
) {
    Canvas(
        modifier = modifier.progressSemantics(
            value = alpha
        )
    ) {
    }
}