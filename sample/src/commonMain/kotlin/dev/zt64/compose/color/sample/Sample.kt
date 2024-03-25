package dev.zt64.compose.color.sample

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import dev.zt64.compose.color.ColorCircle
import dev.zt64.compose.color.StandardColorPicker
import dev.zt64.compose.color.component.HueSlider
import dev.zt64.compose.color.util.hue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Sample() {
    var color by remember { mutableStateOf(Color.Red) }
    var theme by remember { mutableStateOf(Theme.SYSTEM) }

    Theme(
        color = color,
        theme = theme
    ) {
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
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
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ColorCircle(
                    color = color,
                    onColorChange = { color = it },
                    onColorChangeFinished = {
                    }
                )

                StandardColorPicker(
                    modifier = Modifier.size(100.dp),
                    color = color,
                    onColorChange = { color = it }
                )

                HueSlider(
                    modifier = Modifier.width(300.dp),
                    hue = color.hue,
                    saturation = 1f,
                    value = 0.5f,
                    onHueChange = { color = Color.hsv(it, 1f, 0.5f) }
                )
            }
        }
    }
}