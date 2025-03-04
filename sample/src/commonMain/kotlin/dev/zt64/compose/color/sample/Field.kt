package dev.zt64.compose.color.sample

import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.zt64.compose.color.util.*
import kotlin.math.roundToInt

@OptIn(ExperimentalStdlibApi::class)
@Composable
fun HexField(color: Color, onColorChange: (Color) -> Unit) {
    val r by remember(color) { derivedStateOf { (color.red * 255).toInt() } }
    val g by remember(color) { derivedStateOf { (color.green * 255).toInt() } }
    val b by remember(color) { derivedStateOf { (color.blue * 255).toInt() } }
    val hex by remember(color) {
        derivedStateOf {
            buildString {
                append("#")
                append(r.toString(16).padStart(2, '0'))
                append(g.toString(16).padStart(2, '0'))
                append(b.toString(16).padStart(2, '0'))
            }.uppercase()
        }
    }

    FormatField(
        label = "Hex",
        value = hex,
        onValueChange = {
            if (it.isNotEmpty()) {
                val long = try {
                    it.removePrefix("#").toLong(16)
                } catch (_: Exception) {
                    return@FormatField false
                }
                onColorChange(Color(long))
            }

            true
        }
    )
}

@Composable
fun RgbField(color: Color, onColorChange: (Color) -> Unit) {
    val r by remember(color) {
        derivedStateOf { (color.red * 255).toInt() }
    }
    val g by remember(color) {
        derivedStateOf { (color.green * 255).toInt() }
    }
    val b by remember(color) {
        derivedStateOf { (color.blue * 255).toInt() }
    }

    val rgbString = remember(r, g, b) { "$r, $g, $b" }

    FormatField(
        label = "RGB",
        value = rgbString,
        onValueChange = {
            val values = it.split(",").map { it.trim().toIntOrNull() }

            if (values.size == 3 && values.all { it != null }) {
                val (red, green, blue) = values.map { it!! }
                onColorChange(Color(red, green, blue))

                true
            } else {
                false
            }
        }
    )
}

@Composable
fun HsvField(color: Color, onColorChange: (Color) -> Unit) {
    val h by remember(color) { derivedStateOf { color.hue.roundToInt() } }
    val s by remember(color) { derivedStateOf { (color.saturation * 100).roundToInt() } }
    val v by remember(color) { derivedStateOf { (color.hsvValue * 100).roundToInt() } }
    val hsvString = remember(h, s, v) { "$h°, $s%, $v%" }

    FormatField(
        label = "HSV",
        value = hsvString,
        onValueChange = {
            val values = it.split(",").map { it.trim().toFloatOrNull() }

            if (values.size == 3 && values.all { it != null }) {
                val (hue, saturation, value) = values.map { it!! }
                onColorChange(Color.hsv(hue, saturation, value))

                true
            } else {
                false
            }
        }
    )
}

@Composable
fun HslField(color: Color, onColorChange: (Color) -> Unit) {
    val h by remember(color) { derivedStateOf { color.hue.roundToInt() } }
    val s by remember(color) { derivedStateOf { (color.hslSaturation * 100).roundToInt() } }
    val l by remember(color) { derivedStateOf { (color.lightness * 100).roundToInt() } }
    val hslString = remember(h, s, l) { "$h°, $s%, $l%" }

    FormatField(
        label = "HSL",
        value = hslString,
        onValueChange = {
            try {
                val (hue, saturation, lightness) = it
                    .split(",")
                    .map { it.dropLast(1).trim().toFloatOrNull() }

                if (hue != null && saturation != null && lightness != null) {
                    onColorChange(Color.hsl(hue, saturation, lightness))
                }
            } catch (_: Exception) {
                return@FormatField false
            }
            true
        }
    )
}

@Composable
fun FormatField(label: String, value: String, onValueChange: (String) -> Boolean, modifier: Modifier = Modifier) {
    var isError by rememberSaveable { mutableStateOf(false) }

    TextField(
        modifier = modifier
            .widthIn(min = 180.dp, max = 240.dp)
            .onFocusChanged {
                if (!it.isFocused) isError = false
            },
        value = value,
        onValueChange = {
            isError = !onValueChange(it)
        },
        label = {
            Text(label)
        },
        supportingText = if (isError) {
            { Text("Invalid $label") }
        } else {
            null
        },
        isError = isError,
        singleLine = true
    )
}