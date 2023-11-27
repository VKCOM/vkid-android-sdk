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

    private lateinit var state: OneTapBottomSheetState
    init {
        val sheetSettings = parseOneTapBottomSheetAttrs(context, attrs)
        composeView.setContent { Content(sheetSettings) }
        addView(composeView)
    }

    @Composable
    private fun Content(sheetSettings: OneTapBottomSheetAttributeSettings) {
        state = rememberOneTapBottomSheetState()
        OneTapBottomSheet(
            state = state,
            style = sheetSettings.style,
            serviceName = sheetSettings.serviceName,
            scenario = sheetSettings.scenario,
            onAuth = { onAuth(it) },
            onFail = { onFail(it) },
            autoHideOnSuccess = sheetSettings.autoHideOnSuccess
        )
    }

    public fun setCallbacks(
        onAuth: (AccessToken) -> Unit,
        onFail: (VKIDAuthFail) -> Unit = {},
    ) {
        this.onAuth = onAuth
        this.onFail = onFail
    }

    public fun show() {
        state.show()
    }

    public fun hide() {
        state.hide()
    }

    public fun isVisible(): Boolean = state.isVisible

    @Suppress("EmptyFunctionBlock")
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}
}
