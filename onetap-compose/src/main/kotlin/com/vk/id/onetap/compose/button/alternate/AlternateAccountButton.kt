@file:OptIn(InternalVKIDApi::class)

package com.vk.id.onetap.compose.button.alternate

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.vk.id.commn.InternalVKIDApi
import com.vk.id.onetap.common.alternate.style.AlternateAccountButtonStyle
import com.vk.id.onetap.compose.R
import com.vk.id.onetap.compose.button.alternate.style.asColorResource
import com.vk.id.onetap.compose.button.alternate.style.background
import com.vk.id.onetap.compose.button.auth.style.asColor
import com.vk.id.onetap.compose.button.auth.style.border
import com.vk.id.onetap.compose.onetap.style.asFontSize
import com.vk.id.onetap.compose.onetap.style.asLineHeight
import com.vk.id.onetap.compose.onetap.style.clip
import com.vk.id.onetap.compose.onetap.style.height

@Composable
internal fun AlternateAccountButton(
    modifier: Modifier = Modifier,
    style: AlternateAccountButtonStyle = AlternateAccountButtonStyle.Light(),
    @StringRes textResId: Int,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(style.borderStyle, style.cornersStyle)
            .height(style.sizeStyle)
            .clip(style.cornersStyle)
            .clipToBounds()
            .background(style.backgroundStyle)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    color = style.rippleStyle.asColor(),
                ),
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        BasicText(
            text = stringResource(id = textResId),
            style = TextStyle(
                color = style.textStyle.asColorResource(),
                fontSize = style.sizeStyle.asFontSize(),
                lineHeight = style.sizeStyle.asLineHeight(),
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        )
    }
}

@Preview
@Composable
private fun AlternateAccountButtonPreview() {
    AlternateAccountButton(
        textResId = R.string.vkid_auth_use_another_account_short,
        onClick = {},
    )
}
