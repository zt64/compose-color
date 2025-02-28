package dev.zt64.compose.color

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import dev.zt64.compose.color.util.hsvValue
import dev.zt64.compose.color.util.hue
import dev.zt64.compose.color.util.saturation

@Composable
public fun StandardColorPicker(
    color: Color,
    onColorChange: (Color) -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    magnifier: @Composable () -> Unit = {
        ColorPickerDefaults.Magnifier(color, interactionSource)
    },
    onColorChangeFinished: () -> Unit = {}
) {
    Column {
        var size by remember { mutableStateOf(IntSize.Zero) }
        var offset by rememberSaveable(
            color,
            stateSaver = listSaver(
                save = { listOf(it.x, it.y) },
                restore = { (x, y) -> Offset(x, y) }
            )
        ) {
            mutableStateOf(
                positionForColor(
                    color,
                    size
                )
            )
        }

        Box {
            Canvas(
                modifier = modifier
                    .onSizeChanged {
                        size = it
                    }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = {
                                offset = it
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                offset += dragAmount
                                onColorChange(
                                    colorForPosition(
                                        offset,
                                        IntSize(size.width, size.height),
                                        color.hue
                                    )
                                )
                            },
                            onDragEnd = onColorChangeFinished
                        )
                    }
            ) {
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

            Box(
                modifier = Modifier
                    .offset(
                        x = offset.x.dp - 8.dp,
                        y = offset.y.dp - 8.dp
                    )
            ) {
                magnifier()
            }
        }
    }
}

private fun positionForColor(color: Color, size: IntSize): Offset {
    val x = color.saturation * size.width
    val y = size.height - color.hsvValue * size.height
    return Offset(x, y)
}

private fun colorForPosition(position: Offset, size: IntSize, hue: Float): Color {
    if (position.x < 0f || position.x > size.width || position.y < 0f || position.y > size.height) {
        return Color.Unspecified
    }
    val saturation = position.x / size.width
    val value = 1f - (position.y / size.height)
    return Color.hsv(
        hue = hue,
        saturation = saturation,
        value = value
    )
}