package dev.zt64.compose.color.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.dp
import dev.zt64.compose.color.util.hsvValue
import dev.zt64.compose.color.util.hue
import dev.zt64.compose.color.util.saturation

@Composable
public fun HueSlider(
    color: Color,
    onColorChange: (Color) -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    thumb: @Composable () -> Unit = {},
    onColorChangeFinished: () -> Unit = {}
) {
    HueSlider(
        hue = color.hue,
        saturation = color.saturation,
        value = color.hsvValue,
        onHueChange = { hue ->
            onColorChange(Color.hsv(hue, color.saturation, color.hsvValue))
        },
        modifier = modifier,
        interactionSource = interactionSource,
        thumb = thumb,
        onHueChangeFinished = onColorChangeFinished
    )
}

/**
 * A slider that allows the user to select a hue.
 *
 * @param hue
 * @param saturation
 * @param value
 * @param onHueChange
 * @param modifier
 * @param interactionSource
 * @param thumb
 * @param onHueChangeFinished
 */
@Composable
public fun HueSlider(
    hue: Float,
    saturation: Float,
    value: Float,
    onHueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    thumb: @Composable () -> Unit = {},
    onHueChangeFinished: () -> Unit = {}
) {
    val brush by remember(hue, saturation, value) {
        derivedStateOf {
            Brush.horizontalGradient(
                List(6) { i ->
                    Color.hsv(i * 60f, saturation, value)
                }
            )
        }
    }

    Canvas(
        modifier = Modifier
            .progressSemantics(
                value = hue,
                valueRange = 0f..360f
            ).size(
                width = 360.dp,
                height = 16.dp
            ).draggable(
                state = rememberDraggableState {
                    onHueChange((hue + it).coerceIn(0f, 360f))
                },
                orientation = Orientation.Horizontal,
                interactionSource = interactionSource,
                onDragStarted = {
                    onHueChange(it.x)
                },
                onDragStopped = { onHueChangeFinished() }
            ).then(modifier)
    ) {
        drawLine(
            brush = brush,
            start = Offset(0f, center.y),
            end = Offset(size.width, center.y),
            strokeWidth = size.height,
            cap = StrokeCap.Round
        )

        val thumbRadius = size.height / 2
        val thumbPosition = Offset(hue, size.height / 2f)

        drawCircle(
            color = Color.Black,
            center = thumbPosition,
            radius = thumbRadius
        )

        drawCircle(
            color = Color.hsv(hue, saturation, value).compositeOver(Color.White),
            center = thumbPosition,
            radius = thumbRadius - 2.dp.toPx()
        )
    }
}