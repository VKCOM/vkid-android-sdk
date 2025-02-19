@file:OptIn(InternalVKIDApi::class)

package com.vk.id.multibranding.xml

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import android.util.TypedValue.COMPLEX_UNIT_DIP
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import com.vk.id.AccessToken
import com.vk.id.OAuth
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.AuthCodeData
import com.vk.id.auth.VKIDAuthUiParams
import com.vk.id.common.InternalVKIDApi
import com.vk.id.multibranding.OAuthListWidget
import com.vk.id.multibranding.common.style.OAuthListWidgetCornersStyle
import com.vk.id.multibranding.common.style.OAuthListWidgetSizeStyle
import com.vk.id.multibranding.common.style.OAuthListWidgetStyle

/**
 * Multibranding widget that supports auth with multiple [OAuth]s.
 */
public class OAuthListWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val composeView = ComposeView(context)

    /** Styling widget configuration. */
    public var style: OAuthListWidgetStyle = OAuthListWidgetStyle.Light()
        set(value) {
            field = value
            onStyleChange(value)
        }
    private var onStyleChange: (OAuthListWidgetStyle) -> Unit = {}

    /** A set of [OAuth]s the should be displayed to the user. */
    public var oAuths: Set<OAuth> = OAuth.entries.toSet()
        set(value) {
            field = value
            onOAuthsChange(value)
        }
    private var onOAuthsChange: (Set<OAuth>) -> Unit = {}

    /**
     * Optional params to be passed to auth. See [VKIDAuthUiParams.Builder] for more info.
     */
    public var authParams: VKIDAuthUiParams = VKIDAuthUiParams { }
        set(value) {
            field = value
            onAuthParamsChange(value)
        }
    private var onAuthParamsChange: (VKIDAuthUiParams) -> Unit = {}
    private var onAuth: (oAuth: OAuth, accessToken: AccessToken) -> Unit = { _, _ ->
        error("No onAuth callback for OAuthListWidget. Set it with setCallbacks method.")
    }
    private var onAuthCode: (AuthCodeData, Boolean) -> Unit = { _, _ -> }
    private var onFail: (OAuth, VKIDAuthFail) -> Unit = { _, _ -> }

    init {
        val (style, oAuths, scopes) = parseAttrs(context, attrs)
        this.style = style
        this.oAuths = oAuths
        this.authParams = authParams.newBuilder { this.scopes = scopes }
        addView(composeView)
        composeView.setContent { Content() }
    }

    @Suppress("NonSkippableComposable")
    @Composable
    private fun Content() {
        var style by remember { mutableStateOf(style) }
        onStyleChange = { style = it }
        var oAuths by remember { mutableStateOf(oAuths) }
        onOAuthsChange = { oAuths = it }
        var authParams by remember { mutableStateOf(authParams) }
        onAuthParamsChange = { authParams = it }
        OAuthListWidget(
            modifier = Modifier,
            style = style,
            onAuth = { oAuth, accessToken -> onAuth(oAuth, accessToken) },
            onAuthCode = { data, isCompletion -> onAuthCode(data, isCompletion) },
            onFail = { oAuth, fail -> onFail(oAuth, fail) },
            oAuths = oAuths,
            authParams = authParams,
        )
    }

    /**
     * Sets callbacks for the authorization
     *
     * @param onAuth A callback to be invoked upon a successful auth.
     * @param onAuthCode A callback to be invoked upon successful first step of auth - receiving auth code.
     * isCompletion is true if [onSuccess] won't be called.
     * This will happen if you passed auth parameters and implement their validation yourself.
     * In that case we can't exchange auth code for access token and you should do this yourself.
     * @param onFail A callback to be invoked upon an error during auth.
     */
    public fun setCallbacks(
        onAuth: (oAuth: OAuth, accessToken: AccessToken) -> Unit,
        onAuthCode: (data: AuthCodeData, isCompletion: Boolean) -> Unit = { _, _ -> },
        onFail: (oAuth: OAuth, fail: VKIDAuthFail) -> Unit = { _, _ -> },
    ) {
        this.onAuth = onAuth
        this.onAuthCode = onAuthCode
        this.onFail = onFail
    }
}

private fun parseAttrs(
    context: Context,
    attrs: AttributeSet?,
): Triple<OAuthListWidgetStyle, Set<OAuth>, Set<String>> {
    context.theme.obtainStyledAttributes(
        attrs,
        R.styleable.vkid_OAuthListWidget,
        0,
        0
    ).apply {
        try {
            return Triple(
                getStyleConstructor(context = context)(
                    OAuthListWidgetCornersStyle.Custom(context.pixelsToDp(getCornerRadius(context))),
                    getSize(),
                ),
                getOAuths(),
                getScopes(),
            )
        } finally {
            recycle()
        }
    }
}

private fun TypedArray.getCornerRadius(context: Context) = getDimension(
    R.styleable.vkid_OAuthListWidget_vkid_oAuthListCornerRadius,
    context.dpToPixels(OAuthListWidgetCornersStyle.Default.radiusDp)
)

@Suppress("MagicNumber")
private fun TypedArray.getStyleConstructor(
    context: Context
) = when (getInt(R.styleable.vkid_OAuthListWidget_vkid_OAuthListStyle, 0)) {
    1 -> OAuthListWidgetStyle::Dark
    2 -> OAuthListWidgetStyle::Light
    4 -> OAuthListWidgetStyle::Dark
    5 -> OAuthListWidgetStyle::Light
    else -> { cornersStyle: OAuthListWidgetCornersStyle, sizeStyle: OAuthListWidgetSizeStyle ->
        OAuthListWidgetStyle.system(context, cornersStyle, sizeStyle)
    }
}

@Suppress("MagicNumber", "CyclomaticComplexMethod")
private fun TypedArray.getSize() = when (getInt(R.styleable.vkid_OAuthListWidget_vkid_oAuthListSize, 0)) {
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

private fun TypedArray.getOAuths(): Set<OAuth> {
    return (getString(R.styleable.vkid_OAuthListWidget_vkid_oAuthListOAuths) ?: "vk,mail,ok")
        .split(',')
        .filter { it.isNotBlank() }
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

private fun TypedArray.getScopes(): Set<String> {
    return (getString(R.styleable.vkid_OAuthListWidget_vkid_oAuthListScopes) ?: "")
        .split(',', ' ')
        .filter { it.isNotBlank() }
        .toSet()
}

private fun Context.pixelsToDp(
    px: Float
) = px / (resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)

private fun Context.dpToPixels(
    dp: Float
) = TypedValue.applyDimension(COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
