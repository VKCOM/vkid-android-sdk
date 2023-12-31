package com.vk.id.onetap.xml

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.onetap.common.OneTapStyle
import com.vk.id.onetap.compose.onetap.OneTap

/**
 * OneTap is a view that provides VKID One Tap login interface.
 * For more information how to integrate VK ID Authentication check docs https://id.vk.com/business/go/docs/ru/vkid/latest/vk-id/intro/plan
 *
 * You should [setCallbacks] on init view to get token after successful auth.
 */
public class OneTap @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {

    private val composeView = ComposeView(context)

    public var style: OneTapStyle = OneTapStyle.Dark()
        set(value) {
            field = value
            onStyleChange(value)
        }
    private var onStyleChange: (OneTapStyle) -> Unit = {}
    public var isSignInToAnotherAccountEnabled: Boolean = false
        set(value) {
            field = value
            onSignInToAnotherAccountEnabled(value)
        }
    private var onSignInToAnotherAccountEnabled: (Boolean) -> Unit = {}
    private var onAuth: (OneTapOAuth?, AccessToken) -> Unit = { _, _ ->
        throw IllegalStateException("No onAuth callback for VKID OneTap Button. Set it with setCallbacks method.")
    }
    private var onFail: (OneTapOAuth?, VKIDAuthFail) -> Unit = { _, _ -> }
    public var oAuths: Set<OneTapOAuth> = emptySet()
        set(value) {
            field = value
            onOAuthsChange(value)
        }
    private var onOAuthsChange: (Set<OneTapOAuth>) -> Unit = {}
    private var vkid: VKID? = null
        set(value) {
            field = value
            onVKIDChange(value)
        }
    private var onVKIDChange: (VKID?) -> Unit = {}

    init {
        val (style, isSignInToAnotherAccountEnabled, oAuths) = parseOneTapAttrs(context, attrs)
        this.style = style
        this.isSignInToAnotherAccountEnabled = isSignInToAnotherAccountEnabled
        this.oAuths = oAuths
        addView(composeView)
        composeView.setContent { Content() }
    }

    @Composable
    private fun Content() {
        val style = remember { mutableStateOf(style) }
        onStyleChange = { style.value = it }
        val isSignInToAnotherAccountEnabled = remember { mutableStateOf(isSignInToAnotherAccountEnabled) }
        onSignInToAnotherAccountEnabled = { isSignInToAnotherAccountEnabled.value = it }
        val oAuths = remember { mutableStateOf(oAuths) }
        onOAuthsChange = { oAuths.value = it }
        val vkid = remember { mutableStateOf(vkid) }
        onVKIDChange = { vkid.value = it }
        OneTap(
            modifier = Modifier,
            style = style.value,
            onAuth = { oAuth, accessToken -> onAuth(oAuth, accessToken) },
            onFail = { oAuth, fail -> onFail(oAuth, fail) },
            oAuths = oAuths.value,
            signInAnotherAccountButtonEnabled = isSignInToAnotherAccountEnabled.value,
            vkid = vkid.value,
        )
    }

    /**
     * Callbacks that provide auth result for version with multibranding.
     */
    public fun setCallbacks(
        onAuth: (OneTapOAuth?, AccessToken) -> Unit,
        onFail: (OneTapOAuth?, VKIDAuthFail) -> Unit = { _, _ -> },
    ) {
        this.onAuth = onAuth
        this.onFail = onFail
    }

    /**
     * Callbacks that provide auth result.
     */
    public fun setCallbacks(
        onAuth: (AccessToken) -> Unit,
        onFail: (VKIDAuthFail) -> Unit = {},
    ) {
        this.onAuth = { _, token -> onAuth(token) }
        this.onFail = { _, fail -> onFail(fail) }
    }

    /**
     * Set an optional [VKID] instance to use for authentication.
     *  If instance of VKID is not provided, it will be created.
     *  Note that you can't change the [VKID] instance after view was added to layout.
     */
    public fun setVKID(
        vkid: VKID,
    ) {
        check(!composeView.isAttachedToWindow) { "You're not allowed to change VKID instance after it was attached" }
        this.vkid = vkid
    }
}
