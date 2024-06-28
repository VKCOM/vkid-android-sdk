import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.vk.id.OAuth
import com.vk.id.multibranding.OAuthListWidget
import com.vk.id.multibranding.common.style.OAuthListWidgetCornersStyle
import com.vk.id.multibranding.common.style.OAuthListWidgetSizeStyle
import com.vk.id.multibranding.common.style.OAuthListWidgetStyle
import org.intellij.lang.annotations.Language

@Preview(locale = "ru-RU")
@Composable
private fun OAuthListWidgetDark() {
    OAuthListWidget(
        modifier = Modifier.background(Color.White),
        style = OAuthListWidgetStyle.Light(),
        oAuths = setOf(OAuth.VK),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview(locale = "en-US")
@Composable
private fun OAuthListWidgetMailLight() {
    OAuthListWidget(
        style = OAuthListWidgetStyle.Dark(),
        oAuths = setOf(OAuth.MAIL),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview(locale = "fr-FR")
@Composable
private fun OAuthListWidgetWithOKItem() {
    OAuthListWidget(
        oAuths = setOf(OAuth.OK),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview(locale = "de-DE")
@Composable
private fun OAuthListWidgetWithTwoItems() {
    OAuthListWidget(
        oAuths = setOf(OAuth.OK, OAuth.VK),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview(locale = "pl-PL")
@Composable
private fun OAuthListWidgetWithOKAndMailItems() {
    OAuthListWidget(
        oAuths = setOf(OAuth.OK, OAuth.MAIL),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview(locale = "tr-TR")
@Composable()
private fun OAuthListWidgetWithMailAndVkItems() {
    OAuthListWidget(
        oAuths = setOf(OAuth.MAIL, OAuth.VK),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview(locale = "uk-UA")
@Composable
private fun OAuthListWidgetWithThreeItems() {
    OAuthListWidget(
        oAuths = setOf(OAuth.OK, OAuth.MAIL, OAuth.VK),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview(locale = "")
@Composable
private fun OAuthListWidgetCornersStyleNoneLight() {
    OAuthListWidget(
        style = OAuthListWidgetStyle.Light(cornersStyle = OAuthListWidgetCornersStyle.None),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview
@Composable
private fun OAuthListWidgetBorderStyleRoundLight() {
    OAuthListWidget(
        style = OAuthListWidgetStyle.Light(cornersStyle = OAuthListWidgetCornersStyle.Round),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview
@Composable
private fun OAuthListWidgetBorderStyleRoundedLight() {
    OAuthListWidget(
        style = OAuthListWidgetStyle.Light(cornersStyle = OAuthListWidgetCornersStyle.Rounded),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview
@Composable
private fun OAuthListWidgetSizeStyleSmall32Dark() {
    OAuthListWidget(
        style = OAuthListWidgetStyle.Light(sizeStyle = OAuthListWidgetSizeStyle.SMALL_32),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview
@Composable
private fun OAuthListWidgetSizeStyleSmall34Dark() {
    OAuthListWidget(
        style = OAuthListWidgetStyle.Light(sizeStyle = OAuthListWidgetSizeStyle.SMALL_34),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview
@Composable
private fun OAuthListWidgetSizeStyleSmall36Dark() {
    OAuthListWidget(
        style = OAuthListWidgetStyle.Light(sizeStyle = OAuthListWidgetSizeStyle.SMALL_36),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview
@Composable
private fun OAuthListWidgetSizeStyleSmall38Dark() {
    OAuthListWidget(
        style = OAuthListWidgetStyle.Light(sizeStyle = OAuthListWidgetSizeStyle.SMALL_38),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview
@Composable
private fun OAuthListWidgetSizeStyleMedium40Dark() {
    OAuthListWidget(
        style = OAuthListWidgetStyle.Light(sizeStyle = OAuthListWidgetSizeStyle.MEDIUM_40),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview
@Composable
private fun OAuthListWidgetSizeStyleMedium42Dark() {
    OAuthListWidget(
        style = OAuthListWidgetStyle.Light(sizeStyle = OAuthListWidgetSizeStyle.MEDIUM_42),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview
@Composable
private fun OAuthListWidgetSizeStyleMedium44Dark() {
    OAuthListWidget(
        style = OAuthListWidgetStyle.Light(sizeStyle = OAuthListWidgetSizeStyle.MEDIUM_44),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview
@Composable
private fun OAuthListWidgetSizeStyleMedium46Dark() {
    OAuthListWidget(
        style = OAuthListWidgetStyle.Light(sizeStyle = OAuthListWidgetSizeStyle.MEDIUM_46),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview
@Composable
private fun OAuthListWidgetSizeStyleLarge48Dark() {
    OAuthListWidget(
        style = OAuthListWidgetStyle.Light(sizeStyle = OAuthListWidgetSizeStyle.LARGE_48),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview
@Composable
private fun OAuthListWidgetSizeStyleLarge50Dark() {
    OAuthListWidget(
        style = OAuthListWidgetStyle.Light(sizeStyle = OAuthListWidgetSizeStyle.LARGE_50),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview
@Composable
private fun OAuthListWidgetSizeStyleLarge52Dark() {
    OAuthListWidget(
        style = OAuthListWidgetStyle.Light(sizeStyle = OAuthListWidgetSizeStyle.LARGE_52),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview
@Composable
private fun OAuthListWidgetSizeStyleLarge54Dark() {
    OAuthListWidget(
        style = OAuthListWidgetStyle.Light(sizeStyle = OAuthListWidgetSizeStyle.LARGE_54),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview
@Composable
private fun OAuthListWidgetSizeStyleLarge56Dark() {
    OAuthListWidget(
        style = OAuthListWidgetStyle.Light(sizeStyle = OAuthListWidgetSizeStyle.LARGE_56),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )
}

@Preview(locale = "")
@Composable
private fun OAuthListWidgetLangRu() {
    OAuthListWidget(
        oAuths = setOf(OAuth.OK, OAuth.MAIL, OAuth.VK),
        onAuth = { _, _ -> },
        onFail = { _, _ -> },
    )

}
