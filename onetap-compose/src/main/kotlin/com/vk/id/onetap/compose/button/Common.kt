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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.onetap.common.button.VKIDButtonStyle
import com.vk.id.onetap.compose.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Suppress("LongParameterList")
internal fun Modifier.clickable(
    state: VKIDButtonState,
    coroutineScope: CoroutineScope,
    vkid: VKID,
    style: VKIDButtonStyle,
    onAuth: (AccessToken) -> Unit,
    onFail: (VKIDAuthFail) -> Unit
): Modifier = composed {
    clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = rememberRipple(
            color = style.rippleStyle.asColor(),
            radius = style.cornersStyle.radiusDp.dp
        ),
        enabled = state.inProgress.not(),
        role = Role.Button,
        onClick = { startAuth(coroutineScope, state, vkid, onAuth, onFail) }
    )
}

internal fun startAuth(
    coroutineScope: CoroutineScope,
    state: VKIDButtonState,
    vkid: VKID,
    onAuth: (AccessToken) -> Unit,
    onFail: (VKIDAuthFail) -> Unit
) {
    coroutineScope.launch {
        state.inProgress = true
        vkid.authorize(object : VKID.AuthCallback {
            override fun onSuccess(accessToken: AccessToken) {
                state.inProgress = false
                onAuth(accessToken)
            }

            override fun onFail(fail: VKIDAuthFail) {
                state.inProgress = false
                onFail(fail)
            }
        })
    }
}

@Composable
internal fun FetchUserData(
    coroutineScope: CoroutineScope,
    vkid: VKID,
    onFetchingProgress: OnFetchingProgress,
) {
    val resources = LocalContext.current.resources
    val lifecycleState by LocalLifecycleOwner.current.lifecycle.observeAsState()
    DisposableEffect(lifecycleState) {
        var fetchUserJob: Job? = null
        when (lifecycleState) {
            Lifecycle.Event.ON_RESUME -> {
                fetchUserJob = coroutineScope.launch {
                    onFetchingProgress.onPreFetch()
                    val user = vkid.fetchUserData().getOrNull()
                    val newText: String
                    val newIconUrl: String?
                    if (user != null) {
                        newText = resources.getString(R.string.vkid_log_in_as, user.firstName)
                        newIconUrl = user.photo200
                    } else {
                        newText = resources.getString(R.string.vkid_log_in_with_vkid)
                        newIconUrl = null
                    }
                    onFetchingProgress.onFetched(newText, newIconUrl)
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
    suspend fun onFetched(newText: String, newIconUrl: String?)
    fun onDispose()
}

internal const val DURATION_OF_ANIMATION = 300

// common delay is duration of fade out animation + 100 delay between fade out and fade in
internal const val DURATION_OF_DELAY_BETWEEN_FADE_ANIMATIONS = DURATION_OF_ANIMATION + 100L

internal val easeInOutAnimation: AnimationSpec<Float> =
    tween(durationMillis = DURATION_OF_ANIMATION, easing = CubicBezierEasing(0.42f, 0.0f, 0.58f, 1.0f))
