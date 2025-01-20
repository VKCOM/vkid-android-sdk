package com.vk.id.group.subscription.compose.ui

import kotlin.math.roundToInt

internal object SubscriberCountFormatter {

    private const val TEN = 10
    private const val THOUSAND = 1000
    private const val MILLION = 1000000

    fun format(subscriberCount: Int): String {
        return when {
            subscriberCount < THOUSAND -> subscriberCount.toString()
            subscriberCount < MILLION -> formatForDisplay(subscriberCount / THOUSAND.toFloat(), ',') + "K"
            else -> formatForDisplay(subscriberCount / MILLION.toFloat(), '.') + "M"
        }
    }

    private fun formatForDisplay(
        value: Float,
        decimalSeparator: Char
    ): String {
        val valueWithDecimals = (value * TEN).roundToInt()
        if (valueWithDecimals % TEN == 0 || valueWithDecimals / TEN > TEN) {
            return (valueWithDecimals / TEN).toString()
        } else {
            val integerPart = valueWithDecimals / TEN
            val decimalPart = valueWithDecimals % TEN
            return "$integerPart$decimalSeparator$decimalPart"
        }
    }
}
