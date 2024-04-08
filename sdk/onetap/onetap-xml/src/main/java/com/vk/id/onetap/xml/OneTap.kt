package com.vk.id.onetap.xml

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.AuthCodeData
import com.vk.id.auth.VKIDAuthUiParams
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

    /**
     * The styling for the One Tap interface, default is [OneTapStyle.Light]
     */
    public var style: OneTapStyle = OneTapStyle.Light()
        set(value) {
            field = value
            onStyleChange(value)
        }
    private var onStyleChange: (OneTapStyle) -> Unit = {}

    /**
     * Flag to enable a button for signing into another account.
     * Note that if text doesn't fit the available width the view will be hidden regardless of the flag.
     */
    public var isSignInToAnotherAccountEnabled: Boolean = false
        set(value) {
            field = value
            onSignInToAnotherAccountEnabledChange(value)
        }
    private var onSignInToAnotherAccountEnabledChange: (Boolean) -> Unit = {}

    /**
     * Optional params to be passed to auth. See [VKIDAuthUiParams.Builder] for more info.
     */
    public var authParams: VKIDAuthUiParams = VKIDAuthUiParams { }
        set(value) {
            field = value
            onAuthParamsChange(value)
        }
    private var onAuthParamsChange: (VKIDAuthUiParams) -> Unit = {}
    private var onAuth: (OneTapOAuth?, AccessToken) -> Unit = { _, _ ->
        throw IllegalStateException("No onAuth callback for VKID OneTap Button. Set it with setCallbacks method.")
    }
    private var onAuthCode: (AuthCodeData, Boolean) -> Unit = { _, _ -> }
    private var onFail: (OneTapOAuth?, VKIDAuthFail) -> Unit = { _, _ -> }

    /**
     * Adds support multibranding auth. By default this widget is not displayed.
     * This property represents a set of [OneTapOAuth] to be displayed.
     */
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
        clipChildren = false
        clipToPadding = false
    }

    @Suppress("NonSkippableComposable")
    @Composable
    private fun Content() {
        var style by remember { mutableStateOf(style) }
        onStyleChange = { style = it }
        var isSignInToAnotherAccountEnabled by remember { mutableStateOf(isSignInToAnotherAccountEnabled) }
        onSignInToAnotherAccountEnabledChange = { isSignInToAnotherAccountEnabled = it }
        var authParams by remember { mutableStateOf(authParams) }
        onAuthParamsChange = { authParams = it }
        var oAuths by remember { mutableStateOf(oAuths) }
        onOAuthsChange = { oAuths = it }
        var vkid by remember { mutableStateOf(vkid) }
        onVKIDChange = { vkid = it }
        OneTap(
            modifier = Modifier,
            style = style,
            onAuth = { oAuth, accessToken -> onAuth(oAuth, accessToken) },
            onAuthCode = { data, isCompletion -> onAuthCode(data, isCompletion) },
            onFail = { oAuth, fail -> onFail(oAuth, fail) },
            oAuths = oAuths,
            signInAnotherAccountButtonEnabled = isSignInToAnotherAccountEnabled,
            vkid = vkid,
            authParams = authParams,
        )
    }

    /**
     * Callbacks that provide auth result for version with multibranding.
     *
     * @param onAuth A callback to be invoked upon a successful auth.
     * @param onAuthCode A callback to be invoked upon successful first step of auth - receiving auth code
     * which can later be exchanged to access token.
     * isCompletion is true if [onSuccess] won't be called.
     * This will happen if you passed auth parameters and implement their validation yourself.
     * In that case we can't exchange auth code for access token and you should do this yourself.
     * @param onFail A callback to be invoked upon an error during auth.
     */
    public fun setCallbacks(
        onAuth: (oAuth: OneTapOAuth?, accessToken: AccessToken) -> Unit,
        onFail: (oAuth: OneTapOAuth?, fail: VKIDAuthFail) -> Unit = { _, _ -> },
        onAuthCode: (data: AuthCodeData, isCompletion: Boolean) -> Unit = { _, _ -> },
    ) {
        this.onAuth = onAuth
        this.onAuthCode = onAuthCode
        this.onFail = onFail
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
