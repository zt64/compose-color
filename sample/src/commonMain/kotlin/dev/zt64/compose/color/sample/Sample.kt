package dev.zt64.compose.color.sample

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import dev.zt64.compose.color.ColorCircle
import dev.zt64.compose.color.ColorRing
import dev.zt64.compose.color.StandardColorPicker
import dev.zt64.compose.color.util.hsvValue
import dev.zt64.compose.color.util.hue
import dev.zt64.compose.color.util.saturation

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
                    title = { Text("Compose Color Sample") },
                    actions = {
                        var expanded by remember { mutableStateOf(false) }

                        Box {
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                Theme.entries.forEach {
                                    DropdownMenuItem(
                                        text = { Text(it.label) },
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
            Box {
                Row(
                    modifier = Modifier
                        .width(900.dp)
                        .padding(paddingValues)
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        Row {
                            Text("Color Circle")

                            Spacer(Modifier.weight(1f))

                            ColorCircle(
                                color = color,
                                onColorChange = { color = it }
                            )
                        }

                        Row {
                            Text("Standard Color Picker")

                            Spacer(Modifier.weight(1f))

                            StandardColorPicker(
                                modifier = Modifier.size(100.dp),
                                color = color,
                                onColorChange = { color = it }
                            )
                        }

                        Row {
                            Text("Color Ring")

                            Spacer(Modifier.weight(1f))

                            ColorRing(
                                color = color,
                                onColorChange = { color = it }
                            )
                        }

                        // Row {
                        //     Text("Minimal Color Well")
                        //
                        //     Spacer(Modifier.weight(1f))
                        //
                        //     MinimalColorWell(
                        //         color = color,
                        //         colors = listOf(
                        //             listOf(Color.Red, Color.Green, Color.Blue),
                        //             listOf(Color.Cyan, Color.Magenta, Color.Yellow)
                        //         ),
                        //         onColorChange = { color = it }
                        //     )
                        // }
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
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

                        Column {
                            Text(
                                text = "Hue",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(Modifier.height(12.dp))

                            Slider(
                                value = color.hue,
                                onValueChange = {
                                    color = Color.hsv(it, color.saturation, color.hsvValue)
                                },
                                valueRange = 0f..359f,
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
                                )
                            )
                        }

                        Column {
                            Text(
                                text = "Saturation",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(Modifier.height(12.dp))

                            Slider(
                                value = color.saturation,
                                onValueChange = {
                                    color = Color.hsv(color.hue, it, color.hsvValue)
                                },
                                valueRange = 0f..1f,
                                brush = Brush.horizontalGradient(
                                    listOf(
                                        Color.White,
                                        Color.hsv(color.hue, 1f, color.hsvValue)
                                    )
                                )
                            )
                        }

                        Column {
                            Text(
                                text = "Value",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(Modifier.height(12.dp))

                            Slider(
                                value = color.hsvValue,
                                onValueChange = {
                                    color = Color.hsv(color.hue, color.saturation, it)
                                },
                                valueRange = 0f..1f,
                                brush = Brush.horizontalGradient(
                                    listOf(
                                        Color.Black,
                                        Color.hsv(color.hue, color.saturation, 1f)
                                    )
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Slider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    brush: Brush
) {
    Slider(
        value = value,
        onValueChange = onValueChange,
        thumb = {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(Color.White, CircleShape)
            )
        },
        track = {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
            ) {
                drawLine(
                    brush = brush,
                    start = Offset(0f, size.center.y),
                    end = Offset(size.width, size.center.y),
                    strokeWidth = size.height.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        },
        valueRange = valueRange
    )
}