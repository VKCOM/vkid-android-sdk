package com.vk.id.onetap.compose.onetap.sheet.style

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import com.vk.id.onetap.common.OneTapStyle
import com.vk.id.onetap.common.button.style.OneTapButtonCornersStyle
import com.vk.id.onetap.common.button.style.OneTapButtonSizeStyle
import com.vk.id.onetap.compose.R

@Suppress("LongParameterList")
public sealed class OneTapBottomSheetStyle(
    internal val cornersStyle: OneTapSheetCornersStyle,
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
        vkidIcon = R.drawable.vkid_onetap_bottomsheet_logo_light,
        serviceNameTextColor = R.color.vkid_steel_gray_400,
        contentTextColor = R.color.vkid_steel_gray_400,
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
        vkidIcon = R.drawable.vkid_onetap_bottomsheet_logo_dark,
        serviceNameTextColor = R.color.vkid_steel_gray_400,
        contentTextColor = R.color.vkid_gray_500,
        contentTitleTextColor = R.color.vkid_gray_100,
    )

    public class TransparentLight(
        cornersStyle: OneTapSheetCornersStyle = OneTapSheetCornersStyle.Default,
        buttonsCornersStyle: OneTapButtonCornersStyle = OneTapButtonCornersStyle.Default,
        buttonsSizeStyle: OneTapButtonSizeStyle = OneTapButtonSizeStyle.DEFAULT,
    ) : OneTapBottomSheetStyle(
        cornersStyle = cornersStyle,
        oneTapStyle = OneTapStyle.TransparentLight(cornersStyle = buttonsCornersStyle, sizeStyle = buttonsSizeStyle),
        backgroundStyle = OneTapSheetBackgroundStyle.LIGHT,
        vkidIcon = R.drawable.vkid_onetap_bottomsheet_logo_light,
        serviceNameTextColor = R.color.vkid_steel_gray_400,
        contentTextColor = R.color.vkid_steel_gray_400,
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
        vkidIcon = R.drawable.vkid_onetap_bottomsheet_logo_dark,
        serviceNameTextColor = R.color.vkid_steel_gray_400,
        contentTextColor = R.color.vkid_gray_500,
        contentTitleTextColor = R.color.vkid_gray_100,
    )
}

@Composable
public fun rememberOneTapBottomSheetStyle(initialValue: OneTapBottomSheetStyle): MutableState<OneTapBottomSheetStyle> {
    return rememberSaveable(stateSaver = BottomSheetStyleSaver) {
        mutableStateOf(initialValue)
    }
}

internal val BottomSheetStyleSaver: Saver<OneTapBottomSheetStyle, Any> = run {
    val darkStyleKey = "Dark"
    val lightStyleKey = "Light"
    val transparentLightStyleKey = "TransparentLight"
    val transparentDarkStyleKey = "TransparentDark"
    val classNameKey = "classNameKey"
    val sheetCornersRadiusKey = "sheetCornersRadiusKey"
    val buttonsCornersRadiusKey = "buttonsCornersRadiusKey"
    val buttonsSizeKey = "buttonsSizeKey"
    mapSaver(
        save = {
            val keysToSave = mutableMapOf<String, Any>()
            when (it) {
                is OneTapBottomSheetStyle.Dark -> {
                    keysToSave[classNameKey] = darkStyleKey
                }
                is OneTapBottomSheetStyle.Light -> {
                    keysToSave[classNameKey] = lightStyleKey
                }
                is OneTapBottomSheetStyle.TransparentDark -> {
                    keysToSave[classNameKey] = transparentDarkStyleKey
                }
                is OneTapBottomSheetStyle.TransparentLight -> {
                    keysToSave[classNameKey] = transparentLightStyleKey
                }
            }
            keysToSave[sheetCornersRadiusKey] = it.cornersStyle.radiusDp
            keysToSave[buttonsCornersRadiusKey] = it.oneTapStyle.cornersStyle.radiusDp
            keysToSave[buttonsSizeKey] = it.oneTapStyle.sizeStyle
            keysToSave
        },
        restore = {
            val sheetCornersRadius = it[sheetCornersRadiusKey] as? Int
            val cornersStyle = if (sheetCornersRadius != null) {
                OneTapSheetCornersStyle.Custom(sheetCornersRadius)
            } else {
                OneTapSheetCornersStyle.Default
            }
            val buttonsCornersRadius = it[buttonsCornersRadiusKey] as? Int
            val buttonsCornersStyle = if (buttonsCornersRadius != null) {
                OneTapButtonCornersStyle.Custom(buttonsCornersRadius)
            } else {
                OneTapButtonCornersStyle.Default
            }
            val buttonsSizeStyle = it[buttonsSizeKey] as? OneTapButtonSizeStyle ?: OneTapButtonSizeStyle.DEFAULT
            when (it[classNameKey]) {
                darkStyleKey -> OneTapBottomSheetStyle.Dark(cornersStyle, buttonsCornersStyle, buttonsSizeStyle)
                lightStyleKey -> OneTapBottomSheetStyle.Light(cornersStyle, buttonsCornersStyle, buttonsSizeStyle)
                transparentDarkStyleKey -> OneTapBottomSheetStyle.TransparentDark(
                    cornersStyle,
                    buttonsCornersStyle,
                    buttonsSizeStyle
                )
                transparentLightStyleKey -> OneTapBottomSheetStyle.TransparentLight(
                    cornersStyle,
                    buttonsCornersStyle,
                    buttonsSizeStyle
                )
                else -> OneTapBottomSheetStyle.Light()
            }
        }
    )
}
