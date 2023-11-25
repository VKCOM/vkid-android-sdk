package com.vk.id.onetap.xml

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import com.vk.id.AccessToken
import com.vk.id.VKIDAuthFail
import com.vk.id.onetap.compose.onetap.sheet.OneTapBottomSheet
import com.vk.id.onetap.compose.onetap.sheet.OneTapBottomSheetState
import com.vk.id.onetap.compose.onetap.sheet.OneTapScenario
import com.vk.id.onetap.compose.onetap.sheet.rememberOneTapBottomSheetState
import com.vk.id.onetap.compose.onetap.sheet.style.OneTapBottomSheetStyle

public class OneTapBottomSheet @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val composeView = ComposeView(context)

    private var onAuth: (AccessToken) -> Unit = {
        throw IllegalStateException("No onAuth callback for VKID OneTap Button. Set it with setCallbacks method.")
    }
    private var onFail: (VKIDAuthFail) -> Unit = {}

    private lateinit var state: OneTapBottomSheetState
    init {
        composeView.setContent {
            Content()
        }
        addView(composeView)
    }

    @Composable
    private fun Content() {
        state = rememberOneTapBottomSheetState()
        OneTapBottomSheet(
            state = state,
            style = OneTapBottomSheetStyle.Light(),
            serviceName = "todo",
            scenario = OneTapScenario.OrderInService,
            onAuth = { onAuth(it) },
            onFail = { onFail(it) },
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
}
