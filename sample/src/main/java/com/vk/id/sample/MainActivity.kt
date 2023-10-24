package com.vk.id.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vk.id.VKID
import com.vk.id.sample.button.OnetapComposeStylingScreen
import com.vk.id.sample.button.OnetapXmlScreen
import com.vk.id.sample.home.HomeScreen

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
                composable("onetap-compose-styling") { OnetapComposeStylingScreen() }
                composable("onetap-xml") { OnetapXmlScreen() }
            }
        }
    }
}
