package com.vk.id.onetap.compose.onetap.sheet.content

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.vk.id.onetap.common.alternate.style.AlternateAccountButtonStyle
import com.vk.id.onetap.compose.button.alternate.style.asColorResource
import com.vk.id.onetap.compose.button.alternate.style.background
import com.vk.id.onetap.compose.button.auth.style.asColor
import com.vk.id.onetap.compose.button.auth.style.border
import com.vk.id.onetap.compose.onetap.sheet.style.OneTapBottomSheetStyle
import com.vk.id.onetap.compose.onetap.style.asFontSize
import com.vk.id.onetap.compose.onetap.style.asLineHeight
import com.vk.id.onetap.compose.onetap.style.clip
import com.vk.id.onetap.compose.onetap.style.height

@Composable
internal fun SheetContentAuthFailed(
    serviceName: String,
    style: OneTapBottomSheetStyle,
    dismissSheet: () -> Unit,
    repeatClicked: () -> Unit
) {
    SheetContentBox(
        serviceName,
        style,
        dismissSheet,
    ) {
        RepeatButton(
            style.oneTapStyle.alternateAccountButtonStyle,
            repeatClicked
        )
    }
}

@Composable
private fun RepeatButton(style: AlternateAccountButtonStyle, repeatClicked: () -> Unit) {
    // todo alternate and this button to uikit
    Box(
        modifier = Modifier
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
                onClick = repeatClicked
            ),
        contentAlignment = Alignment.Center
    ) {
        BasicText(
            text = "Повторить попытку",
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
