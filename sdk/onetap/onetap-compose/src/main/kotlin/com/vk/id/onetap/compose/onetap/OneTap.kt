@file:OptIn(InternalVKIDApi::class)

package com.vk.id.onetap.compose.onetap

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.VKIDUser
import com.vk.id.auth.AuthCodeData
import com.vk.id.auth.Prompt
import com.vk.id.auth.VKIDAuthParams
import com.vk.id.auth.VKIDAuthUiParams
import com.vk.id.common.InternalVKIDApi
import com.vk.id.group.subscription.common.fail.VKIDGroupSubscriptionFail
import com.vk.id.group.subscription.common.style.GroupSubscriptionStyle
import com.vk.id.group.subscription.compose.ui.GroupSubscriptionSheet
import com.vk.id.group.subscription.compose.ui.GroupSubscriptionSnackbarHost
import com.vk.id.group.subscription.compose.ui.rememberGroupSubscriptionSheetState
import com.vk.id.multibranding.OAuthListWidget
import com.vk.id.multibranding.internal.LocalMultibrandingAnalyticsContext
import com.vk.id.multibranding.internal.MultibrandingAnalyticsContext
import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.onetap.common.OneTapStyle
import com.vk.id.onetap.compose.button.alternate.AdaptiveAlternateAccountButton
import com.vk.id.onetap.compose.button.auth.VKIDButton
import com.vk.id.onetap.compose.button.auth.VKIDButtonSmall
import com.vk.id.onetap.compose.button.auth.VKIDButtonState
import com.vk.id.onetap.compose.button.auth.VKIDButtonTextProvider
import com.vk.id.onetap.compose.button.startAuth
import com.vk.id.onetap.compose.onetap.OneTapAnalytics.uuidFromParams
import com.vk.id.onetap.compose.util.MeasureUnconstrainedViewWidth
import com.vk.id.util.InternalVKIDWithUpdatedLocale

/**
 * Composable function to display a VKID One Tap login interface with multibranding.
 * For more information how to integrate VK ID Authentication check docs https://id.vk.com/business/go/docs/ru/vkid/latest/vk-id/intro/plan
 *
 * @param modifier Modifier for this composable.
 * @param style The styling for the One Tap interface, default is [OneTapStyle.Light]
 * @param onAuth Callback function invoked on successful authentication with an [OneTapOAuth] and an [AccessToken].
 * The first parameter is the OAuth which was used for authorization or null if the main flow with OneTap was used.
 * The second parameter is the access token to be used for working with VK API.
 * @param onAuthCode A callback to be invoked upon successful first step of auth - receiving auth code
 * which can later be exchanged to access token.
 * isCompletion is true if [onAuth] won't be called.
 * This will happen if you passed auth parameters and implement their validation yourself.
 * In that case we can't exchange auth code for access token and you should do this yourself.
 * @param onFail Callback function invoked on authentication failure with on [OneTapOAuth] and a [VKIDAuthFail] object.
 * The first parameter is the OAuth which was used for authorization or null if the main flow with OneTap was used.
 * The second parameter is the error which happened during authorization.
 * @param oAuths A set of OAuths to be displayed.
 * @param signInAnotherAccountButtonEnabled Flag to enable a button for signing into another account.
 *  Note that if text doesn't fit the available width the view will be hidden regardless of the flag.
 * @param authParams Optional params to be passed to auth. See [VKIDAuthUiParams.Builder] for more info.
 * @param fastAuthEnabled Whether to fetch user. Defaults to true.
 * In case this parameter is set to false the user data won't be fetched and user will have to confirm authorization on click.
 * Note that this parameter doesn't support changes in runtime.
 */
@Composable
@Suppress("LongMethod", "CyclomaticComplexMethod")
public fun OneTap(
    modifier: Modifier = Modifier,
    style: OneTapStyle = OneTapStyle.Light(),
    onAuth: (oAuth: OneTapOAuth?, accessToken: AccessToken) -> Unit,
    onAuthCode: (data: AuthCodeData, isCompletion: Boolean) -> Unit = { _, _ -> },
    onFail: (oAuth: OneTapOAuth?, fail: VKIDAuthFail) -> Unit = { _, _ -> },
    oAuths: Set<OneTapOAuth> = emptySet(),
    fastAuthEnabled: Boolean = true,
    signInAnotherAccountButtonEnabled: Boolean = false,
    authParams: VKIDAuthUiParams = VKIDAuthUiParams {},
) {
    OneTap(
        modifier = modifier,
        style = style,
        onAuth = onAuth,
        onAuthCode = onAuthCode,
        onFail = onFail,
        oAuths = oAuths,
        fastAuthEnabled = fastAuthEnabled,
        signInAnotherAccountButtonEnabled = signInAnotherAccountButtonEnabled,
        authParams = authParams,
        scenario = OneTapTitleScenario.SignIn,
    )
}

/**
 * Composable function to display a VKID One Tap login interface with multibranding.
 * For more information how to integrate VK ID Authentication check docs https://id.vk.com/business/go/docs/ru/vkid/latest/vk-id/intro/plan
 *
 * This version integrates Group Subscription flow. The flow will be shown right after successful auth.
 * NOTE: The "groups" scope will be added automatically to the set of requested scopes.
 *
 * @param modifier Modifier for this composable.
 * @param style The styling for the One Tap interface, default is [OneTapStyle.Light]
 * @param onAuth Callback function invoked on successful authentication with an [OneTapOAuth] and an [AccessToken].
 * The first parameter is the OAuth which was used for authorization or null if the main flow with OneTap was used.
 * The second parameter is the access token to be used for working with VK API.
 * @param onAuthCode A callback to be invoked upon successful first step of auth - receiving auth code
 * which can later be exchanged to access token.
 * isCompletion is true if [onAuth] won't be called.
 * This will happen if you passed auth parameters and implement their validation yourself.
 * In that case we can't exchange auth code for access token and you should do this yourself.
 * @param onFail Callback function invoked on authentication failure with on [OneTapOAuth] and a [VKIDAuthFail] object.
 * The first parameter is the OAuth which was used for authorization or null if the main flow with OneTap was used.
 * The second parameter is the error which happened during authorization.
 * @param oAuths A set of OAuths to be displayed.
 * @param signInAnotherAccountButtonEnabled Flag to enable a button for signing into another account.
 *  Note that if text doesn't fit the available width the view will be hidden regardless of the flag.
 * @param authParams Optional params to be passed to auth. See [VKIDAuthUiParams.Builder] for more info.
 * @param fastAuthEnabled Whether to fetch user. Defaults to true.
 * In case this parameter is set to false the user data won't be fetched and user will have to confirm authorization on click.
 * Note that this parameter doesn't support changes in runtime.
 * @param subscribeToGroupId The id of the group the user will be subscribed to.
 * @param onSuccessSubscribingToGroup Will be called upon successful subscription.
 * @param onFailSubscribingToGroup Will be called upon any unsuccessful flow completion along with an description of the specific encountered error.
 * @param groupSubscriptionSnackbarHostState The host state for snackbars.
 * Use along with [GroupSubscriptionSnackbarHost] and pass the same state as there.
 * @param groupSubscriptionStyle The widget style, can change appearance.
 */
@Composable
public fun OneTap(
    modifier: Modifier = Modifier,
    style: OneTapStyle = OneTapStyle.Light(),
    onAuth: (oAuth: OneTapOAuth?, accessToken: AccessToken) -> Unit,
    onAuthCode: (data: AuthCodeData, isCompletion: Boolean) -> Unit = { _, _ -> },
    onFail: (oAuth: OneTapOAuth?, fail: VKIDAuthFail) -> Unit = { _, _ -> },
    oAuths: Set<OneTapOAuth> = emptySet(),
    fastAuthEnabled: Boolean = true,
    signInAnotherAccountButtonEnabled: Boolean = false,
    authParams: VKIDAuthUiParams = VKIDAuthUiParams {},
    scenario: OneTapTitleScenario = OneTapTitleScenario.SignIn,
    subscribeToGroupId: String,
    onSuccessSubscribingToGroup: () -> Unit,
    onFailSubscribingToGroup: (VKIDGroupSubscriptionFail) -> Unit = {},
    groupSubscriptionSnackbarHostState: SnackbarHostState,
    groupSubscriptionStyle: GroupSubscriptionStyle = GroupSubscriptionStyle.Light(),
) {
    var isSuccessfulAuth by remember { mutableStateOf("") }
    Box(modifier = modifier) {
        OneTap(
            style = style,
            onAuth = { oAuth, accessToken ->
                onAuth(oAuth, accessToken)
                isSuccessfulAuth = System.currentTimeMillis().toString()
            },
            onAuthCode = onAuthCode,
            onFail = onFail,
            oAuths = oAuths,
            fastAuthEnabled = fastAuthEnabled,
            signInAnotherAccountButtonEnabled = signInAnotherAccountButtonEnabled,
            authParams = authParams.newBuilder {
                scopes += "groups"
            },
            scenario = scenario,
        )
        if (isSuccessfulAuth.isNotBlank()) {
            val state = rememberGroupSubscriptionSheetState()
            GroupSubscriptionSheet(
                state = state,
                accessTokenProvider = { VKID.instance.accessToken?.token ?: error("Not authorized") },
                groupId = subscribeToGroupId,
                onSuccess = onSuccessSubscribingToGroup,
                onFail = onFailSubscribingToGroup,
                snackbarHostState = groupSubscriptionSnackbarHostState,
                style = groupSubscriptionStyle,
            )
            LaunchedEffect(isSuccessfulAuth) {
                state.show()
            }
        }
    }
}

/**
 * Composable function to display a VKID One Tap login interface with multibranding.
 * For more information how to integrate VK ID Authentication check docs https://id.vk.com/business/go/docs/ru/vkid/latest/vk-id/intro/plan
 *
 * @param modifier Modifier for this composable.
 * @param style The styling for the One Tap interface, default is [OneTapStyle.Light]
 * @param onAuth Callback function invoked on successful authentication with an [OneTapOAuth] and an [AccessToken].
 * The first parameter is the OAuth which was used for authorization or null if the main flow with OneTap was used.
 * The second parameter is the access token to be used for working with VK API.
 * @param onAuthCode A callback to be invoked upon successful first step of auth - receiving auth code
 * which can later be exchanged to access token.
 * isCompletion is true if [onAuth] won't be called.
 * This will happen if you passed auth parameters and implement their validation yourself.
 * In that case we can't exchange auth code for access token and you should do this yourself.
 * @param onFail Callback function invoked on authentication failure with on [OneTapOAuth] and a [VKIDAuthFail] object.
 * The first parameter is the OAuth which was used for authorization or null if the main flow with OneTap was used.
 * The second parameter is the error which happened during authorization.
 * @param oAuths A set of OAuths to be displayed.
 * @param signInAnotherAccountButtonEnabled Flag to enable a button for signing into another account.
 *  Note that if text doesn't fit the available width the view will be hidden regardless of the flag.
 * @param authParams Optional params to be passed to auth. See [VKIDAuthUiParams.Builder] for more info.
 * @param fastAuthEnabled Whether to fetch user. Defaults to true.
 * In case this parameter is set to false the user data won't be fetched and user will have to confirm authorization on click.
 * Note that this parameter doesn't support changes in runtime.
 * @param scenario Scenario for which the OneTap is used. Changes title accordingly.
 */
@Composable
@Suppress("LongMethod", "CyclomaticComplexMethod")
public fun OneTap(
    modifier: Modifier = Modifier,
    style: OneTapStyle = OneTapStyle.Light(),
    onAuth: (oAuth: OneTapOAuth?, accessToken: AccessToken) -> Unit,
    onAuthCode: (data: AuthCodeData, isCompletion: Boolean) -> Unit = { _, _ -> },
    onFail: (oAuth: OneTapOAuth?, fail: VKIDAuthFail) -> Unit = { _, _ -> },
    oAuths: Set<OneTapOAuth> = emptySet(),
    fastAuthEnabled: Boolean = true,
    signInAnotherAccountButtonEnabled: Boolean = false,
    authParams: VKIDAuthUiParams = VKIDAuthUiParams {},
    scenario: OneTapTitleScenario = OneTapTitleScenario.SignIn,
) {
    InternalVKIDWithUpdatedLocale {
        val coroutineScope = rememberCoroutineScope()
        var user by remember { mutableStateOf<VKIDUser?>(null) }
        val fastAuthEnabledValue by remember { mutableStateOf(fastAuthEnabled) }
        if (fastAuthEnabledValue != fastAuthEnabled) {
            error("You can't change fastAuthEnabled in runtime")
        }
        @Composable
        fun IconOneTap() {
            OneTapAnalytics.OneTapIconShown(scenario = scenario, style = style)
            VKIDButtonSmall(
                style = style.vkidButtonStyle,
                onClick = {
                    val extraAuthParams = OneTapAnalytics.oneTapPressedIcon(user)
                    startAuth(
                        coroutineScope,
                        {
                            onAuth(null, it)
                        },
                        onAuthCode,
                        {
                            val uuid = extraAuthParams.uuidFromParams()
                            OneTapAnalytics.authErrorIcon(uuid)
                            onFail(null, it)
                        },
                        params = authParams.asParamsBuilder {
                            extraParams = extraAuthParams
                            if (!fastAuthEnabled) {
                                useOAuthProviderIfPossible = false
                                prompt = Prompt.LOGIN
                            } else if (user == null) {
                                prompt = Prompt.CONSENT
                            }
                        },
                    )
                },
                onUserFetched = {
                    user = it
                    if (user == null) {
                        OneTapAnalytics.sessionNotFound()
                        OneTapAnalytics.userNotFoundIcon()
                    } else {
                        OneTapAnalytics.userWasFoundIcon()
                    }
                },
                fastAuthEnabled = fastAuthEnabled,
            )
        }

        @Composable
        fun LargeOneTap(
            measureInProgress: Boolean,
            largeText: Boolean,
        ) {
            if (!measureInProgress) {
                OneTapAnalytics.OneTapShown(scenario = scenario, style = style)
            }
            CompositionLocalProvider(
                LocalMultibrandingAnalyticsContext provides MultibrandingAnalyticsContext(
                    screen = "nowhere",
                    isPaused = measureInProgress
                )
            ) {
                OneTap(
                    modifier = Modifier,
                    style = style,
                    oAuths = oAuths,
                    signInAnotherAccountButtonEnabled = signInAnotherAccountButtonEnabled,
                    vkidButtonTextProvider = null,
                    onVKIDButtonClick = {
                        val extraAuthParams = OneTapAnalytics.oneTapPressed(user)
                        startAuth(
                            coroutineScope,
                            {
                                onAuth(null, it)
                            },
                            onAuthCode,
                            {
                                val uuid = extraAuthParams.uuidFromParams()
                                OneTapAnalytics.authError(uuid)
                                onFail(null, it)
                            },
                            authParams.asParamsBuilder {
                                theme = style.toProviderTheme()
                                extraParams = extraAuthParams
                                if (!fastAuthEnabled) {
                                    useOAuthProviderIfPossible = false
                                    prompt = Prompt.LOGIN
                                } else if (user == null) {
                                    prompt = Prompt.CONSENT
                                }
                            }
                        )
                    },
                    onAlternateButtonClick = {
                        val extraAuthParams = OneTapAnalytics.alternatePressed()
                        startAuth(
                            coroutineScope,
                            { onAuth(null, it) },
                            onAuthCode,
                            {
                                val uuid = extraAuthParams.uuidFromParams()
                                OneTapAnalytics.authError(uuid)
                                onFail(null, it)
                            },
                            authParams.asParamsBuilder {
                                useOAuthProviderIfPossible = false
                                theme = style.toProviderTheme()
                                prompt = Prompt.LOGIN
                                extraParams = extraAuthParams
                            }
                        )
                    },
                    onAuth = onAuth,
                    onAuthCode = onAuthCode,
                    onFail = onFail,
                    authParams = authParams,
                    onUserFetched = {
                        if (!measureInProgress) {
                            user = it
                            if (user == null) {
                                OneTapAnalytics.sessionNotFound()
                                OneTapAnalytics.userNotFound()
                            } else {
                                OneTapAnalytics.userWasFound(signInAnotherAccountButtonEnabled)
                            }
                        }
                    },
                    fastAuthEnabled = fastAuthEnabled,
                    largeText = largeText,
                    measureInProgress = measureInProgress,
                    scenario = scenario,
                )
            }
        }
        if (style is OneTapStyle.Icon) {
            IconOneTap()
        } else {
            BoxWithConstraints(
                modifier = modifier,
            ) {
                MeasureUnconstrainedViewWidth(viewToMeasure = {
                    LargeOneTap(measureInProgress = true, largeText = true)
                }) { largeTextWidth ->
                    if (largeTextWidth <= maxWidth) {
                        LargeOneTap(measureInProgress = false, largeText = true)
                    } else {
                        MeasureUnconstrainedViewWidth(viewToMeasure = {
                            LargeOneTap(measureInProgress = true, largeText = false)
                        }) { smallTextWidth ->
                            if (smallTextWidth <= maxWidth) {
                                LargeOneTap(measureInProgress = false, largeText = false)
                            } else {
                                IconOneTap()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Suppress("LongParameterList", "NonSkippableComposable")
@Composable
internal fun OneTap(
    modifier: Modifier = Modifier,
    style: OneTapStyle = OneTapStyle.Light(),
    oAuths: Set<OneTapOAuth>,
    signInAnotherAccountButtonEnabled: Boolean = false,
    vkidButtonTextProvider: VKIDButtonTextProvider?,
    onVKIDButtonClick: () -> Unit,
    onAlternateButtonClick: () -> Unit,
    onAuth: (OneTapOAuth?, AccessToken) -> Unit,
    onAuthCode: (AuthCodeData, Boolean) -> Unit,
    onFail: (OneTapOAuth?, VKIDAuthFail) -> Unit,
    authParams: VKIDAuthUiParams = VKIDAuthUiParams {},
    onUserFetched: (VKIDUser?) -> Unit = {},
    fastAuthEnabled: Boolean,
    largeText: Boolean,
    measureInProgress: Boolean,
    scenario: OneTapTitleScenario,
) {
    val vkidButtonState = remember { VKIDButtonState(inProgress = false) }
    Column(modifier = modifier) {
        VKIDButton(
            modifier = Modifier.testTag("vkid_button"),
            style = style.vkidButtonStyle,
            state = vkidButtonState,
            textProvider = vkidButtonTextProvider,
            onClick = onVKIDButtonClick,
            onUserFetched = onUserFetched,
            fastAuthEnabled = fastAuthEnabled,
            largeText = largeText,
            scenario = scenario,
        )
        if (signInAnotherAccountButtonEnabled) {
            AdaptiveAlternateAccountButton(
                vkidButtonState = vkidButtonState,
                style = style.alternateAccountButtonStyle,
                onAlternateButtonClick,
                largeText = largeText,
            )
        }
        if (oAuths.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            OAuthListWidget(
                onAuth = { oAuth, accessToken -> onAuth(OneTapOAuth.fromOAuth(oAuth), accessToken) },
                onAuthCode = onAuthCode,
                onFail = { oAuth, fail -> onFail(OneTapOAuth.fromOAuth(oAuth), fail) },
                style = style.oAuthListWidgetStyle,
                oAuths = oAuths.map { it.toOAuth() }.toSet(),
                authParams = authParams,
                measureInProgress = measureInProgress,
            )
        }
    }
}

private fun OneTapStyle.toProviderTheme(): VKIDAuthParams.Theme? = when (this) {
    is OneTapStyle.Dark,
    is OneTapStyle.SecondaryDark,
    is OneTapStyle.TransparentDark -> VKIDAuthParams.Theme.Dark

    is OneTapStyle.Light,
    is OneTapStyle.SecondaryLight,
    is OneTapStyle.TransparentLight -> VKIDAuthParams.Theme.Light

    is OneTapStyle.Icon -> null
}

@Preview
@Composable
private fun OneTapPreview() {
    OneTap(onAuth = { _, _ -> })
}
