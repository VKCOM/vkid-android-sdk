package com.vk.id.sample.home

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.vk.id.onetap.compose.button.VKIDButton
import com.vk.id.onetap.compose.button.VKIDButtonCornersStyle
import com.vk.id.onetap.compose.button.VKIDButtonStyle
import com.vk.id.sample.BuildConfig
import com.vk.id.sample.R
import com.vk.id.sample.styling.onVKIDAuthFail
import com.vk.id.sample.styling.onVKIDAuthSuccess
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    navController: NavController
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
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
    Box(modifier = Modifier.fillMaxSize()) {
        BuildInfo(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(12.dp)
        )
    }
}

@Composable
private fun Button(
    text: String,
    onClick: () -> Unit,
) {
    Button(
        modifier = Modifier
            .width(355.dp)
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

@Composable
private fun BuildInfo(modifier: Modifier) {
    val context = LocalContext.current
    val text = "VKID sample app ${BuildConfig.VERSION_NAME}\n" +
        "${utcToDate(BuildConfig.VKID_BUILD_TIME)}\n" +
        "${BuildConfig.VERSION_CODE}\n" +
        "CI build ${BuildConfig.CI_BUILD_NUMBER} ${BuildConfig.CI_BUILD_TYPE}"
    ClickableText(
        text = AnnotatedString(text),
        modifier = modifier,
        style = TextStyle(
            color = Color.Gray,
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
        ),
        onClick = {
            val clipboardManager =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("VKID build info", text)
            clipboardManager.setPrimaryClip(clip)
            Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show()
        }
    )
}

@Suppress("SameParameterValue")
private fun utcToDate(vkidBuildTime: Long): String {
    val date = Date(vkidBuildTime)
    val format = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    return format.format(date)
}
