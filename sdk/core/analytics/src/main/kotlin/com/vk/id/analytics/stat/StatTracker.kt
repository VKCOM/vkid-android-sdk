@file:OptIn(InternalVKIDApi::class)

package com.vk.id.analytics.stat

import com.vk.id.analytics.BuildConfig
import com.vk.id.analytics.VKIDAnalytics
import com.vk.id.common.InternalVKIDApi
import com.vk.id.logger.internalVKIDCreateLoggerForClass
import com.vk.id.network.InternalVKIDApiContract
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.LinkedBlockingQueue
import kotlin.time.Duration.Companion.seconds

/**
 * Tracker for [VKIDAnalytics] that track events to stat VK backend
 */
@InternalVKIDApi
public class StatTracker(
    private val clientId: String,
    private val clientSecret: String,
    private val api: Lazy<InternalVKIDApiContract>,
    dispatcher: CoroutineDispatcher
) : VKIDAnalytics.Tracker {

    private val trackerScope = CoroutineScope(dispatcher + SupervisorJob())
    private val logger = internalVKIDCreateLoggerForClass()
    private val batchEvents = LinkedBlockingQueue<JSONObject>()
    private val eventCounter = EventCounter()

    @InternalVKIDApi
    public companion object {
        // External analytics params that needs to be send in redirect uri.
        // UUID of auth session - from auth start to auth result.
        public const val EXTERNAL_PARAM_SESSION_ID: String = "session_id"

        // Start point of auth. Possible values: from_one_tap, from_floating_one_tap, from_multibranding.
        public const val EXTERNAL_PARAM_FLOW_SOURCE: String = "flow_source"
    }

    override fun trackEvent(name: String, vararg params: VKIDAnalytics.EventParam) {
        val eventJson = StatEventJson(name, params, eventCounter.eventId, eventCounter.prevEventId)
        eventCounter.increment()
        batchEvents.add(eventJson.json)
        trackerScope.launch {
            delay(1.seconds)
            val events = mutableListOf<JSONObject>()
            batchEvents.drainTo(events)
            if (events.isNotEmpty()) {
                val eventsJson = JSONArray(events)
                val response = try {
                    val response = api.value.sendStatEventsAnonymously(clientId, clientSecret, BuildConfig.VKID_VERSION_NAME, eventsJson).execute()
                    logger.debug("Send events to stat '$eventsJson': ${response.code}")
                    response.body?.string()
                } catch (ioe: IOException) {
                    logger.error("Network exception while sending events $eventsJson", ioe)
                    null
                }
                response?.let {
                    try {
                        if (JSONObject(it).has("error")) {
                            logger.error(it, null)
                        } else {
                            logger.debug(it)
                        }
                    } catch (@Suppress("SwallowedException") jse: JSONException) {
                        logger.debug(it)
                    }
                }
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is StatTracker && clientSecret == other.clientSecret
    }

    override fun hashCode(): Int {
        return clientSecret.hashCode()
    }
}
