package dev.zt64.compose.pipette

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import dev.zt64.compose.pipette.util.hsvValue
import dev.zt64.compose.pipette.util.hue
import dev.zt64.compose.pipette.util.saturation
import kotlin.test.Test
import kotlin.test.assertEquals

class ColorUtilTest {
    @Test
    fun testHsv() {
        testHsv(0f, 0f, 0f)
        testHsv(0f, 1f, 1f)
        testHsv(120f, 1f, 1f)
    }

    @Test
    fun testHsvSaturationModification() {
        val colorFromAgrb = Color(-13194864)
        val color2 = Color(54, 169, 144)

        assertEquals(colorFromAgrb, color2)

        val colorFromAgrb2 = Color(-8541793)
        println(colorFromAgrb2.toArgb())

        val h = 352f
        val color = Color.hsv(h, 0.5f, 0.5f)

        assertEquals(h, color.hue)
    }

    private fun testHsv(h: Float, s: Float, v: Float) {
        val color = Color.hsv(h, s, v)
        assertEquals(h, color.hue)
        assertEquals(s, color.saturation)
        assertEquals(v, color.hsvValue)
    }
}