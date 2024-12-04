package com.vk.id.tracking.tracer

import com.vk.id.common.InternalVKIDApi

@InternalVKIDApi
public interface CrashReporter {
    public fun report(crash: Throwable)
}
