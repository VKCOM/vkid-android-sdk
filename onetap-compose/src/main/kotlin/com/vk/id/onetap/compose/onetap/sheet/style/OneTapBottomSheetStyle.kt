package com.vk.id.onetap.compose.onetap.sheet.style

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.vk.id.onetap.common.OneTapStyle
import com.vk.id.onetap.common.button.style.OneTapButtonCornersStyle
import com.vk.id.onetap.common.button.style.OneTapButtonSizeStyle
import com.vk.id.onetap.compose.R
import com.vk.id.onetap.common.R as commonR

@Suppress("LongParameterList")
public sealed class OneTapBottomSheetStyle(
    public val cornersStyle: OneTapSheetCornersStyle,
    internal val oneTapStyle: OneTapStyle,
    internal val backgroundStyle: OneTapSheetBackgroundStyle,
    @DrawableRes
    internal val vkidIcon: Int,
    @ColorRes
    internal val serviceNameTextColor: Int,
    @ColorRes
    internal val contentTextColor: Int,
    @ColorRes
    internal val contentTitleTextColor: Int,
) {
    public class Light(
        cornersStyle: OneTapSheetCornersStyle = OneTapSheetCornersStyle.Default,
        buttonsCornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
        buttonsSizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.DEFAULT,
    ) : OneTapBottomSheetStyle(
        cornersStyle = cornersStyle,
        oneTapStyle = OneTapStyle.Light(cornersStyle = buttonsCornersStyle, sizeStyle = buttonsSizeStyle),
        backgroundStyle = OneTapSheetBackgroundStyle.LIGHT,
        vkidIcon = commonR.drawable.vkid_onetap_bottomsheet_logo_light,
        serviceNameTextColor = commonR.color.vkid_steel_gray_400,
        contentTextColor = commonR.color.vkid_steel_gray_400,
        contentTitleTextColor = R.color.vkid_black,
    )

    public class Dark(
        cornersStyle: OneTapSheetCornersStyle = OneTapSheetCornersStyle.Default,
        buttonsCornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
        buttonsSizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.DEFAULT,
    ) : OneTapBottomSheetStyle(
        cornersStyle = cornersStyle,
        oneTapStyle = OneTapStyle.Dark(cornersStyle = buttonsCornersStyle, sizeStyle = buttonsSizeStyle),
        backgroundStyle = OneTapSheetBackgroundStyle.DARK,
        vkidIcon = commonR.drawable.vkid_onetap_bottomsheet_logo_dark,
        serviceNameTextColor = commonR.color.vkid_steel_gray_400,
        contentTextColor = commonR.color.vkid_gray_500,
        contentTitleTextColor = commonR.color.vkid_gray_100,
    )

    public class TransparentLight(
        cornersStyle: OneTapSheetCornersStyle = OneTapSheetCornersStyle.Default,
        buttonsCornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
        buttonsSizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.DEFAULT,
    ) : OneTapBottomSheetStyle(
        cornersStyle = cornersStyle,
        oneTapStyle = OneTapStyle.TransparentLight(cornersStyle = buttonsCornersStyle, sizeStyle = buttonsSizeStyle),
        backgroundStyle = OneTapSheetBackgroundStyle.LIGHT,
        vkidIcon = commonR.drawable.vkid_onetap_bottomsheet_logo_light,
        serviceNameTextColor = commonR.color.vkid_steel_gray_400,
        contentTextColor = commonR.color.vkid_steel_gray_400,
        contentTitleTextColor = R.color.vkid_black,
    )

    public class TransparentDark(
        cornersStyle: OneTapSheetCornersStyle = OneTapSheetCornersStyle.Default,
        buttonsCornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
        buttonsSizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.DEFAULT,
    ) : OneTapBottomSheetStyle(
        cornersStyle = cornersStyle,
        oneTapStyle = OneTapStyle.TransparentDark(cornersStyle = buttonsCornersStyle, sizeStyle = buttonsSizeStyle),
        backgroundStyle = OneTapSheetBackgroundStyle.DARK,
        vkidIcon = commonR.drawable.vkid_onetap_bottomsheet_logo_dark,
        serviceNameTextColor = commonR.color.vkid_steel_gray_400,
        contentTextColor = commonR.color.vkid_gray_500,
        contentTitleTextColor = commonR.color.vkid_gray_100,
    )
}
