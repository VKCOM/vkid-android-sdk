package com.vk.id.onetap.compose.onetap.sheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.onetap.compose.OneTap
import com.vk.id.onetap.compose.R
import kotlinx.coroutines.launch

@Composable
public fun OneTapBottomSheet(
    modifier: Modifier = Modifier,
    state: OneTapBottomSheetState = rememberOneTapBottomSheetState(),
    serviceName: String,
    scenario: OneTapScenario = OneTapScenario.EnterService,
    onAuth: (AccessToken) -> Unit,
    onFail: (VKIDAuthFail) -> Unit = {},
    vkid: VKID? = null
) {
    val context = LocalContext.current
    val useVKID = vkid ?: remember {
        VKID(context)
    }
    OneTapBottomSheetInternal(modifier, state, serviceName, scenario, onAuth, onFail, useVKID)
}

@Suppress("LongParameterList")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OneTapBottomSheetInternal(
    modifier: Modifier = Modifier,
    state: OneTapBottomSheetState,
    serviceName: String,
    scenario: OneTapScenario,
    onAuth: (AccessToken) -> Unit,
    onFail: (VKIDAuthFail) -> Unit,
    vkid: VKID
) {
    var showBottomSheet: Boolean by rememberSaveable {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()
    state.showSheet = remember {
        { show ->
            if (show) {
                showBottomSheet = true
            } else {
                coroutineScope.launch {
                    state.materialSheetState.hide()
                }.invokeOnCompletion {
                    if (!state.isVisible) {
                        showBottomSheet = false
                    }
                }
            }
        }
    }
    if (showBottomSheet) {
        ModalBottomSheet(
            modifier = modifier,
            onDismissRequest = {
                state.hide()
            },
            sheetState = state.materialSheetState,
            containerColor = Color.Transparent,
            dragHandle = null
        ) {
            OneTapBottomSheetContent(
                vkid,
                onAuth,
                onFail,
                serviceName,
                scenario,
                dismissSheet = {
                    state.hide()
                }
            )
        }
    }
}

@Composable
public fun rememberOneTapBottomSheetState(): OneTapBottomSheetState {
    return rememberOneTapBottomSheetStateInternal()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun rememberOneTapBottomSheetStateInternal(): OneTapBottomSheetState {
    val materialSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    return remember(materialSheetState) {
        OneTapBottomSheetState(
            materialSheetState = materialSheetState
        )
    }
}

public class OneTapBottomSheetState
@OptIn(ExperimentalMaterial3Api::class)
internal constructor(
    internal val materialSheetState: SheetState
) {
    internal var showSheet: (Boolean) -> Unit = {}
    public fun show() {
        showSheet(true)
    }

    public fun hide() {
        showSheet(false)
    }

    public val isVisible: Boolean
        get() {
            @OptIn(ExperimentalMaterial3Api::class)
            return materialSheetState.isVisible
        }
}

@Suppress("LongParameterList")
@Composable
internal fun OneTapBottomSheetContent(
    vkid: VKID,
    onAuth: (AccessToken) -> Unit,
    onFail: (VKIDAuthFail) -> Unit,
    serviceName: String,
    scenario: OneTapScenario,
    dismissSheet: () -> Unit
) {
    Box(
        modifier = Modifier
            .safeDrawingPadding()
            .wrapContentHeight()
            .fillMaxWidth()
            .widthIn(min = 344.dp, max = 800.dp)
            .padding(8.dp)
            .clip(RoundedCornerShape(12.0.dp))
            .background(Color.White),
        contentAlignment = Alignment.Center,
    ) {
        val resources = LocalContext.current.resources
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                VkidIcon()
                val titleTextStyle = TextStyle(
                    color = colorResource(R.color.vkid_ui_light_text_secondary),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.W400,
                    letterSpacing = 0.2.sp,
                    lineHeight = 16.sp
                )
                Dot(titleTextStyle)
                ServiceTitle(titleTextStyle, serviceName)
                Spacer(Modifier.weight(1f))
                CloseIcon(dismissSheet)
            }
            Column(
                Modifier.padding(horizontal = 32.dp, vertical = 36.dp)
            ) {
                val title = remember(scenario) {
                    scenario.scenarioTitle(serviceName = serviceName, resources = resources)
                }
                ContentTitle(title)
                Spacer(Modifier.height(8.dp))
                ContentDescription()
            }
            OneTap(
                signInAnotherAccountButtonEnabled = true,
                vkid = vkid,
                onAuth = onAuth,
                onFail = onFail,
                vkidButtonTextProvider = remember(scenario) { scenario.vkidButtonTextProvider(resources) }
            )
        }
    }
}

@Composable
private fun VkidIcon() {
    Image(
        painter = painterResource(R.drawable.vkid_onetap_bottomsheet_logo),
        contentDescription = null,
        contentScale = ContentScale.Fit,
    )
}

@Composable
private fun ServiceTitle(style: TextStyle, serviceName: String) {
    BasicText(text = serviceName, style = style)
}

@Composable
private fun Dot(style: TextStyle) {
    BasicText(
        modifier = Modifier.padding(horizontal = 4.dp, vertical = 0.dp),
        text = "·",
        style = style
    )
}

@Composable
private fun CloseIcon(dismissSheet: () -> Unit) {
    Box(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    dismissSheet()
                }
            )
    ) {
        Image(
            painter = painterResource(R.drawable.vkid_onetap_bottomsheet_close),
            contentDescription = null,
            contentScale = ContentScale.Fit,
        )
    }
}

@Composable
private fun ContentTitle(text: String) {
    BasicText(
        text = text,
        style = TextStyle(
            color = colorResource(id = R.color.vkid_ui_light_text_primary),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.W500,
            lineHeight = 24.sp,
        )
    )
}

@Composable
private fun ContentDescription() {
    BasicText(
        text = "После этого вам станут доступны все возможности сервиса. Ваши данные будут надёжно защищены.",
        style = TextStyle(
            color = colorResource(id = R.color.vkid_ui_light_text_secondary),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontWeight = FontWeight.W400,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp
        )
    )
}

@Preview
@Composable
private fun OneTapBottomSheetPreview() {
    val context = LocalContext.current
    val vkid = remember {
        VKID(context)
    }
    OneTapBottomSheetContent(
        vkid,
        onAuth = {},
        onFail = {},
        "<Название сервиса>",
        OneTapScenario.EnterService,
        dismissSheet = {}
    )
}
