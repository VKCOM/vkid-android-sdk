@file:OptIn(InternalVKIDApi::class)

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.vk.id.VKID
import com.vk.id.common.InternalVKIDApi
import com.vk.id.group.subscription.common.style.GroupSubscriptionButtonsCornersStyle
import com.vk.id.group.subscription.common.style.GroupSubscriptionButtonsSizeStyle
import com.vk.id.group.subscription.common.style.GroupSubscriptionSheetCornersStyle
import com.vk.id.group.subscription.common.style.GroupSubscriptionStyle
import com.vk.id.group.subscription.compose.ui.FailureState
import com.vk.id.group.subscription.compose.ui.GroupSubscriptionSheetStatus
import com.vk.id.group.subscription.compose.ui.GroupSubscriptionSheetStatusData
import com.vk.id.group.subscription.compose.ui.LoadedState
import com.vk.id.group.subscription.compose.ui.ResubscribingState
import com.vk.id.group.subscription.compose.ui.SubscribingState
import com.vk.id.group.subscription.compose.ui.rememberGroupSubscriptionSheetState

private val DATA = GroupSubscriptionSheetStatusData(
    groupImageUrl = "",
    groupDescription = "group description",
    groupName = "group name",
    userImageUrls = listOf("", "", ""),
    subscriberCount = 1,
    friendsCount = 1,
    isGroupVerified = true,
)

@Preview
@Composable
private fun LoadedStatePreview() {
    VKID.initForScreenshotTests(LocalContext.current)
    LoadedState(
        style = GroupSubscriptionStyle.Light(
            cornersStyle = GroupSubscriptionSheetCornersStyle.Rounded,
            buttonsCornersStyle = GroupSubscriptionButtonsCornersStyle.Custom(4f),
            buttonsSizeStyle = GroupSubscriptionButtonsSizeStyle.SMALL_32
        ),
        state = rememberGroupSubscriptionSheetState(),
        status = GroupSubscriptionSheetStatus.Loaded(DATA),
        onFail = {},
    ) { }
}

@Preview
@Composable
private fun LoadedDarkStatePreview() {
    VKID.initForScreenshotTests(LocalContext.current)
    LoadedState(
        style = GroupSubscriptionStyle.Dark(),
        state = rememberGroupSubscriptionSheetState(),
        status = GroupSubscriptionSheetStatus.Loaded(DATA),
        onFail = {},
    ) { }
}

@Preview
@Composable
private fun SubscribingStatePreview() {
    VKID.initForScreenshotTests(LocalContext.current)
    SubscribingState(
        style = GroupSubscriptionStyle.Light(
            cornersStyle = GroupSubscriptionSheetCornersStyle.Rounded,
            buttonsCornersStyle = GroupSubscriptionButtonsCornersStyle.Custom(4f),
            buttonsSizeStyle = GroupSubscriptionButtonsSizeStyle.SMALL_32
        ),
        state = rememberGroupSubscriptionSheetState(),
        status = GroupSubscriptionSheetStatus.Subscribing(DATA),
        onFail = {},
    )
}

@Preview
@Composable
private fun SubscribingDarkStatePreview() {
    VKID.initForScreenshotTests(LocalContext.current)
    SubscribingState(
        style = GroupSubscriptionStyle.Dark(),
        state = rememberGroupSubscriptionSheetState(),
        status = GroupSubscriptionSheetStatus.Subscribing(DATA),
        onFail = {},
    )
}

@Preview
@Composable
private fun ResubscribingStatePreview() {
    VKID.initForScreenshotTests(LocalContext.current)
    ResubscribingState(
        style = GroupSubscriptionStyle.Light(
            cornersStyle = GroupSubscriptionSheetCornersStyle.Rounded,
            buttonsCornersStyle = GroupSubscriptionButtonsCornersStyle.Custom(4f),
            buttonsSizeStyle = GroupSubscriptionButtonsSizeStyle.SMALL_32
        ),
        state = rememberGroupSubscriptionSheetState(),
        onRetry = {},
        onFail = {},
    )
}

@Preview
@Composable
private fun ResubscribingDarkStatePreview() {
    VKID.initForScreenshotTests(LocalContext.current)
    ResubscribingState(
        style = GroupSubscriptionStyle.Dark(),
        state = rememberGroupSubscriptionSheetState(),
        onRetry = {},
        onFail = {},
    )
}

@Preview
@Composable
private fun FailureStatePreview() {
    VKID.initForScreenshotTests(LocalContext.current)
    FailureState(
        style = GroupSubscriptionStyle.Light(
            cornersStyle = GroupSubscriptionSheetCornersStyle.Rounded,
            buttonsCornersStyle = GroupSubscriptionButtonsCornersStyle.Custom(4f),
            buttonsSizeStyle = GroupSubscriptionButtonsSizeStyle.SMALL_32
        ),
        state = rememberGroupSubscriptionSheetState(),
        onRetry = {},
        onFail = {},
    )
}

@Preview
@Composable
private fun FailureDarkStatePreview() {
    VKID.initForScreenshotTests(LocalContext.current)
    FailureState(
        style = GroupSubscriptionStyle.Dark(),
        state = rememberGroupSubscriptionSheetState(),
        onRetry = {},
        onFail = {},
    )
}
