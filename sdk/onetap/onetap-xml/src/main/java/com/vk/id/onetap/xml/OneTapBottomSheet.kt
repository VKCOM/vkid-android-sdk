package com.vk.id.onetap.xml

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.AuthCodeData
import com.vk.id.auth.VKIDAuthUiParams
import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.onetap.compose.onetap.sheet.OneTapBottomSheet
import com.vk.id.onetap.compose.onetap.sheet.OneTapBottomSheetState
import com.vk.id.onetap.compose.onetap.sheet.rememberOneTapBottomSheetState

/**
 * OneTap is a modal bottom sheet that provides VKID One Tap login interface.
 * For more information how to integrate VK ID Authentication check docs https://id.vk.com/business/go/docs/ru/vkid/latest/vk-id/intro/plan
 *
 * You should [setCallbacks] on init view to get token after successful auth.
 *
 * To show or hide bottom sheet call [show] and [hide] methods.
 */
public class OneTapBottomSheet @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val composeView = ComposeView(context)

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
    private var onAuthCode: (AuthCodeData) -> Unit = {}
    private var onFail: (OneTapOAuth?, VKIDAuthFail) -> Unit = { _, _ -> }
    private var vkid: VKID? = null
        set(value) {
            field = value
            onVKIDChange(value)
        }
    private var onVKIDChange: (VKID?) -> Unit = {}

    private var state: OneTapBottomSheetState? = null

    init {
        val sheetSettings = parseOneTapBottomSheetAttrs(context, attrs)
        this.oAuths = sheetSettings.oAuths
        composeView.setContent {
            Content(sheetSettings)
        }
        addView(composeView)
    }

    @Suppress("NonSkippableComposable")
    @Composable
    private fun Content(sheetSettings: OneTapBottomSheetAttributeSettings) {
        val vkid = remember { mutableStateOf(vkid) }
        onVKIDChange = { vkid.value = it }
        val oAuths = remember { mutableStateOf(oAuths) }
        onOAuthsChange = { oAuths.value = it }
        val authParams = remember { mutableStateOf(authParams) }
        onAuthParamsChange = { authParams.value = it }
        OneTapBottomSheet(
            state = rememberOneTapBottomSheetState().also {
                state = it
            },
            style = sheetSettings.style,
            serviceName = sheetSettings.serviceName,
            scenario = sheetSettings.scenario,
            onAuth = { oAuth, accessToken -> onAuth(oAuth, accessToken) },
            onAuthCode = { onAuthCode(it) },
            onFail = { oAuth, fail -> onFail(oAuth, fail) },
            autoHideOnSuccess = sheetSettings.autoHideOnSuccess,
            oAuths = oAuths.value,
            vkid = vkid.value,
            authParams = authParams.value
        )
    }

    /**
     * Callbacks that provide auth result for version with multibranding.
     *
     * @param onAuth A callback to be invoked upon a successful auth.
     * @param onAuthCode A callback to be invoked upon successful first step of auth - receiving auth code
     * which can later be exchanged to access token.
     * @param onFail A callback to be invoked upon an error during auth.
     */
    public fun setCallbacks(
        onAuth: (OneTapOAuth?, AccessToken) -> Unit,
        onAuthCode: (AuthCodeData) -> Unit = {},
        onFail: (OneTapOAuth?, VKIDAuthFail) -> Unit = { _, _ -> },
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
        vkid: VKID
    ) {
        check(!composeView.isAttachedToWindow) { "You're not allowed to change VKID instance after it was attached" }
        this.vkid = vkid
    }

    /**
     * Expand bottom sheet with animation
     */
    public fun show() {
        state?.show()
    }

    /**
     * Hide bottom sheet with animation
     */
    public fun hide() {
        state?.hide()
    }

    /**
     * Whether the bottom sheet is visible.
     */
    public fun isVisible(): Boolean = state?.isVisible ?: false
}
