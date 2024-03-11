package dev.zt64.compose.color

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
public fun AlphaSlider(
    alpha: Float,
    onAlphaChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    onAlphaChangeFinished: () -> Unit = {}
) {
}