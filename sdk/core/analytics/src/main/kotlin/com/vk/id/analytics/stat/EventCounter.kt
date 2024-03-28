package com.vk.id.analytics.stat

import java.util.concurrent.TimeUnit

internal class EventCounter {
    private val startId = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()).toInt()

    @Volatile
    var prevEventId = 0
        private set

    @Volatile
    var eventId = startId
        private set

    @Synchronized
    fun increment() {
        prevEventId = eventId
        eventId++
    }
}
