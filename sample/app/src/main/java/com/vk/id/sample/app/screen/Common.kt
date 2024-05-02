package com.vk.id.sample.app.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vk.id.AccessToken
import com.vk.id.sample.app.R

@Composable
internal fun Button(
    text: String,
    modifier: Modifier = Modifier,
    verticalPadding: Dp = 8.dp,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier
            .width(355.dp)
            .padding(vertical = verticalPadding),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.vkid_azure_A100)),
        onClick = onClick
    ) {
        Text(
            modifier = Modifier.padding(2.dp),
            color = colorResource(id = R.color.vkid_white),
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
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            color = MaterialTheme.colorScheme.onBackground,
            text = """
                |Current user: ${accessToken.userID}
                |First name: ${accessToken.userData.firstName}
                |Last name: ${accessToken.userData.lastName}
                |Phone: ${accessToken.userData.phone}
                |Email: ${accessToken.userData.email}
            """.trimMargin()
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}
