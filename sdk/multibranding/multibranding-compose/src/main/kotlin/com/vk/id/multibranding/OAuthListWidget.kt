package com.vk.id.multibranding

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
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
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vk.id.AccessToken
import com.vk.id.OAuth
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.VKIDAuthParams
import com.vk.id.auth.VKIDAuthParams.Theme
import com.vk.id.common.InternalVKIDApi
import com.vk.id.multibranding.OAuthListWidgetAnalytics.Companion.UNIQUE_SESSION_PARAM_NAME
import com.vk.id.multibranding.common.callback.OAuthListWidgetAuthCallback
import com.vk.id.multibranding.common.style.OAuthListWidgetStyle
import com.vk.id.multibranding.internal.LocalMultibrandingAnalyticsContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Constructs a multibranding widget that supports auth with multiple [OAuth]s.
 *
 * @param modifier Layout configuration for the widget.
 * @param style Styling widget configuration.
 * @param onAuth A callback to be invoked upon a successful auth.
 * @param onFail A callback to be invoked upon an error during auth.
 * @param oAuths A set of [OAuth]s the should be displayed to the user.
 */
@Composable
public fun OAuthListWidget(
    modifier: Modifier = Modifier,
    style: OAuthListWidgetStyle = OAuthListWidgetStyle.Dark(),
    onAuth: OAuthListWidgetAuthCallback,
    onFail: (OAuth, VKIDAuthFail) -> Unit,
    oAuths: Set<OAuth> = OAuth.entries.toSet(),
) {
    OAuthListWidget(
        modifier = modifier,
        style = style,
        onAuth = onAuth,
        onFail = onFail,
        oAuths = oAuths,
        vkid = null
    )
}

/**
 * Constructs a multibranding widget that supports auth with multiple [OAuth]s.
 *
 * @param modifier Layout configuration for the widget.
 * @param style Styling widget configuration.
 * @param onAuth A callback to be invoked upon a successful auth.
 * @param onFail A callback to be invoked upon an error during auth.
 * @param oAuths A set of [OAuth]s the should be displayed to the user.
 * @param vkid An optional [VKID] instance to use for authentication.
 *  If instance of VKID is not provided, it will be created on first composition.
 */
@OptIn(InternalVKIDApi::class)
@Composable
public fun OAuthListWidget(
    modifier: Modifier = Modifier,
    style: OAuthListWidgetStyle = OAuthListWidgetStyle.Dark(),
    onAuth: OAuthListWidgetAuthCallback,
    onFail: (OAuth, VKIDAuthFail) -> Unit,
    oAuths: Set<OAuth> = OAuth.entries.toSet(),
    vkid: VKID? = null,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val useVKID = vkid ?: remember { VKID(context) }
    if (oAuths.isEmpty()) {
        error("You need to add at least one oAuth to display the widget")
    }

    val analyticsContext = LocalMultibrandingAnalyticsContext.current
    val analytics = remember { OAuthListWidgetAnalytics(analyticsContext.screen) }

    LaunchedEffect(oAuths) {
        analytics.oauthAdded(oAuths)
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OAuthTitle()
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            oAuths.sorted().forEachIndexed { index, item ->
                OAuthButton(
                    modifier = Modifier
                        .testTag("oauth_button_${item.name.lowercase()}")
                        .weight(1f),
                    context = context,
                    style = style,
                    item = item,
                    showText = oAuths.size == 1,
                    coroutineScope = coroutineScope,
                    vkid = useVKID,
                    analytics = analytics,
                    onAuth = onAuth,
                    onFail = { onFail(item, it) },
                )
                if (index != oAuths.size - 1) {
                    Spacer(modifier = Modifier.width(12.dp))
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
@Suppress("LongParameterList")
@Composable
private fun OAuthButton(
    modifier: Modifier,
    context: Context,
    style: OAuthListWidgetStyle,
    item: OAuth,
    showText: Boolean,
    coroutineScope: CoroutineScope,
    vkid: VKID,
    analytics: OAuthListWidgetAnalytics,
    onAuth: OAuthListWidgetAuthCallback,
    onFail: (VKIDAuthFail) -> Unit
) {
    analytics.OAuthShown(oAuth = item, isText = showText)
    Row(
        modifier = modifier
            .height(style.sizeStyle)
            .border(style.borderStyle, style.cornersStyle)
            .clip(style.cornersStyle)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    color = style.rippleStyle.asColor(),
                ),
                role = Role.Button,
                onClick = {
                    val extraAuthParams = analytics.onOAuthTap(item, showText)
                    coroutineScope.launch {
                        vkid.authorize(
                            object : VKID.AuthCallback {
                                override fun onSuccess(accessToken: AccessToken) {
                                    analytics.onAuthSuccess(item)
                                    when (onAuth) {
                                        is OAuthListWidgetAuthCallback.WithOAuth -> onAuth(item, accessToken)
                                        is OAuthListWidgetAuthCallback.JustToken -> onAuth(accessToken)
                                    }
                                }

                                override fun onFail(fail: VKIDAuthFail) {
                                    analytics.onAuthError(extraAuthParams[UNIQUE_SESSION_PARAM_NAME] ?: "")
                                    onFail(fail)
                                }
                            },
                            VKIDAuthParams {
                                oAuth = item
                                theme = style.toProviderTheme()
                                extraParams = extraAuthParams
                            }
                        )
                    }
                }
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OAuthListImage(
            item = item,
            style = style,
        )
        if (showText) {
            OAuthListWithTextEnding(
                item = item,
                style = style,
                context = context
            )
        }
    }
}

@Composable
private fun MeasureUnconstrainedViewWidth(
    viewToMeasure: @Composable () -> Unit,
    content: @Composable (measuredWidth: Dp) -> Unit,
) {
    SubcomposeLayout { constraints ->
        val measuredWidth = subcompose("viewToMeasure", viewToMeasure)[0].measure(Constraints()).width.toDp()
        val measurable = subcompose("content") { content(measuredWidth) }
        if (measurable.isNotEmpty()) {
            val contentPlaceable = measurable[0].measure(constraints)
            layout(contentPlaceable.width, contentPlaceable.height) { contentPlaceable.place(0, 0) }
        } else {
            layout(0, 0) {}
        }
    }
}

@Composable
private fun OAuthListWithTextEnding(
    item: OAuth,
    style: OAuthListWidgetStyle,
    context: Context,
) {
    BoxWithConstraints {
        MeasureUnconstrainedViewWidth(viewToMeasure = {
            Row {
                OAuthListWithTextEndingContent(
                    modifier = Modifier.fillMaxWidth(),
                    item = item,
                    style = style,
                    context = context
                )
            }
        }) {
            if (it < maxWidth) {
                Row {
                    OAuthListWithTextEndingContent(
                        modifier = Modifier.weight(1f),
                        item = item,
                        style = style,
                        context = context
                    )
                }
            }
        }
    }
}

@Composable
private fun OAuthListWithTextEndingContent(
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
    Spacer(
        modifier = Modifier
            .iconPadding(style.sizeStyle)
            .width(style.sizeStyle.iconSize())
            .height(style.sizeStyle.iconSize())
    )
}

@Composable
private fun OAuthListImage(
    item: OAuth,
    style: OAuthListWidgetStyle,
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
        onAuth = OAuthListWidgetAuthCallback.WithOAuth { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview
@Composable
private fun OAuthListWidgetWithTwoItems() {
    OAuthListWidget(
        oAuths = setOf(OAuth.VK, OAuth.OK),
        onAuth = OAuthListWidgetAuthCallback.WithOAuth { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview
@Composable
private fun OAuthListWidgetLight() {
    OAuthListWidget(
        style = OAuthListWidgetStyle.Dark(),
        onAuth = OAuthListWidgetAuthCallback.WithOAuth { _, _ -> },
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
        onAuth = OAuthListWidgetAuthCallback.WithOAuth { _, _ -> },
        onFail = { _, _ -> },
    )
}
