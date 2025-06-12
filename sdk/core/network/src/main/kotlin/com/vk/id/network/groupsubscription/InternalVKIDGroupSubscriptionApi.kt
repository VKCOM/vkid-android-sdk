package com.vk.id.network.groupsubscription

import com.vk.id.common.InternalVKIDApi
import com.vk.id.network.common.ApiConstants.API_VERSION_VALUE
import com.vk.id.network.common.ApiConstants.FIELD_API_VERSION
import com.vk.id.network.util.createRequest
import okhttp3.Call
import okhttp3.FormBody
import okhttp3.OkHttpClient

@InternalVKIDApi
public class InternalVKIDGroupSubscriptionApi(
    private val client: OkHttpClient,
) {

    public fun getShouldShowSubscription(
        accessToken: String,
    ): Call = client.createRequest(
        host = HOST_VK_ID,
        path = PATH_SHOULD_SHOW_SUBSCRIPTION,
        requestBody = bodyBuilder(accessToken).build()
    )

    public fun getProfileShortInfo(
        accessToken: String,
    ): Call = client.createRequest(
        host = HOST_VK_API,
        path = PATH_ACCOUNT_PROFILE_SHORT_INFO,
        requestBody = bodyBuilder(accessToken).build()
    )

    public fun getGroup(
        accessToken: String,
        groupId: String,
    ): Call = client.createRequest(
        host = HOST_VK_API,
        path = PATH_GROUPS_GET_BY_ID,
        requestBody = bodyBuilder(accessToken)
            .add("group_ids", groupId)
            .add("fields", """description,verified,is_member""")
            .build()
    )

    public fun getMembers(
        accessToken: String,
        groupId: String,
        justFriends: Boolean,
    ): Call = client.createRequest(
        host = HOST_VK_API,
        path = PATH_GROUPS_GET_MEMBERS,
        requestBody = bodyBuilder(accessToken)
            .add(FIELD_GROUP_ID, groupId)
            .add("sort", "id_asc")
            .add("count", "3")
            .add("fields", "photo_200")
            .apply { if (justFriends) add("filter", "friends") }
            .build()
    )

    public fun subscribeToGroup(
        accessToken: String,
        groupId: String,
    ): Call = client.createRequest(
        host = HOST_VK_API,
        path = PATH_GROUPS_JOIN,
        requestBody = bodyBuilder(accessToken)
            .add(FIELD_GROUP_ID, groupId)
            .add(FIELD_SOURCE, "vkid_sdk")
            .build()
    )

    private fun bodyBuilder(accessToken: String) = FormBody.Builder()
        .add(FIELD_API_VERSION, API_VERSION_VALUE)
        .add(FIELD_ACCESS_TOKEN, accessToken)

    private companion object {
        private const val HOST_VK_API = "https://api.vk.com"
        private const val HOST_VK_ID = "https://id.vk.com"
        private const val PATH_ACCOUNT_PROFILE_SHORT_INFO = "method/account.getProfileShortInfo"
        private const val PATH_GROUPS_GET_BY_ID = "method/groups.getById"
        private const val PATH_GROUPS_GET_MEMBERS = "method/groups.getMembers"
        private const val PATH_GROUPS_JOIN = "method/groups.join"
        private const val PATH_SHOULD_SHOW_SUBSCRIPTION = "vkid_sdk_is_show_subscription"

        private const val FIELD_ACCESS_TOKEN = "access_token"
        private const val FIELD_GROUP_ID = "group_id"
        private const val FIELD_SOURCE = "source"
    }
}
