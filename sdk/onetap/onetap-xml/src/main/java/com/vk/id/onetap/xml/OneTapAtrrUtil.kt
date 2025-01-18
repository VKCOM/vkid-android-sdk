@file:Suppress("TooManyFunctions")

package com.vk.id.onetap.xml

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import com.vk.id.group.subscription.common.style.GroupSubscriptionButtonsCornersStyle
import com.vk.id.group.subscription.common.style.GroupSubscriptionButtonsSizeStyle
import com.vk.id.group.subscription.common.style.GroupSubscriptionSheetCornersStyle
import com.vk.id.group.subscription.common.style.GroupSubscriptionStyle
import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.onetap.common.OneTapStyle
import com.vk.id.onetap.common.button.style.OneTapButtonCornersStyle
import com.vk.id.onetap.common.button.style.OneTapButtonElevationStyle
import com.vk.id.onetap.common.button.style.OneTapButtonSizeStyle
import com.vk.id.onetap.compose.onetap.OneTapTitleScenario
import com.vk.id.onetap.compose.onetap.sheet.OneTapScenario
import com.vk.id.onetap.compose.onetap.sheet.style.OneTapBottomSheetStyle
import com.vk.id.onetap.compose.onetap.sheet.style.OneTapSheetCornersStyle

internal fun parseOneTapAttrs(
    context: Context,
    attrs: AttributeSet?,
): OneTapParsedAttrs {
    context.theme.obtainStyledAttributes(
        attrs,
        R.styleable.vkid_OneTap,
        0,
        0
    ).apply {
        try {
            return OneTapParsedAttrs(
                style = getOneTapStyleConstructor(context)(
                    OneTapButtonCornersStyle.Custom(context.pixelsToDp(getButtonsCornerRadius(context))),
                    getOneTapButtonsSize(),
                    OneTapButtonElevationStyle.Custom(context.pixelsToDp(getOneTapButtonsElevation(context)))
                ),
                isSignInToAnotherAccountEnabled = getSignInToAnotherAccountButtonEnabled(),
                oAuths = getOAuths(),
                scopes = getScopes(),
                fastAuthEnabled = getFastAuthEnabled(),
                scenario = getOneTapScenario(),
                groupId = getGroupId(),
                groupSubscriptionStyle = getGroupSubscriptionStyleConstructor(context)(
                    GroupSubscriptionSheetCornersStyle.Custom(context.pixelsToDp(getGroupSubscriptionCornerRadius(context))),
                    GroupSubscriptionButtonsCornersStyle.Custom(context.pixelsToDp(getGroupSubscriptionButtonCornerRadius(context))),
                    getGroupSubscriptionButtonSize(),
                ),
            )
        } finally {
            recycle()
        }
    }
}

internal data class OneTapParsedAttrs(
    val style: OneTapStyle,
    val isSignInToAnotherAccountEnabled: Boolean,
    val oAuths: Set<OneTapOAuth>,
    val scopes: Set<String>,
    val fastAuthEnabled: Boolean,
    val scenario: OneTapTitleScenario,
    val groupId: String?,
    val groupSubscriptionStyle: GroupSubscriptionStyle,
)

@Suppress("LongParameterList")
internal class OneTapBottomSheetAttributeSettings(
    val style: OneTapBottomSheetStyle,
    val serviceName: String,
    val scenario: OneTapScenario,
    val autoHideOnSuccess: Boolean,
    val oAuths: Set<OneTapOAuth>,
    val scopes: Set<String>,
    val fastAuthEnabled: Boolean,
    val groupId: String?,
    val groupSubscriptionStyle: GroupSubscriptionStyle,
)

internal fun parseOneTapBottomSheetAttrs(
    context: Context,
    attrs: AttributeSet?,
): OneTapBottomSheetAttributeSettings {
    context.theme.obtainStyledAttributes(
        attrs,
        R.styleable.vkid_OneTap,
        0,
        0
    ).apply {
        try {
            return OneTapBottomSheetAttributeSettings(
                style = getSheetStyleConstructor(context)(
                    OneTapSheetCornersStyle.Custom(context.pixelsToDp(getSheetCornerRadius(context))),
                    OneTapButtonCornersStyle.Custom(context.pixelsToDp(getButtonsCornerRadius(context))),
                    getOneTapButtonsSize(),
                ),
                serviceName = getSheetServiceName(),
                scenario = getSheetScenario(),
                autoHideOnSuccess = getSheetAutoHideOnSuccess(),
                oAuths = getOAuths(),
                scopes = getScopes(),
                fastAuthEnabled = getFastAuthEnabled(),
                groupId = getGroupId(),
                groupSubscriptionStyle = getGroupSubscriptionStyleConstructor(context)(
                    GroupSubscriptionSheetCornersStyle.Custom(context.pixelsToDp(getGroupSubscriptionCornerRadius(context))),
                    GroupSubscriptionButtonsCornersStyle.Custom(context.pixelsToDp(getGroupSubscriptionButtonCornerRadius(context))),
                    getGroupSubscriptionButtonSize(),
                ),
            )
        } finally {
            recycle()
        }
    }
}

private fun TypedArray.getSheetCornerRadius(context: Context) = getDimension(
    R.styleable.vkid_OneTap_vkid_bottomSheetCornerRadius,
    context.dpToPixels(OneTapSheetCornersStyle.Default.radiusDp)
)

private fun TypedArray.getButtonsCornerRadius(context: Context) = getDimension(
    R.styleable.vkid_OneTap_vkid_buttonsCornerRadius,
    context.dpToPixels(OneTapButtonCornersStyle.Default.radiusDp)
)

private fun TypedArray.getOneTapButtonsElevation(context: Context) = getDimension(
    R.styleable.vkid_OneTap_vkid_buttonsElevation,
    context.dpToPixels(OneTapButtonElevationStyle.Default.elevationDp)
)

@Suppress("MagicNumber")
private fun TypedArray.getOneTapStyleConstructor(
    context: Context
) = when (getInt(R.styleable.vkid_OneTap_vkid_onetapStyle, 0)) {
    1 -> OneTapStyle::Dark
    2 -> OneTapStyle::TransparentLight
    3 -> OneTapStyle::TransparentDark
    4 -> OneTapStyle::Icon
    5 -> { cornersStyle: OneTapButtonCornersStyle, sizeStyle: OneTapButtonSizeStyle, elevationStyle: OneTapButtonElevationStyle ->
        OneTapStyle.system(
            context = context,
            cornersStyle = cornersStyle,
            sizeStyle = sizeStyle,
            elevationStyle = elevationStyle
        )
    }

    6 -> { cornersStyle: OneTapButtonCornersStyle, sizeStyle: OneTapButtonSizeStyle, elevationStyle: OneTapButtonElevationStyle ->
        OneTapStyle.transparentSystem(
            context = context,
            cornersStyle = cornersStyle,
            sizeStyle = sizeStyle,
            elevationStyle = elevationStyle
        )
    }

    else -> OneTapStyle::Light
}

@Suppress("MagicNumber")
private fun TypedArray.getSheetStyleConstructor(
    context: Context
) = when (getInt(R.styleable.vkid_OneTap_vkid_bottomSheetStyle, 0)) {
    1 -> OneTapBottomSheetStyle::Dark
    2 -> OneTapBottomSheetStyle::TransparentLight
    3 -> OneTapBottomSheetStyle::TransparentDark
    4 -> { cornersStyle: OneTapSheetCornersStyle, buttonsCornersStyle: OneTapButtonCornersStyle, sizeStyle: OneTapButtonSizeStyle ->
        OneTapBottomSheetStyle.system(
            context = context,
            cornersStyle = cornersStyle,
            buttonsCornersStyle = buttonsCornersStyle,
            buttonsSizeStyle = sizeStyle
        )
    }

    5 -> { cornersStyle: OneTapSheetCornersStyle, buttonsCornersStyle: OneTapButtonCornersStyle, sizeStyle: OneTapButtonSizeStyle ->
        OneTapBottomSheetStyle.transparentSystem(
            context = context,
            cornersStyle = cornersStyle,
            buttonsCornersStyle = buttonsCornersStyle,
            buttonsSizeStyle = sizeStyle
        )
    }

    else -> OneTapBottomSheetStyle::Light
}

@Suppress("MagicNumber", "CyclomaticComplexMethod")
private fun TypedArray.getOneTapButtonsSize() = when (getInt(R.styleable.vkid_OneTap_vkid_buttonsSize, 0)) {
    1 -> OneTapButtonSizeStyle.SMALL_32
    2 -> OneTapButtonSizeStyle.SMALL_34
    3 -> OneTapButtonSizeStyle.SMALL_36
    4 -> OneTapButtonSizeStyle.SMALL_38
    5 -> OneTapButtonSizeStyle.MEDIUM_40
    6 -> OneTapButtonSizeStyle.MEDIUM_42
    7 -> OneTapButtonSizeStyle.MEDIUM_44
    8 -> OneTapButtonSizeStyle.MEDIUM_46
    9 -> OneTapButtonSizeStyle.LARGE_48
    10 -> OneTapButtonSizeStyle.LARGE_50
    11 -> OneTapButtonSizeStyle.LARGE_52
    12 -> OneTapButtonSizeStyle.LARGE_54
    13 -> OneTapButtonSizeStyle.LARGE_56
    else -> OneTapButtonSizeStyle.DEFAULT
}

private fun TypedArray.getSignInToAnotherAccountButtonEnabled(): Boolean {
    return getBoolean(R.styleable.vkid_OneTap_vkid_onetapShowSignInToAnotherAccount, false)
}

private fun TypedArray.getSheetServiceName(): String {
    return getString(R.styleable.vkid_OneTap_vkid_bottomSheetServiceName) ?: ""
}

private fun TypedArray.getSheetAutoHideOnSuccess(): Boolean {
    return getBoolean(R.styleable.vkid_OneTap_vkid_autoHideOnSuccess, true)
}

@Suppress("MagicNumber")
private fun TypedArray.getSheetScenario() = when (getInt(R.styleable.vkid_OneTap_vkid_bottomSheetScenario, 0)) {
    1 -> OneTapScenario.RegistrationForTheEvent
    2 -> OneTapScenario.Application
    3 -> OneTapScenario.OrderInService
    4 -> OneTapScenario.Order
    5 -> OneTapScenario.EnterToAccount
    else -> OneTapScenario.EnterService
}

private fun TypedArray.getOAuths(): Set<OneTapOAuth> {
    return (getString(R.styleable.vkid_OneTap_vkid_onetapOAuths) ?: "")
        .split(',')
        .filter { it.isNotBlank() }
        .map {
            when (it) {
                "mail" -> OneTapOAuth.MAIL
                "ok" -> OneTapOAuth.OK
                else -> error("""Unexpected oauth "$it", please use one of "mail" or "ok", separated by commas""")
            }
        }
        .toSet()
}

private fun TypedArray.getScopes(): Set<String> {
    return (getString(R.styleable.vkid_OneTap_vkid_onetapScopes) ?: "")
        .split(',', ' ')
        .filter { it.isNotBlank() }
        .toSet()
}

private fun TypedArray.getFastAuthEnabled(): Boolean {
    return getBoolean(R.styleable.vkid_OneTap_vkid_onetapFastAuthEnabled, true)
}

private fun TypedArray.getOneTapScenario(): OneTapTitleScenario {
    @Suppress("MagicNumber")
    return when (getInt(R.styleable.vkid_OneTap_vkid_onetapScenario, 0)) {
        1 -> OneTapTitleScenario.SignUp
        2 -> OneTapTitleScenario.Get
        3 -> OneTapTitleScenario.Open
        4 -> OneTapTitleScenario.Calculate
        5 -> OneTapTitleScenario.Order
        6 -> OneTapTitleScenario.PlaceOrder
        7 -> OneTapTitleScenario.SendRequest
        8 -> OneTapTitleScenario.Participate
        else -> OneTapTitleScenario.SignIn
    }
}

private fun TypedArray.getGroupId(): String? {
    return getString(R.styleable.vkid_OneTap_vkid_group_id)
}

private fun TypedArray.getGroupSubscriptionStyleConstructor(
    context: Context
) = when (getInt(R.styleable.vkid_OneTap_vkid_group_subscription_style, 0)) {
    1 -> GroupSubscriptionStyle::Dark
    2 -> { cornersStyle: GroupSubscriptionSheetCornersStyle,
            buttonsCornersStyle: GroupSubscriptionButtonsCornersStyle,
            buttonsSizeStyle: GroupSubscriptionButtonsSizeStyle ->
        GroupSubscriptionStyle.system(
            context = context,
            cornersStyle = cornersStyle,
            buttonsCornersStyle = buttonsCornersStyle,
            buttonsSizeStyle = buttonsSizeStyle,
        )
    }

    else -> GroupSubscriptionStyle::Light
}

private fun TypedArray.getGroupSubscriptionCornerRadius(context: Context) = getDimension(
    R.styleable.vkid_OneTap_vkid_group_subscription_corners,
    context.dpToPixels(GroupSubscriptionSheetCornersStyle.Default.radiusDp)
)

private fun TypedArray.getGroupSubscriptionButtonCornerRadius(context: Context) = getDimension(
    R.styleable.vkid_OneTap_vkid_group_subscription_button_corners,
    context.dpToPixels(GroupSubscriptionButtonsCornersStyle.Default.radiusDp)
)

@Suppress("MagicNumber", "CyclomaticComplexMethod")
private fun TypedArray.getGroupSubscriptionButtonSize() = when (getInt(R.styleable.vkid_OneTap_vkid_group_subscription_button_size, 0)) {
    1 -> GroupSubscriptionButtonsSizeStyle.SMALL_32
    2 -> GroupSubscriptionButtonsSizeStyle.SMALL_34
    3 -> GroupSubscriptionButtonsSizeStyle.SMALL_36
    4 -> GroupSubscriptionButtonsSizeStyle.SMALL_38
    5 -> GroupSubscriptionButtonsSizeStyle.MEDIUM_40
    6 -> GroupSubscriptionButtonsSizeStyle.MEDIUM_42
    7 -> GroupSubscriptionButtonsSizeStyle.MEDIUM_44
    8 -> GroupSubscriptionButtonsSizeStyle.MEDIUM_46
    9 -> GroupSubscriptionButtonsSizeStyle.LARGE_48
    10 -> GroupSubscriptionButtonsSizeStyle.LARGE_50
    11 -> GroupSubscriptionButtonsSizeStyle.LARGE_52
    12 -> GroupSubscriptionButtonsSizeStyle.LARGE_54
    13 -> GroupSubscriptionButtonsSizeStyle.LARGE_56
    else -> GroupSubscriptionButtonsSizeStyle.DEFAULT
}

private fun Context.pixelsToDp(
    px: Float
) = px / (resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)

private fun Context.dpToPixels(
    dp: Float
) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
