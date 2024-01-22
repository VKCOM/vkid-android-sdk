package com.vk.id.onetap.compose.onetap.sheet.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vk.id.onetap.compose.R
import com.vk.id.onetap.compose.onetap.sheet.style.OneTapBottomSheetStyle

@Composable
internal fun SheetContentAuthSuccess(
    serviceName: String,
    style: OneTapBottomSheetStyle,
    dismissSheet: () -> Unit,
) {
    SheetContentBox(
        serviceName,
        style,
        dismissSheet,
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        Image(
            painter = painterResource(R.drawable.vkid_sheet_content_success),
            contentDescription = null,
        )
        BasicText(
            text = stringResource(id = R.string.vkid_sheet_state_auth_success),
            modifier = Modifier.padding(top = 12.dp),
            style = TextStyle(
                color = colorResource(style.contentTextColor),
                fontSize = 16.sp,
                fontWeight = FontWeight.W400,
                letterSpacing = 0.1.sp,
                lineHeight = 20.sp
            )
        )
        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Preview
@Composable
private fun OneTapBottomSheetProgressPreview() {
    SheetContentAuthSuccess(
        "<Название сервиса>",
        OneTapBottomSheetStyle.TransparentLight(),
        dismissSheet = {},
    )
}
