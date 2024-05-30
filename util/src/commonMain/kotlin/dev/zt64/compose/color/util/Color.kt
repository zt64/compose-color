package dev.zt64.compose.color.util

import androidx.compose.ui.graphics.Color
import kotlin.math.abs

/**
 * The hue of the color
 */
public val Color.hue: Float
    get() {
        val (r, g, b) = this

        val max = maxOf(r, g, b)
        val min = minOf(r, g, b)
        val delta = max - min

        return when {
            delta == 0f -> 0f
            max == r -> ((g - b) / delta % 6 + 6) % 6
            max == g -> (b - r) / delta + 2
            else -> (r - g) / delta + 4
        } * 60
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
        return if (max == 0f) 0f else (max - min) / (1 - abs(max + min - 1))
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