package com.vk.id.internal.auth

import android.net.Uri
import android.util.Base64
import com.vk.id.OAuth
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
    val webAuthPhoneScreen: Boolean,
    val oAuth: OAuth?,
)

private const val APP_ID = "app_id"
private const val CLIENT_ID = "client_id"

// todo: Change to actual host after oauth2 completion
private const val AUTHORITY_BROWSER = "tk-training.id.cs7777.vk.com"
private const val AUTHORITY_CODE_FLOW = "vkcexternalauth-codeflow"
private const val PATH_BROWSER = "authorize"
private const val CODE_CHALLENGE = "code_challenge"
private const val CODE_CHALLENGE_METHOD = "code_challenge_method"
private const val CODE_CHALLENGE_METHOD_VALUE = "s256"
private const val REDIRECT_URI = "redirect_uri"
private const val RESPONSE_TYPE = "response_type"
private const val RESPONSE_TYPE_CODE = "code"
private const val SCHEME_BROWSER = "https"
private const val STATE = "state"
private const val UUID = "uuid"
private const val DEVICE_ID = "device_id"
private const val ACTION = "action"
private const val LOCALE = "lang_id"
private const val THEME = "scheme"
private const val SCREEN_PARAM = "screen"
private const val SCREEN_PHONE = "phone"

internal fun basicCodeFlowUri(appPackage: String) = Uri.Builder()
    .scheme(appPackage)
    .authority(AUTHORITY_CODE_FLOW)
    .build()

internal fun AuthOptions.toAuthUriBrowser(): Uri {
    val builder = Uri.Builder()
        .appendQueryParameter(CLIENT_ID, appId)
        .appendQueryParameter(RESPONSE_TYPE, RESPONSE_TYPE_CODE)
        .appendQueryParameter(REDIRECT_URI, redirectUri)
        .appendQueryParameter(CODE_CHALLENGE_METHOD, CODE_CHALLENGE_METHOD_VALUE)
        .appendQueryParameter(CODE_CHALLENGE, codeChallenge)
        .appendQueryParameter(STATE, state)
        .appendQueryParameter(DEVICE_ID, deviceId)

    if (oAuth != null) {
        builder.appendQueryParameter(ACTION, oAuth.toQueryParam())
    }
    if (locale != null) {
        builder.appendQueryParameter(LOCALE, locale)
    }
    if (theme != null) {
        builder.appendQueryParameter(THEME, theme)
    }
    if (webAuthPhoneScreen) {
        builder.appendQueryParameter(SCREEN_PARAM, SCREEN_PHONE)
    }
    return builder.scheme(SCHEME_BROWSER)
        .authority(AUTHORITY_BROWSER)
        .path(PATH_BROWSER)
        .build()
}

internal fun AuthOptions.toAuthUriCodeFlow(appPackage: String): Uri {
    val builder = Uri.Builder()
        .appendQueryParameter(APP_ID, appId)
        .appendQueryParameter(RESPONSE_TYPE, RESPONSE_TYPE_CODE)
        .appendQueryParameter(REDIRECT_URI, redirectUri)
        .appendQueryParameter(CODE_CHALLENGE_METHOD, codeChallengeMethod)
        .appendQueryParameter(CODE_CHALLENGE, codeChallenge)
        .appendQueryParameter(STATE, state)
        .appendQueryParameter(UUID, deviceId)

    if (oAuth != null) {
        builder.appendQueryParameter(ACTION, oAuth.toQueryParam())
    }
    if (locale != null) {
        builder.appendQueryParameter(LOCALE, locale)
    }
    if (theme != null) {
        builder.appendQueryParameter(THEME, theme)
    }
    if (webAuthPhoneScreen) {
        builder.appendQueryParameter(SCREEN_PARAM, SCREEN_PHONE)
    }
    return builder.scheme(appPackage)
        .authority(AUTHORITY_CODE_FLOW)
        .build()
}

@Suppress("MagicNumber")
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

private fun OAuth.toQueryParam() = Base64
    .encodeToString("""{"name":"sdk_oauth","params":{"oauth":"$serverName"}}""".encodeToByteArray(), Base64.DEFAULT)
    .filter { it != '\n' }
