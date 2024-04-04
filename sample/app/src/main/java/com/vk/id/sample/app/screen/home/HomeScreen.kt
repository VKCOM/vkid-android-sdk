package com.vk.id.sample.app.screen.home

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.vk.id.AccessToken
import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.onetap.common.OneTapStyle
import com.vk.id.onetap.common.button.style.OneTapButtonCornersStyle
import com.vk.id.onetap.compose.onetap.OneTap
import com.vk.id.sample.app.BuildConfig
import com.vk.id.sample.app.R
import com.vk.id.sample.app.screen.Button
import com.vk.id.sample.app.screen.UseToken
import com.vk.id.sample.xml.uikit.common.getOneTapFailCallback
import com.vk.id.sample.xml.uikit.common.getOneTapSuccessCallback
import com.vk.id.sample.xml.vkid
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
@Suppress("LongMethod")
internal fun HomeScreen(
    navController: NavController
) {
    val context = LocalContext.current
    // for demo purpose only, use secured place to keep token
    var token: AccessToken? by remember { mutableStateOf(null) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        OneTap(
            modifier = Modifier.width(355.dp),
            style = if (isSystemInDarkTheme()) {
                OneTapStyle.Dark(cornersStyle = OneTapButtonCornersStyle.Rounded)
            } else {
                OneTapStyle.Light(cornersStyle = OneTapButtonCornersStyle.Rounded)
            },
            onAuth = getOneTapSuccessCallback(context) { token = it },
            onFail = getOneTapFailCallback(context),
            signInAnotherAccountButtonEnabled = true,
            oAuths = setOf(OneTapOAuth.MAIL, OneTapOAuth.OK),
            vkid = context.vkid,
        )
        token?.let {
            UseToken(it)
        }
        Spacer(modifier = Modifier.height(32.dp))
        Divider(thickness = 1.dp, color = colorResource(id = R.color.vkid_gray900_alpha50))
        Button("Onetap styling (code)") {
            navController.navigate("onetap-styling-compose")
        }
        Button("Onetap styling (xml layout)") {
            navController.navigate("onetap-styling-xml-layout")
        }
        Button("Multibranding (code)") {
            navController.navigate("multibranding-compose")
        }
        Button("Multibranding (xml layout)") {
            navController.navigate("multibranding-xml-layout")
        }
        Button("OneTapBottomSheet (code)") {
            navController.navigate("onetap-bottom-sheet")
        }
        Button("OneTapBottomSheet (xml layout)") {
            navController.navigate("onetap-bottom-sheet-xml")
        }
        Spacer(modifier = Modifier.weight(1f))
        Box(modifier = Modifier.fillMaxWidth()) {
            BuildInfo(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(12.dp)
            )
        }
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
