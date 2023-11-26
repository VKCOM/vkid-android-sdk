package com.vk.id.onetap.compose.onetap.sheet

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.onetap.compose.onetap.sheet.content.OneTapBottomSheetAuthStatus
import com.vk.id.onetap.compose.onetap.sheet.content.SheetContentAuthFailed
import com.vk.id.onetap.compose.onetap.sheet.content.SheetContentAuthInProgress
import com.vk.id.onetap.compose.onetap.sheet.content.SheetContentAuthSuccess
import com.vk.id.onetap.compose.onetap.sheet.content.SheetContentMain
import com.vk.id.onetap.compose.onetap.sheet.content.startAlternateAuth
import com.vk.id.onetap.compose.onetap.sheet.content.startVKIDAuth
import com.vk.id.onetap.compose.onetap.sheet.style.OneTapBottomSheetStyle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
public fun rememberOneTapBottomSheetState(): OneTapBottomSheetState {
    return rememberOneTapBottomSheetStateInternal()
}

@Composable
public fun OneTapBottomSheet(
    modifier: Modifier = Modifier,
    state: OneTapBottomSheetState = rememberOneTapBottomSheetState(),
    serviceName: String,
    scenario: OneTapScenario = OneTapScenario.EnterService,
    onAuth: (AccessToken) -> Unit,
    onFail: (VKIDAuthFail) -> Unit = {},
    style: OneTapBottomSheetStyle = OneTapBottomSheetStyle.Light(),
    vkid: VKID? = null
) {
    val context = LocalContext.current
    val useVKID = vkid ?: remember {
        VKID(context)
    }
    OneTapBottomSheetInternal(modifier, state, serviceName, scenario, onAuth, onFail, style, useVKID)
}

@Suppress("LongParameterList")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OneTapBottomSheetInternal(
    modifier: Modifier = Modifier,
    state: OneTapBottomSheetState,
    serviceName: String,
    scenario: OneTapScenario,
    onAuth: (AccessToken) -> Unit,
    onFail: (VKIDAuthFail) -> Unit,
    style: OneTapBottomSheetStyle,
    vkid: VKID
) {
    val authStatus = rememberSaveable { mutableStateOf(OneTapBottomSheetAuthStatus.Init) }
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
            when (authStatus.value) {
                OneTapBottomSheetAuthStatus.Init -> {
                    SheetContentMain(
                        vkid,
                        onAuth,
                        onFail,
                        serviceName,
                        scenario,
                        style,
                        dismissSheet,
                        authStatus
                    )
                }
                OneTapBottomSheetAuthStatus.AuthStarted -> SheetContentAuthInProgress(serviceName, style, dismissSheet)
                OneTapBottomSheetAuthStatus.AuthFailedAlternate -> SheetContentAuthFailed(
                    serviceName,
                    style,
                    dismissSheet
                ) {
                    startAlternateAuth(coroutineScope, vkid, style, onAuth, onFail, authStatus)
                }

                OneTapBottomSheetAuthStatus.AuthFailedVKID -> SheetContentAuthFailed(serviceName, style, dismissSheet) {
                    startVKIDAuth(coroutineScope, vkid, style, onAuth, onFail, authStatus)
                }

                OneTapBottomSheetAuthStatus.AuthSuccess -> SheetContentAuthSuccess(serviceName, style, dismissSheet)
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
): (Boolean) -> Unit =
    remember {
        { show ->
            authStatus.value = OneTapBottomSheetAuthStatus.Init
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

public class OneTapBottomSheetState
@OptIn(ExperimentalMaterial3Api::class)
internal constructor(
    internal val materialSheetState: SheetState
) {
    internal var showSheet: (Boolean) -> Unit = {}
    public fun show() {
        showSheet(true)
    }

    public fun hide() {
        showSheet(false)
    }

    public val isVisible: Boolean
        get() {
            @OptIn(ExperimentalMaterial3Api::class)
            return materialSheetState.isVisible
        }
}

@Preview
@Composable
private fun OneTapBottomSheetPreview() {
    val context = LocalContext.current
    val vkid = remember {
        VKID(context)
    }
    SheetContentMain(
        vkid,
        onAuth = {},
        onFail = {},
        "<Название сервиса>",
        OneTapScenario.EnterService,
        OneTapBottomSheetStyle.TransparentDark(),
        dismissSheet = {},
        remember { mutableStateOf(OneTapBottomSheetAuthStatus.Init) }
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
