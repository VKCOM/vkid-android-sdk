@file:OptIn(InternalVKIDApi::class)

package com.vk.id.onetap.compose.onetap.sheet

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
<<<<<<< HEAD
import com.vk.id.auth.AuthCodeData
import com.vk.id.auth.Prompt
import com.vk.id.auth.VKIDAuthCallback
import com.vk.id.auth.VKIDAuthUiParams
import com.vk.id.common.InternalVKIDApi
=======
import com.vk.id.auth.VKIDAuthParams
import com.vk.id.common.InternalVKIDApi
import com.vk.id.multibranding.internal.LocalMultibrandingAnalyticsContext
import com.vk.id.multibranding.internal.MultibrandingAnalyticsContext
>>>>>>> develop
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
    authParams: VKIDAuthUiParams = VKIDAuthUiParams {}
) {
<<<<<<< HEAD
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
    )
=======
    val context = LocalContext.current
    val useVKID = vkid ?: remember {
        VKID(context)
    }
    CompositionLocalProvider(LocalMultibrandingAnalyticsContext provides MultibrandingAnalyticsContext(screen = "floating_one_tap")) {
        OneTapBottomSheetInternal(
            modifier = modifier,
            state = state,
            serviceName = serviceName,
            scenario = scenario,
            autoHideOnSuccess = autoHideOnSuccess,
            onAuth = onAuth,
            onFail = onFail,
            oAuths = oAuths,
            style = style,
            vkid = useVKID
        )
    }
>>>>>>> develop
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
) {
    val authStatus = rememberSaveable { mutableStateOf<OneTapBottomSheetAuthStatus>(OneTapBottomSheetAuthStatus.Init) }
    val showBottomSheet = rememberSaveable {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()
    state.showSheet = processSheetShow(authStatus, showBottomSheet, coroutineScope, state)
    if (showBottomSheet.value) {
        ModalBottomSheet(
            modifier = modifier,
            onDismissRequest = {
                state.hide()
            },
            sheetState = state.materialSheetState,
            containerColor = Color.Transparent,
            dragHandle = null
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
                        authStatus = authStatus,
                        authParams = authParams,
                    )
                }

                is OneTapBottomSheetAuthStatus.AuthStarted -> SheetContentAuthInProgress(
                    serviceName,
                    style,
                    dismissSheet
                )

                is OneTapBottomSheetAuthStatus.AuthFailedAlternate -> SheetContentAuthFailed(
                    serviceName,
                    style,
                    dismissSheet
                ) {
                    val extraAuthParams = OneTapBottomSheetAnalytics.retryAuthTap()
                    startAlternateAuth(
<<<<<<< HEAD
                        coroutineScope = coroutineScope,
                        style = style,
                        onAuth = { onAuth(null, it) },
                        onAuthCode = onAuthCode,
                        onFail = { onFail(null, it) },
                        authStatus = authStatus,
                        authParams = authParams,
=======
                        coroutineScope,
                        vkid,
                        style,
                        { onAuth(null, it) },
                        { onFail(null, it) },
                        authStatus,
                        extraAuthParams
>>>>>>> develop
                    )
                }

                is OneTapBottomSheetAuthStatus.AuthFailedVKID -> SheetContentAuthFailed(
                    serviceName,
                    style,
                    dismissSheet
                ) {
<<<<<<< HEAD
                    startVKIDAuth(
                        coroutineScope = coroutineScope,
                        style = style,
                        onAuth = { onAuth(null, it) },
                        onAuthCode = onAuthCode,
                        onFail = { onFail(null, it) },
                        authStatus = authStatus,
                        authParams = authParams,
                    )
=======
                    val extraAuthParams = OneTapBottomSheetAnalytics.retryAuthTap()
                    startVKIDAuth(coroutineScope, vkid, style, { onAuth(null, it) }, { onFail(null, it) }, authStatus, extraAuthParams)
>>>>>>> develop
                }

                is OneTapBottomSheetAuthStatus.AuthFailedMultibranding -> SheetContentAuthFailed(
                    serviceName,
                    style,
                    dismissSheet
                ) {
                    coroutineScope.launch {
                        VKID.instance.authorize(
                            object : VKIDAuthCallback {
                                override fun onAuth(accessToken: AccessToken) = onAuth(status.oAuth, accessToken)
                                override fun onAuthCode(
                                    data: AuthCodeData,
                                    isCompletion: Boolean
                                ) = onAuthCode(data, isCompletion)
                                override fun onFail(fail: VKIDAuthFail) = onFail(status.oAuth, fail)
                            },
                            authParams.asParamsBuilder {
                                oAuth = status.oAuth.toOAuth()
                                theme = style.toProviderTheme()
                                prompt = Prompt.LOGIN
                            }
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
                    SheetContentAuthSuccess(serviceName, style, dismissSheet)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun processSheetShow(
    authStatus: MutableState<OneTapBottomSheetAuthStatus>,
    showBottomSheet: MutableState<Boolean>,
    coroutineScope: CoroutineScope,
    state: OneTapBottomSheetState
): (Boolean) -> Unit = remember {
    {
        val show = it
        if (show) {
            authStatus.value = OneTapBottomSheetAuthStatus.Init
        }
        if (show) {
            showBottomSheet.value = true
        } else {
            coroutineScope.launch {
                state.materialSheetState.hide()
            }.invokeOnCompletion {
                if (!state.isVisible) {
                    showBottomSheet.value = false
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
        authStatus = remember { mutableStateOf(OneTapBottomSheetAuthStatus.Init) },
        authParams = VKIDAuthUiParams {},
    )
}

@Preview
@Composable
private fun OneTapBottomSheetSuccessPreview() {
    SheetContentAuthSuccess(
        "<Название сервиса>",
        OneTapBottomSheetStyle.TransparentDark(),
        dismissSheet = {},
    )
}

@Preview
@Composable
private fun OneTapBottomSheetFailedPreview() {
    SheetContentAuthFailed(
        "<Название сервиса>",
        OneTapBottomSheetStyle.TransparentDark(),
        dismissSheet = {},
        repeatClicked = {}
    )
}
