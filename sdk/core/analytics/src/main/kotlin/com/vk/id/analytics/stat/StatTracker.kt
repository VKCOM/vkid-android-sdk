@file:OptIn(InternalVKIDApi::class)

package com.vk.id.analytics.stat

import com.vk.id.analytics.VKIDAnalytics
import com.vk.id.common.InternalVKIDApi
import com.vk.id.logger.createLoggerForClass
import com.vk.id.network.VKIDApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
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
    private val api: Lazy<VKIDApi>,
    dispatcher: CoroutineDispatcher
) : VKIDAnalytics.Tracker {

    private val trackerScope = CoroutineScope(dispatcher + SupervisorJob())
    private val logger = createLoggerForClass()
    private val batchEvents = LinkedBlockingQueue<JSONObject>()

    override fun trackEvent(name: String, vararg params: VKIDAnalytics.EventParam) {
        val eventJson = actionJson("type_registration_item", statEventJson(name, params.toMap()))
        batchEvents.add(eventJson)
        trackerScope.launch {
            delay(1.seconds)
            val events = mutableListOf<JSONObject>()
            batchEvents.drainTo(events)
            if (events.isNotEmpty()) {
                val eventsJson = JSONArray(events)
                try {
                    val response = api.value.sendStatEventsAnonymously(clientId, clientSecret, eventsJson).execute()
                    logger.debug("Send events to stat '$eventsJson': ${response.code}")
                    response.body?.string()?.let {
                        if (JSONObject(it).has("error")) {
                            logger.error(it, null)
                        } else {
                            logger.debug(it)
                        }
                    }
                } catch (ioe: IOException) {
                    logger.error("Network exception while sending events $eventsJson", ioe)
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

    @Suppress("SameParameterValue")
    /**
     *    {
     *       "type_action":{
     *          "type":"type_registration_item",
     *          "type_registration_item":{
     *             "event_type":"onetap_button_user_found",
     *             "sdk_type":"vkid",
     *             "alternative_sign_in_availability":"not_available",
     *             "button_type":"icon"
     *          }
     *       }
     *    }
     */
    private fun actionJson(typeAction: String, data: JSONObject): JSONObject =
        JSONObject().apply {
            put("screen", "nowhere")
            put("timestamp", System.currentTimeMillis().toString())
            put("type", "type_action")
            put(
                "type_action",
                JSONObject().apply {
                    put("type", typeAction)
                    put(typeAction, data)
                }
            )
        }

    private fun statEventJson(eventName: String, eventParams: Map<String, String>) = JSONObject().apply {
        put("event_type", eventName)
        put(
            "fields",
            JSONArray().apply {
                eventParams.forEach {
                    put(
                        JSONObject().apply {
                            put(it.key, it.value)
                        }
                    )
                }
            }
        )
    }
}

private fun Array<out VKIDAnalytics.EventParam>.toMap(): Map<String, String> {
    val result = mutableMapOf<String, String>()
    forEach {
        result[it.name] = it.value
    }
    return result
}
