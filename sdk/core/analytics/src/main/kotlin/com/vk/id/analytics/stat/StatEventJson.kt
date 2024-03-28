@file:OptIn(InternalVKIDApi::class)

package com.vk.id.analytics.stat

import com.vk.id.analytics.VKIDAnalytics
import com.vk.id.common.InternalVKIDApi
import org.json.JSONArray
import org.json.JSONObject

internal class StatEventJson(
    name: String,
    params: Array<out VKIDAnalytics.EventParam>,
    eventId: Int,
    prevEventId: Int
) {
    val json: JSONObject = actionJson(
        "type_registration_item",
        eventJson(name, params.toMap()),
        eventId,
        prevEventId
    )

    /*
        {
            "id":1711614835,
            "prev_event_id":0,
            "prev_nav_id":0,
            "screen":"nowhere",
            "timestamp":"1711614835713",
            "type":"type_action",
            "type_action":{
                "type":"type_registration_item",
                "type_registration_item":{
                    "event_type":"onetap_button_no_user_show",
                    "fields":[
                    {
                        "name":"sdk_type",
                        "value":"vkid"
                    },
                    {
                        "name":"alternative_sign_in_availability",
                        "value":"not_available"
                    },
                    {
                        "name":"button_type",
                        "value":"icon"
                    }
                    ]
                }
            }
        }
     */
    @Suppress("SameParameterValue")
    private fun actionJson(typeAction: String, data: JSONObject, eventId: Int, prevEventId: Int): JSONObject =
        JSONObject().apply {
            put("id", eventId)
            put("prev_event_id", prevEventId)
            put("prev_nav_id", 0)
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

    private fun eventJson(eventName: String, eventParams: Map<String, String>) = JSONObject().apply {
        put("event_type", eventName)
        put(
            "fields",
            JSONArray().apply {
                eventParams.forEach {
                    put(
                        JSONObject().apply {
                            put("name", it.key)
                            put("value", it.value)
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
