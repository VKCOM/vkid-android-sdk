package com.vk.id.sample.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.vk.id.onetap.common.button.VKIDButtonCornersStyle
import com.vk.id.onetap.common.button.VKIDButtonStyle
import com.vk.id.onetap.compose.button.VKIDButton
import com.vk.id.sample.R
import com.vk.id.sample.button.onVKIDAuthFail
import com.vk.id.sample.button.onVKIDAuthSuccess

@Composable
fun HomeScreen(
    navController: NavController
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        VKIDButton(
            modifier = Modifier.width(355.dp),
            style = VKIDButtonStyle.Blue(cornersStyle = VKIDButtonCornersStyle.Rounded),
            onAuth = { onVKIDAuthSuccess(context, it) },
            onFail = { onVKIDAuthFail(context, it) }
        )
        Spacer(modifier = Modifier.height(32.dp))
        Divider(thickness = 1.dp, color = colorResource(id = R.color.vkid_gray900_alpha50))
        Button("Onetap compose styling") {
            navController.navigate("onetap-compose-styling")
        }
        Button("Onetap xml") {
            navController.navigate("onetap-xml")
        }
    }
}

@Composable
private fun Button(
    text: String,
    onClick: () -> Unit,
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .height(44.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.vkid_azure_A100)),
        onClick = onClick
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        )
    }
}
