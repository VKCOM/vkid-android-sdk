package com.vk.id.internal.auth

internal data class AuthOptions(
    val appId: String,
    val clientSecret: String,
    val codeChallenge: String,
    val codeChallengeMethod: String,
    val deviceId: String,
    val redirectUri: String,
    val state: String,
)
