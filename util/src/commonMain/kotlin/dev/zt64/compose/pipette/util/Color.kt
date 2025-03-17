package dev.zt64.compose.pipette.util

import androidx.compose.ui.graphics.Color
import kotlin.math.round

/**
 * The hue of the color
 */
public val Color.hue: Float
    get() {
        val (r, g, b) = this

        val max = maxOf(r, g, b)
        val min = minOf(r, g, b)
        val delta = max - min

        if (delta == 0f) return 0f

        val hue = when (max) {
            r -> ((g - b) / delta) % 6
            g -> ((b - r) / delta) + 2
            b -> ((r - g) / delta) + 4
            else -> 0f
        }

        return round(((hue * 60f) + 360f) % 360f)
    }

/**
 * The saturation of the color in the HSV color space.
 */
public val Color.saturation: Float
    get() {
        val max = maxComponent()
        val min = minComponent()
        return if (max == 0f) 0f else 1 - min / max
    }

/**
 * The saturation of the color in the HSL color space.
 */
public val Color.hslSaturation: Float
    get() {
        val max = maxComponent()
        val min = minComponent()
        val l = (max + min) / 2
        return when {
            max == min -> 0f
            l <= 0.5f -> (max - min) / (max + min)
            else -> (max - min) / (2 - max - min)
        }
    }

/**
 * The lightness of the color in the HSL color space.
 */
public val Color.lightness: Float
    get() = (maxComponent() + minComponent()) / 2

/**
 * The value of the color in the HSV color space.
 */
public val Color.hsvValue: Float
    get() = maxComponent()

internal fun Color.maxComponent() = maxOf(red, green, blue)

internal fun Color.minComponent() = minOf(red, green, blue)