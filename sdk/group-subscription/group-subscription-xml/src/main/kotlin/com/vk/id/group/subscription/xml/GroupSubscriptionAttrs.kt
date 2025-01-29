package com.vk.id.group.subscription.xml

import android.content.Context
import android.content.res.TypedArray
import android.util.TypedValue
import android.util.TypedValue.COMPLEX_UNIT_DIP
import com.vk.id.common.InternalVKIDApi
import com.vk.id.group.subscription.common.style.GroupSubscriptionButtonsCornersStyle
import com.vk.id.group.subscription.common.style.GroupSubscriptionButtonsSizeStyle
import com.vk.id.group.subscription.common.style.GroupSubscriptionSheetCornersStyle
import com.vk.id.group.subscription.common.style.GroupSubscriptionStyle

@InternalVKIDApi
public fun TypedArray.vkidInternalGetGroupId(): String? {
    return getString(R.styleable.vkid_GroupSubscription_vkid_groupId)
}

@InternalVKIDApi
public fun TypedArray.vkidInternalGetGroupSubscriptionStyleConstructor(
    context: Context
): (
    GroupSubscriptionSheetCornersStyle,
    GroupSubscriptionButtonsCornersStyle,
    GroupSubscriptionButtonsSizeStyle
) -> GroupSubscriptionStyle {
    return when (getInt(R.styleable.vkid_GroupSubscription_vkid_groupSubscriptionStyle, 0)) {
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
}

@InternalVKIDApi
public fun TypedArray.vkidInternalGetGroupSubscriptionCornerRadius(context: Context): Float = getDimension(
    R.styleable.vkid_GroupSubscription_vkid_groupSubscriptionCorners,
    context.dpToPixels(GroupSubscriptionSheetCornersStyle.Default.radiusDp)
)

@InternalVKIDApi
public fun TypedArray.vkidInternalGetGroupSubscriptionButtonCornerRadius(context: Context): Float = getDimension(
    R.styleable.vkid_GroupSubscription_vkid_groupSubscriptionButtonCorners,
    context.dpToPixels(GroupSubscriptionButtonsCornersStyle.Default.radiusDp)
)

@InternalVKIDApi
@Suppress("MagicNumber", "CyclomaticComplexMethod")
public fun TypedArray.vkidInternalGetGroupSubscriptionButtonSize(): GroupSubscriptionButtonsSizeStyle = when (
    getInt(
        R.styleable.vkid_GroupSubscription_vkid_groupSubscriptionButtonSize,
        0
    )
) {
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

private fun Context.dpToPixels(
    dp: Float
) = TypedValue.applyDimension(COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
