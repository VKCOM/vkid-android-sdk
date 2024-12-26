package com.vk.id.group.subscription.compose

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val THOUSAND = 1000
private const val MILLION = 1000000

/**
 * Composable function which creates a state for [OneTapBottomSheet] and can be used as `state` parameter.
 *
 * It provides [GroupSubscriptionSheetState] which can, for example, trigger hiding and showing the bottom sheet.
 */
@Composable
public fun rememberGroupSubscriptionSheetState(): GroupSubscriptionSheetState {
    return rememberGroupSubscrptionSheetStateInternal()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun GroupSubscriptionSheet(
    modifier: Modifier = Modifier,
    state: GroupSubscriptionSheetState = rememberGroupSubscriptionSheetState(),
    @Suppress("UnusedParameter") accessToken: String,
    @Suppress("UnusedParameter") groupId: String,
    @Suppress("UnusedParameter") onSuccess: () -> Unit,
    @Suppress("UnusedParameter") onCancel: () -> Unit
) {
    var status by rememberSaveable { mutableStateOf<GroupSubscriptionSheetStatus>(GroupSubscriptionSheetStatus.Init) }
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    state.showSheet = processSheetShow({ status = it }, { showBottomSheet = it }, coroutineScope, state)
    LaunchedEffect(Unit) {
        delay(@Suppress("MagicNumber") 1000)
        status = GroupSubscriptionSheetStatus.Failure
        delay(@Suppress("MagicNumber") 3000)
        val placeholderImage = "https://trkslon.ru/upload/shop_1/3/3/3/item_3337/item_image3337.jpg"
        val data = GroupSubscriptionSheetStatusData(
            groupImageUrl = placeholderImage,
            groupName = "Создание сайтов",
            groupDescription = "Официальная группа крпнейшей в России сети магазинов детских товаров",
            userImageUrls = listOf(
                placeholderImage,
                placeholderImage,
                placeholderImage,
            ),
            numberOfSubscribers = @Suppress("MagicNumber") 143,
            numberOfFriends = @Suppress("MagicNumber") 12,
            isGroupVerified = true,
        )
        status = GroupSubscriptionSheetStatus.Loaded(data)
        delay(@Suppress("MagicNumber") 1000)
        status = GroupSubscriptionSheetStatus.Subscribing(data)
    }
    if (showBottomSheet) {
        ModalBottomSheet(
            modifier = modifier.testTag("onetap_bottomsheet"),
            onDismissRequest = state::hide,
            sheetState = state.materialSheetState,
            containerColor = Color.Transparent,
            dragHandle = null
        ) {
            Box(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .safeContentPadding()
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .widthIn(min = 344.dp, max = 800.dp)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(colorResource(R.color.vkid_white)),
                contentAlignment = Alignment.Center,
            ) {
                when (val actualStatus = status) {
                    is GroupSubscriptionSheetStatus.Init -> InitState(state)
                    is GroupSubscriptionSheetStatus.Loaded -> LoadedState(state, actualStatus)
                    is GroupSubscriptionSheetStatus.Subscribing -> SubscribingState(state, actualStatus)
                    is GroupSubscriptionSheetStatus.Failure -> FailureState(state)
                }
            }
        }
    }
}

@Composable
private fun InitState(state: GroupSubscriptionSheetState) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(Modifier.weight(1f))
            CloseIcon(state::hide)
        }
        CircleProgress(R.drawable.vkid_sheet_spinner)
    }
}

@Composable
private fun LoadedState(state: GroupSubscriptionSheetState, status: GroupSubscriptionSheetStatus.Loaded) {
    DataState(state, status.data) {
        Text(
            text = "Подписаться на сообщество",
            modifier = Modifier,
            style = TextStyle(
                textAlign = TextAlign.Center,
                color = Color.White,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Medium,
            ),
        )
    }
}

@Composable
private fun SubscribingState(state: GroupSubscriptionSheetState, status: GroupSubscriptionSheetStatus.Subscribing) {
    DataState(state, status.data) {
        Box(modifier = Modifier.size(24.dp)) {
            CircleProgress(R.drawable.vkid_sheet_spinner_white)
        }
    }
}

@Composable
@Suppress("LongMethod")
private fun FailureState(state: GroupSubscriptionSheetState) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
            painter = painterResource(R.drawable.vkid_sheet_error),
            contentDescription = null,
            contentScale = ContentScale.Fit,
        )
        Text(
            text = "Не удалось подписаться",
            modifier = Modifier,
            style = TextStyle(
                textAlign = TextAlign.Center,
                color = Color.Black,
                fontSize = 20.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Medium,
            ),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Попробуйте еще раз",
            modifier = Modifier,
            style = TextStyle(
                textAlign = TextAlign.Center,
                color = colorResource(R.color.vkid_sheet_secondary),
                fontSize = 14.sp,
                lineHeight = 18.sp,
                fontWeight = FontWeight.Normal,
            ),
        )
        Spacer(Modifier.height(32.dp))
        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Попробуйте еще раз",
                modifier = Modifier,
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Medium,
                ),
            )
        }
        Spacer(Modifier.height(12.dp))
        TextButton(
            onClick = state::hide,
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Отмена",
                modifier = Modifier,
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    color = colorResource(R.color.vkid_azure_300),
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Medium,
                ),
            )
        }
    }
}

@Composable
private fun DataState(
    state: GroupSubscriptionSheetState,
    data: GroupSubscriptionSheetStatusData,
    mainButtonContent: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DataStateHeader(state, data)
        Spacer(modifier = Modifier.height(12.dp))
        DataStateLabels(data)
        Spacer(modifier = Modifier.height(8.dp))
        DataStateSubscribers(data)
        Spacer(Modifier.height(20.dp))
        DataStateButtons(state, mainButtonContent)
    }
}

@Composable
private fun CircleProgress(
    @DrawableRes progressRes: Int
) {
    val infiniteTransition = rememberInfiniteTransition(label = "vkid_auth_in_progress_spinner")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "VKID Auth in progress"
    )
    Image(
        modifier = Modifier.graphicsLayer {
            rotationZ = angle
        },
        painter = painterResource(progressRes),
        contentDescription = null,
    )
}

@Composable
private fun ColumnScope.DataStateButtons(
    state: GroupSubscriptionSheetState,
    mainButtonContent: @Composable () -> Unit
) {
    Button(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        mainButtonContent()
    }
    Spacer(Modifier.height(12.dp))
    FilledTonalButton(
        onClick = state::hide,
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.filledTonalButtonColors().copy(
            containerColor = Color(red = 0, green = 0, blue = 0, alpha = 0x0A)
        ),
    ) {
        Text(
            text = "В другой раз",
            modifier = Modifier,
            style = TextStyle(
                textAlign = TextAlign.Center,
                color = colorResource(R.color.vkid_azure_300),
                fontSize = 16.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Medium,
            ),
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
@Suppress("LongMethod")
private fun ColumnScope.DataStateSubscribers(
    data: GroupSubscriptionSheetStatusData
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .width(72.dp)
                .height(24.dp)
        ) {
            data.userImageUrls.forEachIndexed { index, userImageUrl ->
                val transformation = if (index > 0) UserImageTransformation() else CircleCropTransformation()
                Row {
                    Spacer(modifier = Modifier.width(24.dp * index))
                    AsyncImage(
                        modifier = Modifier
                            .height(24.dp)
                            .width(24.dp),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(userImageUrl)
                            .transformations(transformation)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.FillHeight,
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        FlowRow(verticalArrangement = Arrangement.Center) {
            val numberOfSubs = when {
                data.numberOfSubscribers < THOUSAND -> data.numberOfSubscribers
                data.numberOfSubscribers < MILLION -> (data.numberOfSubscribers / THOUSAND).toString() + "K"
                else -> (data.numberOfSubscribers / MILLION).toString() + "M"
            }
            val subscribersText = "$numberOfSubs подписчиков "
            val friendsText = "· ${data.numberOfFriends} друзей"
            Text(
                text = subscribersText,
                modifier = Modifier,
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    color = colorResource(R.color.vkid_sheet_secondary),
                    fontSize = 15.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Normal,
                ),
            )
            if (data.numberOfFriends > 0) {
                Text(
                    text = friendsText,
                    modifier = Modifier,
                    style = TextStyle(
                        textAlign = TextAlign.Center,
                        color = colorResource(R.color.vkid_sheet_secondary),
                        fontSize = 15.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Normal,
                    ),
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.DataStateLabels(
    data: GroupSubscriptionSheetStatusData
) {
    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center,
    ) {
        BoxWithConstraints {
            TightWrapText(
                text = data.groupName,
                modifier = Modifier.widthIn(max = maxWidth - (if (data.isGroupVerified) 20.dp else 0.dp)),
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    fontSize = 23.sp,
                    lineHeight = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
        if (data.isGroupVerified) {
            Spacer(modifier = Modifier.width(2.dp))
            val paddingBottom = with(LocalDensity.current) { 4.sp.toDp() }
            Image(
                modifier = Modifier
                    .height(20.dp + paddingBottom)
                    .width(20.dp)
                    .padding(bottom = paddingBottom),
                painter = painterResource(R.drawable.vkid_verified_20),
                contentDescription = null,
            )
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
    Text(
        text = data.groupDescription,
        modifier = Modifier,
        style = TextStyle(
            textAlign = TextAlign.Start,
            color = colorResource(R.color.vkid_sheet_secondary),
            fontSize = 16.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.Normal,
        ),
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun ColumnScope.DataStateHeader(
    state: GroupSubscriptionSheetState,
    data: GroupSubscriptionSheetStatusData
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Spacer(Modifier.weight(1f))
        CloseIcon(state::hide)
    }
    Box(modifier = Modifier.size(76.dp), contentAlignment = Alignment.TopStart) {
        AsyncImage(
            modifier = Modifier
                .width(72.dp)
                .height(76.dp)
                .padding(bottom = 4.dp)
                .clip(CircleShape),
            model = data.groupImageUrl,
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
        )
        Image(
            modifier = Modifier.padding(top = 48.dp, start = 48.dp),
            painter = painterResource(R.drawable.vkid_sheet_vk_logo),
            contentDescription = null,
            contentScale = ContentScale.Fit,
        )
    }
}

@Composable
private fun CloseIcon(dismissSheet: () -> Unit) {
    Box(
        modifier = Modifier.clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = {
            dismissSheet()
        })
    ) {
        Image(
            painter = painterResource(R.drawable.vkid_group_subscription_close),
            contentDescription = null,
            contentScale = ContentScale.Fit,
        )
    }
}

/**
 * Manages the state of the One Tap Bottom Sheet. Should be created with [rememberOneTapBottomSheetState]
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun processSheetShow(
    onAuthStatusChange: (GroupSubscriptionSheetStatus) -> Unit,
    onShowBottomSheetChange: (Boolean) -> Unit,
    coroutineScope: CoroutineScope,
    state: GroupSubscriptionSheetState
): (Boolean) -> Unit = remember {
    {
        val show = it
        if (show) {
            onAuthStatusChange(GroupSubscriptionSheetStatus.Init)
        }
        if (show) {
            onShowBottomSheetChange(true)
        } else {
            coroutineScope.launch {
                state.materialSheetState.hide()
            }.invokeOnCompletion {
                if (!state.isVisible) {
                    onShowBottomSheetChange(false)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun rememberGroupSubscrptionSheetStateInternal(): GroupSubscriptionSheetState {
    val materialSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    return remember(materialSheetState) {
        GroupSubscriptionSheetState(
            materialSheetState = materialSheetState
        )
    }
}

@Preview
@Composable
private fun DataStatePreview() {
    val placeholderImage = "https://trkslon.ru/upload/shop_1/3/3/3/item_3337/item_image3337.jpg"
    DataState(
        rememberGroupSubscriptionSheetState(),
        GroupSubscriptionSheetStatusData(
            groupImageUrl = placeholderImage,
            groupName = "Создание сайтов",
            groupDescription = "Официальная группа крпнейшей в России сети магазинов детских товаров",
            userImageUrls = listOf(
                placeholderImage,
                placeholderImage,
                placeholderImage,
            ),
            numberOfSubscribers = 143,
            numberOfFriends = 12,
            isGroupVerified = true,
        )
    ) {
        Text("button text")
    }
}
