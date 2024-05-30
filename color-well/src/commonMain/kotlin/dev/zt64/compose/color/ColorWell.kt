package dev.zt64.compose.color

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup

@Composable
public fun MinimalColorWell(
    color: Color,
    colors: List<List<Color>>,
    onColorChange: (Color) -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onColorChangeFinished: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .size(50.dp)
            .background(color)
            .clickable {
                onColorChange(color)
                onColorChangeFinished()
            }
    ) {
        ColorWell(
            color = color,
            colors = colors,
            onColorChange = onColorChange,
            interactionSource = interactionSource,
            onColorChangeFinished = onColorChangeFinished
        )
    }
}

@Composable
public fun ExpandedColorWell(
    color: Color,
    colors: List<List<Color>>,
    onColorChange: (Color) -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onColorChangeFinished: () -> Unit = {}
) {
}

/**
 * TODO: Add documentation
 *
 * @param color
 * @param colors
 * @param onColorChange
 * @param modifier
 * @param interactionSource
 * @param onColorChangeFinished
 */
@Composable
public fun ColorWell(
    color: Color,
    colors: List<List<Color>>,
    onColorChange: (Color) -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onColorChangeFinished: () -> Unit = {}
) {
    var showColorPalette by rememberSaveable { mutableStateOf(false) }

    if (showColorPalette) {
        DisposableEffect(Unit) {
            onDispose {
                onColorChangeFinished()
                showColorPalette = false
            }
        }

        Popup(
            onDismissRequest = { showColorPalette = false }
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(6)
            ) {
                items(
                    items = colors,
                    key = { it }
                ) { row ->
                    row.forEach { color ->
                        Box(
                            modifier = modifier
                                .size(50.dp)
                                .background(color)
                                .clickable { onColorChange(color) }
                        )
                    }
                }
            }
        }
    }
}