package com.vk.id.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vk.id.VKID
import com.vk.id.sample.screen.home.HomeScreen
import com.vk.id.sample.screen.multibranding.MultibrandingComposeScreen
import com.vk.id.sample.screen.multibranding.MultibrandingXmlScreen
import com.vk.id.sample.screen.sheet.OneTapBottomSheetScreen
import com.vk.id.sample.screen.styling.OnetapStylingComposeScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        VKID.logsEnabled = true
        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = "home"
            ) {
                composable("home") { HomeScreen(navController) }
                composable("onetap-styling-compose") { OnetapStylingComposeScreen() }
                composable("multibranding-compose") { MultibrandingComposeScreen() }
                composable("multibranding-xml") { MultibrandingXmlScreen() }
                composable("onetap-bottom-sheet") { OneTapBottomSheetScreen() }
            }
        }
    }
}
