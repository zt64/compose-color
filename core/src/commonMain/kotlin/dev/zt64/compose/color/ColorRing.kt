package dev.zt64.compose.color

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toOffset
import dev.zt64.compose.color.util.hsvValue
import dev.zt64.compose.color.util.hue
import dev.zt64.compose.color.util.saturation
import kotlin.math.*

/**
 * A color ring that allows the user to select a hue by rotating a handle around the ring. The color
 * ring is a continuous gradient of colors from red to red.
 *
 * To be able to also control the saturation, use the [ColorCircle] composable.
 *
 * @sample dev.zt64.compose.color.samples.ColorRingSample
 *
 * @param color
 * @param onColorChange
 * @param modifier
 * @param interactionSource
 * @param onColorChangeFinished
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
public fun ColorRing(
    color: Color,
    onColorChange: (Color) -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    thumb: @Composable () -> Unit = {
    },
    onColorChangeFinished: () -> Unit = {}
) {
    val color by rememberUpdatedState(color)
    var strokeWidth = remember { 16f }
    var radius by remember { mutableStateOf(0f) }
    var center by remember { mutableStateOf(Offset.Zero) }
    var handleCenter by remember {
        mutableStateOf(Offset.Zero)
    }

    fun updateHandlePosition(angle: Double) {
        val rad = angle * PI / 180
        handleCenter = center + Offset(
            x = (radius + strokeWidth / 2) * cos(rad).toFloat(),
            y = (radius + strokeWidth / 2) * sin(rad).toFloat()
        )
    }

    val brush = remember(color) {
        Brush.sweepGradient(
            List(7) {
                Color.hsv(
                    hue = (it * 60).toFloat(),
                    saturation = color.saturation,
                    value = color.hsvValue
                )
            }
        )
    }

    Canvas(
        modifier = modifier
            .size(100.dp)
            .onSizeChanged {
                radius = (it.width - strokeWidth) / 2f
                center = Offset(it.width / 2f, it.height / 2f)
                updateHandlePosition(color.hue.toDouble())
            }
            .pointerInput(Unit) {
                detectTapGestures {
                    handleCenter = Offset.Zero + it
                    onColorChange(
                        Color.hsv(
                            hue = getRotationAngle(it, size.center.toOffset()).toFloat(),
                            saturation = color.saturation,
                            value = color.hsvValue
                        )
                    )
                    onColorChangeFinished()
                }
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = onColorChangeFinished
                ) { change, _ ->
                    val angle = getRotationAngle(change.position, center)
                    updateHandlePosition(angle)
                    onColorChange(
                        Color.hsv(
                            hue = angle.toFloat(),
                            saturation = color.saturation,
                            value = color.hsvValue
                        )
                    )
                    change.consume()
                }
            }
    ) {
        drawCircle(
            brush = brush,
            style = Stroke(strokeWidth)
        )

        drawCircle(
            color = Color.White,
            radius = strokeWidth / 2f,
            center = handleCenter
        )
    }
}

private fun getRotationAngle(currentPosition: Offset, center: Offset): Double {
    val (dx, dy) = currentPosition - center
    val theta = atan2(dy, dx).toDouble()

    var angle = theta * (180.0 / PI)

    if (angle < 0) {
        angle += 360.0
    }
    return angle
}