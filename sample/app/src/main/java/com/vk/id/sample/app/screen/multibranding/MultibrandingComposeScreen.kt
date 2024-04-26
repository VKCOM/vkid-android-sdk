package com.vk.id.sample.app.screen.multibranding

import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.vk.id.AccessToken
import com.vk.id.OAuth
import com.vk.id.auth.AuthCodeData
import com.vk.id.auth.VKIDAuthUiParams
import com.vk.id.multibranding.OAuthListWidget
import com.vk.id.multibranding.common.style.OAuthListWidgetCornersStyle
import com.vk.id.multibranding.common.style.OAuthListWidgetSizeStyle
import com.vk.id.multibranding.common.style.OAuthListWidgetStyle
import com.vk.id.multibranding.xml.OAuthListWidget
import com.vk.id.sample.app.screen.UseToken
import com.vk.id.sample.app.uikit.selector.CheckboxSelector
import com.vk.id.sample.app.uikit.selector.DropdownSelector
import com.vk.id.sample.app.uikit.selector.EnumStateCheckboxSelector
import com.vk.id.sample.app.uikit.selector.SliderSelector
import com.vk.id.sample.app.uikit.selector.styleConstructors
import com.vk.id.sample.app.uikit.theme.AppTheme
import com.vk.id.sample.app.util.carrying.carry
import com.vk.id.sample.xml.uikit.common.dpToPixels
import com.vk.id.sample.xml.uikit.common.getMultibrandingFailCallback
import com.vk.id.sample.xml.uikit.common.getMultibrandingSuccessCallback
import com.vk.id.sample.xml.uikit.common.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.reflect.KCallable

private const val MIN_WIDTH_DP = 100f
private const val TOTAL_WIDTH_PADDING_DP = 16
private const val MAX_RADIUS_DP = 30

@Preview
@Composable
@Suppress("LongMethod")
internal fun MultibrandingComposeScreen() {
    val context = LocalContext.current
    val token = remember { mutableStateOf<AccessToken?>(null) }
    var code by remember { mutableStateOf<String?>(null) }
    val screenWidth = LocalConfiguration.current.screenWidthDp - TOTAL_WIDTH_PADDING_DP
    val widthPercent = remember { mutableFloatStateOf(1f) }
    val cornersStylePercent = remember { mutableFloatStateOf(0f) }
    val selectedSize = remember { mutableStateOf(OAuthListWidgetSizeStyle.DEFAULT) }
    val selectedOAuths = remember { mutableStateOf(OAuth.entries.toSet()) }
    val styleConstructor = remember<MutableState<KCallable<OAuthListWidgetStyle>?>> { mutableStateOf(null) }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            styleConstructor.value = OAuthListWidgetStyle.Companion::system.carry(context)
        }
    }
    val shouldUseXml = remember { mutableStateOf(false) }
    var selectedStyle by remember { mutableStateOf(OAuthListWidgetStyle.system(context)) }
    LaunchedEffect(
        styleConstructor.value,
        cornersStylePercent.floatValue,
        selectedSize.value,
    ) {
        withContext(Dispatchers.IO) {
            styleConstructor.value?.let {
                selectedStyle = it.call(
                    OAuthListWidgetCornersStyle.Custom(MAX_RADIUS_DP * cornersStylePercent.floatValue),
                    selectedSize.value,
                )
            }
        }
    }

    var styleConstructors by remember { mutableStateOf<Map<String, KCallable<OAuthListWidgetStyle>>>(emptyMap()) }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            styleConstructors = OAuthListWidgetStyle::class.styleConstructors(context)
        }
    }
    var scopes by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var codeChallenge by remember { mutableStateOf("") }
    AppTheme(
        useDarkTheme = selectedStyle is OAuthListWidgetStyle.Dark
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 8.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
        ) {
            if (selectedOAuths.value.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    val width = maxOf(MIN_WIDTH_DP, (screenWidth * widthPercent.floatValue))
                    val onAuthCode = { data: AuthCodeData, isCompletion: Boolean ->
                        code = data.code
                        token.value = null
                        if (isCompletion) {
                            showToast(context, "Received auth code")
                        }
                    }
                    val authParams = VKIDAuthUiParams {
                        this.scopes = scopes.split(' ', ',').toSet()
                        this.state = state.takeIf { it.isNotBlank() }
                        this.codeChallenge = codeChallenge.takeIf { it.isNotBlank() }
                    }
                    if (shouldUseXml.value) {
                        var oAuthListWidget: OAuthListWidget? by remember { mutableStateOf(null) }
                        AndroidView(factory = { context ->
                            OAuthListWidget(context).apply {
                                setCallbacks(
                                    onAuth = getMultibrandingSuccessCallback(context) {},
                                    onAuthCode = onAuthCode,
                                    onFail = getMultibrandingFailCallback(context),
                                )
                                oAuthListWidget = this
                            }
                        })
                        oAuthListWidget?.apply {
                            this.layoutParams = ViewGroup.LayoutParams(
                                context.dpToPixels(width.toInt()),
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                            )
                            this.style = selectedStyle
                            this.oAuths = selectedOAuths.value
                            this.authParams = authParams
                        }
                    } else {
                        OAuthListWidget(
                            modifier = Modifier.width(width.dp),
                            style = selectedStyle,
                            onAuth = getMultibrandingSuccessCallback(context) { token.value = it },
                            onAuthCode = onAuthCode,
                            onFail = getMultibrandingFailCallback(context),
                            oAuths = selectedOAuths.value,
                            authParams = authParams,
                        )
                    }
                }
            }
            code?.let { value ->
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = value,
                    onValueChange = { code = it },
                    label = { Text("Resulting auth code") },
                    maxLines = 1,
                )
            }
            token.value?.let {
                UseToken(accessToken = it)
            }
            Spacer(modifier = Modifier.height(16.dp))
            CheckboxSelector(
                title = "XML",
                isChecked = shouldUseXml.value,
                onCheckedChange = { shouldUseXml.value = it }
            )
            EnumStateCheckboxSelector(state = selectedOAuths)
            DropdownSelector(
                modifier = Modifier.padding(vertical = 16.dp),
                values = styleConstructors,
                selectedValue = selectedStyle::class.simpleName ?: error("Can get simple style"),
                onValueSelected = { styleConstructor.value = it },
                label = { Text("style") },
            )
            SliderSelector(title = "Width", selectedState = widthPercent)
            SliderSelector(title = "Corners", selectedState = cornersStylePercent)
            DropdownSelector(
                values = OAuthListWidgetSizeStyle.entries.associateBy { it.name },
                selectedValue = selectedSize.value.name,
                onValueSelected = { selectedSize.value = it },
                label = { Text("size") },
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = scopes,
                onValueChange = { scopes = it },
                label = { Text("Scopes (Space-separated)") },
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state,
                onValueChange = { state = it },
                label = { Text("State (Optional)") },
                shape = RectangleShape,
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = codeChallenge,
                onValueChange = { codeChallenge = it },
                label = { Text("Code challenge (Optional)") },
                shape = RectangleShape,
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
