package com.vk.id.sample.screen.sheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vk.id.onetap.compose.onetap.sheet.OneTapBottomSheet
import com.vk.id.onetap.compose.onetap.sheet.OneTapScenario
import com.vk.id.onetap.compose.onetap.sheet.rememberOneTapBottomSheetState
import com.vk.id.sample.screen.home.Button
import com.vk.id.sample.screen.styling.util.onVKIDAuthSuccess

@Preview
@Composable
fun OneTapBottomSheetScreen() {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        val bottomSheetState = rememberOneTapBottomSheetState()
        OneTapBottomSheet(
            onAuth = { onVKIDAuthSuccess(context, it) },
            state = bottomSheetState,
            scenario = OneTapScenario.OrderInService,
            serviceName = "VKID Sample"
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(text = "OneTapModalBottomSheet", onClick = {
            if (bottomSheetState.isVisible) {
                bottomSheetState.hide()
            } else {
                bottomSheetState.show()
            }
        })
    }
}
