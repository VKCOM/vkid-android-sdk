package com.vk.id.onetap.compose.util

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp

@Composable
internal fun MeasureUnconstrainedViewWidth(
    viewToMeasure: @Composable () -> Unit,
    content: @Composable (measuredWidth: Dp) -> Unit,
) {
    SubcomposeLayout { constraints ->
        val measuredWidth = subcompose("viewToMeasure", viewToMeasure)[0].measure(Constraints()).width.toDp()
        val measurable = subcompose("content") { content(measuredWidth) }
        if (measurable.isNotEmpty()) {
            val contentPlaceable = measurable[0].measure(constraints)
            layout(contentPlaceable.width, contentPlaceable.height) { contentPlaceable.place(0, 0) }
        } else {
            layout(0, 0) {}
        }
    }
}

@Composable
internal fun PlaceComposableIfFitsWidth(
    modifier: Modifier,
    measureModifier: Modifier = modifier,
    viewToMeasure: @Composable (Modifier, measureInProgress: Boolean) -> Unit,
    fallback: @Composable () -> Unit
) {
    MeasureUnconstrainedViewWidth(viewToMeasure = { viewToMeasure(measureModifier, true) }) {
        BoxWithConstraints(
            modifier = modifier,
        ) {
            if (it <= maxWidth) {
                viewToMeasure(Modifier, false)
            } else {
                fallback()
            }
        }
    }
}
