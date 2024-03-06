package com.vk.id.sample.app.screen.bottomnav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

internal enum class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    Main(
        route = "main",
        title = "Main",
        icon = Icons.Filled.Home,
    ),

    Utils(
        route = "utils",
        title = "Utils",
        icon = Icons.Filled.Settings,
    )
}
