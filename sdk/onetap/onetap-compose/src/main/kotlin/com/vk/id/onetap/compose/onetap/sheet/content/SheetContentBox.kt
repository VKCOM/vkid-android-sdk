@file:OptIn(InternalVKIDApi::class)

package com.vk.id.onetap.compose.onetap.sheet.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vk.id.common.InternalVKIDApi
import com.vk.id.onetap.compose.R
import com.vk.id.onetap.compose.onetap.sheet.style.OneTapBottomSheetStyle
import com.vk.id.onetap.compose.onetap.sheet.style.background
import com.vk.id.onetap.compose.onetap.sheet.style.clip

@Composable
internal fun SheetContentBox(
    style: OneTapBottomSheetStyle,
    rowContent: @Composable RowScope.() -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .safeContentPadding()
            .wrapContentHeight()
            .widthIn(min = 200.dp, max = 420.dp)
            .clip(style.cornersStyle)
            .background(style.backgroundStyle),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(verticalAlignment = Alignment.Top) {
                rowContent()
            }
            Column(
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                content()
            }
        }
    }
}

@Composable
internal fun SheetContentBox(
    style: OneTapBottomSheetStyle,
    dismissSheet: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    SheetContentBox(
        style = style,
        rowContent = {
            Spacer(Modifier.weight(1f))
            CloseIcon(dismissSheet)
        },
        content = content,
    )
}

@Composable
internal fun CloseIcon(dismissSheet: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .size(44.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    dismissSheet()
                }
            ),
        contentAlignment = Alignment.Center,
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
        OneTapBottomSheetStyle.TransparentLight(),
        rowContent = {
            Spacer(modifier = Modifier.width(12.dp))
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(R.drawable.vkid_sheet_logo_light),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .padding(top = 8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            CloseIcon {}
        }
    ) {
        Text(text = "1 2 3")
    }
}
