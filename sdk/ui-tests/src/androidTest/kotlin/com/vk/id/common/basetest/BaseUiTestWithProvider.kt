package com.vk.id.common.basetest

import com.vk.id.VKIDUser
import com.vk.id.common.mockprovider.MockAuthProviderConfig
import com.vk.id.common.mockprovider.TestAuthProviderActivity
import org.junit.Before

public open class BaseUiTestWithProvider : BaseUiTest() {

    @Before
    public fun resetTestAuthProviderActivityConfig() {
        TestAuthProviderActivity.mockAuthProviderConfig = MockAuthProviderConfig()
    }

    internal fun user(user: VKIDUser?) {
        TestAuthProviderActivity.mockAuthProviderConfig.user = user
    }

    internal fun overrideDeviceId(deviceId: String) {
        TestAuthProviderActivity.mockAuthProviderConfig.deviceId = deviceId
    }

    internal fun deviceIdIsNull() {
        TestAuthProviderActivity.mockAuthProviderConfig.deviceIdIsNull = true
    }

    internal fun overrideOAuthToNull() {
        TestAuthProviderActivity.mockAuthProviderConfig.overrideOAuthToNull = true
    }

    internal fun overrideState(state: String?) {
        TestAuthProviderActivity.mockAuthProviderConfig.overrideState = state
    }

    internal fun requireUnsetUseAuthProviderIfPossible() {
        TestAuthProviderActivity.mockAuthProviderConfig.requireUnsetUseAuthProviderIfPossible = true
    }
}
