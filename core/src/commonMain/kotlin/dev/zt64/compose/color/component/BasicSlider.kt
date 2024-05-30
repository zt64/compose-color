package dev.zt64.compose.color.component

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import androidx.compose.ui.util.*
import kotlinx.coroutines.coroutineScope
import kotlin.jvm.JvmInline
import kotlin.math.*

@Composable
internal fun BasicSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onValueChangeFinished: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    steps: Int = 0,
    thumb: @Composable (SliderState) -> Unit,
    track: @Composable (SliderState) -> Unit,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f
) {
    val onValueChangeFinishedState = rememberUpdatedState(onValueChangeFinished)
    val state = remember(steps, valueRange) {
        SliderState(
            value,
            steps,
            { onValueChangeFinishedState.value?.invoke() },
            valueRange
        )
    }

    state.onValueChange = onValueChange
    state.value = value

    Slider(
        state = state,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        thumb = thumb,
        track = track
    )
}

@Composable
private fun Slider(
    state: SliderState,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    thumb: @Composable (SliderState) -> Unit,
    track: @Composable (SliderState) -> Unit
) {
    SliderImpl(
        state = state,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        thumb = thumb,
        track = track
    )
}

@Stable
private fun Modifier.sliderTapModifier(
    state: SliderState,
    interactionSource: MutableInteractionSource,
    enabled: Boolean
) = if (enabled) {
    pointerInput(state, interactionSource) {
        detectTapGestures(
            onPress = { state.onPress(it) },
            onTap = {
                state.dispatchRawDelta(0f)
                state.gestureEndAction()
            }
        )
    }
} else {
    this
}

@Composable
private fun SliderImpl(
    modifier: Modifier,
    state: SliderState,
    enabled: Boolean,
    interactionSource: MutableInteractionSource,
    thumb: @Composable (SliderState) -> Unit,
    track: @Composable (SliderState) -> Unit
) {
    state.isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    val press = Modifier.sliderTapModifier(
        state,
        interactionSource,
        enabled
    )
    val drag = Modifier.draggable(
        orientation = Orientation.Horizontal,
        reverseDirection = state.isRtl,
        enabled = enabled,
        interactionSource = interactionSource,
        onDragStopped = { state.gestureEndAction() },
        startDragImmediately = state.isDragging,
        state = state
    )

    Layout(
        content = {
            Box(modifier = Modifier.layoutId(SliderComponents.THUMB)) {
                thumb(state)
            }
            Box(modifier = Modifier.layoutId(SliderComponents.TRACK)) {
                track(state)
            }
        },
        modifier = modifier
            .requiredSizeIn(
                minWidth = ThumbWidth,
                minHeight = TrackHeight
            ).sliderSemantics(state, enabled)
            .focusable(enabled, interactionSource)
            .then(press)
            .then(drag)
    ) { measurables, constraints ->

        val thumbPlaceable = measurables
            .fastFirst { it.layoutId == SliderComponents.THUMB }
            .measure(constraints)

        val trackPlaceable = measurables
            .fastFirst { it.layoutId == SliderComponents.TRACK }
            .measure(
                constraints
                    .offset(
                        horizontal = -thumbPlaceable.width
                    ).copy(minHeight = 0)
            )

        val sliderWidth = thumbPlaceable.width + trackPlaceable.width
        val sliderHeight = max(trackPlaceable.height, thumbPlaceable.height)

        state.updateDimensions(
            trackPlaceable.height.toFloat(),
            thumbPlaceable.width.toFloat(),
            sliderWidth
        )

        val trackOffsetX = thumbPlaceable.width / 2
        val thumbOffsetX = ((trackPlaceable.width) * state.coercedValueAsFraction).roundToInt()
        val trackOffsetY = (sliderHeight - trackPlaceable.height) / 2
        val thumbOffsetY = (sliderHeight - thumbPlaceable.height) / 2

        layout(sliderWidth, sliderHeight) {
            trackPlaceable.placeRelative(trackOffsetX, trackOffsetY)
            thumbPlaceable.placeRelative(thumbOffsetX, thumbOffsetY)
        }
    }
}

@Stable
internal class SliderState(
    value: Float = 0f,
    val steps: Int = 0,
    val onValueChangeFinished: (() -> Unit)? = null,
    val valueRange: ClosedFloatingPointRange<Float> = 0f..1f
) : DraggableState {
    private var valueState by mutableFloatStateOf(value)

    /**
     * [Float] that indicates the current value that the thumb
     * currently is in respect to the track.
     */
    var value: Float
        set(newVal) {
            val coercedValue = newVal.coerceIn(valueRange.start, valueRange.endInclusive)
            val snappedValue = snapValueToTick(
                coercedValue,
                tickFractions,
                valueRange.start,
                valueRange.endInclusive
            )
            valueState = snappedValue
        }
        get() = valueState

    override suspend fun drag(
        dragPriority: MutatePriority,
        block: suspend DragScope.() -> Unit
    ): Unit = coroutineScope {
        isDragging = true
        scrollMutex.mutateWith(dragScope, dragPriority, block)
        isDragging = false
    }

    override fun dispatchRawDelta(delta: Float) {
        val maxPx = max(totalWidth - thumbWidth / 2, 0f)
        val minPx = min(thumbWidth / 2, maxPx)
        rawOffset = (rawOffset + delta + pressOffset)
        pressOffset = 0f
        val offsetInTrack = snapValueToTick(rawOffset, tickFractions, minPx, maxPx)
        val scaledUserValue = scaleToUserValue(minPx, maxPx, offsetInTrack)
        if (scaledUserValue != this.value) {
            if (onValueChange != null) {
                onValueChange?.let { it(scaledUserValue) }
            } else {
                this.value = scaledUserValue
            }
        }
    }

    /**
     * callback in which value should be updated
     */
    var onValueChange: ((Float) -> Unit)? = null

    val tickFractions = stepsToTickFractions(steps)
    private var totalWidth by mutableIntStateOf(0)
    var isRtl = false
    var trackHeight by mutableFloatStateOf(0f)
    var thumbWidth by mutableFloatStateOf(0f)

    val coercedValueAsFraction
        get() = calcFraction(
            valueRange.start,
            valueRange.endInclusive,
            value.coerceIn(valueRange.start, valueRange.endInclusive)
        )

    var isDragging by mutableStateOf(false)
        private set

    fun updateDimensions(
        newTrackHeight: Float,
        newThumbWidth: Float,
        newTotalWidth: Int
    ) {
        trackHeight = newTrackHeight
        thumbWidth = newThumbWidth
        totalWidth = newTotalWidth
    }

    val gestureEndAction = {
        if (!isDragging) {
            // check isDragging in case the change is still in progress (touch -> drag case)
            onValueChangeFinished?.invoke()
        }
    }

    fun onPress(pos: Offset) {
        val to = if (isRtl) totalWidth - pos.x else pos.x
        pressOffset = to - rawOffset
    }

    private var rawOffset by mutableFloatStateOf(scaleToOffset(0f, 0f, value))
    private var pressOffset by mutableFloatStateOf(0f)
    private val dragScope: DragScope = object : DragScope {
        override fun dragBy(pixels: Float): Unit = dispatchRawDelta(pixels)
    }

    private val scrollMutex = MutatorMutex()

    private fun scaleToUserValue(
        minPx: Float,
        maxPx: Float,
        offset: Float
    ) = scale(minPx, maxPx, offset, valueRange.start, valueRange.endInclusive)

    private fun scaleToOffset(
        minPx: Float,
        maxPx: Float,
        userValue: Float
    ) = scale(valueRange.start, valueRange.endInclusive, userValue, minPx, maxPx)
}

private fun snapValueToTick(
    current: Float,
    tickFractions: FloatArray,
    minPx: Float,
    maxPx: Float
): Float {
    // target is a closest anchor to the `current`, if exists
    return tickFractions
        .minByOrNull { abs(lerp(minPx, maxPx, it) - current) }
        ?.run { lerp(minPx, maxPx, this) }
        ?: current
}

private fun stepsToTickFractions(steps: Int): FloatArray {
    return if (steps == 0) floatArrayOf() else FloatArray(steps + 2) { it.toFloat() / (steps + 1) }
}

// Scale x1 from a1..b1 range to a2..b2 range
private fun scale(
    a1: Float,
    b1: Float,
    x1: Float,
    a2: Float,
    b2: Float
) = lerp(a2, b2, calcFraction(a1, b1, x1))

// Calculate the 0..1 fraction that `pos` value represents between `a` and `b`
private fun calcFraction(
    a: Float,
    b: Float,
    pos: Float
) = (if (b - a == 0f) 0f else (pos - a) / (b - a)).coerceIn(0f, 1f)

private enum class SliderComponents {
    THUMB,
    TRACK
}

@Immutable
@JvmInline
internal value class SliderRange(val packedValue: Long) {
    /**
     * start of the [SliderRange]
     */
    @Stable
    val start: Float
        get() {
            // Explicitly compare against packed values to avoid auto-boxing of Size.Unspecified
            check(this.packedValue != Unspecified.packedValue) {
                "SliderRange is unspecified"
            }
            return unpackFloat1(packedValue)
        }

    /**
     * End (inclusive) of the [SliderRange]
     */
    @Stable
    val endInclusive: Float
        get() {
            // Explicitly compare against packed values to avoid auto-boxing of Size.Unspecified
            check(this.packedValue != Unspecified.packedValue) {
                "SliderRange is unspecified"
            }
            return unpackFloat2(packedValue)
        }

    companion object {
        /**
         * Represents an unspecified [SliderRange] value, usually a replacement for `null`
         * when a primitive value is desired.
         */
        @Stable
        val Unspecified = SliderRange(Float.NaN, Float.NaN)
    }

    /**
     * String representation of the [SliderRange]
     */
    override fun toString() = if (isSpecified) {
        "$start..$endInclusive"
    } else {
        "FloatRange.Unspecified"
    }
}

@Stable
internal fun SliderRange(
    start: Float,
    endInclusive: Float
): SliderRange {
    val isUnspecified = start.isNaN() && endInclusive.isNaN()

    require(isUnspecified || start <= endInclusive + SliderRangeTolerance) {
        "start($start) must be <= endInclusive($endInclusive)"
    }
    return SliderRange(packFloats(start, endInclusive))
}

@Stable
internal fun SliderRange(range: ClosedFloatingPointRange<Float>): SliderRange {
    val start = range.start
    val endInclusive = range.endInclusive
    val isUnspecified = start.isNaN() && endInclusive.isNaN()
    require(isUnspecified || start <= endInclusive + SliderRangeTolerance) {
        "ClosedFloatingPointRange<Float>.start($start) must be <= " +
            "ClosedFloatingPoint.endInclusive($endInclusive)"
    }
    return SliderRange(packFloats(start, endInclusive))
}

@Stable
internal val SliderRange.isSpecified: Boolean
    get() = packedValue != SliderRange.Unspecified.packedValue

internal val TrackHeight = SliderTokens.InactiveTrackHeight
internal val ThumbWidth = SliderTokens.HandleWidth

@Suppress("ktlint:standard:property-naming")
private const val SliderRangeTolerance = 0.0001

internal object SliderTokens {
    val HandleWidth = 4.0.dp
    val InactiveTrackHeight = 16.0.dp
}

private fun Modifier.sliderSemantics(
    state: SliderState,
    enabled: Boolean
): Modifier {
    return semantics {
        if (!enabled) disabled()
        setProgress { targetValue ->
            var newValue = targetValue.coerceIn(
                state.valueRange.start,
                state.valueRange.endInclusive
            )
            val originalVal = newValue
            val resolvedValue = if (state.steps > 0) {
                var distance: Float = newValue
                for (i in 0..state.steps + 1) {
                    val stepValue = lerp(
                        state.valueRange.start,
                        state.valueRange.endInclusive,
                        i.toFloat() / (state.steps + 1)
                    )
                    if (abs(stepValue - originalVal) <= distance) {
                        distance = abs(stepValue - originalVal)
                        newValue = stepValue
                    }
                }
                newValue
            } else {
                newValue
            }

            // This is to keep it consistent with AbsSeekbar.java: return false if no
            // change from current.
            if (resolvedValue == state.value) {
                false
            } else {
                if (resolvedValue != state.value) {
                    if (state.onValueChange != null) {
                        state.onValueChange?.let {
                            it(resolvedValue)
                        }
                    } else {
                        state.value = resolvedValue
                    }
                }
                state.onValueChangeFinished?.invoke()
                true
            }
        }
    }.progressSemantics(
        state.value,
        state.valueRange.start..state.valueRange.endInclusive,
        state.steps
    )
}