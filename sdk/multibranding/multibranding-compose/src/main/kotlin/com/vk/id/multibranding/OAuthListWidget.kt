@file:OptIn(InternalVKIDApi::class)

package com.vk.id.multibranding

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vk.id.AccessToken
import com.vk.id.OAuth
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.analytics.stat.StatTracker
import com.vk.id.auth.AuthCodeData
import com.vk.id.auth.Prompt
import com.vk.id.auth.VKIDAuthCallback
import com.vk.id.auth.VKIDAuthParams.Theme
import com.vk.id.auth.VKIDAuthUiParams
import com.vk.id.common.InternalVKIDApi
import com.vk.id.group.subscription.common.fail.VKIDGroupSubscriptionFail
import com.vk.id.group.subscription.common.style.GroupSubscriptionStyle
import com.vk.id.group.subscription.compose.ui.GroupSubscriptionSheet
import com.vk.id.group.subscription.compose.ui.GroupSubscriptionSnackbarHost
import com.vk.id.group.subscription.compose.ui.rememberGroupSubscriptionSheetState
import com.vk.id.multibranding.common.style.OAuthListWidgetStyle
import com.vk.id.multibranding.internal.LocalMultibrandingAnalyticsContext
import com.vk.id.util.InternalVKIDWithUpdatedLocale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Constructs a multibranding widget that supports auth with multiple [OAuth]s.
 *
 * @param modifier Layout configuration for the widget.
 * @param style Styling widget configuration.
 * @param onAuth A callback to be invoked upon a successful auth.
 * @param onAuthCode A callback to be invoked upon successful first step of auth - receiving auth code which can later be exchanged to access token.
 * @param onFail A callback to be invoked upon an error during auth.
 * @param oAuths A set of [OAuth]s the should be displayed to the user.
 * @param authParams Optional params to be passed to auth. See [VKIDAuthUiParams.Builder] for more info.
 */
@Composable
public fun OAuthListWidget(
    modifier: Modifier = Modifier,
    style: OAuthListWidgetStyle = OAuthListWidgetStyle.Dark(),
    onAuth: (oAuth: OAuth, accessToken: AccessToken) -> Unit,
    onAuthCode: (data: AuthCodeData, isCompletion: Boolean) -> Unit = { _, _ -> },
    onFail: (oAuth: OAuth, fail: VKIDAuthFail) -> Unit,
    oAuths: Set<OAuth> = OAuth.entries.toSet(),
    authParams: VKIDAuthUiParams = VKIDAuthUiParams {},
) {
    OAuthListWidget(
        modifier = modifier,
        style = style,
        onAuth = onAuth,
        onAuthCode = onAuthCode,
        onFail = onFail,
        oAuths = oAuths,
        authParams = authParams,
        measureInProgress = false,
    )
}

/**
 * Constructs a multibranding widget that supports auth with multiple [OAuth]s.
 *
 * This version integrates Group Subscription flow. The flow will be shown right after successful auth.
 * NOTE: The "groups" scope will be added automatically to the set of requested scopes.
 *
 * @param modifier Layout configuration for the widget.
 * @param style Styling widget configuration.
 * @param onAuth A callback to be invoked upon a successful auth.
 * @param onAuthCode A callback to be invoked upon successful first step of auth - receiving auth code which can later be exchanged to access token.
 * @param onFail A callback to be invoked upon an error during auth.
 * @param oAuths A set of [OAuth]s the should be displayed to the user.
 * @param authParams Optional params to be passed to auth. See [VKIDAuthUiParams.Builder] for more info.
 * @param subscribeToGroupId The id of the group the user will be subscribed to.
 * @param onSuccessSubscribingToGroup Will be called upon successful subscription.
 * @param onFailSubscribingToGroup Will be called upon any unsuccessful flow completion along with an description of the specific encountered error.
 * @param groupSubscriptionSnackbarHostState The host state for snackbars.
 * Use along with [GroupSubscriptionSnackbarHost] and pass the same state as there.
 * @param groupSubscriptionStyle The widget style, can change appearance.
 */
@Composable
public fun OAuthListWidget(
    modifier: Modifier = Modifier,
    style: OAuthListWidgetStyle = OAuthListWidgetStyle.Dark(),
    onAuth: (oAuth: OAuth, accessToken: AccessToken) -> Unit,
    onAuthCode: (data: AuthCodeData, isCompletion: Boolean) -> Unit = { _, _ -> },
    onFail: (oAuth: OAuth, fail: VKIDAuthFail) -> Unit,
    oAuths: Set<OAuth> = OAuth.entries.toSet(),
    authParams: VKIDAuthUiParams = VKIDAuthUiParams {},
    subscribeToGroupId: String,
    onSuccessSubscribingToGroup: () -> Unit,
    onFailSubscribingToGroup: (VKIDGroupSubscriptionFail) -> Unit = {},
    groupSubscriptionSnackbarHostState: SnackbarHostState,
    groupSubscriptionStyle: GroupSubscriptionStyle = GroupSubscriptionStyle.Light(),
) {
    var isSuccessfulAuth by remember { mutableStateOf("") }
    Box(modifier = modifier) {
        OAuthListWidget(
            style = style,
            onAuth = { oAuth, accessToken ->
                onAuth(oAuth, accessToken)
                isSuccessfulAuth = System.currentTimeMillis().toString()
            },
            onAuthCode = onAuthCode,
            onFail = onFail,
            oAuths = oAuths,
            authParams = authParams.newBuilder {
                scopes += "groups"
            },
            measureInProgress = false,
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

@InternalVKIDApi
@Composable
public fun OAuthListWidget(
    modifier: Modifier = Modifier,
    style: OAuthListWidgetStyle = OAuthListWidgetStyle.Dark(),
    onAuth: (oAuth: OAuth, accessToken: AccessToken) -> Unit,
    onAuthCode: (data: AuthCodeData, isCompletion: Boolean) -> Unit = { _, _ -> },
    onFail: (oAuth: OAuth, fail: VKIDAuthFail) -> Unit,
    oAuths: Set<OAuth> = OAuth.entries.toSet(),
    authParams: VKIDAuthUiParams = VKIDAuthUiParams {},
    measureInProgress: Boolean,
) {
    InternalVKIDWithUpdatedLocale {
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        if (oAuths.isEmpty()) {
            error("You need to add at least one oAuth to display the widget")
        }

        val analyticsContext = LocalMultibrandingAnalyticsContext.current
        val analytics = remember { OAuthListWidgetAnalytics(analyticsContext.screen, analyticsContext.isPaused) }

        LaunchedEffect(oAuths) {
            analytics.oauthAdded(oAuths)
        }

        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!measureInProgress) {
                OAuthTitle()
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                oAuths.forEachIndexed { index, item ->
                    OAuthButton(
                        modifier = Modifier
                            .testTag("oauth_button_${item.name.lowercase()}")
                            .weight(1f),
                        context = context,
                        style = style,
                        item = item,
                        showText = oAuths.size == 1,
                        coroutineScope = coroutineScope,
                        analytics = analytics,
                        onAuth = onAuth,
                        onAuthCode = onAuthCode,
                        onFail = { onFail(item, it) },
                        authParams = authParams,
                    )
                    if (index != oAuths.size - 1) {
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun OAuthTitle() = BasicText(
    text = stringResource(id = R.string.vkid_oauth_list_widget_note),
    style = TextStyle(
        fontSize = 13.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.Normal,
        color = colorResource(id = R.color.vkid_steel_gray_400),
        textAlign = TextAlign.Center,
    )
)

@OptIn(InternalVKIDApi::class)
@Suppress("LongParameterList", "NonSkippableComposable")
@Composable
private fun OAuthButton(
    modifier: Modifier,
    context: Context,
    style: OAuthListWidgetStyle,
    item: OAuth,
    showText: Boolean,
    coroutineScope: CoroutineScope,
    onAuth: (oAuth: OAuth, accessToken: AccessToken) -> Unit,
    onAuthCode: (AuthCodeData, Boolean) -> Unit,
    onFail: (VKIDAuthFail) -> Unit,
    authParams: VKIDAuthUiParams,
    analytics: OAuthListWidgetAnalytics,
) {
    analytics.OAuthShown(oAuth = item, isText = showText)
    Row(
        modifier = modifier
            .height(style.sizeStyle)
            .border(style.borderStyle, style.cornersStyle)
            .clip(style.cornersStyle)
            .background(style)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    color = style.rippleStyle.asColor(),
                ),
                role = Role.Button,
                onClick = {
                    val extraAuthParams = analytics.onOAuthTap(item, showText)
                    coroutineScope.launch {
                        VKID.instance.authorize(
                            object : VKIDAuthCallback {
                                override fun onAuth(accessToken: AccessToken) {
                                    onAuth(item, accessToken)
                                }

                                override fun onAuthCode(
                                    data: AuthCodeData,
                                    isCompletion: Boolean
                                ) = onAuthCode(data, isCompletion)

                                override fun onFail(fail: VKIDAuthFail) {
                                    analytics.onAuthError(extraAuthParams[StatTracker.EXTERNAL_PARAM_SESSION_ID] ?: "", oAuth = item)
                                    onFail(fail)
                                }
                            },
                            authParams
                                .asParamsBuilder {
                                    oAuth = item
                                    theme = style.toProviderTheme()
                                    prompt = Prompt.LOGIN
                                    extraParams = extraAuthParams
                                }
                                .build()
                        )
                    }
                }
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OAuthListImage(
            item = item,
            style = style
        )
        if (showText) {
            OAuthListWithTextEnding(
                modifier = Modifier,
                item = item,
                style = style,
                context = context
            )
        }
    }
}

@Composable
private fun OAuthListWithTextEnding(
    modifier: Modifier,
    item: OAuth,
    style: OAuthListWidgetStyle,
    context: Context,
) {
    OAuthListText(
        modifier = modifier,
        item = item,
        style = style,
        context = context,
    )
}

@Composable
private fun OAuthListImage(
    item: OAuth,
    style: OAuthListWidgetStyle
) = Image(
    painter = painterResource(
        id = when (item) {
            OAuth.VK -> R.drawable.vkid_vk_icon_blue
            OAuth.MAIL -> R.drawable.vkid_mail_icon_blue
            OAuth.OK -> R.drawable.vkid_ok_icon_yellow
        }
    ),
    contentDescription = null,
    modifier = Modifier
        .iconPadding(style.sizeStyle)
        .width(style.sizeStyle.iconSize())
        .height(style.sizeStyle.iconSize()),
)

@OptIn(InternalVKIDApi::class)
@Composable
private fun OAuthListText(
    modifier: Modifier,
    item: OAuth,
    style: OAuthListWidgetStyle,
    context: Context,
) = Box(
    modifier = modifier.fillMaxHeight(),
    contentAlignment = Alignment.Center,
) {
    BasicText(
        text = getWidgetTitle(context, item),
        modifier = Modifier,
        style = TextStyle(
            textAlign = TextAlign.Center,
            color = style.textStyle.asColorResource(),
            fontSize = style.sizeStyle.asFontSize(),
            lineHeight = style.sizeStyle.asLineHeight(),
            fontWeight = FontWeight.Medium,
        ),
        maxLines = 1
    )
}

private fun getWidgetTitle(
    context: Context,
    oAuth: OAuth
) = when (oAuth) {
    OAuth.VK -> context.getString(R.string.vkid_oauth_list_widget_title_vk)
    OAuth.MAIL -> context.getString(R.string.vkid_oauth_list_widget_title_mail)
    OAuth.OK -> context.getString(R.string.vkid_oauth_list_widget_title_ok)
}

private fun OAuthListWidgetStyle.toProviderTheme() = when (this) {
    is OAuthListWidgetStyle.Light -> Theme.Light
    is OAuthListWidgetStyle.Dark -> Theme.Dark
}

@Preview
@Composable
private fun OAuthListWidgetWithOneItem() {
    OAuthListWidget(
        oAuths = setOf(OAuth.OK),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview
@Composable
private fun OAuthListWidgetWithTwoItems() {
    OAuthListWidget(
        oAuths = setOf(OAuth.OK, OAuth.VK),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview
@Composable
private fun OAuthListWidgetLight() {
    OAuthListWidget(
        style = OAuthListWidgetStyle.Dark(),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview
@Composable
private fun OAuthListWidgetDark() {
    OAuthListWidget(
        modifier = Modifier.background(Color.White),
        style = OAuthListWidgetStyle.Light(),
        oAuths = setOf(OAuth.VK),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}
