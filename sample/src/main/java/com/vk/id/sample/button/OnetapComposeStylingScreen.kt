package com.vk.id.sample.button

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vk.id.AccessToken
import com.vk.id.VKIDAuthFail
import com.vk.id.onetap.common.button.VKIDButtonCornersStyle
import com.vk.id.onetap.common.button.VKIDButtonElevationStyle
import com.vk.id.onetap.common.button.VKIDButtonSizeStyle
import com.vk.id.onetap.common.button.VKIDButtonStyle
import com.vk.id.onetap.compose.button.VKIDButton
import com.vk.id.onetap.compose.button.VKIDButtonSmall
import com.vk.id.sample.R

private const val TOKEN_VISIBLE_CHARACTERS = 10

@Composable
fun OnetapComposeStylingScreen() {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        buttonsData.forEach {
            when (it) {
                is ListItem.Spacer -> Spacer(
                    modifier = Modifier
                        .darkBackground(it.isDarkBackground)
                        .height(24.dp)
                        .fillMaxWidth(),
                )
                is ListItem.HalfSpacer -> Spacer(
                    modifier = Modifier
                        .darkBackground(it.isDarkBackground)
                        .height(12.dp)
                        .fillMaxWidth(),
                )
                is ListItem.Title -> Row(
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        modifier = Modifier.padding(all = 8.dp),
                        text = it.text,
                        fontSize = 24.sp,
                        lineHeight = 28.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                is ListItem.Button -> Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .darkBackground(it.isDarkBackground)
                        .fillMaxWidth()
                ) {
                    VKIDButton(
                        modifier = it.modifier,
                        style = it.style,
                        onAuth = { onVKIDAuthSuccess(context, it) },
                        onFail = { onVKIDAuthFail(context, it) }
                    )
                }
                is ListItem.SmallButton -> VKIDButtonSmall(
                    style = it.style,
                    onAuth = { onVKIDAuthSuccess(context, it) },
                    onFail = { onVKIDAuthFail(context, it) }
                )
            }
        }
    }
}

internal fun onVKIDAuthSuccess(context: Context, accessToken: AccessToken) {
    val token = accessToken.token.hideLastCharacters(TOKEN_VISIBLE_CHARACTERS)
    showToast(context, "There is token: $token")
}

private fun showToast(context: Context, text: String) {
    Toast.makeText(context, text, Toast.LENGTH_LONG).show()
}

internal fun onVKIDAuthFail(context: Context, fail: VKIDAuthFail) {
    when (fail) {
        is VKIDAuthFail.Canceled -> {
            showToast(context, "Auth canceled")
        }

        else -> {
            showToast(context, "Something wrong: ${fail.description}")
        }
    }
}

private fun String.hideLastCharacters(firstCharactersToKeepVisible: Int): String {
    return if (this.length <= firstCharactersToKeepVisible) {
        this
    } else {
        this.substring(0, firstCharactersToKeepVisible) + "..."
    }
}

private fun Modifier.darkBackground(isDarkBackground: Boolean) = composed {
    if (isDarkBackground) {
        background(color = colorResource(id = R.color.vkid_gray900))
    } else {
        this
    }
}

private val buttonsData = listOf(
    ListItem.Title("Primary"),
    ListItem.HalfSpacer(),
    ListItem.Button(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.None,
        )
    ),
    ListItem.Spacer(),
    ListItem.Button(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
        ),
    ),
    ListItem.Spacer(),
    ListItem.Button(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Round,
        ),
    ),
    ListItem.HalfSpacer(),
    ListItem.Title("Secondary light"),
    ListItem.HalfSpacer(isDarkBackground = true),
    ListItem.Button(
        style = VKIDButtonStyle.TransparentLight(
            cornersStyle = VKIDButtonCornersStyle.None,
        ),
        isDarkBackground = true,
    ),
    ListItem.Spacer(isDarkBackground = true),
    ListItem.Button(
        style = VKIDButtonStyle.TransparentLight(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
        ),
        isDarkBackground = true,
    ),
    ListItem.Spacer(isDarkBackground = true),
    ListItem.Button(
        style = VKIDButtonStyle.TransparentLight(
            cornersStyle = VKIDButtonCornersStyle.Round,
        ),
        isDarkBackground = true,
    ),
    ListItem.HalfSpacer(isDarkBackground = true),
    ListItem.Title("Secondary dark"),
    ListItem.HalfSpacer(),
    ListItem.Button(
        style = VKIDButtonStyle.TransparentDark(
            cornersStyle = VKIDButtonCornersStyle.None,
        ),
    ),
    ListItem.Spacer(),
    ListItem.Button(
        style = VKIDButtonStyle.TransparentDark(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
        ),
    ),
    ListItem.Spacer(),
    ListItem.Button(
        style = VKIDButtonStyle.TransparentDark(
            cornersStyle = VKIDButtonCornersStyle.Round,
        ),
    ),
    ListItem.HalfSpacer(),
    ListItem.Title("Elevation"),
    ListItem.HalfSpacer(),
    ListItem.Button(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            elevationStyle = VKIDButtonElevationStyle.Default
        )
    ),
    ListItem.Spacer(),
    ListItem.Button(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            elevationStyle = VKIDButtonElevationStyle.Custom(4)
        ),
    ),
    ListItem.Spacer(),
    ListItem.Button(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            elevationStyle = VKIDButtonElevationStyle.Custom(8)
        ),
    ),
    ListItem.HalfSpacer(),
    ListItem.Title("Sizes"),
    ListItem.HalfSpacer(),
    ListItem.Button(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.SMALL_32
        )
    ),
    ListItem.Spacer(),
    ListItem.Button(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.SMALL_34
        ),
    ),
    ListItem.Spacer(),
    ListItem.Button(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.SMALL_36
        ),
    ),
    ListItem.Spacer(),
    ListItem.Button(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.SMALL_38
        ),
    ),
    ListItem.Spacer(),
    ListItem.Button(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.MEDIUM_40
        ),
    ),
    ListItem.Spacer(),
    ListItem.Button(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.MEDIUM_42
        ),
    ),
    ListItem.Spacer(),
    ListItem.Button(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.MEDIUM_44
        ),
    ),
    ListItem.Spacer(),
    ListItem.Button(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.MEDIUM_46
        ),
    ),
    ListItem.Spacer(),
    ListItem.Button(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.LARGE_48
        ),
    ),
    ListItem.Spacer(),
    ListItem.Button(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.LARGE_50
        ),
    ),
    ListItem.Spacer(),
    ListItem.Button(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.LARGE_52
        ),
    ),
    ListItem.Spacer(),
    ListItem.Button(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.LARGE_54
        ),
    ),
    ListItem.Spacer(),
    ListItem.Button(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Rounded,
            sizeStyle = VKIDButtonSizeStyle.LARGE_56
        ),
    ),
    ListItem.HalfSpacer(),
    ListItem.Title("Small button"),
    ListItem.HalfSpacer(),
    ListItem.SmallButton(
        style = VKIDButtonStyle.Blue(
            cornersStyle = VKIDButtonCornersStyle.Default,
            sizeStyle = VKIDButtonSizeStyle.MEDIUM_44,
            elevationStyle = VKIDButtonElevationStyle.Default
        ),
    ),
    ListItem.HalfSpacer(),
)

private sealed class ListItem {

    data class HalfSpacer(
        val isDarkBackground: Boolean = false
    ) : ListItem()

    data class Spacer(
        val isDarkBackground: Boolean = false
    ) : ListItem()

    data class Title(
        val text: String
    ) : ListItem()

    data class Button(
        val style: VKIDButtonStyle,
        val modifier: Modifier = Modifier.width(335.dp),
        val isDarkBackground: Boolean = false,
    ) : ListItem()

    data class SmallButton(
        val style: VKIDButtonStyle
    ) : ListItem()
}
