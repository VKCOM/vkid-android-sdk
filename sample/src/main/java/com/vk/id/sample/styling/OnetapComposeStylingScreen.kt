package com.vk.id.sample.styling

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vk.id.onetap.compose.button.VKIDButton
import com.vk.id.onetap.compose.button.VKIDButtonSmall
import com.vk.id.sample.R

@Composable
fun OnetapComposeStylingScreen() {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        buttonStylingData.forEach {
            when (it) {
                is ListItem.Spacer -> Spacer(
                    modifier = Modifier
                        .darkBackground(it.isDarkBackground)
                        .height(24.dp)
                        .fillMaxWidth(),
                )
                is ListItem.HalfSpacer -> Spacer(
                    modifier = Modifier
                        .darkBackground(it.isDarkBackground)
                        .height(12.dp)
                        .fillMaxWidth(),
                )
                is ListItem.Title -> Row(
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        modifier = Modifier.padding(all = 8.dp),
                        text = it.text,
                        fontSize = 24.sp,
                        lineHeight = 28.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                is ListItem.Button -> Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .darkBackground(it.isDarkBackground)
                        .fillMaxWidth()
                ) {
                    VKIDButton(
                        modifier = Modifier.width(it.width.dp),
                        style = it.style,
                        onAuth = { onVKIDAuthSuccess(context, it) },
                        onFail = { onVKIDAuthFail(context, it) }
                    )
                }
                is ListItem.SmallButton -> VKIDButtonSmall(
                    style = it.style,
                    onAuth = { onVKIDAuthSuccess(context, it) },
                    onFail = { onVKIDAuthFail(context, it) }
                )
            }
        }
    }
}

private fun Modifier.darkBackground(isDarkBackground: Boolean) = composed {
    if (isDarkBackground) {
        background(color = colorResource(id = R.color.vkid_gray900))
    } else {
        this
    }
}
