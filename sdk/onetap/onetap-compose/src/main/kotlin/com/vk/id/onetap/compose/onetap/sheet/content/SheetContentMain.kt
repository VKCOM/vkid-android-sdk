package com.vk.id.onetap.compose.onetap.sheet.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vk.id.AccessToken
import com.vk.id.VKIDAuthFail
import com.vk.id.VKIDUser
import com.vk.id.auth.AuthCodeData
import com.vk.id.auth.VKIDAuthUiParams
import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.onetap.compose.R
import com.vk.id.onetap.compose.onetap.OneTap
import com.vk.id.onetap.compose.onetap.OneTapAnalytics.uuidFromParams
import com.vk.id.onetap.compose.onetap.OneTapTitleScenario
import com.vk.id.onetap.compose.onetap.sheet.OneTapBottomSheetAnalytics
import com.vk.id.onetap.compose.onetap.sheet.OneTapScenario
import com.vk.id.onetap.compose.onetap.sheet.scenarioTitle
import com.vk.id.onetap.compose.onetap.sheet.style.OneTapBottomSheetStyle
import com.vk.id.onetap.compose.onetap.sheet.vkidButtonTextProvider
import com.vk.id.onetap.compose.util.MeasureUnconstrainedViewWidth
import kotlinx.coroutines.CoroutineScope

@Suppress("LongParameterList", "NonSkippableComposable", "LongMethod")
@Composable
internal fun SheetContentMain(
    onAuth: (OneTapOAuth?, AccessToken) -> Unit,
    onAuthCode: (AuthCodeData, Boolean) -> Unit,
    onFail: (OneTapOAuth?, VKIDAuthFail) -> Unit,
    oAuths: Set<OneTapOAuth>,
    serviceName: String,
    scenario: OneTapScenario,
    style: OneTapBottomSheetStyle,
    dismissSheet: () -> Unit,
    onAuthStatusChange: (OneTapBottomSheetAuthStatus) -> Unit,
    authParams: VKIDAuthUiParams,
    coroutineScope: CoroutineScope,
    fastAuthEnabled: Boolean,
    signInAnotherAccountButtonEnabled: Boolean,
) {
    SheetContentBox(
        style = style,
        rowContent = {
            Spacer(modifier = Modifier.width(12.dp))
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(R.drawable.vkid_sheet_logo),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .padding(top = 8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            CloseIcon(dismissSheet)
        }
    ) {
        var user by remember { mutableStateOf<VKIDUser?>(null) }
        OneTapBottomSheetAnalytics.OneTapBottomSheetShown(style.toProviderTheme(), scenario)
        val resources = LocalContext.current.resources
        Column(
            Modifier.padding(horizontal = 8.dp, vertical = 16.dp)
        ) {
            val title = remember(scenario) {
                scenario.scenarioTitle(serviceName = serviceName, resources = resources)
            }
            ContentTitle(title, style)
            Spacer(Modifier.height(12.dp))
            ContentDescription(stringResource(id = R.string.vkid_scenario_common_description), style)
        }
        @Composable
        fun OneTapButton(
            measureInProgress: Boolean,
            largeText: Boolean,
        ) {
            Box(
                modifier = Modifier.padding(8.dp)
            ) {
                OneTap(
                    style = style.oneTapStyle,
                    signInAnotherAccountButtonEnabled = signInAnotherAccountButtonEnabled,
                    oAuths = oAuths,
                    vkidButtonTextProvider = remember(scenario) { scenario.vkidButtonTextProvider(resources) },
                    onVKIDButtonClick = {
                        val extraAuthParams = OneTapBottomSheetAnalytics.oneTapPressed(user)
                        startVKIDAuth(
                            coroutineScope = coroutineScope,
                            style = style,
                            onAuth = { onAuth(null, it) },
                            onAuthCode = onAuthCode,
                            onFail = {
                                OneTapBottomSheetAnalytics.authError(extraAuthParams.uuidFromParams())
                                onFail(null, it)
                            },
                            onAuthStatusChange = onAuthStatusChange,
                            authParams = authParams,
                            extraAuthParams = extraAuthParams,
                            fastAuthEnabled = fastAuthEnabled,
                            user = user,
                        )
                    },
                    onAlternateButtonClick = {
                        val extraAuthParams = OneTapBottomSheetAnalytics.alternatePressed()
                        startAlternateAuth(
                            coroutineScope = coroutineScope,
                            style = style,
                            onAuth = { onAuth(null, it) },
                            onAuthCode = onAuthCode,
                            onFail = {
                                OneTapBottomSheetAnalytics.authError(extraAuthParams.uuidFromParams())
                                onFail(null, it)
                            },
                            onAuthStatusChange = onAuthStatusChange,
                            authParams = authParams,
                            extraAuthParams = extraAuthParams,
                        )
                    },
                    onAuth = onAuth,
                    onAuthCode = onAuthCode,
                    onFail = { oAuth, fail ->
                        check(oAuth != null) { error("oAuth is not provided in a multibranding flow error") }
                        onAuthStatusChange(OneTapBottomSheetAuthStatus.AuthFailedMultibranding(oAuth))
                        onFail(oAuth, fail)
                    },
                    authParams = authParams,
                    onUserFetched = {
                        user = it
                        if (it == null) {
                            OneTapBottomSheetAnalytics.noActiveSession()
                            OneTapBottomSheetAnalytics.noUserButtonShown()
                        } else {
                            OneTapBottomSheetAnalytics.userWasFound(true)
                        }
                    },
                    fastAuthEnabled = fastAuthEnabled,
                    largeText = largeText,
                    measureInProgress = measureInProgress,
                    scenario = OneTapTitleScenario.SignIn,
                )
            }
        }
        BoxWithConstraints {
            MeasureUnconstrainedViewWidth(viewToMeasure = {
                OneTapButton(measureInProgress = true, largeText = true)
            }) { largeTextWidth ->
                OneTapButton(measureInProgress = false, largeText = largeTextWidth <= maxWidth)
            }
        }
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
            fontSize = 23.sp,
            fontWeight = FontWeight.W600,
            lineHeight = 28.sp,
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
