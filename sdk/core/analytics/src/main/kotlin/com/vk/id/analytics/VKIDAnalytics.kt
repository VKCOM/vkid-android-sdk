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
    public class EventParam(public val name: String, public val value: String)

    public companion object Trackers : Tracker {

        /** Track event to all trackers, added with [addTracker] */
        @JvmStatic
        override fun trackEvent(name: String, vararg params: EventParam) {
            trackersArray.forEach { it.trackEvent(name, *params) }
        }

        /** Adds new tracker. */
        @JvmStatic
        public fun addTracker(vararg trackers: Tracker) {
            for (tracker in trackers) {
                require(tracker !== this) { "Cannot add VKIDAnalytics into itself." }
            }
            synchronized(this.trackers) {
                this.trackers.addAll(trackers)
                trackersArray = this.trackers.toTypedArray()
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
