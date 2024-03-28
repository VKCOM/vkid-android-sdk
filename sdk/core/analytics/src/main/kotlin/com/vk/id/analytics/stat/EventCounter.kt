package com.vk.id.analytics.stat

import java.util.concurrent.TimeUnit

internal class EventCounter {
    private val startId = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()).toInt()
    var prevEventId = 0
        private set
    var eventId = startId
        private set

    fun increment() {
        prevEventId = eventId
        eventId++
    }
}
