package com.vk.id.onetap.xml

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import com.vk.id.AccessToken
import com.vk.id.VKIDAuthFail
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
) : ViewGroup(context, attrs, defStyleAttr) {

    private val composeView = ComposeView(context)

    private var onAuth: (AccessToken) -> Unit = {
        throw IllegalStateException("No onAuth callback for VKID OneTap Button. Set it with setCallbacks method.")
    }
    private var onFail: (VKIDAuthFail) -> Unit = {}

    private var state: OneTapBottomSheetState? = null
    init {
        val sheetSettings = parseOneTapBottomSheetAttrs(context, attrs)
        composeView.setContent {
            Content(sheetSettings)
        }
        addView(composeView)
    }

    @Composable
    private fun Content(sheetSettings: OneTapBottomSheetAttributeSettings) {
        OneTapBottomSheet(
            state = rememberOneTapBottomSheetState().also {
                state = it
            },
            style = sheetSettings.style,
            serviceName = sheetSettings.serviceName,
            scenario = sheetSettings.scenario,
            onAuth = { onAuth(it) },
            onFail = { onFail(it) },
            autoHideOnSuccess = sheetSettings.autoHideOnSuccess
        )
    }

    /**
     * Callbacks that provides auth result.
     */
    public fun setCallbacks(
        onAuth: (AccessToken) -> Unit,
        onFail: (VKIDAuthFail) -> Unit = {},
    ) {
        this.onAuth = onAuth
        this.onFail = onFail
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

    @Suppress("EmptyFunctionBlock")
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}
}
