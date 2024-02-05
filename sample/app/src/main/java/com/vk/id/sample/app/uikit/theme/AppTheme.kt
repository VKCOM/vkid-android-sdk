package com.vk.id.sample.app.uikit.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.vk.id.sample.app.R
import com.vk.id.sample.xml.R as xmlR

@Composable
internal fun AppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    if (!useDarkTheme) {
        MaterialTheme(
            colorScheme = lightColors(),
            content = content
        )
    } else {
        MaterialTheme(
            colorScheme = darkColors(),
            content = content
        )
    }
}

@Composable
private fun lightColors() = lightColorScheme(
    primary = colorResource(R.color.vkid_azure_A100),
    background = colorResource(R.color.vkid_white),
)

@Composable
private fun darkColors() = darkColorScheme(
    primary = colorResource(R.color.vkid_azure_A100),
    background = colorResource(id = xmlR.color.vkid_gray900)
)
