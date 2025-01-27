package com.vk.id.network.groupsubscription

import com.vk.id.common.InternalVKIDApi
import com.vk.id.network.groupsubscription.data.InternalVKIDGroupByIdData
import com.vk.id.network.groupsubscription.data.InternalVKIDGroupMembersData
import com.vk.id.network.groupsubscription.data.InternalVKIDMemberData
import com.vk.id.network.groupsubscription.exception.InternalVKIDAlreadyGroupMemberException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException

@InternalVKIDApi
public class InternalVKIDGroupSubscriptionApiService(
    private val api: InternalVKIDGroupSubscriptionApi,
) {
    public suspend fun isServiceAccount(
        accessToken: String,
    ): Boolean {
        return withContext(Dispatchers.IO) {
            val response = api.getProfileShortInfo(accessToken).execute()
            val body = JSONObject(requireNotNull(response.body).string())
            if (body.isNull("error")) {
                body.getJSONObject("response").getBoolean("is_service_account")
            } else {
                throw IOException(body.getString("error"))
            }
        }
    }

    public suspend fun getGroup(
        accessToken: String,
        groupId: String
    ): InternalVKIDGroupByIdData {
        return withContext(Dispatchers.IO) {
            val response = api.getGroup(accessToken = accessToken, groupId = groupId).execute()
            val body = JSONObject(requireNotNull(response.body).string())
            if (body.isNull("error")) {
                with(body.getJSONObject("response").getJSONArray("groups").get(0) as JSONObject) {
                    if (getInt("is_member") == 1) throw InternalVKIDAlreadyGroupMemberException()
                    InternalVKIDGroupByIdData(
                        groupId = getString("id"),
                        name = getString("name"),
                        imageUrl = getString("photo_200"),
                        description = getString("description"),
                        isVerified = getInt("verified") == 1,
                    )
                }
            } else {
                throw IOException(body.getString("error"))
            }
        }
    }

    public suspend fun getMembers(
        accessToken: String,
        groupId: String,
        justFriends: Boolean
    ): InternalVKIDGroupMembersData {
        return withContext(Dispatchers.IO) {
            val response = api.getMembers(accessToken = accessToken, groupId = groupId, justFriends = justFriends).execute()
            val body = JSONObject(requireNotNull(response.body).string())
            if (body.isNull("error")) {
                val bodyResponse = body.getJSONObject("response")
                val items = bodyResponse.getJSONArray("items")
                InternalVKIDGroupMembersData(
                    count = bodyResponse.getInt("count"),
                    members = (0 until items.length()).map {
                        with(items.getJSONObject(it)) {
                            InternalVKIDMemberData(
                                imageUrl = getString("photo_200"),
                            )
                        }
                    }
                )
            } else {
                throw IOException(body.getString("error"))
            }
        }
    }

    public suspend fun subscribeToGroup(
        accessToken: String,
        groupId: String,
    ) {
        withContext(Dispatchers.IO) {
            val response = api.subscribeToGroup(accessToken = accessToken, groupId = groupId).execute()
            val body = JSONObject(requireNotNull(response.body).string())
            if (!body.has("response") || body.getInt("response") != 1) {
                throw IOException(body.getString("error"))
            }
        }
    }
}
