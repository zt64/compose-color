package dev.zt64.compose.color.sample

import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.materialkolor.DynamicMaterialTheme
import com.materialkolor.PaletteStyle

enum class Theme(val icon: ImageVector, val label: String) {
    LIGHT(Icons.Default.LightMode, "Light"),
    DARK(Icons.Default.DarkMode, "Dark"),
    SYSTEM(Icons.Default.Settings, "System")
}

@Composable
fun Theme(color: Color, theme: Theme, content: @Composable () -> Unit) {
    DynamicMaterialTheme(
        seedColor = color,
        useDarkTheme = theme == Theme.DARK || theme == Theme.SYSTEM && isSystemInDarkTheme(),
        style = PaletteStyle.Fidelity,
        content = content,
        animate = true,
        animationSpec = tween()
    )
}