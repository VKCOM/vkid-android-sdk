package com.vk.id.fetchuser

import com.vk.id.VKIDUser
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.state.StateGenerator

internal class VKIDUserInfoFetcher(
    private val api: VKIDApiService,
    private val stateGenerator: StateGenerator,
    private val serviceCredentials: ServiceCredentials,
) {
    fun fetch(
        idToken: String,
        onSuccess: (VKIDUser) -> Unit,
        onFailedApiCall: (Throwable) -> Unit,
        onFailedOAuthState: () -> Unit,
    ) {
        val clientId = serviceCredentials.clientID
        val userInfoState = stateGenerator.regenerateState()
        val userInfoResult = api.getUserInfo(
            idToken = idToken,
            clientId = serviceCredentials.clientID,
            deviceId = clientId,
            state = userInfoState
        ).execute()
        userInfoResult.onFailure(onFailedApiCall)
        userInfoResult.onSuccess {
            if (it.state != userInfoState) {
                onFailedOAuthState()
                return
            }
            onSuccess(
                VKIDUser(
                    firstName = it.firstName,
                    lastName = it.lastName,
                    photo200 = it.avatar,
                    phone = it.phone,
                    email = it.email,
                )
            )
        }
    }
}
