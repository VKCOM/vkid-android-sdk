package com.vk.id.onetap.xml

import android.content.Context
import android.util.AttributeSet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.AbstractComposeView
import com.vk.id.AccessToken
import com.vk.id.VKIDAuthFail
import com.vk.id.onetap.compose.button.VKIDButtonSmall
import com.vk.id.onetap.compose.button.VKIDButtonStyle

public class VKIDButtonSmall @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AbstractComposeView(context, attrs, defStyleAttr) {

    public var style: VKIDButtonStyle = VKIDButtonStyle.Blue()
        set(value) {
            field = value
            onStyleChange(value)
        }
    private var onStyleChange: (VKIDButtonStyle) -> Unit = {}
    private var onAuth: (AccessToken) -> Unit = {}
    private var onFail: (VKIDAuthFail) -> Unit = {}

    @Composable
    override fun Content() {
        val style = remember { mutableStateOf(style) }
        onStyleChange = { style.value = it }
        VKIDButtonSmall(
            style = style.value,
            onAuth = { onAuth(it) },
            onFail = { onFail(it) },
        )
    }

    public fun setCallbacks(
        onAuth: (AccessToken) -> Unit = {},
        onFail: (VKIDAuthFail) -> Unit = {},
    ) {
        this.onAuth = onAuth
        this.onFail = onFail
    }
}
