@file:OptIn(InternalVKIDApi::class)

package com.vk.id.onetap.compose.onetap.sheet.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vk.id.common.InternalVKIDApi
import com.vk.id.onetap.common.alternate.style.InternalVKIDAlternateAccountButtonStyle
import com.vk.id.onetap.compose.R
import com.vk.id.onetap.compose.button.alternate.style.asColorResource
import com.vk.id.onetap.compose.button.alternate.style.background
import com.vk.id.onetap.compose.button.auth.style.asColor
import com.vk.id.onetap.compose.button.auth.style.border
import com.vk.id.onetap.compose.onetap.sheet.OneTapBottomSheetAnalytics
import com.vk.id.onetap.compose.onetap.sheet.style.OneTapBottomSheetStyle
import com.vk.id.onetap.compose.onetap.style.asFontSize
import com.vk.id.onetap.compose.onetap.style.clip

@Composable
internal fun SheetContentAuthFailed(
    serviceName: String,
    style: OneTapBottomSheetStyle,
    dismissSheet: () -> Unit,
    repeatClicked: () -> Unit
) {
    OneTapBottomSheetAnalytics.BottomSheetErrorShown()
    SheetContentBox(
        serviceName,
        style,
        dismissSheet,
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        Image(
            painter = painterResource(R.drawable.vkid_sheet_content_error),
            contentDescription = null,
        )
        BasicText(
            text = stringResource(R.string.vkid_sheet_state_auth_failed),
            modifier = Modifier.padding(top = 12.dp),
            style = TextStyle(
                color = colorResource(style.contentTextColor),
                fontSize = 16.sp,
                fontWeight = FontWeight.W400,
                letterSpacing = 0.1.sp,
                lineHeight = 20.sp
            )
        )
        RepeatButton(style.oneTapStyle.alternateAccountButtonStyle, repeatClicked)
        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Composable
private fun RepeatButton(style: InternalVKIDAlternateAccountButtonStyle, repeatClicked: () -> Unit) {
    // todo alternate and this button to uikit
    Box(
        modifier = Modifier
            .padding(top = 16.dp)
            .border(style.borderStyle, style.cornersStyle)
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
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 6.5.dp, bottom = 7.5.dp),
            text = stringResource(R.string.vkid_sheet_state_auth_failed_retry),
            style = TextStyle(
                color = style.textStyle.asColorResource(),
                fontSize = style.sizeStyle.asFontSize(),
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
            )
        )
    }
}

@Preview
@Composable
private fun OneTapBottomSheetProgressPreview() {
    SheetContentAuthFailed(
        "<Название сервиса>",
        OneTapBottomSheetStyle.Light(),
        dismissSheet = {},
        repeatClicked = {}
    )
}
