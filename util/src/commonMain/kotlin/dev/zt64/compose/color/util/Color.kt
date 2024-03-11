package dev.zt64.compose.color.util

import androidx.compose.ui.graphics.Color
import kotlin.math.abs

/**
 * The hue of the color in the HSL color space.
 */
public val Color.hue: Float
    get() {
        val r = red
        val g = green
        val b = blue

        val max = maxOf(r, g, b)
        val min = minOf(r, g, b)
        val delta = max - min
        return when {
            delta == 0f -> 0f
            max == r -> (g - b) / delta % 6
            max == g -> (b - r) / delta + 2
            else -> (r - g) / delta + 4
        } * 60
    }

/**
 * The saturation of the color in the HSL color space.
 */
public val Color.saturation: Float
    get() {
        val max = maxComponent()
        val min = minComponent()
        val l = (max + min) / 2
        return if (max == min) 0f else (max - min) / (1 - abs(2 * l - 1))
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
    get() = maxOf(red, green, blue)

internal fun Color.maxComponent(): Float = maxOf(red, green, blue)

internal fun Color.minComponent(): Float = minOf(red, green, blue)