package com.vk.id.sample.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.navigation.activity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vk.id.VKID
import com.vk.id.sample.app.screen.home.HomeScreen
import com.vk.id.sample.app.screen.multibranding.MultibrandingComposeScreen
import com.vk.id.sample.app.screen.sheet.OneTapBottomSheetScreen
import com.vk.id.sample.app.screen.styling.OnetapStylingComposeScreen
import com.vk.id.sample.app.uikit.theme.AppTheme
import com.vk.id.sample.xml.multibranding.MultibrandingXmlCodeActivity
import com.vk.id.sample.xml.multibranding.MultibrandingXmlLayoutActivity
import com.vk.id.sample.xml.onetap.OneTapStylingXmlLayoutActivity
import com.vk.id.sample.xml.onetapsheet.OnetapSheetStylingXmlActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        VKID.logsEnabled = true
        setContent {
            val navController = rememberNavController()
            AppTheme {
                Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                    NavHost(
                        navController = navController,
                        startDestination = "home"
                    ) {
                        composable("home") { HomeScreen(navController) }
                        composable("onetap-styling-compose") { OnetapStylingComposeScreen() }
                        activity("onetap-styling-xml-layout") { activityClass = OneTapStylingXmlLayoutActivity::class }
                        composable("multibranding-compose") { MultibrandingComposeScreen() }
                        activity("multibranding-xml-code") { activityClass = MultibrandingXmlCodeActivity::class }
                        activity("multibranding-xml-layout") { activityClass = MultibrandingXmlLayoutActivity::class }
                        composable("onetap-bottom-sheet") { OneTapBottomSheetScreen() }
                        activity("onetap-bottom-sheet-xml") { activityClass = OnetapSheetStylingXmlActivity::class }
                    }
                }
            }
        }
    }
}
