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
import com.vk.id.VKIDAuthFail
import com.vk.id.onetap.common.OneTapStyle
import com.vk.id.onetap.compose.onetap.OneTap

public class OneTap @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
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
    private var onAuth: (AccessToken) -> Unit = {
        throw IllegalStateException("No onAuth callback for VKID OneTap Button. Set it with setCallbacks method.")
    }
    private var onFail: (VKIDAuthFail) -> Unit = {}

    init {
        val (style, isSignInToAnotherAccountEnabled) = parseAttrs(context, attrs)
        this.style = style
        this.isSignInToAnotherAccountEnabled = isSignInToAnotherAccountEnabled
        addView(composeView)
        composeView.setContent { Content() }
    }

    @Composable
    private fun Content() {
        val style = remember { mutableStateOf(style) }
        onStyleChange = { style.value = it }
        val isSignInToAnotherAccountEnabled = remember { mutableStateOf(isSignInToAnotherAccountEnabled) }
        onSignInToAnotherAccountEnabled = { isSignInToAnotherAccountEnabled.value = it }
        OneTap(
            modifier = Modifier,
            style = style.value,
            onAuth = { onAuth(it) },
            onFail = { onFail(it) },
            signInAnotherAccountButtonEnabled = isSignInToAnotherAccountEnabled.value
        )
    }

    public fun setCallbacks(
        onAuth: (AccessToken) -> Unit,
        onFail: (VKIDAuthFail) -> Unit = {},
    ) {
        this.onAuth = onAuth
        this.onFail = onFail
    }
}
