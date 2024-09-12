@file:OptIn(InternalVKIDApi::class)

package com.vk.id.onetap.compose.button

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.semantics.Role
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.VKIDUser
import com.vk.id.auth.AuthCodeData
import com.vk.id.auth.VKIDAuthCallback
import com.vk.id.auth.VKIDAuthParams
import com.vk.id.common.InternalVKIDApi
import com.vk.id.onetap.common.auth.style.InternalVKIDButtonRippleStyle
import com.vk.id.onetap.compose.button.auth.style.asColor
import com.vk.id.onetap.compose.onetap.OneTapTitleScenario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal fun Modifier.clickable(
    onClick: () -> Unit
): Modifier = composed {
    clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = ripple(
            color = InternalVKIDButtonRippleStyle.DARK.asColor(),
        ),
        role = Role.Button,
        onClick = onClick
    )
}

internal fun startAuth(
    coroutineScope: CoroutineScope,
    onAuth: (AccessToken) -> Unit,
    onAuthCode: (AuthCodeData, Boolean) -> Unit,
    onFail: (VKIDAuthFail) -> Unit,
    params: VKIDAuthParams.Builder = VKIDAuthParams.Builder()
) {
    params.internalUse = true
    coroutineScope.launch {
        VKID.instance.authorize(
            object : VKIDAuthCallback {
                override fun onAuth(accessToken: AccessToken) = onAuth(accessToken)
                override fun onAuthCode(data: AuthCodeData, isCompletion: Boolean) = onAuthCode(data, isCompletion)
                override fun onFail(fail: VKIDAuthFail) = onFail(fail)
            },
            params.build()
        )
    }
}

@Composable
internal fun FetchUserData(
    coroutineScope: CoroutineScope,
    onFetchingProgress: OnFetchingProgress,
    scenario: OneTapTitleScenario?,
) {
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
    DisposableEffect(lifecycleOwner.value, scenario) {
        var fetchUserJob: Job? = null
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    fetchUserJob = coroutineScope.launch {
                        onFetchingProgress.onPreFetch()
                        val user = VKID.instance.fetchUserData().getOrNull()
                        onFetchingProgress.onFetched(user)
                    }
                }
                else -> {}
            }
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
            fetchUserJob?.cancel()
            onFetchingProgress.onDispose()
        }
    }
}

internal interface OnFetchingProgress {
    suspend fun onPreFetch()
    suspend fun onFetched(user: VKIDUser?)
    fun onDispose()
}

internal const val DURATION_OF_ANIMATION = 300L

internal const val DURATION_OF_DELAY_BETWEEN_FADE_ANIMATIONS = 100L

internal val easeInOutAnimation: AnimationSpec<Float> =
    tween(durationMillis = DURATION_OF_ANIMATION.toInt(), easing = CubicBezierEasing(0.42f, 0.0f, 0.58f, 1.0f))

internal const val SIZE_OF_VK_ICON = 28f
