package com.vk.id.onetap.compose.onetap.sheet.content

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
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
internal fun SheetContentAuthInProgress(serviceName: String, style: OneTapBottomSheetStyle, dismissSheet: () -> Unit) {
    SheetContentBox(
        serviceName,
        style,
        dismissSheet,
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        CircleProgress(R.drawable.vkid_sheet_spinner)
        BasicText(
            text = stringResource(id = R.string.vkid_sheet_state_auth_in_progress),
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

@Composable
private fun CircleProgress(
    @DrawableRes
    progressRes: Int
) {
    val infiniteTransition = rememberInfiniteTransition(label = "vkid_auth_in_progress_spinner")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "VKID Auth in progress"
    )
    Image(
        modifier = Modifier
            .graphicsLayer {
                rotationZ = angle
            },
        painter = painterResource(progressRes),
        contentDescription = null,
    )
}

@Preview
@Composable
private fun OneTapBottomSheetProgressPreview() {
    SheetContentAuthInProgress(
        "<Название сервиса>",
        OneTapBottomSheetStyle.TransparentDark(),
        dismissSheet = {},
    )
}
