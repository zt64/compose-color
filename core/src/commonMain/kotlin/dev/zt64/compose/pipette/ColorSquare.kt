package dev.zt64.compose.pipette

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
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
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * Standard color picker that allows the user to select a color by dragging a thumb around the color space.
 *
 * The color is represented in HSV color space with a fixed hue. The saturation and value can be controlled by
 * dragging the thumb.
 *
 * **Note** that the color is represented in HSV color space with a fixed hue. Androidx Compose Color cannot represent
 * colors in HSV color space. Therefore, the color must be passed as separate parameters.
 *
 * @param hue The hue of the color
 * @param saturation The saturation of the color
 * @param value The value of the color
 * @param onColorChange Callback that is called when the color changes
 * @param modifier The modifier to be applied to the color square
 * @param interactionSource The interaction source for the color square
 * @param thumb Composable that is used to draw the thumb
 * @param onColorChangeFinished Callback that is called when the user finishes changing the color
 */
@Composable
public fun ColorSquare(
    hue: Float,
    saturation: Float,
    value: Float,
    onColorChange: (hue: Float, saturation: Float, value: Float) -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    thumb: @Composable () -> Unit = {
        ColorPickerDefaults.Thumb(Color.hsv(hue, saturation, value), interactionSource)
    },
    onColorChangeFinished: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    var size by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = modifier
            .size(100.dp)
            .onSizeChanged { size = it }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        hsvColorForPosition(it, size, hue).let { (h, s, v) -> onColorChange(h, s, v) }
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
                        hsvColorForPosition(change.position, size, hue).let { (h, s, v) -> onColorChange(h, s, v) }
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
                                Color.hsv(hue, 1f, 1f)
                            )
                        )
                    )
                    drawRect(Brush.verticalGradient(listOf(Color.Transparent, Color.Black)))
                }
            }
    ) {
        val offset = remember(saturation, value, size) {
            val x = saturation * size.width
            val y = size.height - value * size.height
            Offset(x, y)
        }

        Box(
            modifier = Modifier.offset {
                IntOffset(offset.x.roundToInt(), offset.y.roundToInt())
            }
        ) {
            thumb()
        }
    }
}

private fun hsvColorForPosition(position: Offset, size: IntSize, hue: Float): Triple<Float, Float, Float> {
    val clampedX = position.x.coerceIn(0f, size.width.toFloat())
    val clampedY = position.y.coerceIn(0f, size.height.toFloat())

    val saturation = clampedX / size.width
    val value = 1f - (clampedY / size.height)

    return Triple(hue, saturation, value)
}