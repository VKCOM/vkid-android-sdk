@file:OptIn(InternalVKIDApi::class)

package com.vk.id.onetap.compose.button

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.vk.id.auth.VKIDAuthCallback
import com.vk.id.auth.VKIDAuthParams
import com.vk.id.common.InternalVKIDApi
import com.vk.id.onetap.common.auth.style.VKIDButtonStyle
import com.vk.id.onetap.compose.button.auth.style.asColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Suppress("LongParameterList")
internal fun Modifier.clickable(
    style: VKIDButtonStyle,
    onClick: () -> Unit
): Modifier = composed {
    clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = rememberRipple(
            color = style.rippleStyle.asColor(),
        ),
        role = Role.Button,
        onClick = onClick
    )
}

internal fun startAuth(
    coroutineScope: CoroutineScope,
    vkid: VKID,
    onAuth: (AccessToken) -> Unit,
    onFail: (VKIDAuthFail) -> Unit,
    params: VKIDAuthParams = VKIDAuthParams {}
) {
    coroutineScope.launch {
        vkid.authorize(
            object : VKIDAuthCallback {
                override fun onSuccess(accessToken: AccessToken) {
                    onAuth(accessToken)
                }

                override fun onFail(fail: VKIDAuthFail) {
                    onFail(fail)
                }
            },
            params
        )
    }
}

@Composable
internal fun FetchUserData(
    coroutineScope: CoroutineScope,
    vkid: VKID,
    onFetchingProgress: OnFetchingProgress,
) {
    val lifecycleState by LocalLifecycleOwner.current.lifecycle.observeAsState()
    DisposableEffect(lifecycleState) {
        var fetchUserJob: Job? = null
        when (lifecycleState) {
            Lifecycle.Event.ON_RESUME -> {
                fetchUserJob = coroutineScope.launch {
                    onFetchingProgress.onPreFetch()
                    val user = vkid.fetchUserData().getOrNull()
                    onFetchingProgress.onFetched(user)
                }
            }

            else -> {}
        }
        onDispose {
            onFetchingProgress.onDispose()
            fetchUserJob?.cancel()
        }
    }
}

@Composable
private fun Lifecycle.observeAsState(): State<Lifecycle.Event> {
    val state = remember { mutableStateOf(Lifecycle.Event.ON_ANY) }
    DisposableEffect(this) {
        val observer = LifecycleEventObserver { _, event ->
            state.value = event
        }
        this@observeAsState.addObserver(observer)
        onDispose {
            this@observeAsState.removeObserver(observer)
        }
    }
    return state
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
