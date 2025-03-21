package dev.zt64.compose.pipette.sample

// Compose Color stores internally as an ARGB, which doesn't preserve the HSV values properly,
// so we store them separately to avoid errors. This shouldn't be necessary for most, simpler use case.
typealias HsvColor = Triple<Float, Float, Float>

val HsvColor.hue: Float
    get() = first

val HsvColor.saturation: Float
    get() = second

val HsvColor.value: Float
    get() = third