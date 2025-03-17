package dev.zt64.compose.pipette.gradle

import org.gradle.api.provider.Property

fun <T> Property<T>.assign(value: T) = set(value)