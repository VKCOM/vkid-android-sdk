package com.vk.id.tracking.core

import com.vk.id.common.InternalVKIDApi

@InternalVKIDApi
public interface AnalyticsTracking {
    public fun log(message: String)
}
