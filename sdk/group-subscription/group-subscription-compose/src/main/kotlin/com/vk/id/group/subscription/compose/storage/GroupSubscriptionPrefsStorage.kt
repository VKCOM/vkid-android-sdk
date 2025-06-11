@file:OptIn(InternalVKIDApi::class)

package com.vk.id.group.subscription.compose.storage

import com.vk.id.common.InternalVKIDApi
import com.vk.id.storage.InternalVKIDPreferencesStorage
import java.util.Date

internal class GroupSubscriptionPrefsStorage(
    private val prefs: InternalVKIDPreferencesStorage
) {

    fun saveDisplays(
        userId: Long,
        displays: Set<Date>
    ) {
        prefs.set(getKey(userId), displays.map { it.time }.joinToString(separator = ","))
    }

    fun getDisplays(userId: Long): Set<Date> {
        return prefs.getString(getKey(userId))
            ?.split(",")
            ?.filter { it.isNotBlank() }
            ?.map { Date(it.toLong()) }
            ?.toSet()
            ?: emptySet()
    }

    private fun getKey(userId: Long) = "${KEY_DISPLAYS_PREFIX}_$userId"

    private companion object {
        private const val KEY_DISPLAYS_PREFIX = "GROUP_SUBSCRIPTION_DISPLAYS"
    }
}
