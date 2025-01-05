package com.vk.id.group.subscription.compose.snackbar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.vk.id.group.subscription.compose.R

@Composable
internal fun GroupSubscriptionSnackbar(
    message: String,
) {
    Snackbar(
        containerColor = colorResource(R.color.vkid_group_subscription_background),
        shape = ShapeDefaults.Small,
        action = {
            Spacer(modifier = Modifier.height(56.dp))
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.vkid_check_circle_outline),
                contentDescription = null,
                modifier = Modifier,
                tint = colorResource(R.color.vkid_green)
            )
            Text(
                text = message,
                style = TextStyle(
                    color = colorResource(R.color.vkid_white)
                ),
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}
