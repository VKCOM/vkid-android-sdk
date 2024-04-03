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
    public interface Tracker {
        public fun trackEvent(name: String, vararg params: EventParam)
    }

    /** Parameter for event **/
    public class EventParam(
        public val name: String,
        public val strValue: String? = null,
        public val intValue: Int? = null
    )

    public companion object Trackers : Tracker {

        /** Track event to all trackers, added with [addTracker] */
        @JvmStatic
        override fun trackEvent(name: String, vararg params: EventParam) {
            trackersArray.forEach { it.trackEvent(name, *params) }
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
