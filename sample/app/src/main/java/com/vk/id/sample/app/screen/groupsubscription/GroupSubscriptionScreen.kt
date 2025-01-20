package com.vk.id.sample.app.screen.groupsubscription

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.vk.id.VKID
import com.vk.id.group.subscription.common.style.GroupSubscriptionButtonsCornersStyle
import com.vk.id.group.subscription.common.style.GroupSubscriptionButtonsSizeStyle
import com.vk.id.group.subscription.common.style.GroupSubscriptionSheetCornersStyle
import com.vk.id.group.subscription.common.style.GroupSubscriptionStyle
import com.vk.id.group.subscription.compose.ui.GroupSubscriptionSheet
import com.vk.id.group.subscription.compose.ui.GroupSubscriptionSnackbarHost
import com.vk.id.group.subscription.compose.ui.rememberGroupSubscriptionSheetState
import com.vk.id.group.subscription.xml.GroupSubscriptionSheet
import com.vk.id.group.subscription.xml.GroupSubscriptionSnackbarHost
import com.vk.id.sample.app.screen.Button
import com.vk.id.sample.app.uikit.selector.CheckboxSelector
import com.vk.id.sample.app.uikit.selector.DropdownSelector
import com.vk.id.sample.app.uikit.selector.SliderSelector
import com.vk.id.sample.app.uikit.selector.styleConstructors
import com.vk.id.sample.app.uikit.theme.AppTheme
import com.vk.id.sample.app.util.carrying.carry
import com.vk.id.sample.xml.uikit.common.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.reflect.KCallable

private const val MAX_RADIUS_DP = 100

@Composable
@Suppress("LongMethod")
internal fun GroupSubscriptionScreen() {
    val context = LocalContext.current
    val state = rememberGroupSubscriptionSheetState()
    val styleConstructor = remember { mutableStateOf(GroupSubscriptionStyle::system.carry(context)) }
    var selectedStyle by remember { mutableStateOf(GroupSubscriptionStyle.system(context)) }
    val sheetCornersStylePercent = remember { mutableFloatStateOf(0f) }
    val buttonsCornersStylePercent = remember { mutableFloatStateOf(0f) }
    val selectedSize = remember { mutableStateOf(GroupSubscriptionButtonsSizeStyle.DEFAULT) }
    val useDarkTheme = selectedStyle is GroupSubscriptionStyle.Dark
    var styleConstructors by remember { mutableStateOf<Map<String, KCallable<GroupSubscriptionStyle>>>(emptyMap()) }
    val shouldUseXml = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            styleConstructors = GroupSubscriptionStyle::class.styleConstructors(context)
        }
    }
    LaunchedEffect(
        styleConstructor.value to sheetCornersStylePercent.floatValue,
        selectedSize.value to buttonsCornersStylePercent.floatValue
    ) {
        withContext(Dispatchers.IO) {
            selectedStyle = styleConstructor.value.call(
                GroupSubscriptionSheetCornersStyle.Custom(MAX_RADIUS_DP * sheetCornersStylePercent.floatValue),
                GroupSubscriptionButtonsCornersStyle.Custom(MAX_RADIUS_DP * buttonsCornersStylePercent.floatValue),
                selectedSize.value,
            )
        }
    }
    AppTheme(
        useDarkTheme = useDarkTheme
    ) {
        val snackbarHostState = remember { SnackbarHostState() }
        Box(contentAlignment = Alignment.Center) {
            var host: GroupSubscriptionSnackbarHost? by remember { mutableStateOf(null) }
            var androidView: GroupSubscriptionSheet? by remember { mutableStateOf(null) }
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CheckboxSelector(
                    title = "XML",
                    isChecked = shouldUseXml.value,
                    onCheckedChange = { shouldUseXml.value = it }
                )
                DropdownSelector(
                    modifier = Modifier.padding(vertical = 16.dp),
                    values = styleConstructors,
                    selectedValue = selectedStyle::class.simpleName ?: error("Can get simple style"),
                    onValueSelected = {
                        styleConstructor.value = it
                    },
                    label = { Text("style") },
                )
                DropdownSelector(
                    values = GroupSubscriptionButtonsSizeStyle.entries.associateBy { it.name },
                    selectedValue = selectedSize.value.name,
                    onValueSelected = { selectedSize.value = it },
                    label = { Text("size") },
                )
                SliderSelector(
                    title = "Sheet corners",
                    selectedState = sheetCornersStylePercent,
                    onStateChange = { sheetCornersStylePercent.floatValue = it }
                )
                SliderSelector(
                    title = "Button corners",
                    selectedState = buttonsCornersStylePercent,
                    onStateChange = { buttonsCornersStylePercent.floatValue = it }
                )
                Button("Show") {
                    state.show()
                    androidView?.show()
                }
            }
            if (shouldUseXml.value) {
                AndroidView(factory = { context ->
                    GroupSubscriptionSheet(context).apply {
                        setCallbacks(
                            onSuccess = { showToast(context, "Success") },
                            onFail = { showToast(context, "Fail: ${it.description}") },
                        )
                        this.groupId = "1"
                        this.snackbarHost = host ?: GroupSubscriptionSnackbarHost(context)
                        androidView = this
                    }
                })
                androidView?.apply {
                    this.snackbarHost = host
                    this.style = selectedStyle
                    this.setCallbacks(
                        onSuccess = { showToast(context, "Subscribed") },
                        onFail = { showToast(context, "Fail: ${it.description}") },
                    )
                }
            } else {
                androidView = null
                GroupSubscriptionSheet(
                    state = state,
                    accessTokenProvider = {
                        VKID.instance.accessToken?.token ?: run {
                            showToast(context, "Not authorized")
                            ""
                        }
                    },
                    groupId = "1",
                    onSuccess = { showToast(context, "Success") },
                    onFail = { showToast(context, "Fail: ${it.description}") },
                    style = selectedStyle,
                    snackbarHostState = snackbarHostState
                )
            }
            GroupSubscriptionSnackbarHost(
                snackbarHostState = snackbarHostState,
                style = selectedStyle,
            )
            if (shouldUseXml.value) {
                AndroidView({ context ->
                    GroupSubscriptionSnackbarHost(context).also {
                        host = it
                        it.style = selectedStyle
                    }
                }, modifier = Modifier.fillMaxSize())
                host?.style = selectedStyle
            }
        }
    }
}
