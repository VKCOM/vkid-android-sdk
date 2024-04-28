@file:OptIn(InternalVKIDApi::class)

package com.vk.id.onetap.compose.button.alternate

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vk.id.common.InternalVKIDApi
import com.vk.id.onetap.common.alternate.style.InternalVKIDAlternateAccountButtonStyle
import com.vk.id.onetap.compose.R
import com.vk.id.onetap.compose.button.alternate.style.asColorResource
import com.vk.id.onetap.compose.button.alternate.style.background
import com.vk.id.onetap.compose.button.auth.VKIDButtonState
import com.vk.id.onetap.compose.button.auth.style.asColor
import com.vk.id.onetap.compose.button.auth.style.border
import com.vk.id.onetap.compose.onetap.style.asFontSize
import com.vk.id.onetap.compose.onetap.style.asLineHeight
import com.vk.id.onetap.compose.onetap.style.clip
import com.vk.id.onetap.compose.onetap.style.height
import com.vk.id.onetap.compose.util.MeasureUnconstrainedViewWidth

@Composable
internal fun AdaptiveAlternateAccountButton(
    vkidButtonState: VKIDButtonState,
    style: InternalVKIDAlternateAccountButtonStyle = InternalVKIDAlternateAccountButtonStyle.Light(),
    onClick: () -> Unit,
) {
    AnimatedVisibility(
        modifier = Modifier.padding(top = 12.dp),
        visible = !vkidButtonState.userLoadFailed,
    ) {
        BoxWithConstraints {
            MeasureUnconstrainedViewWidth(viewToMeasure = {
                AlternateAccountButton(
                    style = style,
                    textResId = R.string.vkid_auth_use_another_account,
                    onClick = onClick
                )
            }) { largeViewMeasuredWidth ->
                if (largeViewMeasuredWidth < maxWidth) {
                    AlternateAccountButton(
                        style = style,
                        textResId = R.string.vkid_auth_use_another_account,
                        onClick = onClick
                    )
                } else {
                    MeasureUnconstrainedViewWidth(viewToMeasure = {
                        AlternateAccountButton(
                            style = style,
                            textResId = R.string.vkid_auth_use_another_account_short,
                            onClick = onClick
                        )
                    }) { shortViewMasuredWidth ->
                        if (shortViewMasuredWidth < maxWidth) {
                            AlternateAccountButton(
                                style = style,
                                textResId = R.string.vkid_auth_use_another_account_short,
                                onClick = onClick
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AlternateAccountButton(
    modifier: Modifier = Modifier,
    style: InternalVKIDAlternateAccountButtonStyle = InternalVKIDAlternateAccountButtonStyle.Light(),
    @StringRes textResId: Int,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .testTag("sign_in_to_another_account")
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
