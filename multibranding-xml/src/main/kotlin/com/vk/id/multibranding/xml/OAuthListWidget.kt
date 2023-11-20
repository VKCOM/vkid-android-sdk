package com.vk.id.multibranding.xml

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AbstractComposeView
import com.vk.id.multibranding.OAuth
import com.vk.id.multibranding.OAuthListWidget
import com.vk.id.multibranding.OAuthListWidgetAuthCallback
import com.vk.id.multibranding.OAuthListWidgetCornersStyle
import com.vk.id.multibranding.OAuthListWidgetSizeStyle
import com.vk.id.multibranding.OAuthListWidgetStyle

public class OAuthListWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AbstractComposeView(context, attrs, defStyleAttr) {

    public var style: OAuthListWidgetStyle = parseAttrs(context, attrs)
        set(value) {
            field = value
            onStyleChange(value)
        }
    private var onStyleChange: (OAuthListWidgetStyle) -> Unit = {}
    private var onAuth: OAuthListWidgetAuthCallback = OAuthListWidgetAuthCallback.JustToken {}
    private var onFail: () -> Unit = {}

    @Composable
    override fun Content() {
        val style = remember { mutableStateOf(style) }
        onStyleChange = { style.value = it }
        OAuthListWidget(
            modifier = Modifier,
            style = style.value,
            onAuth = OAuthListWidgetAuthCallback.WithOAuth { oAuth: OAuth, token: String ->
                when (val callback = onAuth) {
                    is OAuthListWidgetAuthCallback.WithOAuth -> callback(oAuth, token)
                    is OAuthListWidgetAuthCallback.JustToken -> callback(token)
                }
            },
        )
    }

    public fun setCallbacks(
        onAuth: OAuthListWidgetAuthCallback,
        onFail: () -> Unit = {},
    ) {
        this.onAuth = onAuth
        this.onFail = onFail
    }
}

private fun parseAttrs(
    context: Context,
    attrs: AttributeSet?,
): OAuthListWidgetStyle {
    context.theme.obtainStyledAttributes(
        attrs,
        R.styleable.OAuthListWidget,
        0,
        0
    ).apply {
        try {
            return getStyleConstructor()(
                OAuthListWidgetCornersStyle.Custom(getCornerRadius().toInt()),
                getSize(),
            )
        } finally {
            recycle()
        }
    }
}

private fun TypedArray.getCornerRadius() = getDimension(
    R.styleable.OAuthListWidget_vkid_oauth_list__cornerRadius,
    OAuthListWidgetCornersStyle.Default.radiusDp.toFloat()
)

private fun TypedArray.getStyleConstructor() = when (getInt(R.styleable.OAuthListWidget_vkid_oauth_list_style, 0)) {
    1 -> OAuthListWidgetStyle::Light
    else -> OAuthListWidgetStyle::Dark
}

@Suppress("MagicNumber", "CyclomaticComplexMethod")
private fun TypedArray.getSize() = when (getInt(R.styleable.OAuthListWidget_vkid_oauth_list__size, 0)) {
    1 -> OAuthListWidgetSizeStyle.SMALL_32
    2 -> OAuthListWidgetSizeStyle.SMALL_34
    3 -> OAuthListWidgetSizeStyle.SMALL_36
    4 -> OAuthListWidgetSizeStyle.SMALL_38
    5 -> OAuthListWidgetSizeStyle.MEDIUM_40
    6 -> OAuthListWidgetSizeStyle.MEDIUM_42
    7 -> OAuthListWidgetSizeStyle.MEDIUM_44
    8 -> OAuthListWidgetSizeStyle.MEDIUM_46
    9 -> OAuthListWidgetSizeStyle.LARGE_48
    10 -> OAuthListWidgetSizeStyle.LARGE_50
    11 -> OAuthListWidgetSizeStyle.LARGE_52
    12 -> OAuthListWidgetSizeStyle.LARGE_54
    13 -> OAuthListWidgetSizeStyle.LARGE_56
    else -> OAuthListWidgetSizeStyle.DEFAULT
}
