package com.vk.id.health.metrics.utils

import java.math.BigDecimal
import java.math.RoundingMode

private const val HUNDRED_PERCENT = 100

internal fun formatChangePercent(
    oldValue: Long,
    newValue: Long,
): String {
    val changePercent = BigDecimal(HUNDRED_PERCENT - newValue.toDouble() / oldValue * HUNDRED_PERCENT)
        .setScale(2, RoundingMode.HALF_EVEN)
        .stripTrailingZeros()
    val sign = when {
        changePercent > BigDecimal.ZERO -> "-"
        changePercent == BigDecimal.ZERO -> ""
        else -> "+"
    }
    val colorSign = if (sign == "-") "-" else "+"
    return "{$colorSign$sign${changePercent.abs().toPlainString()}%$colorSign}"
}
