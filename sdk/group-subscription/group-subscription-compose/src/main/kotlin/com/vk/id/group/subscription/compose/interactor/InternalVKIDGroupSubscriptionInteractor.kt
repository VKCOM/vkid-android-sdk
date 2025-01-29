@file:OptIn(InternalVKIDApi::class)

package com.vk.id.group.subscription.compose.interactor

import com.vk.id.common.InternalVKIDApi
import com.vk.id.network.groupsubscription.InternalVKIDGroupSubscriptionApiContract
import com.vk.id.network.groupsubscription.data.InternalVKIDGroupByIdData
import com.vk.id.network.groupsubscription.data.InternalVKIDGroupData
import com.vk.id.network.groupsubscription.data.InternalVKIDGroupMembersData
import com.vk.id.storage.InternalVKIDTokenStorage
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.withContext

internal class InternalVKIDGroupSubscriptionInteractor(
    private val apiService: InternalVKIDGroupSubscriptionApiContract,
    private val tokenStorage: InternalVKIDTokenStorage,
    private val groupId: String,
    private val externalAccessTokenProvider: (() -> String)?,
) {
    private companion object {
        const val IMAGE_NUMBER = 3
    }

    private val accessToken: String
        get() = externalAccessTokenProvider?.invoke() ?: tokenStorage.currentAccessToken?.token ?: throw NotAuthorizedException()

    internal suspend fun loadGroup(): InternalVKIDGroupData {
        if (!apiService.isServiceAccount(accessToken)) {
            return getGroup()
        } else {
            throw ServiceAccountException()
        }
    }

    internal suspend fun subscribeToGroup() {
        apiService.subscribeToGroup(accessToken = accessToken, groupId = groupId)
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
