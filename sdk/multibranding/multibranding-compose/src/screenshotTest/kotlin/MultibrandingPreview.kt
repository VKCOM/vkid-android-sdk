@file:OptIn(InternalVKIDApi::class)

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.vk.id.OAuth
import com.vk.id.VKID
import com.vk.id.common.InternalVKIDApi
import com.vk.id.multibranding.OAuthListWidget
import com.vk.id.multibranding.common.style.OAuthListWidgetCornersStyle
import com.vk.id.multibranding.common.style.OAuthListWidgetSizeStyle
import com.vk.id.multibranding.common.style.OAuthListWidgetStyle

@Preview(locale = "ru")
@Composable
private fun OAuthListWidgetDark() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    OAuthListWidget(
        modifier = Modifier.background(Color.White),
        style = OAuthListWidgetStyle.Light(),
        oAuths = setOf(OAuth.VK),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview(locale = "en")
@Composable
private fun OAuthListWidgetMailLight() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    OAuthListWidget(
        style = OAuthListWidgetStyle.Dark(),
        oAuths = setOf(OAuth.MAIL),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview(locale = "fr")
@Composable
private fun OAuthListWidgetWithOKItem() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    OAuthListWidget(
        oAuths = setOf(OAuth.OK),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview(locale = "de")
@Composable
private fun OAuthListWidgetWithTwoItems() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    OAuthListWidget(
        oAuths = setOf(OAuth.OK, OAuth.VK),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview(locale = "pl")
@Composable
private fun OAuthListWidgetWithOKAndMailItems() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    OAuthListWidget(
        oAuths = setOf(OAuth.OK, OAuth.MAIL),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview(locale = "tr")
@Composable
private fun OAuthListWidgetWithMailAndVkItems() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    OAuthListWidget(
        oAuths = setOf(OAuth.MAIL, OAuth.VK),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview(locale = "uk")
@Composable
private fun OAuthListWidgetWithThreeItems() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    OAuthListWidget(
        oAuths = setOf(OAuth.OK, OAuth.MAIL, OAuth.VK),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview
@Composable
private fun OAuthListWidgetCornersStyleNoneLight() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    OAuthListWidget(
        style = OAuthListWidgetStyle.Light(cornersStyle = OAuthListWidgetCornersStyle.None),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview
@Composable
private fun OAuthListWidgetBorderStyleRoundLight() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    OAuthListWidget(
        style = OAuthListWidgetStyle.Light(cornersStyle = OAuthListWidgetCornersStyle.Round),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview
@Composable
private fun OAuthListWidgetBorderStyleRoundedLight() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    OAuthListWidget(
        style = OAuthListWidgetStyle.Light(cornersStyle = OAuthListWidgetCornersStyle.Rounded),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview
@Composable
private fun OAuthListWidgetSizeStyleSmall32Dark() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    OAuthListWidget(
        style = OAuthListWidgetStyle.Light(sizeStyle = OAuthListWidgetSizeStyle.SMALL_32),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview
@Composable
private fun OAuthListWidgetSizeStyleSmall34Dark() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    OAuthListWidget(
        style = OAuthListWidgetStyle.Light(sizeStyle = OAuthListWidgetSizeStyle.SMALL_34),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview
@Composable
private fun OAuthListWidgetSizeStyleSmall36Dark() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    OAuthListWidget(
        style = OAuthListWidgetStyle.Light(sizeStyle = OAuthListWidgetSizeStyle.SMALL_36),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview
@Composable
private fun OAuthListWidgetSizeStyleSmall38Dark() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    OAuthListWidget(
        style = OAuthListWidgetStyle.Light(sizeStyle = OAuthListWidgetSizeStyle.SMALL_38),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview
@Composable
private fun OAuthListWidgetSizeStyleMedium40Dark() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    OAuthListWidget(
        style = OAuthListWidgetStyle.Light(sizeStyle = OAuthListWidgetSizeStyle.MEDIUM_40),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview
@Composable
private fun OAuthListWidgetSizeStyleMedium42Dark() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    OAuthListWidget(
        style = OAuthListWidgetStyle.Light(sizeStyle = OAuthListWidgetSizeStyle.MEDIUM_42),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview
@Composable
private fun OAuthListWidgetSizeStyleMedium44Dark() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    OAuthListWidget(
        style = OAuthListWidgetStyle.Light(sizeStyle = OAuthListWidgetSizeStyle.MEDIUM_44),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview
@Composable
private fun OAuthListWidgetSizeStyleMedium46Dark() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    OAuthListWidget(
        style = OAuthListWidgetStyle.Light(sizeStyle = OAuthListWidgetSizeStyle.MEDIUM_46),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview
@Composable
private fun OAuthListWidgetSizeStyleLarge48Dark() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    OAuthListWidget(
        style = OAuthListWidgetStyle.Light(sizeStyle = OAuthListWidgetSizeStyle.LARGE_48),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview
@Composable
private fun OAuthListWidgetSizeStyleLarge50Dark() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    OAuthListWidget(
        style = OAuthListWidgetStyle.Light(sizeStyle = OAuthListWidgetSizeStyle.LARGE_50),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview
@Composable
private fun OAuthListWidgetSizeStyleLarge52Dark() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    OAuthListWidget(
        style = OAuthListWidgetStyle.Light(sizeStyle = OAuthListWidgetSizeStyle.LARGE_52),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview
@Composable
private fun OAuthListWidgetSizeStyleLarge54Dark() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    OAuthListWidget(
        style = OAuthListWidgetStyle.Light(sizeStyle = OAuthListWidgetSizeStyle.LARGE_54),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview
@Composable
private fun OAuthListWidgetSizeStyleLarge56Dark() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    OAuthListWidget(
        style = OAuthListWidgetStyle.Light(sizeStyle = OAuthListWidgetSizeStyle.LARGE_56),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview(locale = "")
@Composable
private fun OAuthListWidgetLangRu() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    OAuthListWidget(
        oAuths = setOf(OAuth.OK, OAuth.MAIL, OAuth.VK),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}
