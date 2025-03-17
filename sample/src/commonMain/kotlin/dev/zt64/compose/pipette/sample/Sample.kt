package dev.zt64.compose.pipette.sample

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SwitchLeft
import androidx.compose.material.icons.filled.SwitchRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import dev.zt64.compose.pipette.ColorCircle
import dev.zt64.compose.pipette.ColorRing
import dev.zt64.compose.pipette.ColorSquare
import dev.zt64.compose.pipette.util.hsvValue
import dev.zt64.compose.pipette.util.hue
import dev.zt64.compose.pipette.util.saturation

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun Sample() {
    var color by rememberSaveable(
        stateSaver = listSaver(
            save = { listOf(it.toArgb()) },
            restore = { Color(it[0]) }
        )
    ) {
        mutableStateOf(Color.Red)
    }
    var theme by rememberSaveable { mutableStateOf(Theme.SYSTEM) }
    var useDynamicTheme by rememberSaveable { mutableStateOf(false) }

    Theme(
        color = color,
        theme = theme,
        useDynamicTheme = useDynamicTheme
    ) {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    title = { Text("Compose Pipette Sample") },
                    actions = {
                        var expanded by remember { mutableStateOf(false) }
                        val uriHandler = LocalUriHandler.current

                        IconButton(
                            onClick = {
                                uriHandler.openUri("https://github.com/zt64/compose-pipette")
                            }
                        ) {
                            Icon(
                                imageVector = GithubIcon,
                                contentDescription = null
                            )
                        }

                        IconButton(
                            onClick = { useDynamicTheme = !useDynamicTheme }
                        ) {
                            Icon(
                                imageVector = if (useDynamicTheme) Icons.Default.SwitchLeft else Icons.Default.SwitchRight,
                                contentDescription = null
                            )
                        }

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
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = {
                        val r = (0..255).random()
                        val g = (0..255).random()
                        val b = (0..255).random()

                        color = Color(r, g, b)
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null
                        )
                    },
                    text = {
                        Text("Randomize")
                    }
                )
            }
        ) { paddingValues ->
            val first = remember {
                movableContentOf {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.height(IntrinsicSize.Min)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxHeight()
                            ) {
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
                                    .width(100.dp)
                                    .fillMaxHeight()
                                    .background(color, MaterialTheme.shapes.medium)
                            )
                        }

                        Column {
                            Text(
                                text = "Hue",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(Modifier.height(8.dp))

                            val hue by remember(color) {
                                derivedStateOf { color.hue }
                            }
                            Slider(
                                value = hue,
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

                            Spacer(Modifier.height(8.dp))

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

                            Spacer(Modifier.height(8.dp))

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

            val second = remember {
                movableContentOf {
                    FlowRow(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Color Circle")

                            Spacer(Modifier.height(6.dp))

                            ColorCircle(
                                color = color,
                                onColorChange = { color = it }
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Standard Color Picker")

                            Spacer(Modifier.height(6.dp))

                            ColorSquare(
                                color = color,
                                onColorChange = { color = it }
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Color Ring")

                            Spacer(Modifier.height(6.dp))

                            ColorRing(
                                color = color,
                                onColorChange = { color = it }
                            )
                        }
                    }
                }
            }

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp)
            ) {
                when {
                    maxWidth < 1000.dp -> {
                        Column(
                            modifier = Modifier
                                .matchParentSize()
                                .verticalScroll(rememberScrollState())
                        ) {
                            first()
                            second()
                        }
                    }

                    else -> {
                        Row {
                            first()
                            second()
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
    val interactionSource = remember { MutableInteractionSource() }
    Slider(
        value = value,
        onValueChange = onValueChange,
        thumb = {
            val interactions = remember { mutableStateListOf<Interaction>() }
            LaunchedEffect(interactionSource) {
                interactionSource.interactions.collect { interaction ->
                    when (interaction) {
                        is PressInteraction.Press -> interactions.add(interaction)
                        is PressInteraction.Release -> interactions.remove(interaction.press)
                        is PressInteraction.Cancel -> interactions.remove(interaction.press)
                        is DragInteraction.Start -> interactions.add(interaction)
                        is DragInteraction.Stop -> interactions.remove(interaction.start)
                        is DragInteraction.Cancel -> interactions.remove(interaction.start)
                    }
                }
            }

            val size = if (interactions.isNotEmpty()) 28.dp else 24.dp
            Spacer(
                Modifier
                    .size(size)
                    .hoverable(interactionSource = interactionSource)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
            )
        },
        track = {
            Canvas(
                modifier = Modifier
                    .widthIn(max = 700.dp)
                    .height(12.dp)
                    .fillMaxWidth()
            ) {
                drawLine(
                    brush = brush,
                    start = Offset(0f, size.center.y),
                    end = Offset(size.width, size.center.y),
                    strokeWidth = size.height,
                    cap = StrokeCap.Round
                )
            }
        },
        valueRange = valueRange,
        interactionSource = interactionSource
    )
}