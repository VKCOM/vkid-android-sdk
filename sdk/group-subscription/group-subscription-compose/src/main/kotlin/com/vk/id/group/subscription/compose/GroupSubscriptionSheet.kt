@file:OptIn(InternalVKIDApi::class)

package com.vk.id.group.subscription.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
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
import com.vk.id.VKID
import com.vk.id.common.InternalVKIDApi
import com.vk.id.group.subscription.common.VKIDGroupSubscriptionFail
import com.vk.id.group.subscription.compose.close.CloseIcon
import com.vk.id.group.subscription.compose.interactor.InternalVKIDGroupSubscriptionInteractor
import com.vk.id.group.subscription.compose.interactor.ServiceAccountException
import com.vk.id.group.subscription.compose.progress.CircleProgressWhite
import com.vk.id.group.subscription.compose.snackbar.GroupSubscriptionSnackbar
import com.vk.id.group.subscription.compose.util.PrimaryButton
import com.vk.id.group.subscription.compose.util.SecondaryButton
import com.vk.id.group.subscription.compose.util.TightWrapText
import com.vk.id.group.subscription.compose.util.UserImageTransformation
import com.vk.id.network.groupsubscription.exception.InternalVKIDAlreadyGroupMemberException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val THOUSAND = 1000
private const val MILLION = 1000000

/**
 * Composable function which creates a state for [GroupSubscriptionSheet] and can be used as `state` parameter.
 *
 * It provides [GroupSubscriptionSheetState] which can, for example, trigger hiding and showing the bottom sheet.
 */
@Composable
public fun rememberGroupSubscriptionSheetState(): GroupSubscriptionSheetState {
    return rememberGroupSubscriptionSheetStateInternal()
}

@Composable
public fun GroupSubscriptionSnackbarHost(
    snackbarHostState: SnackbarHostState
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        SnackbarHost(
            modifier = Modifier.align(Alignment.BottomCenter),
            hostState = snackbarHostState
        ) { snackbarData: SnackbarData ->
            GroupSubscriptionSnackbar(snackbarData.visuals.message)
        }
    }
}

@Composable
@Suppress("ModifierNotUsedAtRoot")
public fun GroupSubscriptionSheet(
    modifier: Modifier = Modifier,
    state: GroupSubscriptionSheetState = rememberGroupSubscriptionSheetState(),
    accessTokenProvider: (() -> String)? = null,
    groupId: String,
    onSuccess: () -> Unit,
    onFail: (VKIDGroupSubscriptionFail) -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }
    Box {
        GroupSubscriptionSnackbarHost(snackbarHostState)
        GroupSubscriptionSheet(
            modifier = modifier,
            state = state,
            accessTokenProvider = accessTokenProvider,
            groupId = groupId,
            onSuccess = onSuccess,
            onFail = onFail,
            snackbarHostState = snackbarHostState,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Suppress("LongMethod")
public fun GroupSubscriptionSheet(
    modifier: Modifier = Modifier,
    state: GroupSubscriptionSheetState = rememberGroupSubscriptionSheetState(),
    accessTokenProvider: (() -> String)? = null,
    groupId: String,
    onSuccess: () -> Unit,
    onFail: (VKIDGroupSubscriptionFail) -> Unit = {},
    snackbarHostState: SnackbarHostState,
) {
    val coroutineScope = rememberCoroutineScope()
    val status = rememberSaveable { mutableStateOf<GroupSubscriptionSheetStatus>(GroupSubscriptionSheetStatus.Init) }
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    val rememberedOnFail by rememberUpdatedState(onFail)
    val snackbarLabel = stringResource(R.string.vkid_group_subscription_snackbar_label)
    val actualOnSuccess by rememberUpdatedState {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(snackbarLabel)
        }
        onSuccess()
    }
    processSheetShow(
        { status.value = it },
        { showBottomSheet = it },
        coroutineScope,
        state,
    )
    val interactor = remember(groupId) {
        InternalVKIDGroupSubscriptionInteractor(
            apiService = VKID.instance.groupSubscriptionApiService,
            tokenStorage = VKID.instance.tokenStorage,
            groupId = groupId,
            externalAccessTokenProvider = accessTokenProvider,
        )
    }
    remember {
        state.showSheet = {
            if (it) {
                status.value = GroupSubscriptionSheetStatus.Init
                coroutineScope.launch {
                    try {
                        with(interactor.loadGroup()) {
                            status.value = GroupSubscriptionSheetStatus.Loaded(
                                GroupSubscriptionSheetStatusData(
                                    groupImageUrl = imageUrl,
                                    groupName = name,
                                    groupDescription = description,
                                    userImageUrls = userImageUrls,
                                    friendsCount = friendsCount,
                                    subscriberCount = subscriberCount,
                                    isGroupVerified = isVerified,
                                )
                            )
                            showBottomSheet = true
                        }
                    } catch (@Suppress("SwallowedException") e: InternalVKIDAlreadyGroupMemberException) {
                        state.hide()
                        rememberedOnFail(VKIDGroupSubscriptionFail.AlreadyGroupMember())
                        showBottomSheet = false
                    } catch (@Suppress("SwallowedException") e: ServiceAccountException) {
                        state.hide()
                        rememberedOnFail(VKIDGroupSubscriptionFail.ServiceAccount())
                        showBottomSheet = false
                    } catch (@Suppress("TooGenericExceptionCaught") t: Throwable) {
                        state.hide()
                        rememberedOnFail(VKIDGroupSubscriptionFail.Other(throwable = t))
                        showBottomSheet = false
                    }
                }
            } else {
                showBottomSheet = false
            }
        }
        1 // Ignore result, just make sync LaunchedEffect(Unit)
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
                when (val actualStatus = status.value) {
                    is GroupSubscriptionSheetStatus.Init -> Unit
                    is GroupSubscriptionSheetStatus.Loaded -> LoadedState(state, actualStatus) {
                        subscribeToGroup(
                            status,
                            actualStatus.data,
                            state,
                            coroutineScope,
                            interactor,
                            actualOnSuccess,
                            GroupSubscriptionSheetStatus.Subscribing(actualStatus.data)
                        )
                    }

                    is GroupSubscriptionSheetStatus.Subscribing -> SubscribingState(state, actualStatus)
                    is GroupSubscriptionSheetStatus.Failure -> FailureState(state) {
                        subscribeToGroup(
                            status,
                            actualStatus.data,
                            state,
                            coroutineScope,
                            interactor,
                            actualOnSuccess,
                            GroupSubscriptionSheetStatus.Resubscribing(actualStatus.data)
                        )
                    }

                    is GroupSubscriptionSheetStatus.Resubscribing -> ResubscribingState(state) {
                        subscribeToGroup(
                            status,
                            actualStatus.data,
                            state,
                            coroutineScope,
                            interactor,
                            actualOnSuccess,
                            GroupSubscriptionSheetStatus.Resubscribing(actualStatus.data)
                        )
                    }
                }
            }
        }
    }
}

@Suppress("LongParameterList")
private fun subscribeToGroup(
    status: MutableState<GroupSubscriptionSheetStatus>,
    data: GroupSubscriptionSheetStatusData,
    state: GroupSubscriptionSheetState,
    coroutineScope: CoroutineScope,
    interactor: InternalVKIDGroupSubscriptionInteractor,
    onSuccess: () -> Unit,
    progressStatus: GroupSubscriptionSheetStatus
) {
    status.value = progressStatus
    coroutineScope.launch {
        try {
            interactor.subscribeToGroup()
            onSuccess()
            state.hide()
        } catch (@Suppress("TooGenericExceptionCaught", "SwallowedException") t: Throwable) {
            status.value = GroupSubscriptionSheetStatus.Failure(data)
        }
    }
}

@Composable
private fun LoadedState(
    state: GroupSubscriptionSheetState,
    status: GroupSubscriptionSheetStatus.Loaded,
    onSubscribeButtonClick: () -> Unit,
) {
    DataState(state, status.data, onSubscribeButtonClick) {
        Text(
            text = stringResource(R.string.vkid_group_subscription_primary),
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
private fun SubscribingState(
    state: GroupSubscriptionSheetState,
    status: GroupSubscriptionSheetStatus.Subscribing,
) {
    DataState(state, status.data, {}) {
        Box(modifier = Modifier.size(24.dp)) {
            CircleProgressWhite("Subscribing to group spinner")
        }
    }
}

@Composable
private fun ResubscribingState(
    state: GroupSubscriptionSheetState,
    onRetry: () -> Unit,
) {
    FailureDataState(state, onRetry) {
        Box(modifier = Modifier.size(24.dp)) {
            CircleProgressWhite("Resubscribing to group spinner")
        }
    }
}

@Composable
private fun FailureState(
    state: GroupSubscriptionSheetState,
    onRetry: () -> Unit,
) {
    FailureDataState(state, onRetry) {
        Text(
            text = stringResource(R.string.vkid_group_subscription_fail_primary),
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
@Suppress("LongMethod")
private fun FailureDataState(
    state: GroupSubscriptionSheetState,
    onRetry: () -> Unit,
    retryButtonContent: @Composable () -> Unit,
) {
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
            text = stringResource(R.string.vkid_group_subscription_fail_title),
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
            text = stringResource(R.string.vkid_group_subscription_fail_description),
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
        PrimaryButton(onRetry) {
            retryButtonContent()
        }
        Spacer(Modifier.height(12.dp))
        SecondaryButton(stringResource(R.string.vkid_group_subscription_fail_secondary), state::hide)
    }
}

@Composable
private fun DataState(
    state: GroupSubscriptionSheetState,
    data: GroupSubscriptionSheetStatusData,
    onSubscribeButtonClick: () -> Unit,
    subscribeButtonContent: @Composable () -> Unit
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
        DataStateButtons(onSubscribeButtonClick, state, subscribeButtonContent)
    }
}

@Composable
private fun ColumnScope.DataStateButtons(
    onSubscribeButtonClick: () -> Unit,
    state: GroupSubscriptionSheetState,
    subscribeButtonContent: @Composable () -> Unit
) {
    PrimaryButton(onClick = onSubscribeButtonClick) {
        subscribeButtonContent()
    }
    Spacer(Modifier.height(12.dp))
    SecondaryButton(stringResource(R.string.vkid_group_subscription_secondary), state::hide)
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
                        model = ImageRequest.Builder(LocalContext.current).data(userImageUrl).transformations(transformation).build(),
                        contentDescription = null,
                        contentScale = ContentScale.FillHeight,
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        FlowRow(verticalArrangement = Arrangement.Center) {
            val numberOfSubs = when {
                data.subscriberCount < THOUSAND -> data.subscriberCount.toString()
                data.subscriberCount < MILLION -> (data.subscriberCount / THOUSAND).toString() + "K"
                else -> (data.subscriberCount / MILLION).toString() + "M"
            }
            val subscribersText = numberOfSubs + " " + pluralStringResource(R.plurals.vkid_group_subscription_subscribers, data.subscriberCount) + " "
            val friendsText = "· ${data.friendsCount} " + pluralStringResource(R.plurals.vkid_group_subscription_friends, data.friendsCount)
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
            if (data.friendsCount > 0) {
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
private fun rememberGroupSubscriptionSheetStateInternal(): GroupSubscriptionSheetState {
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
            subscriberCount = 143,
            friendsCount = 12,
            isGroupVerified = true,
        ),
        {},
    ) {
        Text("button text")
    }
}
