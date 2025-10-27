package com.vk.id.onetap.compose.onetap.sheet.content

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
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
import com.vk.id.onetap.compose.onetap.sheet.style.background
import com.vk.id.onetap.compose.onetap.sheet.style.clip
import com.vk.id.onetap.compose.onetap.sheet.vkidButtonTextProvider
import com.vk.id.onetap.compose.util.MeasureUnconstrainedViewHeight
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
    when (LocalConfiguration.current.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            LandscapeSheet(
                onAuth = onAuth,
                onAuthCode = onAuthCode,
                onFail = onFail,
                oAuths = oAuths,
                serviceName = serviceName,
                scenario = scenario,
                style = style,
                dismissSheet = dismissSheet,
                onAuthStatusChange = onAuthStatusChange,
                authParams = authParams,
                coroutineScope = coroutineScope,
                fastAuthEnabled = fastAuthEnabled,
                signInAnotherAccountButtonEnabled = signInAnotherAccountButtonEnabled,
            )
        }

        else -> {
            VerticalSheet(
                onAuth = onAuth,
                onAuthCode = onAuthCode,
                onFail = onFail,
                oAuths = oAuths,
                serviceName = serviceName,
                scenario = scenario,
                style = style,
                dismissSheet = dismissSheet,
                onAuthStatusChange = onAuthStatusChange,
                authParams = authParams,
                coroutineScope = coroutineScope,
                fastAuthEnabled = fastAuthEnabled,
                signInAnotherAccountButtonEnabled = signInAnotherAccountButtonEnabled,
            )
        }
    }
}

@Suppress("LongParameterList")
@Composable
private fun LandscapeSheet(
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
    @SuppressLint("UnusedBoxWithConstraintsScope")
    BoxWithConstraints {
        val maxSheetHeight = maxHeight
        Box(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .safeContentPadding()
                .wrapContentHeight()
                .wrapContentWidth()
                .clip(style.cornersStyle)
                .background(style.backgroundStyle),
            contentAlignment = Alignment.TopEnd,
        ) {
            CloseIcon(dismissSheet)
            @Composable
            fun SheetRightColumn(modifier: Modifier) {
                Column(
                    modifier
                        .padding(top = 20.dp, bottom = 20.dp, end = 20.dp)
                ) {
                    OneTapBottomSheetAnalytics.OneTapBottomSheetShown(style.toProviderTheme(), scenario)
                    val resources = LocalContext.current.resources
                    val title = remember(scenario) {
                        scenario.scenarioTitle(serviceName = serviceName, resources = resources)
                    }
                    ContentTitle(Modifier.padding(end = 24.dp, start = 24.dp), title, style, isVertical = true)
                    Spacer(Modifier.height(8.dp))
                    ContentDescription(stringResource(id = R.string.vkid_scenario_common_description), style, isVertical = true)
                    Spacer(Modifier.height(16.dp))
                    OneTapButton(
                        measureInProgress = false,
                        largeText = true,
                        onAuth = onAuth,
                        onAuthCode = onAuthCode,
                        onFail = onFail,
                        oAuths = oAuths,
                        scenario = scenario,
                        style = style,
                        onAuthStatusChange = onAuthStatusChange,
                        authParams = authParams,
                        coroutineScope = coroutineScope,
                        fastAuthEnabled = fastAuthEnabled,
                        signInAnotherAccountButtonEnabled = signInAnotherAccountButtonEnabled,
                    )
                }
            }

            val verticalPadding = 37.dp
            val maxHeight = maxSheetHeight - verticalPadding * 2
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                VKIDImage(
                    modifier = Modifier
                        .width(206.dp)
                        .padding(vertical = verticalPadding)
                        .padding(start = 20.dp, end = 22.dp)
                        .aspectRatio(1.0f),
                    style = style,
                )
                MeasureUnconstrainedViewHeight(viewToMeasure = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        VKIDImage(
                            modifier = Modifier
                                .width(156.dp)
                                .padding(vertical = verticalPadding)
                                .padding(start = 20.dp, end = 22.dp)
                                .aspectRatio(1.0f),
                            style = style,
                        )
                        SheetRightColumn(Modifier.width(254.dp))
                    }
                }) { largeTextHeight ->
                    val columnWidth = if (largeTextHeight > maxHeight) 350.dp else 254.dp
                    SheetRightColumn(Modifier.width(columnWidth))
                }
            }
        }
    }
}

@Suppress("LongParameterList")
@Composable
private fun VerticalSheet(
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
    @Composable
    fun OneTapButton(
        measureInProgress: Boolean,
        largeText: Boolean,
    ) {
        OneTapButton(
            measureInProgress = measureInProgress,
            largeText = largeText,
            onAuth = onAuth,
            onAuthCode = onAuthCode,
            onFail = onFail,
            oAuths = oAuths,
            scenario = scenario,
            style = style,
            onAuthStatusChange = onAuthStatusChange,
            authParams = authParams,
            coroutineScope = coroutineScope,
            fastAuthEnabled = fastAuthEnabled,
            signInAnotherAccountButtonEnabled = signInAnotherAccountButtonEnabled,
        )
    }
    SheetContentBox(
        style = style,
        rowContent = {
            Spacer(modifier = Modifier.width(52.dp))
            Spacer(modifier = Modifier.weight(1f))
            VKIDImage(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .size(120.dp),
                style = style,
            )
            Spacer(modifier = Modifier.weight(1f))
            CloseIcon(dismissSheet)
        }
    ) {
        OneTapBottomSheetAnalytics.OneTapBottomSheetShown(style.toProviderTheme(), scenario)
        val resources = LocalContext.current.resources
        Column(
            Modifier.padding(top = 16.dp, bottom = 24.dp)
        ) {
            val title = remember(scenario) {
                scenario.scenarioTitle(serviceName = serviceName, resources = resources)
            }
            ContentTitle(Modifier, title, style, isVertical = false)
            Spacer(Modifier.height(12.dp))
            ContentDescription(stringResource(id = R.string.vkid_scenario_common_description), style, isVertical = false)
        }
        @SuppressLint("UnusedBoxWithConstraintsScope")
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
private fun VKIDImage(
    modifier: Modifier,
    style: OneTapBottomSheetStyle,
) {
    Image(
        modifier = modifier,
        painter = painterResource(
            when (style) {
                is OneTapBottomSheetStyle.Light,
                is OneTapBottomSheetStyle.TransparentLight -> R.drawable.vkid_sheet_logo_light

                is OneTapBottomSheetStyle.Dark,
                is OneTapBottomSheetStyle.TransparentDark -> R.drawable.vkid_sheet_logo_dark
            }
        ),
        contentDescription = null,
        contentScale = ContentScale.Crop,
    )
}

@Suppress("LongMethod", "LongParameterList")
@Composable
private fun OneTapButton(
    measureInProgress: Boolean,
    largeText: Boolean,
    onAuth: (OneTapOAuth?, AccessToken) -> Unit,
    onAuthCode: (AuthCodeData, Boolean) -> Unit,
    onFail: (OneTapOAuth?, VKIDAuthFail) -> Unit,
    oAuths: Set<OneTapOAuth>,
    scenario: OneTapScenario,
    style: OneTapBottomSheetStyle,
    onAuthStatusChange: (OneTapBottomSheetAuthStatus) -> Unit,
    authParams: VKIDAuthUiParams,
    coroutineScope: CoroutineScope,
    fastAuthEnabled: Boolean,
    signInAnotherAccountButtonEnabled: Boolean,
) {
    var user by remember { mutableStateOf<VKIDUser?>(null) }
    val resources = LocalContext.current.resources
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

@Composable
private fun ContentTitle(
    modifier: Modifier,
    text: String,
    style: OneTapBottomSheetStyle,
    isVertical: Boolean,
) {
    BasicText(
        modifier = modifier.fillMaxWidth(),
        text = text,
        style = TextStyle(
            color = colorResource(id = style.contentTitleTextColor),
            textAlign = TextAlign.Center,
            fontSize = if (isVertical) 17.sp else 23.sp,
            fontWeight = FontWeight.W600,
            lineHeight = if (isVertical) 22.sp else 28.sp,
        )
    )
}

@Composable
private fun ContentDescription(
    text: String,
    style: OneTapBottomSheetStyle,
    isVertical: Boolean,
) {
    BasicText(
        modifier = Modifier.fillMaxWidth(),
        text = text,
        style = TextStyle(
            color = colorResource(id = style.contentTextColor),
            textAlign = TextAlign.Center,
            fontSize = if (isVertical) 13.sp else 16.sp,
            fontWeight = FontWeight.W400,
            lineHeight = if (isVertical) 16.sp else 20.sp,
            letterSpacing = 0.1.sp
        )
    )
}
