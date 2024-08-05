@file:OptIn(InternalVKIDApi::class)

package com.vk.id.onetap.compose.button.auth

import android.content.res.Resources
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.vk.id.VKIDUser
import com.vk.id.common.InternalVKIDApi
import com.vk.id.onetap.common.auth.style.InternalVKIDButtonStyle
import com.vk.id.onetap.compose.R
import com.vk.id.onetap.compose.button.DURATION_OF_ANIMATION
import com.vk.id.onetap.compose.button.DURATION_OF_DELAY_BETWEEN_FADE_ANIMATIONS
import com.vk.id.onetap.compose.button.FetchUserData
import com.vk.id.onetap.compose.button.OnFetchingProgress
import com.vk.id.onetap.compose.button.SIZE_OF_VK_ICON
import com.vk.id.onetap.compose.button.auth.style.asColorResource
import com.vk.id.onetap.compose.button.auth.style.background
import com.vk.id.onetap.compose.button.auth.style.border
import com.vk.id.onetap.compose.button.clickable
import com.vk.id.onetap.compose.button.easeInOutAnimation
import com.vk.id.onetap.compose.icon.VKIcon
import com.vk.id.onetap.compose.onetap.OneTapTitleScenario
import com.vk.id.onetap.compose.onetap.style.asFontSize
import com.vk.id.onetap.compose.onetap.style.asLineHeight
import com.vk.id.onetap.compose.onetap.style.clip
import com.vk.id.onetap.compose.onetap.style.height
import com.vk.id.onetap.compose.onetap.style.iconPadding
import com.vk.id.onetap.compose.onetap.style.shadow
import com.vk.id.onetap.compose.progress.CircleProgress
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay

@Composable
internal fun VKIDButton(
    modifier: Modifier = Modifier,
    style: InternalVKIDButtonStyle = InternalVKIDButtonStyle.Light(),
    state: VKIDButtonState,
    textProvider: VKIDButtonTextProvider? = null,
    onClick: () -> Unit,
    onUserFetched: (VKIDUser?) -> Unit = {},
    fastAuthEnabled: Boolean,
    largeText: Boolean = true,
    scenario: OneTapTitleScenario,
) {
    val resources = LocalContext.current.resources
    val useTextProvider = textProvider ?: remember { DefaultTextProvider(resources) }
    // Runs only on initial composition
    if (!LocalInspectionMode.current) {
        LaunchedEffect(scenario) {
            state.text = useTextProvider.noUserText(scenario)
            state.shortText = useTextProvider.noUserShortText(scenario)
        }
    } else {
        state.text = useTextProvider.noUserText(scenario)
        state.shortText = useTextProvider.noUserShortText(scenario)
    }
    val coroutineScope = rememberCoroutineScope()
    if (fastAuthEnabled) {
        FetchUserDataWithAnimation(coroutineScope, state, useTextProvider, onUserFetched, scenario)
    }
    Box(
        modifier = modifier
            .shadow(style)
            .height(style.sizeStyle)
            .border(style.borderStyle, style.cornersStyle)
            .clip(style.cornersStyle)
            .clipToBounds()
            .background(style.backgroundStyle)
            .clickable(style, onClick),
    ) {
        // 0.001 and 0.999 because weight can't be null
        @Suppress("MagicNumber")
        val animatedSpaceWeight by animateFloatAsState(
            targetValue = if (state.userLoadFailed) 0.001f else 0.999f,
            label = "iconWeight",
            animationSpec = easeInOutAnimation
        )
        val leftIconWidth: Float = SIZE_OF_VK_ICON + (style.sizeStyle.iconPadding().value * 2)
        val animatedRightIconWidthCompensation by animateFloatAsState(
            targetValue = if (state.userLoadFailed) leftIconWidth / 2 else leftIconWidth,
            label = "rightIconCompensation",
            animationSpec = easeInOutAnimation
        )

        Row(
            Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.weight(1f - animatedSpaceWeight))
            Spacer(modifier = Modifier.width(animatedRightIconWidthCompensation.dp * (1 - animatedSpaceWeight)))
            LeftIconBox(style)
            Spacer(modifier = Modifier.weight(animatedSpaceWeight))
            TextBox(largeText, state, style)
            Spacer(modifier = Modifier.weight(animatedSpaceWeight))
            Spacer(modifier = Modifier.weight(1f - animatedSpaceWeight))
            RightIconBox(state, style, Modifier)
        }
    }
}

@Suppress("NonSkippableComposable")
@Composable
private fun FetchUserDataWithAnimation(
    coroutineScope: CoroutineScope,
    state: VKIDButtonState,
    buttonTextProvider: VKIDButtonTextProvider,
    onUserFetched: (VKIDUser?) -> Unit,
    scenario: OneTapTitleScenario,
) {
    FetchUserData(
        coroutineScope,
        object : OnFetchingProgress {
            override suspend fun onPreFetch() {
                if (state.userIconUrl == null) {
                    state.rightIconVisible = true
                    state.inProgress = true
                }
            }

            override suspend fun onFetched(user: VKIDUser?) {
                // For testing comment all other stuff and uncomment this function
                // foreverAnimationTest(user!!, state, resources)
                onUserFetched(user)
                if (user != null) {
                    val newText = buttonTextProvider.userFoundText(user, scenario)
                    val newShortText = buttonTextProvider.userFoundShortText(user, scenario)
                    val newIconUrl = user.photo200
                    animateFetchedUserIfNeeded(state, newText, newShortText, newIconUrl)
                } else {
                    val newText = buttonTextProvider.noUserText(scenario)
                    val newShortText = buttonTextProvider.noUserShortText(scenario)
                    animateFailedUser(state, newText, newShortText)
                }
            }

            override fun onDispose() {
                state.inProgress = false
            }
        },
        scenario
    )
}

// for testing
@Suppress("unused", "MagicNumber")
private suspend fun foreverAnimationTest(user: VKIDUser, state: VKIDButtonState, resources: Resources) {
    val newText = resources.getString(R.string.vkid_log_in_as, user.firstName)
    val text = resources.getString(R.string.vkid_log_in_with_vkid)
    val newShortText = "Продолжить"
    val shortText = "VK ID"
    val newIconUrl = user.photo200
    fun resetState() {
        state.inProgress = false
        state.userIconUrl = null
        state.text = text
        state.shortText = shortText
        state.userLoadFailed = false
        state.textVisible = true
        state.rightIconVisible = false
    }
    while (true) {
        animateFetchedUserIfNeeded(state, newText, newShortText, newIconUrl)
        delay(2000)
        animateFailedUser(state, text, newShortText)
        delay(2000)
        resetState()
        delay(2000)
        animateFetchedUserIfNeeded(state, newText, newShortText, newIconUrl)
        delay(2000)
        resetState()
        delay(2000)
    }
}

private suspend fun animateFetchedUserIfNeeded(
    state: VKIDButtonState,
    newText: String,
    newShortText: String,
    newIconUrl: String?,
) {
    if (state.text != newText || state.shortText != newShortText || state.userIconUrl != newIconUrl) {
        state.textVisible = false
        state.rightIconVisible = false
        delay(DURATION_OF_ANIMATION)
        // trying to give some time to load image
        state.inProgress = false
        state.userIconUrl = newIconUrl
        delay(DURATION_OF_DELAY_BETWEEN_FADE_ANIMATIONS)
        state.text = newText
        state.shortText = newShortText
        state.userLoadFailed = false
        state.textVisible = true
        state.rightIconVisible = true
    }
}

private suspend fun animateFailedUser(
    state: VKIDButtonState,
    newText: String,
    newShortText: String,
) {
    state.text = newText
    state.shortText = newShortText
    state.rightIconVisible = false
    delay(DURATION_OF_ANIMATION)
    delay(DURATION_OF_DELAY_BETWEEN_FADE_ANIMATIONS)
    state.inProgress = false
    state.userLoadFailed = true
}

@Composable
private fun LeftIconBox(
    style: InternalVKIDButtonStyle
) {
    Box(
        modifier = Modifier
            .iconPadding(style.sizeStyle),
        contentAlignment = Alignment.CenterStart,
    ) {
        VKIcon(style = style.iconStyle)
    }
}

@Composable
private fun TextBox(
    largeText: Boolean,
    state: VKIDButtonState,
    style: InternalVKIDButtonStyle,
) {
    val animatedAlpha by animateFloatAsState(
        targetValue = if (state.textVisible) 1.0f else 0f,
        label = "textAlpha",
        animationSpec = easeInOutAnimation
    )

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .graphicsLayer { this.alpha = animatedAlpha },
        contentAlignment = Alignment.Center
    ) {
        BasicText(
            text = if (largeText) state.text else state.shortText,
            style = TextStyle(
                color = style.textStyle.asColorResource(),
                fontSize = style.sizeStyle.asFontSize(),
                lineHeight = style.sizeStyle.asLineHeight(),
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        )
    }
}

@Composable
private fun RightIconBox(
    state: VKIDButtonState,
    style: InternalVKIDButtonStyle,
    modifier: Modifier,
) {
    val animatedAlpha by animateFloatAsState(
        targetValue = if (state.rightIconVisible) 1.0f else 0f,
        animationSpec = easeInOutAnimation,
        label = "rightIconAlpha"
    )
    Box(
        modifier = modifier
            .iconPadding(style.sizeStyle)
            .graphicsLayer { this.alpha = animatedAlpha },
        contentAlignment = Alignment.CenterEnd
    ) {
        if (state.inProgress) {
            CircleProgress(style.progressStyle)
        } else if (state.userIconUrl != null) {
            AsyncImage(
                model = state.userIconUrl,
                contentDescription = null,
                modifier = Modifier.clip(CircleShape),
                contentScale = ContentScale.FillHeight,
            )
        } else {
            Spacer(modifier = Modifier.size(24.dp))
        }
    }
}

@Preview
@Composable
private fun PreviewVKIDButton() {
    VKIDButton(
        onClick = {},
        state = VKIDButtonState(
            inProgress = false
        ),
        fastAuthEnabled = true,
        scenario = OneTapTitleScenario.SignIn,
    )
}

@Preview
@Composable
private fun PreviewVKIDButtonProgress() {
    VKIDButton(
        onClick = {},
        state = VKIDButtonState(
            inProgress = true,
            rightIconVisible = true,
        ),
        fastAuthEnabled = true,
        scenario = OneTapTitleScenario.SignIn,
    )
}

@Preview
@Composable
private fun PreviewVKIDButtonUserFailed() {
    VKIDButton(
        onClick = {},
        state = VKIDButtonState(
            inProgress = false,
            userLoadFailed = true
        ),
        fastAuthEnabled = true,
        scenario = OneTapTitleScenario.SignIn,
    )
}
