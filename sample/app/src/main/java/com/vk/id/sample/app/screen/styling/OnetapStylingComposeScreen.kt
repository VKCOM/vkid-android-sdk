package com.vk.id.sample.app.screen.styling

import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.SnackbarHostState
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
import com.vk.id.auth.AuthCodeData
import com.vk.id.auth.VKIDAuthUiParams
import com.vk.id.group.subscription.compose.ui.GroupSubscriptionSnackbarHost
import com.vk.id.group.subscription.xml.GroupSubscriptionSnackbarHost
import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.onetap.common.OneTapStyle
import com.vk.id.onetap.common.button.style.OneTapButtonCornersStyle
import com.vk.id.onetap.common.button.style.OneTapButtonElevationStyle
import com.vk.id.onetap.common.button.style.OneTapButtonSizeStyle
import com.vk.id.onetap.compose.onetap.OneTap
import com.vk.id.onetap.compose.onetap.OneTapTitleScenario
import com.vk.id.onetap.xml.OneTap
import com.vk.id.sample.app.screen.UseToken
import com.vk.id.sample.app.uikit.selector.CheckboxSelector
import com.vk.id.sample.app.uikit.selector.DropdownSelector
import com.vk.id.sample.app.uikit.selector.EnumStateCheckboxSelector
import com.vk.id.sample.app.uikit.selector.SliderSelector
import com.vk.id.sample.app.uikit.selector.styleConstructors
import com.vk.id.sample.app.uikit.theme.AppTheme
import com.vk.id.sample.app.util.carrying.carry
import com.vk.id.sample.xml.uikit.common.dpToPixels
import com.vk.id.sample.xml.uikit.common.getOneTapFailCallback
import com.vk.id.sample.xml.uikit.common.getOneTapSuccessCallback
import com.vk.id.sample.xml.uikit.common.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.reflect.KCallable

private const val TOTAL_WIDTH_PADDING_DP = 16
private const val MIN_WIDTH_DP = 48f
private const val MAX_RADIUS_DP = 30
private const val MAX_ELEVATION_DP = 20

@Preview
@Suppress("LongMethod", "CyclomaticComplexMethod")
@Composable
internal fun OnetapStylingComposeScreen() {
    val token = remember { mutableStateOf<AccessToken?>(null) }
    var code by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    val screenWidth = LocalConfiguration.current.screenWidthDp - TOTAL_WIDTH_PADDING_DP

    val widthPercent = remember { mutableFloatStateOf(1f) }
    val cornersStylePercent = remember { mutableFloatStateOf(0f) }
    val selectedSize = remember { mutableStateOf(OneTapButtonSizeStyle.DEFAULT) }
    val selectedElevationStyle = remember { mutableFloatStateOf(0f) }
    val selectedOAuths = remember { mutableStateOf(emptySet<OneTapOAuth>()) }
    val styleConstructor = remember<MutableState<KCallable<OneTapStyle>?>> { mutableStateOf(null) }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            styleConstructor.value = OneTapStyle.Companion::system.carry(context)
        }
    }
    val shouldUseXml = remember { mutableStateOf(false) }
    val signInToAnotherAccountEnabled = remember { mutableStateOf(false) }
    val fastAuthEnabled = remember { mutableStateOf(true) }
    var scopes by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var codeChallenge by remember { mutableStateOf("") }
    var selectedStyle by remember { mutableStateOf(OneTapStyle.system(context)) }
    val groupSubscription = remember { mutableStateOf(false) }
    LaunchedEffect(
        styleConstructor.value to cornersStylePercent.floatValue,
        selectedSize.value to selectedElevationStyle.floatValue
    ) {
        withContext(Dispatchers.IO) {
            styleConstructor.value?.let {
                selectedStyle = it.call(
                    OneTapButtonCornersStyle.Custom(MAX_RADIUS_DP * cornersStylePercent.floatValue),
                    selectedSize.value,
                    OneTapButtonElevationStyle.Custom(MAX_ELEVATION_DP * selectedElevationStyle.floatValue),
                )
            }
        }
    }

    var styleConstructors by remember { mutableStateOf<Map<String, KCallable<OneTapStyle>>>(emptyMap()) }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            styleConstructors = OneTapStyle::class.styleConstructors(context)
        }
    }

    val useDarkTheme = selectedStyle is OneTapStyle.Dark ||
        selectedStyle is OneTapStyle.TransparentDark

    var scenario by remember { mutableStateOf(OneTapTitleScenario.SignIn) }
    AppTheme(
        useDarkTheme = useDarkTheme
    ) {
        val snackbarHostState = remember { SnackbarHostState() }
        Box {
            var host: GroupSubscriptionSnackbarHost? by remember { mutableStateOf(null) }
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 8.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
            ) {
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
                        @Composable
                        fun OneTapAndroidView(fastAuthEnabled: Boolean) {
                            var oneTapView: OneTap? by remember { mutableStateOf(null) }
                            AndroidView(factory = { context ->
                                OneTap(context).apply {
                                    setCallbacks(
                                        onAuth = getOneTapSuccessCallback(context) { token.value = it },
                                        onAuthCode = onAuthCode,
                                        onFail = getOneTapFailCallback(context),
                                    )
                                    oneTapView = this
                                    this.fastAuthEnabled = fastAuthEnabled
                                }
                            })
                            oneTapView?.apply {
                                this.layoutParams = LayoutParams(
                                    if (selectedStyle is OneTapStyle.Icon) WRAP_CONTENT else context.dpToPixels(width.toInt()),
                                    WRAP_CONTENT,
                                )
                                this.style = selectedStyle
                                this.oAuths = selectedOAuths.value
                                this.isSignInToAnotherAccountEnabled = signInToAnotherAccountEnabled.value
                                this.authParams = authParams
                                this.scenario = scenario
                                this.groupId = "1".takeIf { groupSubscription.value }
                                this.snackbarHost = host
                                this.setGroupSubscriptionCallbacks(
                                    onSuccess = { showToast(context, "Subscribed") },
                                    onFail = { showToast(context, "Fail: ${it.description}") },
                                )
                            }
                        }
                        if (fastAuthEnabled.value) {
                            OneTapAndroidView(fastAuthEnabled = true)
                        } else {
                            OneTapAndroidView(fastAuthEnabled = false)
                        }
                    } else {
                        // Force state drop when changing the parameter
                        @Composable
                        fun RenderOneTap(fastAuthEnabled: Boolean) {
                            if (groupSubscription.value) {
                                OneTap(
                                    modifier = Modifier.width(width.dp),
                                    style = selectedStyle,
                                    onAuth = getOneTapSuccessCallback(context) { token.value = it },
                                    onAuthCode = onAuthCode,
                                    onFail = getOneTapFailCallback(context),
                                    oAuths = selectedOAuths.value,
                                    signInAnotherAccountButtonEnabled = signInToAnotherAccountEnabled.value,
                                    authParams = authParams,
                                    fastAuthEnabled = fastAuthEnabled,
                                    scenario = scenario,
                                    subscribeToGroupId = "1",
                                    onSuccessSubscribingToGroup = { showToast(context, "Subscribed") },
                                    onFailSubscribingToGroup = { showToast(context, "Fail: ${it.description}") },
                                    groupSubscriptionSnackbarHostState = snackbarHostState,
                                )
                            } else {
                                OneTap(
                                    modifier = Modifier.width(width.dp),
                                    style = selectedStyle,
                                    onAuth = getOneTapSuccessCallback(context) { token.value = it },
                                    onAuthCode = onAuthCode,
                                    onFail = getOneTapFailCallback(context),
                                    oAuths = selectedOAuths.value,
                                    signInAnotherAccountButtonEnabled = signInToAnotherAccountEnabled.value,
                                    authParams = authParams,
                                    fastAuthEnabled = fastAuthEnabled,
                                    scenario = scenario,
                                )
                            }
                        }
                        if (fastAuthEnabled.value) {
                            RenderOneTap(true)
                        } else {
                            RenderOneTap(false)
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
                CheckboxSelector(
                    title = "Change account",
                    isChecked = signInToAnotherAccountEnabled.value,
                    onCheckedChange = { signInToAnotherAccountEnabled.value = it }
                )
                CheckboxSelector(
                    title = "Fetch user",
                    isChecked = fastAuthEnabled.value,
                    onCheckedChange = { fastAuthEnabled.value = it }
                )
                CheckboxSelector(
                    title = "Group Subscription",
                    isChecked = groupSubscription.value,
                    onCheckedChange = { groupSubscription.value = it }
                )
                EnumStateCheckboxSelector(state = selectedOAuths, onNewState = { selectedOAuths.value = it })
                DropdownSelector(
                    modifier = Modifier.padding(vertical = 16.dp),
                    values = styleConstructors,
                    selectedValue = selectedStyle::class.simpleName ?: error("Can get simple style"),
                    onValueSelected = {
                        styleConstructor.value = it
                    },
                    label = { Text("style") },
                )
                SliderSelector(title = "Width", selectedState = widthPercent, onStateChange = { widthPercent.floatValue = it })
                SliderSelector(title = "Corners", selectedState = cornersStylePercent, onStateChange = { cornersStylePercent.floatValue = it })
                SliderSelector(
                    title = "Elevation",
                    selectedState = selectedElevationStyle,
                    onStateChange = { selectedElevationStyle.floatValue = it }
                )
                DropdownSelector(
                    values = OneTapButtonSizeStyle.entries.associateBy { it.name },
                    selectedValue = selectedSize.value.name,
                    onValueSelected = { selectedSize.value = it },
                    label = { Text("size") },
                )
                DropdownSelector(
                    values = OneTapTitleScenario.entries.associateBy { it.name },
                    selectedValue = scenario.name,
                    onValueSelected = { scenario = it },
                    label = { Text("scenario") },
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
            GroupSubscriptionSnackbarHost(snackbarHostState = snackbarHostState)
            AndroidView({ context ->
                GroupSubscriptionSnackbarHost(context).also { host = it }
            }, modifier = Modifier.fillMaxSize())
        }
    }
}
