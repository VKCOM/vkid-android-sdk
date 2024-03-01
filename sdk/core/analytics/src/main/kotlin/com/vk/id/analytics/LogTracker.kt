package com.vk.id.analytics

import android.util.Log
import com.vk.id.common.InternalVKIDApi

/**
 * Tracker for [VKIDAnalytics] that track events to Android logcat
 */
@InternalVKIDApi
public class LogTracker(private val tag: String = "VKID Analytics") : VKIDAnalytics.Tracker {
    /** Log event to logcat with format 'event name' { param1name: param1value, ... } **/
    override fun trackEvent(name: String, vararg params: VKIDAnalytics.EventParam) {
        var paramsString = "{ "
        var first = true
        for (p in params) {
            if (!first) {
                paramsString += ", "
            }
            first = false
            paramsString += p.name
            paramsString += ": "
            paramsString += p.value
        }
        paramsString += " }"
        Log.d(tag, "$name $paramsString")
    }
}
