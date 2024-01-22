package com.vk.silentauth

internal data class SilentAuthInfoWithProviderWeight(
    val info: SilentAuthInfo,
    val providerWeight: Int,
)
