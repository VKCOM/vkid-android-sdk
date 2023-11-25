package com.vk.id.sample.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.activity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vk.id.VKID
import com.vk.id.sample.app.screen.home.HomeScreen
import com.vk.id.sample.app.screen.multibranding.MultibrandingComposeScreen
import com.vk.id.sample.app.screen.sheet.OneTapBottomSheetScreen
import com.vk.id.sample.app.screen.styling.OnetapStylingComposeScreen
import com.vk.id.sample.xml.multibranding.MultibrandingXmlCodeActivity
import com.vk.id.sample.xml.multibranding.MultibrandingXmlLayoutActivity
import com.vk.id.sample.xml.onetap.OnetapStylingXmlCodeActivity

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
                activity("onetap-styling-xml") { activityClass = OnetapStylingXmlCodeActivity::class }
                composable("multibranding-compose") { MultibrandingComposeScreen() }
                activity("multibranding-xml-code") { activityClass = MultibrandingXmlCodeActivity::class }
                activity("multibranding-xml-layout") { activityClass = MultibrandingXmlLayoutActivity::class }
                composable("onetap-bottom-sheet") { OneTapBottomSheetScreen() }
            }
        }
    }
}
