package com.vk.id.sample.xml.groupsubscription

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager

@Suppress("MagicNumber")
public object GroupSubscriptionLimitPrefs {

    private const val KEY_LIMIT_ENABLED = "KEY_GROUP_SUBSCRIPTION_LIMIT_ENABLED"
    private const val KEY_LIMIT_PERIOD = "KEY_GROUP_SUBSCRIPTION_LIMIT_PERIOD"
    private const val KEY_LIMIT_VALUE = "KEY_GROUP_SUBSCRIPTION_LIMIT_VALUE"

    public fun setLimitEnabled(
        context: Context,
        isEnabled: Boolean,
    ) {
        getPrefs(context).edit(commit = true) { putBoolean(KEY_LIMIT_ENABLED, isEnabled) }
    }

    public fun getLimitEnabled(
        context: Context
    ): Boolean {
        return getPrefs(context).getBoolean(KEY_LIMIT_ENABLED, true)
    }

    public fun setLimitPeriod(
        context: Context,
        period: Int
    ) {
        getPrefs(context).edit(commit = true) { putInt(KEY_LIMIT_PERIOD, period) }
    }

    public fun getLimitPeriod(
        context: Context
    ): Int {
        return getPrefs(context).getInt(KEY_LIMIT_PERIOD, 30)
    }

    public fun setLimitValue(
        context: Context,
        value: Int
    ) {
        getPrefs(context).edit(commit = true) { putInt(KEY_LIMIT_VALUE, value) }
    }

    public fun getLimitValue(
        context: Context
    ): Int {
        return getPrefs(context).getInt(KEY_LIMIT_VALUE, 2)
    }

    private fun getPrefs(context: Context) = PreferenceManager.getDefaultSharedPreferences(context)
}
