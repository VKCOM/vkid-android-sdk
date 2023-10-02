package com.vk.id.internal.auth

import android.net.Uri
import com.vk.id.auth.VKIDAuthParams

internal data class AuthOptions(
    val appId: String,
    val clientSecret: String,
    val codeChallenge: String,
    val codeChallengeMethod: String,
    val deviceId: String,
    val redirectUri: String,
    val state: String,
    val locale: String?,
    val theme: String?,
)

private const val APP_ID = "app_id"
private const val AUTHORITY_BROWSER = "id.vk.com"
private const val AUTHORITY_CODE_FLOW = "vkcexternalauth-codeflow"
private const val PATH_BROWSER = "auth"
private const val CODE_CHALLENGE = "code_challenge"
private const val CODE_CHALLENGE_METHOD = "code_challenge_method"
private const val REDIRECT_URI = "redirect_uri"
private const val RESPONSE_TYPE = "response_type"
private const val RESPONSE_TYPE_CODE = "code"
private const val SCHEME_BROWSER = "https"
private const val STATE = "state"
private const val UUID = "uuid"
private const val LOCALE = "lang_id"
private const val THEME = "scheme"

internal fun basicCodeFlowUri(appPackage: String) = Uri.Builder()
    .scheme(appPackage)
    .authority(AUTHORITY_CODE_FLOW)
    .build()

internal fun AuthOptions.toAuthUriBrowser(): Uri = toAuthUriBuilder()
    .scheme(SCHEME_BROWSER)
    .authority(AUTHORITY_BROWSER)
    .path(PATH_BROWSER)
    .build()

internal fun AuthOptions.toAuthUriCodeFlow(appPackage: String): Uri = toAuthUriBuilder()
    .scheme(appPackage)
    .authority(AUTHORITY_CODE_FLOW)
    .build()

private fun AuthOptions.toAuthUriBuilder(): Uri.Builder {
    val builder = Uri.Builder()
        .appendQueryParameter(APP_ID, appId)
        .appendQueryParameter(RESPONSE_TYPE, RESPONSE_TYPE_CODE)
        .appendQueryParameter(REDIRECT_URI, redirectUri)
        .appendQueryParameter(CODE_CHALLENGE_METHOD, codeChallengeMethod)
        .appendQueryParameter(CODE_CHALLENGE, codeChallenge)
        .appendQueryParameter(STATE, state)
        .appendQueryParameter(UUID, deviceId)

    if (locale != null) {
        builder.appendQueryParameter(LOCALE, locale)
    }
    if (theme != null) {
        builder.appendQueryParameter(THEME, theme)
    }
    return builder
}

internal fun VKIDAuthParams.Locale.toQueryParam(): String = when (this) {
    VKIDAuthParams.Locale.RUS -> 0
    VKIDAuthParams.Locale.UKR -> 1
    VKIDAuthParams.Locale.ENG -> 3
    VKIDAuthParams.Locale.SPA -> 4
    VKIDAuthParams.Locale.GERMAN -> 6
    VKIDAuthParams.Locale.POL -> 15
    VKIDAuthParams.Locale.FRA -> 16
    VKIDAuthParams.Locale.TURKEY -> 82
}.toString()

internal fun VKIDAuthParams.Theme.toQueryParam(): String = when (this) {
    VKIDAuthParams.Theme.Light -> "bright_light"
    VKIDAuthParams.Theme.Dark -> "space_gray"
}
