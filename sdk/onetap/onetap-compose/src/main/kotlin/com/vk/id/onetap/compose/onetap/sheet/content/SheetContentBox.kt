package com.vk.id.onetap.compose.onetap.sheet.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vk.id.onetap.compose.R
import com.vk.id.onetap.compose.onetap.sheet.style.OneTapBottomSheetStyle
import com.vk.id.onetap.compose.onetap.sheet.style.background
import com.vk.id.onetap.compose.onetap.sheet.style.clip

@Composable
internal fun SheetContentBox(
    serviceName: String,
    style: OneTapBottomSheetStyle,
    dismissSheet: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .safeContentPadding()
            .wrapContentHeight()
            .fillMaxWidth()
            .widthIn(min = 344.dp, max = 800.dp)
            .padding(8.dp)
            .clip(style.cornersStyle)
            .background(style.backgroundStyle),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                VkidIcon(style)
                val titleTextStyle = TextStyle(
                    color = colorResource(style.contentTextColor),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.W400,
                    letterSpacing = 0.2.sp,
                    lineHeight = 16.sp
                )
                Dot(titleTextStyle)
                ServiceTitle(titleTextStyle, serviceName)
                Spacer(Modifier.weight(1f))
                CloseIcon(dismissSheet)
            }
            content()
        }
    }
}

@Composable
private fun VkidIcon(style: OneTapBottomSheetStyle) {
    Image(
        painter = painterResource(style.vkidIcon),
        contentDescription = null,
        contentScale = ContentScale.Fit,
    )
}

@Composable
private fun ServiceTitle(style: TextStyle, serviceName: String) {
    BasicText(text = serviceName, style = style)
}

@Composable
private fun Dot(style: TextStyle) {
    BasicText(
        modifier = Modifier.padding(horizontal = 4.dp, vertical = 0.dp),
        text = "·",
        style = style
    )
}

@Composable
private fun CloseIcon(dismissSheet: () -> Unit) {
    Box(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    dismissSheet()
                }
            )
    ) {
        Image(
            painter = painterResource(R.drawable.vkid_onetap_bottomsheet_close),
            contentDescription = null,
            contentScale = ContentScale.Fit,
        )
    }
}

@Preview
@Composable
private fun SheetContentBoxPreview() {
    SheetContentBox(
        "service name",
        OneTapBottomSheetStyle.TransparentLight(),
        {}
    ) {
        Box(modifier = Modifier.background(Color.Green).size(100.dp))
    }
}
