@file:Suppress("DEPRECATION")

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.vk.id.common.InternalVKIDApi
import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.onetap.common.OneTapStyle
import com.vk.id.onetap.common.button.style.OneTapButtonCornersStyle
import com.vk.id.onetap.common.button.style.OneTapButtonElevationStyle
import com.vk.id.onetap.common.button.style.OneTapButtonSizeStyle
import com.vk.id.onetap.compose.onetap.OneTap


@Preview
@Composable
private fun OneTapIconStylePreview() {
    OneTap(
        style = OneTapStyle.Icon(),
        onAuth = { _, _ -> },
    )
}

@Preview
@Composable
private fun OneTapSecondaryLightStylePreview() {
    OneTap(
        style = OneTapStyle.SecondaryLight(),
        onAuth = { _, _ -> },
        signInAnotherAccountButtonEnabled = true,
        oAuths = setOf(OneTapOAuth.OK, OneTapOAuth.MAIL),
    )
}

@Preview
@Composable
private fun OneTapTransparentLightStylePreview() {
    OneTap(
        style = OneTapStyle.TransparentLight(),
        onAuth = { _, _ -> },
        signInAnotherAccountButtonEnabled = true,
        oAuths = setOf(OneTapOAuth.OK, OneTapOAuth.MAIL),
    )
}

@Preview
@Composable
private fun OneTapLightStylePreview() {
    OneTap(onAuth = { _, _ -> })
}

@Preview
@Composable
private fun OneTapSecondaryDarkStylePreview() {
    OneTap(
        style = OneTapStyle.SecondaryDark(),
        onAuth = { _, _ -> },
        signInAnotherAccountButtonEnabled = true,
        oAuths = setOf(OneTapOAuth.OK, OneTapOAuth.MAIL),
    )
}

@Preview
@Composable
private fun OneTapTransparentDarkStylePreview() {
    OneTap(
        style = OneTapStyle.TransparentDark(),
        onAuth = { _, _ -> },
        signInAnotherAccountButtonEnabled = true,
        oAuths = setOf(OneTapOAuth.OK, OneTapOAuth.MAIL),
    )
}

@Preview
@Composable
private fun OneTapNoneCornerRadiusStylePreview() {
    OneTap(
        style = OneTapStyle.TransparentLight(cornersStyle = OneTapButtonCornersStyle.None),
        onAuth = { _, _ -> }
    )
}

@Preview
@Composable
private fun OneTapRoundedCornerRadiusStylePreview() {
    OneTap(
        style = OneTapStyle.TransparentLight(cornersStyle = OneTapButtonCornersStyle.Rounded),
        onAuth = { _, _ -> }
    )
}

@Preview
@Composable
private fun OneTapRoundCornerRadiusStylePreview() {
    OneTap(
        style = OneTapStyle.TransparentLight(cornersStyle = OneTapButtonCornersStyle.Round),
        onAuth = { _, _ -> }
    )
}

// Text is barely visible on screenshot because of transparentDark style on white background
// See https://issuetracker.google.com/issues/352263200
@Preview
@Composable
private fun OneTapButtonSmall32StylePreview() {
    OneTap(
        style = OneTapStyle.TransparentDark(sizeStyle = OneTapButtonSizeStyle.SMALL_32),
        onAuth = { _, _ -> }
    )
}

@Preview
@Composable
private fun OneTapButtonSmall34StylePreview() {
    OneTap(
        style = OneTapStyle.TransparentDark(sizeStyle = OneTapButtonSizeStyle.SMALL_34),
        onAuth = { _, _ -> }
    )
}

@Preview
@Composable
private fun OneTapButtonSmall36StylePreview() {
    OneTap(
        style = OneTapStyle.TransparentDark(sizeStyle = OneTapButtonSizeStyle.SMALL_36),
        onAuth = { _, _ -> }
    )
}

@Preview
@Composable
private fun OneTapButtonSmall38StylePreview() {
    OneTap(
        style = OneTapStyle.TransparentDark(sizeStyle = OneTapButtonSizeStyle.SMALL_38),
        onAuth = { _, _ -> }
    )
}

@Preview
@Composable
private fun OneTapButtonMedium40StylePreview() {
    OneTap(
        style = OneTapStyle.TransparentLight(sizeStyle = OneTapButtonSizeStyle.MEDIUM_40),
        onAuth = { _, _ -> },
    )
}

@Preview
@Composable
private fun OneTapButtonMedium42StylePreview() {
    OneTap(
        style = OneTapStyle.TransparentLight(sizeStyle = OneTapButtonSizeStyle.MEDIUM_42),
        onAuth = { _, _ -> }
    )
}

@Preview
@Composable
private fun OneTapButtonMedium44StylePreview() {
    OneTap(
        style = OneTapStyle.TransparentLight(sizeStyle = OneTapButtonSizeStyle.MEDIUM_44),
        onAuth = { _, _ -> }
    )
}

@Preview
@Composable
private fun OneTapButtonMedium46StylePreview() {
    OneTap(
        style = OneTapStyle.TransparentLight(sizeStyle = OneTapButtonSizeStyle.MEDIUM_46),
        onAuth = { _, _ -> }
    )


}

@Preview
@Composable
private fun OneTapButtonLarge48StylePreview() {
    OneTap(
        style = OneTapStyle.Icon(sizeStyle = OneTapButtonSizeStyle.LARGE_48),
        onAuth = { _, _ -> },
    )
}

@Preview
@Composable
private fun OneTapButtonLarge50StylePreview() {
    OneTap(
        style = OneTapStyle.Icon(sizeStyle = OneTapButtonSizeStyle.LARGE_50),
        onAuth = { _, _ -> }
    )
}

@Preview
@Composable
private fun OneTapButtonLarge52StylePreview() {
    OneTap(
        style = OneTapStyle.Icon(sizeStyle = OneTapButtonSizeStyle.LARGE_52),
        onAuth = { _, _ -> }
    )
}

@Preview
@Composable
private fun OneTapButtonLarge54StylePreview() {
    OneTap(
        style = OneTapStyle.Icon(sizeStyle = OneTapButtonSizeStyle.LARGE_54),
        onAuth = { _, _ -> }
    )
}

@Preview
@Composable
private fun OneTapButtonLarge56StylePreview() {
    OneTap(
        style = OneTapStyle.Icon(sizeStyle = OneTapButtonSizeStyle.LARGE_56),
        onAuth = { _, _ -> }
    )
}

@Preview
@Composable
private fun OneTapButtonElevationStylePreview() {
    OneTap(
        style = OneTapStyle.Light(elevationStyle = OneTapButtonElevationStyle.Custom(1000f)),
        onAuth = { _, _ -> }
    )
}

@Preview
@Composable
private fun OneTapOKOauthPreview() {
    OneTap(oAuths = setOf(OneTapOAuth.OK),
        onAuth = { _, _ -> })
}

@Preview
@Composable
private fun OneTapMailOauthPreview() {
    OneTap(oAuths = setOf(OneTapOAuth.MAIL),
        onAuth = { _, _ -> })
}

@Preview
@Composable
private fun OneTapOKAndMailOauthPreview() {
    OneTap(oAuths = setOf(OneTapOAuth.OK, OneTapOAuth.MAIL),
        onAuth = { _, _ -> })
}

@Preview
@Composable
private fun OneTapMailAndOKOauthsPreview() {
    OneTap(oAuths = setOf(OneTapOAuth.MAIL, OneTapOAuth.OK),
        onAuth = { _, _ -> })
}

@Preview
@Composable
private fun OneTapFastAuthEnabledPreview() {
    OneTap(signInAnotherAccountButtonEnabled = true,
        fastAuthEnabled = false,
        onAuth = { _, _ -> })
}

@Preview
@Composable
private fun OneTapSignInAnotherAccountButtonEnabledPreview() {
    OneTap(signInAnotherAccountButtonEnabled = true,
        onAuth = { _, _ -> })
}
