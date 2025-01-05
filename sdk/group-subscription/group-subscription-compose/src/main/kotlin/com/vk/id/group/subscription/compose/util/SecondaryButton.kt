package com.vk.id.group.subscription.compose.util

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vk.id.group.subscription.compose.R

@Composable
internal fun SecondaryButton(
    text: String,
    onClick: () -> Unit
) {
    androidx.compose.material3.FilledTonalButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.filledTonalButtonColors().copy(
            containerColor = Color(red = 0, green = 0, blue = 0, alpha = 0x0A)
        ),
    ) {
        Text(
            text = text,
            modifier = Modifier,
            style = TextStyle(
                textAlign = TextAlign.Center,
                color = colorResource(R.color.vkid_azure_300),
                fontSize = 16.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Medium,
            ),
        )
    }
}
