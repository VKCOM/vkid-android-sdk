package com.vk.id.group.subscription.compose

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import kotlin.math.ceil
import kotlin.math.floor

// https://issuetracker.google.com/issues/206039942
@Composable
internal fun TightWrapText(
    text: String,
    modifier: Modifier,
    style: TextStyle,
    maxLines: Int,
    overflow: TextOverflow,
) {
    var textLayoutResult: TextLayoutResult? by remember { mutableStateOf(null) }
    BasicText(
        text = text,
        modifier = modifier.layout { measurable, constraints ->
            val placeable = measurable.measure(constraints)
            val newTextLayoutResult = textLayoutResult!!

            if (newTextLayoutResult.lineCount == 0) {
                // Default behavior if there is no text
                layout(placeable.width, placeable.height) {
                    placeable.placeRelative(0, 0)
                }
            } else {
                val minX = (0 until newTextLayoutResult.lineCount).minOf(newTextLayoutResult::getLineLeft)
                val maxX = (0 until newTextLayoutResult.lineCount).maxOf(newTextLayoutResult::getLineRight)

                layout(ceil(maxX - minX).toInt(), placeable.height) {
                    placeable.place(-floor(minX).toInt(), 0)
                }
            }
        },
        style = style,
        maxLines = maxLines,
        overflow = overflow,
        onTextLayout = {
            textLayoutResult = it
        }
    )
}
