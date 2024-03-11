package dev.zt64.compose.color

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*

@Composable
public fun HueSlider(
    hue: Float,
    saturation: Float,
    value: Float,
    onHueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    onHueChangeFinished: () -> Unit = {}
) {
    Canvas(modifier) {
        drawLine(
            brush = Brush.horizontalGradient(
                listOf(
                    Color.hsv(0f, saturation, value),
                    Color.hsv(60f, saturation, value),
                    Color.hsv(120f, saturation, value),
                    Color.hsv(180f, saturation, value),
                    Color.hsv(240f, saturation, value),
                    Color.hsv(300f, saturation, value),
                    Color.hsv(360f, saturation, value)
                )
            ),
            start = Offset.Zero,
            end = Offset(size.width, 0f),
            strokeWidth = 10f
        )
        drawCircle(
            brush = SolidColor(Color.hsv(hue, saturation, value).compositeOver(Color.White)),
            center = Offset(size.width / 2, 0f),
            radius = 10f
        )
    }
}