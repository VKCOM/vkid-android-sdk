@file:OptIn(InternalVKIDApi::class)

package com.vk.id.analytics.stat

import com.vk.id.analytics.VKIDAnalytics
import com.vk.id.common.InternalVKIDApi
import com.vk.id.logger.createLoggerForClass
import com.vk.id.network.VKIDApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

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

    override fun trackEvent(name: String, vararg params: VKIDAnalytics.EventParam) {
        trackerScope.launch {
            val eventsJson = actionJson("type_registration_item", statEventJson(name, params.toMap()))
            val response = api.value.sendStatEventsAnonymously(clientId, clientSecret, eventsJson).execute()
            logger.debug("Send event to stat '$name': ${response.code}")
            response.body?.string()?.let {
                if (JSONObject(it).has("error")) {
                    logger.error(it, null)
                }
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is StatTracker && clientSecret == other.clientSecret && api == other.api
    }

    override fun hashCode(): Int {
        return clientSecret.hashCode()
    }

    @Suppress("SameParameterValue")
    /**
     * [
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
     * ]
     */
    private fun actionJson(typeAction: String, data: JSONObject): JSONArray = JSONArray(
        listOf(
            JSONObject().apply {
                put(
                    "type_action",
                    JSONObject().apply {
                        put("type", typeAction)
                        put(typeAction, data)
                    }
                )
            }
        )
    )

    private fun statEventJson(eventName: String, eventParams: Map<String, String>) = JSONObject().apply {
        put("event_type", eventName)
        eventParams.forEach {
            put(it.key, it.value)
        }
    }
}

private fun Array<out VKIDAnalytics.EventParam>.toMap(): Map<String, String> {
    val result = mutableMapOf<String, String>()
    forEach {
        result[it.name] = it.value
    }
    return result
}
