package dev.zt64.compose.color.util

import androidx.compose.ui.graphics.Color
import kotlin.test.Test
import kotlin.test.assertEquals

class ColorTest {
    @Test
    fun testHsl() {
        val color = Color.hsl(0f, 0f, 0f)
        assertEquals(0f, color.hue)
        assertEquals(0f, color.saturation)
        assertEquals(0f, color.lightness)

        val color2 = Color.hsl(0f, 1f, 0.5f)
        assertEquals(0f, color2.hue)
        assertEquals(1f, color2.saturation)
        assertEquals(0.5f, color2.lightness)

        val color3 = Color.hsl(120f, 1f, 0.5f)
        assertEquals(120f, color3.hue)
        assertEquals(1f, color3.saturation)
        assertEquals(0.5f, color3.lightness)
    }

    @Test
    fun testHsv() {
        val color = Color.hsv(0f, 0f, 0f)
        assertEquals(0f, color.hue)
        assertEquals(0f, color.saturation)
        assertEquals(0f, color.hsvValue)

        val color2 = Color.hsv(0f, 1f, 1f)
        assertEquals(0f, color2.hue)
        assertEquals(1f, color2.saturation)
        assertEquals(1f, color2.hsvValue)

        val color3 = Color.hsv(120f, 1f, 1f)
        assertEquals(120f, color3.hue)
        assertEquals(1f, color3.saturation)
        assertEquals(1f, color3.hsvValue)
    }
}