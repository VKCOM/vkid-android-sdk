package com.vk.id.onetap.compose.onetap.sheet.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.VKIDUser
import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.onetap.compose.R
import com.vk.id.onetap.compose.onetap.OneTap
import com.vk.id.onetap.compose.onetap.sheet.OneTapBottomSheetAnalytics
import com.vk.id.onetap.compose.onetap.sheet.OneTapScenario
import com.vk.id.onetap.compose.onetap.sheet.scenarioTitle
import com.vk.id.onetap.compose.onetap.sheet.style.OneTapBottomSheetStyle
import com.vk.id.onetap.compose.onetap.sheet.vkidButtonTextProvider

@Suppress("LongParameterList", "NonSkippableComposable")
@Composable
internal fun SheetContentMain(
    vkid: VKID,
    onAuth: (OneTapOAuth?, AccessToken) -> Unit,
    onFail: (OneTapOAuth?, VKIDAuthFail) -> Unit,
    oAuths: Set<OneTapOAuth>,
    serviceName: String,
    scenario: OneTapScenario,
    style: OneTapBottomSheetStyle,
    dismissSheet: () -> Unit,
    authStatus: MutableState<OneTapBottomSheetAuthStatus>
) {
    SheetContentBox(
        serviceName = serviceName,
        style = style,
        dismissSheet = dismissSheet,
    ) {
        var user by remember { mutableStateOf<VKIDUser?>(null) }
        OneTapBottomSheetAnalytics.OneTapBottomSheetShown(style.toProviderTheme(), scenario)
        val resources = LocalContext.current.resources
        Column(
            Modifier.padding(horizontal = 32.dp, vertical = 36.dp)
        ) {
            val title = remember(scenario) {
                scenario.scenarioTitle(serviceName = serviceName, resources = resources)
            }
            ContentTitle(title, style)
            Spacer(Modifier.height(8.dp))
            ContentDescription(stringResource(id = R.string.vkid_scenario_common_description), style)
        }
        val coroutineScope = rememberCoroutineScope()
        OneTap(
            style = style.oneTapStyle,
            signInAnotherAccountButtonEnabled = true,
            oAuths = oAuths,
            vkid = vkid,
            vkidButtonTextProvider = remember(scenario) { scenario.vkidButtonTextProvider(resources) },
            onVKIDButtonClick = {
                val extraAuthParams = OneTapBottomSheetAnalytics.oneTapPressed(user)
                startVKIDAuth(coroutineScope, vkid, style, { onAuth(null, it) }, { onFail(null, it) }, authStatus, extraAuthParams)
            },
            onAlternateButtonClick = {
                startAlternateAuth(coroutineScope, vkid, style, { onAuth(null, it) }, { onFail(null, it) }, authStatus)
            },
            onAuth = onAuth,
            onFail = { oAuth, fail ->
                check(oAuth != null) { error("oAuth is not provided in a multibranding flow error") }
                authStatus.value = OneTapBottomSheetAuthStatus.AuthFailedMultibranding(oAuth)
                onFail(oAuth, fail)
            },
            onUserFetched = {
                user = it
                if (it == null) {
                    OneTapBottomSheetAnalytics.noActiveSession()
                    OneTapBottomSheetAnalytics.noUserButtonShown()
                } else {
                    OneTapBottomSheetAnalytics.userWasFound(true)
                }
            }
        )
    }
}

@Composable
private fun ContentTitle(text: String, style: OneTapBottomSheetStyle) {
    BasicText(
        modifier = Modifier.fillMaxWidth(),
        text = text,
        style = TextStyle(
            color = colorResource(id = style.contentTitleTextColor),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.W500,
            lineHeight = 24.sp,
        )
    )
}

@Composable
private fun ContentDescription(text: String, style: OneTapBottomSheetStyle) {
    BasicText(
        modifier = Modifier.fillMaxWidth(),
        text = text,
        style = TextStyle(
            color = colorResource(id = style.contentTextColor),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontWeight = FontWeight.W400,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp
        )
    )
}
