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
    val json: JSONObject

    private companion object {
        private val specialParams = listOf("screen_current", "screen_to", "error", "wrapper_sdk_type", "app_id")
        private val techEvents = setOf("vkid_sdk_init")
    }

    init {
        var screen = "nowhere"
        val filteredParams = mutableListOf<VKIDAnalytics.EventParam>()
        val topLevelParams = mutableMapOf<String, Any>()
        for (p in params) {
            when (p.name) {
                "screen" -> {
                    screen = p.strValue ?: ""
                }

                in specialParams -> {
                    topLevelParams[p.name] = p.strValue ?: p.intValue ?: ""
                }

                else -> {
                    filteredParams.add(p)
                }
            }
        }
        val typeAction = actionForEvent(name)
        val eventJson = if (name in techEvents) {
            techEventJson(name, topLevelParams)
        } else {
            eventJson(name, filteredParams, topLevelParams)
        }
        json = actionJson(
            typeAction = typeAction,
            screen = screen,
            data = eventJson,
            eventId = eventId,
            prevEventId = prevEventId,
        )
    }

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
    @Suppress("SameParameterValue", "LongParameterList")
    private fun actionJson(
        typeAction: String,
        screen: String,
        data: JSONObject,
        eventId: Int,
        prevEventId: Int,
    ): JSONObject =
        JSONObject().apply {
            put("id", eventId)
            put("prev_event_id", prevEventId)
            put("prev_nav_id", 0)
            put("screen", screen)
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

    private fun eventJson(eventName: String, eventParams: List<VKIDAnalytics.EventParam>, topLevelParams: Map<String, Any>) = JSONObject().apply {
        put("event_type", eventName)
        put(
            "fields",
            JSONArray().apply {
                eventParams.forEach {
                    put(
                        JSONObject().apply {
                            put("name", it.name)
                            if (it.strValue != null) {
                                put("value", it.strValue)
                            } else if (it.intValue != null) {
                                put("value", it.intValue)
                            }
                        }
                    )
                }
            }
        )
        for (p in topLevelParams) {
            put(p.key, p.value)
        }
    }

    private fun techEventJson(eventName: String, topLevelParams: Map<String, Any>) = JSONObject().apply {
        put("step", eventName)
        for (p in topLevelParams) {
            put(p.key, p.value)
        }
    }

    private fun actionForEvent(name: String): String =
        if (name in techEvents) {
            "type_sak_sessions_event_item"
        } else {
            "type_registration_item"
        }
}
