package com.vk.id.onetap.compose.util

import androidx.compose.runtime.Composable
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
