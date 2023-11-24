package com.vk.id.multibranding.xml

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import com.vk.id.AccessToken
import com.vk.id.OAuth
import com.vk.id.VKIDAuthFail
import com.vk.id.multibranding.OAuthListWidget
import com.vk.id.multibranding.common.callback.OAuthListWidgetAuthCallback
import com.vk.id.multibranding.common.style.OAuthListWidgetCornersStyle
import com.vk.id.multibranding.common.style.OAuthListWidgetSizeStyle
import com.vk.id.multibranding.common.style.OAuthListWidgetStyle

public class OAuthListWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val composeView = ComposeView(context)
    public var style: OAuthListWidgetStyle = OAuthListWidgetStyle.Dark()
        set(value) {
            field = value
            onStyleChange(value)
        }
    private var onStyleChange: (OAuthListWidgetStyle) -> Unit = {}
    public var allowedOAuths: Set<OAuth> = emptySet()
        set(value) {
            field = value
            onAllowedOAuthsChange(value)
        }
    private var onAllowedOAuthsChange: (Set<OAuth>) -> Unit = {}
    private var onAuth: OAuthListWidgetAuthCallback = OAuthListWidgetAuthCallback.JustToken {}
    private var onFail: (VKIDAuthFail) -> Unit = {}

    init {
        val (style, allowedOAuths) = parseAttrs(context, attrs)
        this.style = style
        this.allowedOAuths = allowedOAuths
        addView(composeView)
        composeView.setContent { Content() }
    }

    @Composable
    private fun Content() {
        val style = remember { mutableStateOf(style) }
        onStyleChange = { style.value = it }
        val allowedOAuths = remember { mutableStateOf(allowedOAuths) }
        onAllowedOAuthsChange = { allowedOAuths.value = it }
        OAuthListWidget(
            modifier = Modifier,
            style = style.value,
            onAuth = OAuthListWidgetAuthCallback.WithOAuth { oAuth: OAuth, token: AccessToken ->
                when (val callback = onAuth) {
                    is OAuthListWidgetAuthCallback.WithOAuth -> callback(oAuth, token)
                    is OAuthListWidgetAuthCallback.JustToken -> callback(token)
                }
            },
            onFail = onFail,
            allowedOAuths = allowedOAuths.value
        )
    }

    public fun setCallbacks(
        onAuth: OAuthListWidgetAuthCallback,
        onFail: (VKIDAuthFail) -> Unit = {},
    ) {
        this.onAuth = onAuth
        this.onFail = onFail
    }
}

private fun parseAttrs(
    context: Context,
    attrs: AttributeSet?,
): Pair<OAuthListWidgetStyle, Set<OAuth>> {
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
            ) to getAllowedOAuths()
        } finally {
            recycle()
        }
    }
}

private fun TypedArray.getCornerRadius() = getDimension(
    R.styleable.OAuthListWidget_vkid_oauth_list_cornerRadius,
    OAuthListWidgetCornersStyle.Default.radiusDp.toFloat()
)

private fun TypedArray.getStyleConstructor() = when (getInt(R.styleable.OAuthListWidget_vkid_oauth_list_style, 0)) {
    1 -> OAuthListWidgetStyle::Light
    else -> OAuthListWidgetStyle::Dark
}

@Suppress("MagicNumber", "CyclomaticComplexMethod")
private fun TypedArray.getSize() = when (getInt(R.styleable.OAuthListWidget_vkid_oauth_list_size, 0)) {
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

private fun TypedArray.getAllowedOAuths(): Set<OAuth> {
    return (getString(R.styleable.OAuthListWidget_vkid_oauth_list_allowed_oauths) ?: "vk,mail,ok")
        .split(',')
        .map {
            when (it) {
                "vk" -> OAuth.VK
                "mail" -> OAuth.MAIL
                "ok" -> OAuth.OK
                else -> error("""Unexpected oauth "$it", please use one of "vk", "mail" or "ok", separated by commas""")
            }
        }
        .toSet()
}
