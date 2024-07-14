package com.vk.id.health.metrics.utils

import java.math.BigDecimal
import java.math.RoundingMode

private const val HUNDRED_PERCENT = 100

internal fun formatChangePercent(
    oldValue: Number,
    newValue: Number,
    increaseIsNegative: Boolean = true,
): String {
    val changePercent = if (oldValue.toDouble() == 0.0) {
        BigDecimal.valueOf(HUNDRED_PERCENT.toLong())
    } else {
        BigDecimal(newValue.toDouble() / oldValue.toDouble() * HUNDRED_PERCENT)
            .setScale(2, RoundingMode.HALF_EVEN)
            .stripTrailingZeros()
    }
    val sign = when {
        changePercent < BigDecimal.ZERO -> "-"
        changePercent == BigDecimal.ZERO -> ""
        else -> "+"
    }
    val colorSign = if ((sign == "-").xor(increaseIsNegative)) "-" else "+"
    return "{$colorSign$sign${changePercent.abs().toPlainString()}%$colorSign}"
}
