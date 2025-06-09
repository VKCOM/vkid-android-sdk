@file:OptIn(InternalVKIDApi::class)

package com.vk.id.onetap.compose.onetap.sheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.AuthCodeData
import com.vk.id.auth.Prompt
import com.vk.id.auth.VKIDAuthCallback
import com.vk.id.auth.VKIDAuthUiParams
import com.vk.id.common.InternalVKIDApi
import com.vk.id.group.subscription.common.fail.VKIDGroupSubscriptionFail
import com.vk.id.group.subscription.common.style.GroupSubscriptionStyle
import com.vk.id.group.subscription.compose.ui.GroupSubscriptionSheet
import com.vk.id.group.subscription.compose.ui.GroupSubscriptionSnackbarHost
import com.vk.id.group.subscription.compose.ui.rememberGroupSubscriptionSheetState
import com.vk.id.multibranding.internal.LocalMultibrandingAnalyticsContext
import com.vk.id.multibranding.internal.MultibrandingAnalyticsContext
import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.onetap.compose.onetap.sheet.content.OneTapBottomSheetAuthStatus
import com.vk.id.onetap.compose.onetap.sheet.content.SheetContentAuthFailed
import com.vk.id.onetap.compose.onetap.sheet.content.SheetContentAuthInProgress
import com.vk.id.onetap.compose.onetap.sheet.content.SheetContentAuthSuccess
import com.vk.id.onetap.compose.onetap.sheet.content.SheetContentMain
import com.vk.id.onetap.compose.onetap.sheet.content.startAlternateAuth
import com.vk.id.onetap.compose.onetap.sheet.content.startVKIDAuth
import com.vk.id.onetap.compose.onetap.sheet.content.toProviderTheme
import com.vk.id.onetap.compose.onetap.sheet.style.OneTapBottomSheetStyle
import com.vk.id.util.InternalVKIDWithUpdatedLocale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

/**
 * Composable function which creates a state for [OneTapBottomSheet] and can be used as `state` parameter.
 *
 * It provides [OneTapBottomSheetState] which can, for example, trigger hiding and showing the bottom sheet.
 */
@Composable
public fun rememberOneTapBottomSheetState(): OneTapBottomSheetState {
    return rememberOneTapBottomSheetStateInternal()
}

/**
 * Composable function to display a bottom sheet for VKID One Tap authentication with multibranding.
 *
 * This version integrates Group Subscription flow. The flow will be shown right after successful auth.
 * NOTE: The "groups" scope will be added automatically to the set of requested scopes.
 *
 * @param modifier Modifier for this composable.
 * @param state The state of the bottom sheet. To control sheet create state instance with [rememberOneTapBottomSheetState] and pass it here.
 * @param serviceName The name of the service for authentication. Will be displayed as title of sheet.
 * @param scenario The [OneTapScenario] under which the authentication is being performed. It reflects on the texts of the button and sheet.
 * @param autoHideOnSuccess Automatically hide the sheet on successful authentication.
 * @param onAuth Callback function invoked on successful authentication with an [OneTapOAuth] and an [AccessToken].
 * The first parameter is the OAuth which was used for authorization or null if the main flow with OneTap was used.
 * The second parameter is the access token to be used for working with VK API.
 * @param onAuthCode A callback to be invoked upon successful first step of auth - receiving auth code
 * which can later be exchanged to access token.
 * isCompletion is true if [onAuth] won't be called.
 * This will happen if you passed auth parameters and implement their validation yourself.
 * In that case we can't exchange auth code for access token and you should do this yourself.
 * @param onFail Callback function invoked on authentication failure with an [OneTapOAuth] and a [VKIDAuthFail] object.
 * The first parameter is the OAuth which was used for authorization or null if the main flow with OneTap was used.
 * The second parameter is the error which happened during authorization.
 * @param style The [OneTapBottomSheetStyle] of the bottom sheet. Default is [OneTapBottomSheetStyle.Light]
 * @param authParams Optional params to be passed to auth. See [VKIDAuthUiParams.Builder] for more info.
 * @param fastAuthEnabled Whether to fetch user. Defaults to true.
 * In case this parameter is set to false the user data won't be fetched and user will have to confirm authorization on click.
 * Note: this parameter doesn't support changes in runtime.
 * Note: This parameter will hide "change account" button because in this case OneTap will have the same behaviour.
 * @param subscribeToGroupId The id of the group the user will be subscribed to.
 * @param onSuccessSubscribingToGroup Will be called upon successful subscription.
 * @param onFailSubscribingToGroup Will be called upon any unsuccessful flow completion along with an description of the specific encountered error.
 * @param groupSubscriptionSnackbarHostState The host state for snackbars.
 * Use along with [GroupSubscriptionSnackbarHost] and pass the same state as there.
 * NOTE: In case you pass null, the host will be put in the place you put this Composable.
 * @param groupSubscriptionStyle The widget style, can change appearance.
 * @param autoShowSheetDelayMillis Delay in millis after which sheet will be automatically shown.
 * Examples:
 * - null: not shown automatically
 * - 0: shown automatically immediately
 * - 1000: show automatically after 1 second
 */
@Composable
public fun OneTapBottomSheet(
    modifier: Modifier = Modifier,
    state: OneTapBottomSheetState = rememberOneTapBottomSheetState(),
    serviceName: String,
    scenario: OneTapScenario = OneTapScenario.EnterService,
    autoHideOnSuccess: Boolean = true,
    onAuth: (oAuth: OneTapOAuth?, accessToken: AccessToken) -> Unit,
    onAuthCode: (data: AuthCodeData, isCompletion: Boolean) -> Unit = { _, _ -> },
    onFail: (oAuth: OneTapOAuth?, fail: VKIDAuthFail) -> Unit = { _, _ -> },
    oAuths: Set<OneTapOAuth> = emptySet(),
    style: OneTapBottomSheetStyle = OneTapBottomSheetStyle.Light(),
    authParams: VKIDAuthUiParams = VKIDAuthUiParams {},
    fastAuthEnabled: Boolean = true,
    subscribeToGroupId: String,
    onSuccessSubscribingToGroup: () -> Unit,
    onFailSubscribingToGroup: (VKIDGroupSubscriptionFail) -> Unit = {},
    groupSubscriptionSnackbarHostState: SnackbarHostState? = null,
    groupSubscriptionStyle: GroupSubscriptionStyle = GroupSubscriptionStyle.Light(),
    autoShowSheetDelayMillis: Long? = null,
) {
    val snackbarHostState = groupSubscriptionSnackbarHostState ?: remember { SnackbarHostState() }
    var isSuccessfulAuth by remember { mutableStateOf("") }
    Box(modifier = modifier) {
        OneTapBottomSheet(
            state = state,
            serviceName = serviceName,
            scenario = scenario,
            autoHideOnSuccess = autoHideOnSuccess,
            onAuth = { oAuth, accessToken ->
                onAuth(oAuth, accessToken)
                isSuccessfulAuth = System.currentTimeMillis().toString()
            },
            onAuthCode = onAuthCode,
            onFail = onFail,
            oAuths = oAuths,
            style = style,
            authParams = authParams.newBuilder {
                scopes += "groups"
            },
            fastAuthEnabled = fastAuthEnabled,
            autoShowSheetDelayMillis = autoShowSheetDelayMillis,
        )
        if (isSuccessfulAuth.isNotBlank()) {
            val groupSubscriptionSheetState = rememberGroupSubscriptionSheetState()
            if (groupSubscriptionSnackbarHostState == null) {
                GroupSubscriptionSnackbarHost(snackbarHostState)
            }
            GroupSubscriptionSheet(
                state = groupSubscriptionSheetState,
                accessTokenProvider = { VKID.instance.accessToken?.token ?: error("Not authorized") },
                groupId = subscribeToGroupId,
                onSuccess = onSuccessSubscribingToGroup,
                onFail = onFailSubscribingToGroup,
                snackbarHostState = snackbarHostState,
                style = groupSubscriptionStyle,
            )
            LaunchedEffect(isSuccessfulAuth) {
                groupSubscriptionSheetState.show()
            }
        }
    }
}

/**
 * Composable function to display a bottom sheet for VKID One Tap authentication with multibranding.
 *
 * @param modifier Modifier for this composable.
 * @param state The state of the bottom sheet. To control sheet create state instance with [rememberOneTapBottomSheetState] and pass it here.
 * @param serviceName The name of the service for authentication. Will be displayed as title of sheet.
 * @param scenario The [OneTapScenario] under which the authentication is being performed. It reflects on the texts of the button and sheet.
 * @param autoHideOnSuccess Automatically hide the sheet on successful authentication.
 * @param onAuth Callback function invoked on successful authentication with an [OneTapOAuth] and an [AccessToken].
 * The first parameter is the OAuth which was used for authorization or null if the main flow with OneTap was used.
 * The second parameter is the access token to be used for working with VK API.
 * @param onAuthCode A callback to be invoked upon successful first step of auth - receiving auth code
 * which can later be exchanged to access token.
 * isCompletion is true if [onAuth] won't be called.
 * This will happen if you passed auth parameters and implement their validation yourself.
 * In that case we can't exchange auth code for access token and you should do this yourself.
 * @param onFail Callback function invoked on authentication failure with an [OneTapOAuth] and a [VKIDAuthFail] object.
 * The first parameter is the OAuth which was used for authorization or null if the main flow with OneTap was used.
 * The second parameter is the error which happened during authorization.
 * @param style The [OneTapBottomSheetStyle] of the bottom sheet. Default is [OneTapBottomSheetStyle.Light]
 * @param authParams Optional params to be passed to auth. See [VKIDAuthUiParams.Builder] for more info.
 * @param fastAuthEnabled Whether to fetch user. Defaults to true.
 * In case this parameter is set to false the user data won't be fetched and user will have to confirm authorization on click.
 * Note: this parameter doesn't support changes in runtime.
 * Note: This parameter will hide "change account" button because in this case OneTap will have the same behaviour.
 * @param autoShowSheetDelayMillis Delay in millis after which sheet will be automatically shown.
 * Examples:
 * - null: not shown automatically
 * - 0: shown automatically immediately
 * - 1000: show automatically after 1 second
 */
@OptIn(InternalVKIDApi::class)
@Composable
public fun OneTapBottomSheet(
    modifier: Modifier = Modifier,
    state: OneTapBottomSheetState = rememberOneTapBottomSheetState(),
    serviceName: String,
    scenario: OneTapScenario = OneTapScenario.EnterService,
    autoHideOnSuccess: Boolean = true,
    onAuth: (oAuth: OneTapOAuth?, accessToken: AccessToken) -> Unit,
    onAuthCode: (data: AuthCodeData, isCompletion: Boolean) -> Unit = { _, _ -> },
    onFail: (oAuth: OneTapOAuth?, fail: VKIDAuthFail) -> Unit = { _, _ -> },
    oAuths: Set<OneTapOAuth> = emptySet(),
    style: OneTapBottomSheetStyle = OneTapBottomSheetStyle.Light(),
    authParams: VKIDAuthUiParams = VKIDAuthUiParams {},
    fastAuthEnabled: Boolean = true,
    autoShowSheetDelayMillis: Long? = null,
) {
    InternalVKIDWithUpdatedLocale {
        val rememberedFastAuthEnabledValue by remember { mutableStateOf(fastAuthEnabled) }
        if (rememberedFastAuthEnabledValue != fastAuthEnabled) {
            error("You can't change fastAuthEnabled in runtime")
        }
        autoShowSheetDelayMillis?.let {
            LaunchedEffect(it) {
                launch {
                    delay(it)
                    state.show()
                }
            }
        }
        CompositionLocalProvider(LocalMultibrandingAnalyticsContext provides MultibrandingAnalyticsContext(screen = "floating_one_tap")) {
            OneTapBottomSheetInternal(
                modifier = modifier,
                state = state,
                serviceName = serviceName,
                scenario = scenario,
                autoHideOnSuccess = autoHideOnSuccess,
                onAuth = onAuth,
                onAuthCode = onAuthCode,
                onFail = onFail,
                oAuths = oAuths,
                style = style,
                authParams = authParams,
                fastAuthEnabled = fastAuthEnabled,
                signInAnotherAccountButtonEnabled = fastAuthEnabled,
            )
        }
    }
}

@Suppress("LongParameterList", "LongMethod", "NonSkippableComposable")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OneTapBottomSheetInternal(
    modifier: Modifier = Modifier,
    state: OneTapBottomSheetState,
    serviceName: String,
    scenario: OneTapScenario,
    autoHideOnSuccess: Boolean,
    onAuth: (OneTapOAuth?, AccessToken) -> Unit,
    onAuthCode: (AuthCodeData, Boolean) -> Unit,
    onFail: (OneTapOAuth?, VKIDAuthFail) -> Unit,
    style: OneTapBottomSheetStyle,
    oAuths: Set<OneTapOAuth> = emptySet(),
    authParams: VKIDAuthUiParams,
    fastAuthEnabled: Boolean,
    signInAnotherAccountButtonEnabled: Boolean,
) {
    val authStatus = rememberSaveable { mutableStateOf<OneTapBottomSheetAuthStatus>(OneTapBottomSheetAuthStatus.Init) }
    val showBottomSheet = rememberSaveable {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()
    state.showSheet = processSheetShow({ authStatus.value = it }, { showBottomSheet.value = it }, coroutineScope, state)
    if (showBottomSheet.value) {
        ModalBottomSheet(
            modifier = modifier.testTag("onetap_bottomsheet"),
            onDismissRequest = {
                state.hide()
            },
            sheetState = state.materialSheetState,
            containerColor = Color.Transparent,
            dragHandle = null,
        ) {
            InternalVKIDWithUpdatedLocale {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    val dismissSheet = {
                        state.hide()
                    }
                    when (val status = authStatus.value) {
                        is OneTapBottomSheetAuthStatus.Init -> {
                            SheetContentMain(
                                onAuth = onAuth,
                                onAuthCode = onAuthCode,
                                onFail = onFail,
                                oAuths = oAuths,
                                serviceName = serviceName,
                                scenario = scenario,
                                dismissSheet = dismissSheet,
                                style = style,
                                onAuthStatusChange = { authStatus.value = it },
                                authParams = authParams,
                                coroutineScope = coroutineScope,
                                fastAuthEnabled = fastAuthEnabled,
                                signInAnotherAccountButtonEnabled = signInAnotherAccountButtonEnabled,
                            )
                        }

                        is OneTapBottomSheetAuthStatus.AuthStarted -> SheetContentAuthInProgress(
                            style,
                            dismissSheet
                        )

                        is OneTapBottomSheetAuthStatus.AuthFailedAlternate -> SheetContentAuthFailed(
                            style,
                            dismissSheet
                        ) {
                            val extraAuthParams = OneTapBottomSheetAnalytics.retryAuthTap()
                            startAlternateAuth(
                                coroutineScope = coroutineScope,
                                style = style,
                                onAuth = { onAuth(null, it) },
                                onAuthCode = onAuthCode,
                                onFail = { onFail(null, it) },
                                onAuthStatusChange = { authStatus.value = it },
                                authParams = authParams,
                                extraAuthParams = extraAuthParams
                            )
                        }

                        is OneTapBottomSheetAuthStatus.AuthFailedVKID -> SheetContentAuthFailed(
                            style,
                            dismissSheet
                        ) {
                            val extraAuthParams = OneTapBottomSheetAnalytics.retryAuthTap()
                            startVKIDAuth(
                                coroutineScope = coroutineScope,
                                style = style,
                                onAuth = { onAuth(null, it) },
                                onAuthCode = onAuthCode,
                                onFail = { onFail(null, it) },
                                onAuthStatusChange = { authStatus.value = it },
                                authParams = authParams,
                                extraAuthParams = extraAuthParams,
                                fastAuthEnabled = fastAuthEnabled,
                                user = status.user,
                            )
                        }

                        is OneTapBottomSheetAuthStatus.AuthFailedMultibranding -> SheetContentAuthFailed(
                            style,
                            dismissSheet
                        ) {
                            coroutineScope.launch {
                                authStatus.value = OneTapBottomSheetAuthStatus.AuthStarted
                                VKID.instance.authorize(
                                    object : VKIDAuthCallback {
                                        override fun onAuth(accessToken: AccessToken) {
                                            authStatus.value = OneTapBottomSheetAuthStatus.AuthSuccess
                                            onAuth(status.oAuth, accessToken)
                                        }

                                        override fun onAuthCode(
                                            data: AuthCodeData,
                                            isCompletion: Boolean
                                        ) {
                                            if (isCompletion) authStatus.value = OneTapBottomSheetAuthStatus.AuthSuccess
                                            onAuthCode(data, isCompletion)
                                        }

                                        override fun onFail(fail: VKIDAuthFail) {
                                            authStatus.value = OneTapBottomSheetAuthStatus.AuthFailedMultibranding(status.oAuth)
                                            onFail(status.oAuth, fail)
                                        }
                                    },
                                    authParams.asParamsBuilder {
                                        oAuth = status.oAuth.toOAuth()
                                        theme = style.toProviderTheme()
                                        prompt = Prompt.LOGIN
                                    }.build()
                                )
                            }
                        }

                        is OneTapBottomSheetAuthStatus.AuthSuccess -> {
                            if (autoHideOnSuccess) {
                                LaunchedEffect(Unit) {
                                    delay(1.seconds)
                                    state.hide()
                                }
                            }
                            SheetContentAuthSuccess(style, dismissSheet)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun processSheetShow(
    onAuthStatusChange: (OneTapBottomSheetAuthStatus) -> Unit,
    onShowBottomSheetChange: (Boolean) -> Unit,
    coroutineScope: CoroutineScope,
    state: OneTapBottomSheetState
): (Boolean) -> Unit = remember {
    {
        val show = it
        if (show) {
            onAuthStatusChange(OneTapBottomSheetAuthStatus.Init)
        }
        if (show) {
            onShowBottomSheetChange(true)
        } else {
            coroutineScope.launch {
                state.materialSheetState.hide()
            }.invokeOnCompletion {
                if (!state.isVisible) {
                    onShowBottomSheetChange(false)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun rememberOneTapBottomSheetStateInternal(): OneTapBottomSheetState {
    val materialSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    return remember(materialSheetState) {
        OneTapBottomSheetState(
            materialSheetState = materialSheetState
        )
    }
}

/**
 * Manages the state of the One Tap Bottom Sheet. Should be created with [rememberOneTapBottomSheetState]
 */
@Stable
public class OneTapBottomSheetState
@OptIn(ExperimentalMaterial3Api::class)
internal constructor(
    internal val materialSheetState: SheetState
) {
    internal var showSheet: (Boolean) -> Unit = {}

    /**
     * Shows the bottom sheet.
     */
    public fun show() {
        showSheet(true)
    }

    /**
     * Hides the bottom sheet.
     */
    public fun hide() {
        showSheet(false)
    }

    /**
     * Returns the visibility state of the bottom sheet.
     */
    public val isVisible: Boolean
        get() {
            @OptIn(ExperimentalMaterial3Api::class)
            return materialSheetState.isVisible
        }
}

@Preview
@Composable
private fun OneTapBottomSheetPreview() {
    SheetContentMain(
        onAuth = { _, _ -> },
        onAuthCode = { _, _ -> },
        onFail = { _, _ -> },
        oAuths = emptySet(),
        serviceName = "<Название сервиса>",
        scenario = OneTapScenario.EnterService,
        style = OneTapBottomSheetStyle.TransparentDark(),
        dismissSheet = {},
        onAuthStatusChange = {},
        authParams = VKIDAuthUiParams {},
        rememberCoroutineScope(),
        fastAuthEnabled = true,
        signInAnotherAccountButtonEnabled = true,
    )
}

@Preview
@Composable
private fun OneTapBottomSheetSuccessPreview() {
    SheetContentAuthSuccess(
        OneTapBottomSheetStyle.TransparentDark(),
        dismissSheet = {},
    )
}

@Preview
@Composable
private fun OneTapBottomSheetFailedPreview() {
    SheetContentAuthFailed(
        OneTapBottomSheetStyle.TransparentDark(),
        dismissSheet = {},
        repeatClicked = {}
    )
}
