@file:OptIn(InternalVKIDApi::class)

package com.vk.id.group.subscription.compose.interactor

import com.vk.id.VKID
import com.vk.id.common.InternalVKIDApi
import com.vk.id.group.subscription.compose.storage.GroupSubscriptionPrefsStorage
import com.vk.id.groupsubscription.GroupSubscriptionLimit
import com.vk.id.network.groupsubscription.InternalVKIDGroupSubscriptionApiContract
import com.vk.id.network.groupsubscription.data.InternalVKIDGroupByIdData
import com.vk.id.network.groupsubscription.data.InternalVKIDGroupData
import com.vk.id.network.groupsubscription.data.InternalVKIDGroupMembersData
import com.vk.id.storage.InternalVKIDTokenStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date

internal class GroupSubscriptionInteractor(
    private val apiService: InternalVKIDGroupSubscriptionApiContract,
    private val tokenStorage: InternalVKIDTokenStorage,
    private val groupId: String,
    private val externalAccessTokenProvider: (() -> String)?,
    private val storage: GroupSubscriptionPrefsStorage,
    private val limit: GroupSubscriptionLimit?,
) {
    private companion object {
        const val IMAGE_NUMBER = 3
    }

    private val accessToken: String
        get() = externalAccessTokenProvider?.invoke() ?: tokenStorage.currentAccessToken?.token ?: throw NotAuthorizedException()

    internal suspend fun saveDisplay() {
        return withContext(Dispatchers.IO) {
            val userId = VKID.instance.accessToken?.userID ?: return@withContext
            val newDisplays = storage.getDisplays(userId) + Date()
            storage.saveDisplays(userId, newDisplays)
        }
    }

    internal suspend fun loadGroup(): InternalVKIDGroupData {
        when {
            !shouldShowGroupSubscription() -> throw LimitReachedException()
            !apiService.isServiceAccount(accessToken) -> return getGroup()
            else -> throw ServiceAccountException()
        }
    }

    internal suspend fun subscribeToGroup() {
        apiService.subscribeToGroup(accessToken = accessToken, groupId = groupId)
    }

    private suspend fun shouldShowGroupSubscription(): Boolean {
        return withContext(Dispatchers.IO) {
            if (limit == null) return@withContext true
            val limitStart = Calendar.getInstance()
            limitStart.time = Date()
            limitStart.add(Calendar.DAY_OF_YEAR, -limit.periodInDays)
            val userId = VKID.instance.accessToken?.userID ?: return@withContext false
            val matchingDisplays = storage.getDisplays(userId).filter { it.after(limitStart.time) }.toSet()
            storage.saveDisplays(userId, matchingDisplays)
            matchingDisplays.size < limit.maxSubscriptionsToShow
        }
    }

    private suspend fun getGroup(): InternalVKIDGroupData {
        return withContext(currentCoroutineContext()) {
            val (group, members, friends) = awaitAll(
                async { apiService.getGroup(accessToken = accessToken, groupId = groupId) },
                async { apiService.getMembers(accessToken = accessToken, groupId = groupId, justFriends = false) },
                async { apiService.getMembers(accessToken = accessToken, groupId = groupId, justFriends = true) },
            )
            group as InternalVKIDGroupByIdData
            members as InternalVKIDGroupMembersData
            friends as InternalVKIDGroupMembersData
            InternalVKIDGroupData(
                imageUrl = group.imageUrl,
                name = group.name,
                description = group.description,
                userImageUrls = (members.members + friends.members).map { it.imageUrl }.take(IMAGE_NUMBER),
                subscriberCount = members.count,
                friendsCount = friends.count,
                isVerified = group.isVerified
            )
        }
    }
}
