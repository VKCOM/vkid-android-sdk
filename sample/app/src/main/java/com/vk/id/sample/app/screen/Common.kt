package com.vk.id.sample.app.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vk.id.AccessToken
import com.vk.id.sample.app.R
import com.vk.id.sample.xml.uikit.common.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

@Composable
internal fun Button(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    androidx.compose.material3.Button(
        modifier = modifier
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
internal fun UseToken(accessToken: AccessToken) {
    Column(horizontalAlignment = Alignment.Start) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Current user: ${accessToken.userID}")
        Spacer(modifier = Modifier.height(12.dp))
        val coroutineScope = rememberCoroutineScope()
        val context = LocalContext.current
        Button(text = "Use token to get Birthday", modifier = Modifier.width(300.dp)) {
            coroutineScope.launch {
                val bday = getUserBday(accessToken)
                showToast(context, "Birth Day: $bday")
            }
        }
    }
}

private suspend fun getUserBday(accessToken: AccessToken): String {
    return withContext(Dispatchers.IO) {
        val api = OkHttpClient.Builder().build()
        val url = "https://api.vk.com/method/users.get?user_ids=${accessToken.userID}&fields=bdate&access_token=${accessToken.token}&v=5.131 HTTP/1.1"
        val request = Request.Builder()
            .url(url)
            .build()
        val response = api.newCall(request).execute()
        val responseJson: JSONObject = JSONObject(requireNotNull(response.body).string())
            .getJSONArray("response")
            .getJSONObject(0)
        responseJson.getString("bdate")
    }
}
