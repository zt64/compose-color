package dev.zt64.compose.pipette

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import dev.zt64.compose.pipette.util.hsvValue
import dev.zt64.compose.pipette.util.hue
import dev.zt64.compose.pipette.util.saturation
import kotlinx.coroutines.launch

/**
 * Standard color picker that allows the user to select a color by dragging a thumb around the color space.
 *
 * The color is represented in HSV color space with a fixed hue. The saturation and value can be controlled by
 * dragging the thumb.
 *
 * @param color The current color
 * @param onColorChange Callback that is called when the color changes
 * @param modifier The modifier to be applied to the color square
 * @param interactionSource The interaction source for the color square
 * @param thumb Composable that is used to draw the thumb
 * @param onColorChangeFinished Callback that is called when the user finishes changing the color
 */
@Composable
public fun ColorSquare(
    color: Color,
    onColorChange: (Color) -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    thumb: @Composable () -> Unit = {
        ColorPickerDefaults.Thumb(color, interactionSource)
    },
    onColorChangeFinished: () -> Unit = {}
) {
    val color by rememberUpdatedState(color)
    val scope = rememberCoroutineScope()
    var size by remember { mutableStateOf(IntSize.Zero) }
    val offset by rememberSaveable(
        color,
        size,
        stateSaver = listSaver(
            save = { listOf(it.x, it.y) },
            restore = { (x, y) -> Offset(x, y) }
        )
    ) {
        mutableStateOf(positionForColor(color, size))
    }

    Box(
        modifier = modifier
            .size(100.dp)
            .onSizeChanged { size = it }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        onColorChange(colorForPosition(it, size, color.hue))
                        onColorChangeFinished()
                    }
                )
            }
            .pointerInput(Unit) {
                var interaction: DragInteraction.Start? = null

                detectDragGestures(
                    onDragStart = {
                        scope.launch {
                            interaction = DragInteraction.Start()
                            interactionSource.emit(interaction)
                        }
                    },
                    onDrag = { change, _ ->
                        onColorChange(colorForPosition(change.position, size, color.hue))
                    },
                    onDragEnd = {
                        scope.launch {
                            interaction?.let {
                                interactionSource.emit(DragInteraction.Stop(it))
                            }
                        }
                        onColorChangeFinished()
                    },
                    onDragCancel = {
                        scope.launch {
                            interaction?.let {
                                interactionSource.emit(DragInteraction.Cancel(it))
                            }
                        }
                    }
                )
            }
            .drawWithCache {
                onDrawBehind {
                    drawRect(Color.White)
                    drawRect(
                        Brush.horizontalGradient(
                            listOf(
                                Color.Transparent,
                                Color.hsv(color.hue, 1f, 1f)
                            )
                        )
                    )
                    drawRect(Brush.verticalGradient(listOf(Color.Transparent, Color.Black)))
                }
            }
    ) {
        Box(
            modifier = Modifier.offset {
                IntOffset(offset.x.toInt(), offset.y.toInt())
            }
        ) {
            thumb()
        }
    }
}

private fun positionForColor(color: Color, size: IntSize): Offset {
    val x = color.saturation * size.width
    val y = size.height - color.hsvValue * size.height
    return Offset(x, y)
}

private fun colorForPosition(position: Offset, size: IntSize, hue: Float): Color {
    val clampedX = position.x.coerceIn(0f, size.width.toFloat())
    val clampedY = position.y.coerceIn(0f, size.height.toFloat())

    return Color.hsv(
        hue = hue,
        saturation = clampedX / size.width,
        value = 1f - (clampedY / size.height)
    )
}