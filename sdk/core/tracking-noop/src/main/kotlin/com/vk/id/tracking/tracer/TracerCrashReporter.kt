package com.vk.id.tracking.tracer

import android.content.Context
import com.vk.id.common.InternalVKIDApi

@InternalVKIDApi
public class TracerCrashReporter(
    @Suppress("UNUSED_PARAMETER") context: Context
) : CrashReporter {

    override fun report(crash: Throwable): Unit = Unit
}
