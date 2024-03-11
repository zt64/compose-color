package dev.zt64.compose.color

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.*

/**
 * TODO: Add documentation
 *
 * @param color
 * @param onColorChange
 * @param modifier
 * @param onColorChangeFinished
 */
@Composable
public fun ColorCircle(
    color: Color,
    onColorChange: (Color) -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onColorChangeFinished: () -> Unit = {},
    magnifier: @Composable (Color) -> Unit = { color ->
        ColorPickerDefaults.Magnifier(
            color = color,
            interactionSource = interactionSource
        )
    }
) {
    // val colorWheel = remember { ColorWheel(100) }

    var position by remember { mutableStateOf(Offset(0f, 0f)) }
    Box(
        modifier = modifier
            .size(100.dp)
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    val down = awaitFirstDown(requireUnconsumed = false)

                    drag(down.id) { change ->
                        if (change.positionChange() != Offset.Zero) change.consume()
                    }
                }
            }
    ) {
        ColorCircle(
            modifier = Modifier.matchParentSize()
        )
        Box(
            modifier = Modifier
                .size(10.dp)
                .offset {
                    val size = 100.dp.toPx()
                    val x = position.x.coerceIn(0f, size)
                    val y = position.y.coerceIn(0f, size)
                    IntOffset(x.roundToInt(), y.roundToInt())
                }
        ) {
            magnifier(color)
        }
    }
}

@Composable
internal fun ColorCircle(modifier: Modifier = Modifier) {
    val brush = remember {
        Brush.sweepGradient(
            colors = listOf(
                Color.hsv(0f, 1f, 1f),
                Color.hsv(60f, 1f, 1f),
                Color.hsv(120f, 1f, 1f),
                Color.hsv(180f, 1f, 1f),
                Color.hsv(240f, 1f, 1f),
                Color.hsv(300f, 1f, 1f),
                Color.hsv(360f, 1f, 1f),
                Color.hsv(0f, 1f, 1f)
            )
        )
    }

    Canvas(modifier) {
        drawCircle(brush)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color.White, Color.Transparent)
            )
        )
    }
}

private val MagnifierPopupShape = GenericShape { size, _ ->
    val width = size.width
    val height = size.height

    val arrowY = height * 0.8f
    val arrowXOffset = width * 0.4f

    addRoundRect(RoundRect(0f, 0f, width, arrowY, cornerRadius = CornerRadius(20f, 20f)))

    moveTo(arrowXOffset, arrowY)
    lineTo(width / 2f, height)
    lineTo(width - arrowXOffset, arrowY)
    close()
}

private fun colorForPosition(
    position: Offset,
    size: IntSize,
    value: Float
): Color? {
    val centerX = size.width / 2.0
    val centerY = size.height / 2.0
    val xOffset = position.x - centerX
    val yOffset = position.y - centerY

    val centerOffset = hypot(xOffset, yOffset)
    val radius = min(centerX, centerY)

    if (centerOffset > radius) return null

    val degrees = atan2(yOffset, xOffset) * 180 / PI
    val centerAngle = (degrees + 360.0) % 360.0

    return Color.hsv(
        hue = centerAngle.toFloat(),
        saturation = (centerOffset / radius).toFloat(),
        value = value,
        alpha = 1.0f
    )
}