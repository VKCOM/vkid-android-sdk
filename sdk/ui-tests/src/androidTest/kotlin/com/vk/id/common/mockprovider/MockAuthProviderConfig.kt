package com.vk.id.common.mockprovider

import com.vk.id.VKIDUser

internal data class MockAuthProviderConfig(
    var deviceId: String? = null,
    var deviceIdIsNull: Boolean = false,
    var overrideState: String? = null,
    var overrideOAuthToNull: Boolean = false,
    var user: VKIDUser? = null,
    var requireUnsetUseAuthProviderIfPossible: Boolean = false,
)
