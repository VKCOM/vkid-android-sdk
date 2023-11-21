package com.vk.id.multibranding

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val sourceItems = listOf(
    OAuth.VK,
    OAuth.MAIL,
    OAuth.OK,
)

public sealed interface OAuthListWidgetAuthCallback {
    public fun interface WithOAuth : (OAuth, String) -> Unit, OAuthListWidgetAuthCallback
    public fun interface JustToken : (String) -> Unit, OAuthListWidgetAuthCallback
}

@Composable
public fun OAuthListWidget(
    modifier: Modifier = Modifier,
    style: OAuthListWidgetStyle = OAuthListWidgetStyle.Light(),
    onAuth: OAuthListWidgetAuthCallback,
    allowedOAuths: Set<OAuth> = OAuth.values().toSet()
) {
    val context = LocalContext.current
    val items = remember { sourceItems.filter { it in allowedOAuths } }
    Row(
        modifier = modifier
    ) {
        items.forEachIndexed { index, item ->
            OAuthButton(
                modifier = Modifier.weight(1f),
                context = context,
                style = style,
                item = item,
                showText = items.size == 1,
                onAuth = onAuth,
            )
            if (index != items.lastIndex) {
                Spacer(modifier = Modifier.width(12.dp))
            }
        }
    }
}

@Suppress("LongParameterList")
@Composable
private fun OAuthButton(
    modifier: Modifier,
    context: Context,
    style: OAuthListWidgetStyle,
    item: OAuth,
    showText: Boolean,
    onAuth: OAuthListWidgetAuthCallback,
) {
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
                    when (onAuth) {
                        is OAuthListWidgetAuthCallback.WithOAuth -> onAuth(item, "FAKE_TOKEN")
                        is OAuthListWidgetAuthCallback.JustToken -> onAuth("FAKE_TOKEN")
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
                    textModifier = Modifier.fillMaxWidth(),
                    item = item,
                    style = style,
                    context = context
                )
            }
        }) {
            if (it < maxWidth) {
                Row {
                    OAuthListWithTextEndingContent(
                        textModifier = Modifier.weight(1f),
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
    textModifier: Modifier,
    item: OAuth,
    style: OAuthListWidgetStyle,
    context: Context,
) {
    OAuthListText(
        modifier = textModifier,
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
            OAuth.VK -> R.drawable.vk_icon_blue
            OAuth.MAIL -> R.drawable.mail_icon_blue
            OAuth.OK -> R.drawable.ok_icon_yellow
        }
    ),
    contentDescription = null,
    modifier = Modifier
        .iconPadding(style.sizeStyle)
        .width(style.sizeStyle.iconSize())
        .height(style.sizeStyle.iconSize()),
)

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

@Preview
@Composable
private fun OAuthListWidgetWithOneItem() {
    OAuthListWidget(
        allowedOAuths = setOf(OAuth.OK),
        onAuth = OAuthListWidgetAuthCallback.WithOAuth { _, _ -> }
    )
}

@Preview
@Composable
private fun OAuthListWidgetWithTwoItems() {
    OAuthListWidget(
        allowedOAuths = setOf(OAuth.VK, OAuth.OK),
        onAuth = OAuthListWidgetAuthCallback.WithOAuth { _, _ -> }
    )
}

@Preview
@Composable
private fun OAuthListWidgetLight() {
    OAuthListWidget(
        style = OAuthListWidgetStyle.Light(),
        onAuth = OAuthListWidgetAuthCallback.WithOAuth { _, _ -> }
    )
}

@Preview
@Composable
private fun OAuthListWidgetDark() {
    OAuthListWidget(
        modifier = Modifier.background(Color.White),
        style = OAuthListWidgetStyle.Dark(),
        allowedOAuths = setOf(OAuth.VK),
        onAuth = OAuthListWidgetAuthCallback.WithOAuth { _, _ -> }
    )
}
