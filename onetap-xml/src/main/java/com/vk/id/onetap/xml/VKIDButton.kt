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

// TODO: Rename class
public class VKIDButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val composeView = ComposeView(context)

    public var style: OneTapStyle = parseAttrs(context, attrs)
        set(value) {
            field = value
            onStyleChange(value)
        }
    private var onStyleChange: (OneTapStyle) -> Unit = {}
    private var onAuth: (AccessToken) -> Unit = {
        throw IllegalStateException("No onAuth callback for VKID OneTap Button. Set it with setCallbacks method.")
    }
    private var onFail: (VKIDAuthFail) -> Unit = {}

    init {
        addView(composeView)
        composeView.setContent { Content() }
    }

    @Composable
    private fun Content() {
        val style = remember { mutableStateOf(style) }
        onStyleChange = { style.value = it }
        OneTap(
            modifier = Modifier,
            style = style.value,
            onAuth = { onAuth(it) },
            onFail = { onFail(it) },
            // TODO: sign in another account button
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
