package com.vk.id.network.groupsubscription

import com.vk.id.common.InternalVKIDApi
import com.vk.id.network.groupsubscription.data.InternalVKIDGroupByIdData
import com.vk.id.network.groupsubscription.data.InternalVKIDGroupMembersData

@InternalVKIDApi
public interface InternalVKIDGroupSubscriptionApiContract {

    public suspend fun shouldShowSubscription(
        accessToken: String
    ): Boolean

    public suspend fun isServiceAccount(
        accessToken: String,
    ): Boolean

    public suspend fun getGroup(
        accessToken: String,
        groupId: String
    ): InternalVKIDGroupByIdData

    public suspend fun getMembers(
        accessToken: String,
        groupId: String,
        justFriends: Boolean
    ): InternalVKIDGroupMembersData

    public suspend fun subscribeToGroup(
        accessToken: String,
        groupId: String,
    )
}
