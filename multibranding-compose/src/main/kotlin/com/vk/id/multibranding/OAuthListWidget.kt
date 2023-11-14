package com.vk.id.multibranding

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
public fun OAuthListWidget(
    modifier: Modifier = Modifier,
    style: OAuthListWidgetStyle = OAuthListWidgetStyle.Light(),
    onAuth: (oAuth: OAuth, token: String) -> Unit = { _, _ -> },
    onError: () -> Unit = {},
    isOAuthAllowed: (oAuth: OAuth) -> Boolean = { true }
) {
    val sourceItems = listOf(
        OAuthItemData(
            name = OAuth.VK,
            title = "Войти через VK", // TODO: Localization
        ),
        OAuthItemData(
            name = OAuth.MAIL,
            title = "Войти через Mail",
        ),
        OAuthItemData(
            name = OAuth.OK,
            title = "Войти через OK",
        ),
    )
    val items = sourceItems.filter { isOAuthAllowed(it.name) }
    Row(
        modifier = modifier
    ) {
        items.forEachIndexed { index, item ->
            OAuthButton(
                modifier = Modifier.weight(1f),
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

@Composable
private fun OAuthButton(
    modifier: Modifier,
    style: OAuthListWidgetStyle,
    item: OAuthItemData,
    showText: Boolean,
    onAuth: (oAuth: OAuth, token: String) -> Unit,
) {
    Row(
        modifier = modifier
            .height(52.dp)
            .border(style.borderStyle, style.cornersStyle)
            .padding(12.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    color = style.rippleStyle.asColor(),
                ),
                role = Role.Button,
                onClick = { onAuth(item.name, "FAKE_TOKEN") }
            ),
        horizontalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(
                id = when (item.name) {
                    OAuth.VK -> R.drawable.vk_icon_blue
                    OAuth.MAIL -> R.drawable.mail_icon_blue
                    OAuth.OK -> R.drawable.ok_icon_yellow
                }
            ),
            contentDescription = null,
            modifier = Modifier
                .width(28.dp)
                .height(28.dp)
        )
        if (showText) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    modifier = Modifier,
                    text = item.title,
                    textAlign = TextAlign.Center,
                    color = style.textStyle.asColorResource(),
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
            Spacer(
                modifier = Modifier
                    .width(28.dp)
                    .height(28.dp)
            )
        }
    }
}

@Preview
@Composable
private fun OAuthListWidgetWithOneItem() {
    OAuthListWidget(
        isOAuthAllowed = { it == OAuth.OK },
    )
}

@Preview
@Composable
private fun OAuthListWidgetWithTwoItems() {
    OAuthListWidget(
        isOAuthAllowed = { it in setOf(OAuth.VK, OAuth.OK) },
    )
}

@Preview
@Composable
private fun OAuthListWidgetLight() {
    OAuthListWidget(
        style = OAuthListWidgetStyle.Light(),
    )
}

@Preview
@Composable
private fun OAuthListWidgetDark() {
    OAuthListWidget(
        modifier = Modifier.background(Color.White),
        style = OAuthListWidgetStyle.Dark(),
        isOAuthAllowed = { it == OAuth.OK },
    )
}

private data class OAuthItemData(
    val name: OAuth,
    val title: String,
)
