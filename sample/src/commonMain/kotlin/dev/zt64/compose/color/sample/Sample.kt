package dev.zt64.compose.color.sample

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import dev.zt64.compose.color.*
import dev.zt64.compose.color.component.HueSlider
import dev.zt64.compose.color.component.SaturationSlider
import dev.zt64.compose.color.util.*
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Sample() {
    var color by remember { mutableStateOf(Color.Red) }
    var theme by remember { mutableStateOf(Theme.SYSTEM) }

    Theme(
        color = color,
        theme = theme
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Compose Color") },
                    actions = {
                        var expanded by remember { mutableStateOf(false) }

                        Box {
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                Theme.entries.forEach {
                                    DropdownMenuItem(
                                        text = {
                                            Text(it.label)
                                        },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = it.icon,
                                                contentDescription = null
                                            )
                                        },
                                        onClick = {
                                            theme = it
                                            expanded = false
                                        }
                                    )
                                }
                            }

                            IconButton(
                                onClick = { expanded = true }
                            ) {
                                Icon(
                                    imageVector = theme.icon,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .width(400.dp)
                    .fillMaxHeight()
                    .padding(paddingValues)
                    .padding(horizontal = 14.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row {
                    Column {
                        HexField(
                            color = color,
                            onColorChange = { color = it }
                        )

                        RgbField(
                            color = color,
                            onColorChange = { color = it }
                        )

                        HsvField(
                            color = color,
                            onColorChange = { color = it }
                        )

                        HslField(
                            color = color,
                            onColorChange = { color = it }
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Box(
                        modifier = Modifier
                            .requiredSize(100.dp)
                            .background(color, MaterialTheme.shapes.medium)
                    )
                }

                Row {
                    ListItem(
                        modifier = Modifier.width(IntrinsicSize.Max),
                        headlineContent = {
                            Text("Color Circle")
                        }
                    )

                    Spacer(Modifier.weight(1f, true))

                    ColorCircle(
                        color = color,
                        onColorChange = { color = it },
                        onColorChangeFinished = {
                        }
                    )
                }

                Row {
                    ListItem(
                        modifier = Modifier.width(IntrinsicSize.Max),
                        headlineContent = {
                            Text("Standard Color Picker")
                        }
                    )

                    Spacer(Modifier.weight(1f, true))

                    StandardColorPicker(
                        modifier = Modifier.size(100.dp),
                        color = color,
                        onColorChange = { color = it }
                    )
                }

                Row {
                    ListItem(
                        modifier = Modifier.width(IntrinsicSize.Max),
                        headlineContent = {
                            Text("Color Ring")
                        }
                    )

                    Spacer(Modifier.weight(1f, true))

                    ColorRing(
                        color = color,
                        onColorChange = { color = it }
                    )
                }

                Row {
                    ListItem(
                        modifier = Modifier.width(IntrinsicSize.Max),
                        headlineContent = {
                            Text("Minimal Color Well")
                        }
                    )

                    Spacer(Modifier.weight(1f, true))

                    MinimalColorWell(
                        color = color,
                        colors = listOf(
                            listOf(Color.Red, Color.Green, Color.Blue),
                            listOf(Color.Cyan, Color.Magenta, Color.Yellow)
                        ),
                        onColorChange = { color = it }
                    )
                }

                Column {
                    ListItem(
                        headlineContent = {
                            Text(
                                text = "Hue",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    )

                    Spacer(Modifier.height(12.dp))

                    Slider(
                        value = color.hue,
                        onValueChange = { color = Color.hsv(it, color.saturation, color.hsvValue) },
                        track = {
                            Canvas(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                            ) {
                                drawLine(
                                    brush = Brush.horizontalGradient(
                                        listOf(
                                            Color.Red,
                                            Color.Yellow,
                                            Color.Green,
                                            Color.Cyan,
                                            Color.Blue,
                                            Color.Magenta,
                                            Color.Red
                                        )
                                    ),
                                    start = Offset(0f, size.center.y),
                                    end = Offset(size.width, size.center.y),
                                    strokeWidth = size.height,
                                    cap = StrokeCap.Round
                                )
                            }
                        },
                        valueRange = 0f..360f
                    )

                    HueSlider(
                        modifier = Modifier
                            .width(300.dp)
                            .padding(horizontal = 12.dp),
                        hue = color.hue,
                        saturation = color.saturation,
                        value = color.hsvValue,
                        onHueChange = { color = Color.hsv(it, color.saturation, color.hsvValue) }
                    )
                }

                Column {
                    ListItem(
                        headlineContent = {
                            Text(
                                text = "Saturation",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    )

                    Spacer(Modifier.height(12.dp))

                    Slider(
                        value = color.saturation,
                        onValueChange = { color = Color.hsv(color.hue, it, color.hsvValue) },
                        track = {
                            Canvas(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                            ) {
                                drawLine(
                                    brush = Brush.horizontalGradient(
                                        listOf(
                                            Color.White,
                                            Color.hsv(color.hue, 1f, color.hsvValue)
                                        )
                                    ),
                                    start = Offset(0f, size.center.y),
                                    end = Offset(size.width, size.center.y),
                                    strokeWidth = 4.dp.toPx(),
                                    cap = StrokeCap.Round
                                )
                            }
                        },
                        valueRange = 0f..1f
                    )

                    SaturationSlider(
                        modifier = Modifier.width(300.dp),
                        hue = color.hue,
                        saturation = color.saturation,
                        onSaturationChange = { color = Color.hsv(color.hue, it, color.hsvValue) }
                    )
                }

                Column {
                    ListItem(
                        headlineContent = {
                            Text(
                                text = "Value",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    )

                    Spacer(Modifier.height(12.dp))

                    Slider(
                        value = color.hsvValue,
                        onValueChange = { color = Color.hsv(color.hue, color.saturation, it) },
                        track = {
                            Canvas(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                            ) {
                                drawLine(
                                    brush = Brush.horizontalGradient(
                                        listOf(
                                            Color.Black,
                                            Color.hsv(color.hue, color.saturation, 1f)
                                        )
                                    ),
                                    start = Offset(0f, size.center.y),
                                    end = Offset(size.width, size.center.y),
                                    strokeWidth = 4.dp.toPx(),
                                    cap = StrokeCap.Round
                                )
                            }
                        },
                        valueRange = 0f..1f
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalStdlibApi::class)
@Composable
fun HexField(
    color: Color,
    onColorChange: (Color) -> Unit
) {
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
            }
        }
    }

    FormatField(
        label = "HEX",
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
fun RgbField(
    color: Color,
    onColorChange: (Color) -> Unit
) {
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
fun HsvField(
    color: Color,
    onColorChange: (Color) -> Unit
) {
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
fun HslField(
    color: Color,
    onColorChange: (Color) -> Unit
) {
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
fun FormatField(
    label: String,
    value: String,
    onValueChange: (String) -> Boolean,
    modifier: Modifier = Modifier
) {
    var isError by remember { mutableStateOf(false) }

    TextField(
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
        singleLine = true,
        modifier = modifier.width(IntrinsicSize.Max).onFocusChanged {
            if (!it.isFocused) {
                isError = false
            }
        }
    )
}