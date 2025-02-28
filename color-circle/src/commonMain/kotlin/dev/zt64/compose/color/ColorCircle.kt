package dev.zt64.compose.color

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import dev.zt64.compose.color.util.hsvValue
import dev.zt64.compose.color.util.hue
import dev.zt64.compose.color.util.saturation
import kotlinx.coroutines.launch
import kotlin.math.*

/**
 * Color circle that allows the user to select a hue by dragging a magnifier around the circle.
 * The color is represented in HSV color space with a fixed saturation and value.
 *
 * @param color The current color
 * @param onColorChange Callback that is called when the color changes
 * @param modifier
 * @param interactionSource
 * @param onColorChangeFinished Callback that is called when the user finishes changing the color
 * @param magnifier Composable that is used to draw the magnifier
 */
@Composable
public fun ColorCircle(
    color: Color,
    onColorChange: (Color) -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onColorChangeFinished: () -> Unit = {},
    magnifier: @Composable (Color) -> Unit = {
        ColorPickerDefaults.Magnifier(
            color = color,
            interactionSource = interactionSource
        )
    }
) {
    val scope = rememberCoroutineScope()
    val color by rememberUpdatedState(color)
    var radius by rememberSaveable {
        mutableStateOf(0f)
    }
    var offsetX by rememberSaveable(color) {
        mutableStateOf(positionForColor(color, radius).x)
    }
    var offsetY by rememberSaveable(color) {
        mutableStateOf(positionForColor(color, radius).y)
    }

    Box(
        modifier = modifier
            .size(100.dp)
            .onGloballyPositioned {
                radius = it.size.width.toFloat() / 2f
            }
            .pointerInput(Unit) {
                detectTapGestures { tapPosition ->
                    val newColor = colorForPosition(tapPosition, radius, color.hsvValue)
                    if (newColor.isSpecified) {
                        offsetX = tapPosition.x
                        offsetY = tapPosition.y
                        onColorChange(newColor)
                    }
                }
            }
            .pointerInput(Unit) {
                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false)

                    val press = PressInteraction.Press(down.position)

                    scope.launch {
                        interactionSource.emit(press)
                    }

                    drag(down.id) { change ->
                        change.consume()
                        val newPosition = clampPositionToRadius(change.position, radius)
                        val newColor = colorForPosition(newPosition, radius, color.hsvValue)
                        if (newColor.isSpecified) {
                            offsetX = newPosition.x
                            offsetY = newPosition.y
                            onColorChange(newColor)
                        }
                    }

                    scope.launch {
                        interactionSource.emit(PressInteraction.Release(press))
                    }

                    onColorChangeFinished()
                }
            }
    ) {
        ColorCircle(
            modifier = Modifier.matchParentSize(),
            value = color.hsvValue
        )

        Box(
            modifier = Modifier.offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
        ) {
            magnifier(color)
        }
    }
}

@Composable
internal fun ColorCircle(value: Float, modifier: Modifier = Modifier) {
    val brush = remember(value) {
        Brush.sweepGradient(
            colors = List(7) { i ->
                Color.hsv(
                    hue = i * 60f,
                    saturation = 1f,
                    value = value
                )
            }
        )
    }

    Canvas(modifier) {
        drawCircle(brush)
        drawCircle(Brush.radialGradient(listOf(Color.hsv(0f, 0f, value), Color.Transparent)))
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

private fun colorForPosition(position: Offset, radius: Float, value: Float): Color {
    val xOffset = position.x - radius
    val yOffset = position.y - radius

    val centerOffset = hypot(xOffset, yOffset)

    if (centerOffset > radius) return Color.Unspecified

    val degrees = atan2(yOffset, xOffset).toDegrees()
    val centerAngle = (degrees + 360.0) % 360.0

    return Color.hsv(
        hue = centerAngle.toFloat(),
        saturation = centerOffset / radius,
        value = value,
        alpha = 1.0f
    )
}

private fun positionForColor(color: Color, radius: Float): Offset {
    val saturation = color.saturation

    val angle = color.hue.toRadians()
    val x = radius + saturation * radius * cos(angle)
    val y = radius + saturation * radius * sin(angle)

    return Offset(x.toFloat(), y.toFloat())
}

private fun Float.toRadians(): Double = this * PI / 180

private fun Float.toDegrees(): Double = this * 180 / PI

private fun clampPositionToRadius(position: Offset, radius: Float): Offset {
    val xOffset = position.x - radius
    val yOffset = position.y - radius

    val centerOffset = hypot(xOffset, yOffset)

    // If the position is outside the circle, adjust it to be on the edge in the same direction
    if (centerOffset > radius) {
        val scale = radius / centerOffset
        return Offset((xOffset * scale) + radius, (yOffset * scale) + radius)
    }

    // If the position is inside the circle, return it as is
    return position
}