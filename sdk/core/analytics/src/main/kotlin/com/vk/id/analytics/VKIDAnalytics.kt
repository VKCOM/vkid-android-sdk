package com.vk.id.analytics

import com.vk.id.common.InternalVKIDApi

/**
 * Class to track analytics events for VKID
 */
@InternalVKIDApi
public class VKIDAnalytics private constructor() {
    /**
     * Interface to implement to add to [VKIDAnalytics] with [addTracker]
     */
    @InternalVKIDApi
    public interface Tracker {
        public fun trackEvent(accessToken: String?, name: String, vararg params: EventParam)
        public fun trackEvent(name: String, vararg params: EventParam): Unit = trackEvent(null, name, *params)
    }

    /** Parameter for event **/
    @InternalVKIDApi
    @Suppress("ForbiddenPublicDataClass")
    public data class EventParam(
        public val name: String,
        public val strValue: String? = null,
        public val intValue: Int? = null,
    )

    @InternalVKIDApi
    public companion object Trackers : Tracker {

        /** Track event to all trackers, added with [addTracker] */
        @JvmStatic
        override fun trackEvent(accessToken: String?, name: String, vararg params: EventParam) {
            trackersArray.forEach { it.trackEvent(accessToken, name, *params,) }
        }

        /** Adds new tracker. If [tracker] was already added then nothing happens.*/
        @JvmStatic
        public fun addTracker(tracker: Tracker) {
            if (trackers.contains(tracker)) {
                return
            }
            require(tracker !== this) { "Cannot add VKIDAnalytics into itself." }
            synchronized(this.trackers) {
                trackers.add(tracker)
                trackersArray = trackers.toTypedArray()
            }
        }

        /** Remove an added tracker. */
        @JvmStatic
        public fun removeTracker(tracker: Tracker) {
            synchronized(trackers) {
                trackers.remove(tracker)
                trackersArray = trackers.toTypedArray()
            }
        }

        // Both fields guarded by 'trackers'.
        private val trackers = ArrayList<Tracker>()

        @Volatile
        private var trackersArray = emptyArray<Tracker>()
    }
}
