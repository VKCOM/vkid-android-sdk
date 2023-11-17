package com.vk.id.multibranding

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vk.id.multibranding.compose.R

private val sourceItems = listOf(
    OAuthItemData(
        name = OAuth.VK,
        title = "VK ID",
    ),
    OAuthItemData(
        name = OAuth.MAIL,
        title = "Mail",
    ),
    OAuthItemData(
        name = OAuth.OK,
        title = "OK",
    ),
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
    isOAuthAllowed: (oAuth: OAuth) -> Boolean = { true }
) {
    val context = LocalContext.current
    val items = remember { sourceItems.filter { isOAuthAllowed(it.name) } }
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
    item: OAuthItemData,
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
                        is OAuthListWidgetAuthCallback.WithOAuth -> onAuth(item.name, "FAKE_TOKEN")
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
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                BasicText(
                    text = context.getString(R.string.vkid_oauth_list_widget_title, item.title),
                    modifier = Modifier,
                    style = TextStyle(
                        textAlign = TextAlign.Center,
                        color = style.textStyle.asColorResource(),
                        fontSize = style.sizeStyle.asFontSize(),
                        lineHeight = style.sizeStyle.asLineHeight(),
                        fontWeight = FontWeight.Medium,
                    )
                )
            }
            Spacer(
                modifier = Modifier
                    .iconPadding(style.sizeStyle)
                    .width(style.sizeStyle.iconSize())
                    .height(style.sizeStyle.iconSize())
            )
        }
    }
}

@Composable
private fun OAuthListImage(
    item: OAuthItemData,
    style: OAuthListWidgetStyle,
) = Image(
    painter = painterResource(
        id = when (item.name) {
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

@Preview
@Composable
private fun OAuthListWidgetWithOneItem() {
    OAuthListWidget(
        isOAuthAllowed = { it == OAuth.OK },
        onAuth = OAuthListWidgetAuthCallback.WithOAuth { _, _ -> }
    )
}

@Preview
@Composable
private fun OAuthListWidgetWithTwoItems() {
    OAuthListWidget(
        isOAuthAllowed = { it in setOf(OAuth.VK, OAuth.OK) },
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
        isOAuthAllowed = { it == OAuth.VK },
        onAuth = OAuthListWidgetAuthCallback.WithOAuth { _, _ -> }
    )
}

private data class OAuthItemData(
    val name: OAuth,
    val title: String,
)
