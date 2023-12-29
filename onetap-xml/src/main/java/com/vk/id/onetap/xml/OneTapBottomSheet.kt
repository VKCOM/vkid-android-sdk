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

    public var oAuths: Set<OneTapOAuth> = emptySet()
        set(value) {
            field = value
            onOAuthsChange(value)
        }
    private var onOAuthsChange: (Set<OneTapOAuth>) -> Unit = {}
    private var onAuth: (OneTapOAuth?, AccessToken) -> Unit = { _, _ ->
        throw IllegalStateException("No onAuth callback for VKID OneTap Button. Set it with setCallbacks method.")
    }
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

    @Composable
    private fun Content(sheetSettings: OneTapBottomSheetAttributeSettings) {
        val vkid = remember { mutableStateOf(vkid) }
        onVKIDChange = { vkid.value = it }
        val oAuths = remember { mutableStateOf(oAuths) }
        onOAuthsChange = { oAuths.value = it }
        OneTapBottomSheet(
            state = rememberOneTapBottomSheetState().also {
                state = it
            },
            style = sheetSettings.style,
            serviceName = sheetSettings.serviceName,
            scenario = sheetSettings.scenario,
            onAuth = { oAuth, accessToken -> onAuth(oAuth, accessToken) },
            onFail = { oAuth, fail -> onFail(oAuth, fail) },
            autoHideOnSuccess = sheetSettings.autoHideOnSuccess,
            oAuths = oAuths.value,
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
     */
    public fun setVKID(
        vkid: VKID
    ) {
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

    public fun isVisible(): Boolean = state?.isVisible ?: false
}
