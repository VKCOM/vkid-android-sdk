package com.vk.id.sample.app.screen.sheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vk.id.AccessToken
import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.onetap.compose.onetap.sheet.OneTapBottomSheet
import com.vk.id.onetap.compose.onetap.sheet.OneTapScenario
import com.vk.id.onetap.compose.onetap.sheet.rememberOneTapBottomSheetState
import com.vk.id.onetap.compose.onetap.sheet.style.OneTapBottomSheetStyle
import com.vk.id.onetap.compose.onetap.sheet.style.rememberOneTapBottomSheetStyle
import com.vk.id.sample.app.screen.Button
import com.vk.id.sample.app.screen.UseToken
import com.vk.id.sample.app.uikit.selector.DropdownSelector
import com.vk.id.sample.xml.uikit.common.getOneTapFailCallback
import com.vk.id.sample.xml.uikit.common.getOneTapSuccessCallback
import java.util.Locale
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.createType
import kotlin.reflect.full.functions
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.primaryConstructor

@Preview
@Composable
@Suppress("LongMethod")
fun OneTapBottomSheetScreen() {
    val context = LocalContext.current
    val token: MutableState<AccessToken?> = remember { mutableStateOf(null) }
    val selectedScenario = rememberSaveable { mutableStateOf(OneTapScenario.EnterService) }
    val selectedStyle = rememberOneTapBottomSheetStyle(OneTapBottomSheetStyle.system(context))
    val autoHideSheetOnSuccess = rememberSaveable { mutableStateOf(true) }
    val selectedOAuths = rememberSaveable { mutableStateOf(setOf(OneTapOAuth.OK, OneTapOAuth.MAIL)) }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val bottomSheetState = rememberOneTapBottomSheetState()
        OneTapBottomSheet(
            style = selectedStyle.value,
            onAuth = getOneTapSuccessCallback(context) { token.value = it },
            onFail = getOneTapFailCallback(context),
            state = bottomSheetState,
            scenario = selectedScenario.value,
            autoHideOnSuccess = autoHideSheetOnSuccess.value,
            serviceName = "VKID Sample",
            oAuths = selectedOAuths.value,
        )
        DropdownSelector(
            values = enumValues<OneTapScenario>().associateBy { it.name },
            selectedValue = selectedScenario.value.name,
            onValueSelected = { selectedScenario.value = it }
        )
        DropdownSelector(
            values = OneTapBottomSheetStyle::class.sealedSubclasses.associate {
                it.simpleName!! to { it.primaryConstructor!!.call() }
            } + OneTapBottomSheetStyle::class.companionObject!!.functions.filter { it.returnType.isSubtypeOf(OneTapBottomSheetStyle::class.createType()) }
                .associate {
                    it.name.replaceFirstChar { char -> if (char.isLowerCase()) char.titlecase(Locale.getDefault()) else char.toString() } to
                        {
                            it.callBy(mapOf(it.parameters.first() to OneTapBottomSheetStyle.Companion, it.parameters[1] to context)) as OneTapBottomSheetStyle
                        }
                },
            selectedValue = selectedStyle.value::class.simpleName ?: error("Can get simple style"),
            onValueSelected = { selectedStyle.value = it.invoke() }
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            OAuthSelector(selectedOAuths = selectedOAuths, name = "Mail", oAuth = OneTapOAuth.MAIL)
            OAuthSelector(selectedOAuths = selectedOAuths, name = "OK", oAuth = OneTapOAuth.OK)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = autoHideSheetOnSuccess.value, onCheckedChange = { autoHideSheetOnSuccess.value = it })
            Text(
                "Auto hide on success",
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Spacer(Modifier.padding(16.dp))
        Row {
            Button(
                text = "Show",
                modifier = Modifier.width(100.dp)
            ) {
                if (bottomSheetState.isVisible) {
                    bottomSheetState.hide()
                } else {
                    bottomSheetState.show()
                }
            }
            Spacer(Modifier.weight(1f))
        }
        token.value?.let {
            UseToken(accessToken = it)
        }
    }
}

@Composable
private fun OAuthSelector(
    selectedOAuths: MutableState<Set<OneTapOAuth>>,
    name: String,
    oAuth: OneTapOAuth,
) {
    Text(
        text = name,
        color = MaterialTheme.colorScheme.onBackground
    )
    Checkbox(
        checked = selectedOAuths.value.contains(oAuth),
        onCheckedChange = {
            if (it) {
                selectedOAuths.value = selectedOAuths.value + oAuth
            } else {
                selectedOAuths.value = selectedOAuths.value - oAuth
            }
        }
    )
}
