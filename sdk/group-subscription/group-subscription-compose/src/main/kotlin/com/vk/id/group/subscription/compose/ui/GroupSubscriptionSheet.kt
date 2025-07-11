@file:OptIn(InternalVKIDApi::class, ExperimentalMaterial3Api::class)

package com.vk.id.group.subscription.compose.ui

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
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
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
import com.vk.id.group.subscription.common.fail.VKIDGroupSubscriptionFail
import com.vk.id.group.subscription.common.style.GroupSubscriptionStyle
import com.vk.id.group.subscription.compose.R
import com.vk.id.group.subscription.compose.analytics.GroupSubscriptionAnalytics
import com.vk.id.group.subscription.compose.close.CloseIcon
import com.vk.id.group.subscription.compose.interactor.ClientLimitReachedException
import com.vk.id.group.subscription.compose.interactor.GroupSubscriptionInteractor
import com.vk.id.group.subscription.compose.interactor.RemoteLimitReachedException
import com.vk.id.group.subscription.compose.interactor.ServiceAccountException
import com.vk.id.group.subscription.compose.progress.CircleProgress
import com.vk.id.group.subscription.compose.snackbar.GroupSubscriptionSnackbar
import com.vk.id.group.subscription.compose.storage.GroupSubscriptionPrefsStorage
import com.vk.id.group.subscription.compose.util.PrimaryButton
import com.vk.id.group.subscription.compose.util.SecondaryButton
import com.vk.id.group.subscription.compose.util.TightWrapText
import com.vk.id.group.subscription.compose.util.UserImageTransformation
import com.vk.id.group.subscription.compose.util.backgroundColor
import com.vk.id.group.subscription.compose.util.textPrimaryButtonColor
import com.vk.id.group.subscription.compose.util.textPrimaryColor
import com.vk.id.group.subscription.compose.util.textSecondaryColor
import com.vk.id.network.groupsubscription.exception.InternalVKIDAlreadyGroupMemberException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

/**
 * Composable function which creates a state for [GroupSubscriptionSheet] and can be used as `state` parameter.
 *
 * It provides [GroupSubscriptionSheetState] which can, for example, trigger hiding and showing the bottom sheet.
 *
 * @since 2.5.0
 */
@Composable
public fun rememberGroupSubscriptionSheetState(): GroupSubscriptionSheetState {
    return rememberGroupSubscriptionSheetStateInternal()
}

/**
 * Composable that displays and host for Group Subscription snackbars.
 *
 * In most cases you need to use it along with a widget that provides Group Subscription functionality. The only exception
 * is when widget constructor doesn't accept [SnackbarHostState]. You should place it where you want snackbars to appear, respecting screen insets.
 *
 * @param snackbarHostState The host state for snackbars. You need to pass this value to the widget as well in the corresponding parameter.
 * @param style The style of the widget, will be used for styling snackbars. You need to pass the same value as the one use pass to the widget.
 *
 * @since 2.5.0
 */
@Composable
public fun GroupSubscriptionSnackbarHost(
    snackbarHostState: SnackbarHostState,
    style: GroupSubscriptionStyle = GroupSubscriptionStyle.Light(),
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
            GroupSubscriptionSnackbar(style, snackbarData.visuals.message)
        }
    }
}

/**
 * A bottomsheet that provides Group Subscription functionality.
 *
 * Launches VK group subscription flow.
 *
 * @param modifier Modifier for the widget, most likely should not be changed.
 * @param state Sheet state, can be used for showing and hiding the sheet.
 * @param accessTokenProvider The function that provides an access token that will be used for retrieving group information and subscribing the user.
 * NOTE: The token must have "groups" scope, otherwise you'll get an error.
 * NOTE: The token won't be automatically refreshed, in case it's outdated you'll get an error.
 * NOTE: In case you will pass null, the last token you received with the SDK will be used.
 * @param groupId The id of the group the user will be subscribed to.
 * @param onSuccess Will be called upon successful subscription.
 * @param onFail Will be called upon any unsuccessful flow completion along with an description of the specific encountered error.
 * @param snackbarHostState The host state, should be the same as [GroupSubscriptionSnackbarHost].
 * NOTE: In case you pass null, the host will be put in the place you put this Composable.
 * @param style The widget style, can change appearance.
 *
 * @since 2.5.0
 */
@Composable
@Suppress("LongMethod", "CyclomaticComplexMethod")
public fun GroupSubscriptionSheet(
    modifier: Modifier = Modifier,
    state: GroupSubscriptionSheetState = rememberGroupSubscriptionSheetState(),
    accessTokenProvider: (() -> String)? = null,
    groupId: String,
    onSuccess: () -> Unit,
    onFail: (VKIDGroupSubscriptionFail) -> Unit = {},
    snackbarHostState: SnackbarHostState? = null,
    style: GroupSubscriptionStyle = GroupSubscriptionStyle.Light(),
) {
    val interactor = remember(groupId) {
        GroupSubscriptionInteractor(
            apiService = VKID.instance.groupSubscriptionApiService,
            tokenStorage = VKID.instance.tokenStorage,
            groupId = groupId,
            externalAccessTokenProvider = accessTokenProvider,
            storage = GroupSubscriptionPrefsStorage(VKID.instance.prefsStorage),
            limit = VKID.instance.groupSubscriptionLimit,
        )
    }
    GroupSubscriptionAnalytics.style.set(style)
    GroupSubscriptionAnalytics.groupId.set(groupId)
    val actualSnackbarHostState = snackbarHostState ?: remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val status = rememberSaveable { mutableStateOf<GroupSubscriptionSheetStatus>(GroupSubscriptionSheetStatus.Init) }
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    val rememberedOnFail by rememberUpdatedState(onFail)
    val snackbarLabel = stringResource(R.string.vkid_group_subscription_snackbar_label)
    var isSuccess by remember { mutableStateOf(false) }
    val actualOnSuccess by rememberUpdatedState {
        isSuccess = true
        coroutineScope.launch {
            GroupSubscriptionAnalytics.successShown(accessTokenProvider?.invoke() ?: VKID.instance.accessToken?.token)
            actualSnackbarHostState.showSnackbar(snackbarLabel)
        }
        onSuccess()
    }
    var wasVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        snapshotFlow { state.isVisible }.distinctUntilChanged().collect { isVisible ->
            if (isVisible) {
                isSuccess = false
            }
            if (!isVisible && wasVisible && !isSuccess) {
                rememberedOnFail(VKIDGroupSubscriptionFail.Dismiss())
            }
            wasVisible = isVisible
        }
    }
    processSheetShow(
        { status.value = it },
        { showBottomSheet = it },
        coroutineScope,
        state,
    )
    remember(groupId) {
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
                    } catch (@Suppress("SwallowedException") e: ClientLimitReachedException) {
                        state.hide()
                        rememberedOnFail(VKIDGroupSubscriptionFail.ClientLimitReached())
                        showBottomSheet = false
                    } catch (@Suppress("SwallowedException") e: RemoteLimitReachedException) {
                        state.hide()
                        rememberedOnFail(VKIDGroupSubscriptionFail.RemoteLimitReached())
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
    Box(modifier = modifier) {
        if (showBottomSheet) {
            ModalBottomSheet(
                modifier = Modifier.testTag("group_subscription_sheet"),
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
                        .clip(RoundedCornerShape(style.cornersStyle.radiusDp.dp))
                        .background(backgroundColor(style)),
                    contentAlignment = Alignment.Center,
                ) {
                    GroupSubscriptionAnalytics.isErrorState
                        .set(status.value is GroupSubscriptionSheetStatus.Failure || status.value is GroupSubscriptionSheetStatus.Resubscribing)
                    when (val actualStatus = status.value) {
                        is GroupSubscriptionSheetStatus.Init -> Unit
                        is GroupSubscriptionSheetStatus.Loaded -> LoadedState({ interactor.saveDisplay() }, onFail, style, state, actualStatus) {
                            GroupSubscriptionAnalytics.subscribeToGroupClick(VKID.instance.accessToken?.token)
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

                        is GroupSubscriptionSheetStatus.Subscribing -> SubscribingState(style, state, actualStatus, onFail)
                        is GroupSubscriptionSheetStatus.Failure -> FailureState(style, state, onFail) {
                            GroupSubscriptionAnalytics.retryClick(VKID.instance.accessToken?.token)
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

                        is GroupSubscriptionSheetStatus.Resubscribing -> ResubscribingState(style, state, onFail) {
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
        if (snackbarHostState == null) {
            GroupSubscriptionSnackbarHost(actualSnackbarHostState)
        }
    }
}

@Suppress("LongParameterList")
private fun subscribeToGroup(
    status: MutableState<GroupSubscriptionSheetStatus>,
    data: GroupSubscriptionSheetStatusData,
    state: GroupSubscriptionSheetState,
    coroutineScope: CoroutineScope,
    interactor: GroupSubscriptionInteractor,
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
@Suppress("LongParameterList")
internal fun LoadedState(
    saveDisplay: suspend () -> Unit,
    onFail: (VKIDGroupSubscriptionFail) -> Unit,
    style: GroupSubscriptionStyle,
    state: GroupSubscriptionSheetState,
    status: GroupSubscriptionSheetStatus.Loaded,
    onSubscribeButtonClick: () -> Unit,
) {
    var once by rememberSaveable { mutableStateOf(true) }
    val rememberedSaveDisplay by rememberUpdatedState(saveDisplay)
    LaunchedEffect(Unit) {
        if (once) {
            rememberedSaveDisplay()
            once = false
        }
    }
    GroupSubscriptionAnalytics.SheetShown(VKID.instance.accessToken?.token)
    DataState(style, state, status.data, onFail, onSubscribeButtonClick) {
        Text(
            text = stringResource(R.string.vkid_group_subscription_primary),
            modifier = Modifier,
            style = TextStyle(
                textAlign = TextAlign.Center,
                color = textPrimaryButtonColor(style),
                fontSize = style.buttonsSizeStyle.textSizeSp.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Medium,
            ),
        )
    }
}

@Composable
internal fun SubscribingState(
    style: GroupSubscriptionStyle,
    state: GroupSubscriptionSheetState,
    status: GroupSubscriptionSheetStatus.Subscribing,
    onFail: (VKIDGroupSubscriptionFail) -> Unit,
) {
    DataState(style, state, status.data, onFail, {}) {
        Box(modifier = Modifier.size(24.dp)) {
            CircleProgress(style, "Subscribing to group spinner")
        }
    }
}

@Composable
internal fun ResubscribingState(
    style: GroupSubscriptionStyle,
    state: GroupSubscriptionSheetState,
    onFail: (VKIDGroupSubscriptionFail) -> Unit,
    onRetry: () -> Unit,
) {
    FailureDataState(style, state, onFail, onRetry) {
        Box(modifier = Modifier.size(24.dp)) {
            CircleProgress(style, "Resubscribing to group spinner")
        }
    }
}

@Composable
internal fun FailureState(
    style: GroupSubscriptionStyle,
    state: GroupSubscriptionSheetState,
    onFail: (VKIDGroupSubscriptionFail) -> Unit,
    onRetry: () -> Unit,
) {
    GroupSubscriptionAnalytics.ErrorShown(VKID.instance.accessToken?.token)
    FailureDataState(style, state, onFail, onRetry) {
        Text(
            text = stringResource(R.string.vkid_group_subscription_fail_primary),
            modifier = Modifier,
            style = TextStyle(
                textAlign = TextAlign.Center,
                color = textPrimaryButtonColor(style),
                fontSize = style.buttonsSizeStyle.textSizeSp.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Medium,
            ),
        )
    }
}

@Composable
@Suppress("LongMethod")
private fun FailureDataState(
    style: GroupSubscriptionStyle,
    state: GroupSubscriptionSheetState,
    onFail: (VKIDGroupSubscriptionFail) -> Unit,
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
                color = textPrimaryColor(style),
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
                color = textSecondaryColor(style),
                fontSize = 14.sp,
                lineHeight = 18.sp,
                fontWeight = FontWeight.Normal,
            ),
        )
        Spacer(Modifier.height(32.dp))
        PrimaryButton(style, testTag = "group_subscription_retry", onClick = onRetry) {
            retryButtonContent()
        }
        Spacer(Modifier.height(12.dp))
        SecondaryButton(style, testTag = "group_subscription_cancel", text = stringResource(R.string.vkid_group_subscription_fail_secondary)) {
            state.hide()
            GroupSubscriptionAnalytics.cancelClick(VKID.instance.accessToken?.token)
            onFail(VKIDGroupSubscriptionFail.Cancel())
        }
    }
}

@Suppress("LongParameterList")
@Composable
private fun DataState(
    style: GroupSubscriptionStyle,
    state: GroupSubscriptionSheetState,
    data: GroupSubscriptionSheetStatusData,
    onFail: (VKIDGroupSubscriptionFail) -> Unit,
    onSubscribeButtonClick: () -> Unit,
    subscribeButtonContent: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DataStateHeader(onFail, state, data)
        Spacer(modifier = Modifier.height(12.dp))
        DataStateLabels(style, data)
        Spacer(modifier = Modifier.height(8.dp))
        DataStateSubscribers(style, data)
        Spacer(Modifier.height(20.dp))
        DataStateButtons(style, onSubscribeButtonClick, state, onFail, subscribeButtonContent)
    }
}

@Composable
private fun ColumnScope.DataStateButtons(
    style: GroupSubscriptionStyle,
    onSubscribeButtonClick: () -> Unit,
    state: GroupSubscriptionSheetState,
    onFail: (VKIDGroupSubscriptionFail) -> Unit,
    subscribeButtonContent: @Composable () -> Unit
) {
    PrimaryButton(style, testTag = "group_subscription_subscribe", onClick = onSubscribeButtonClick) {
        subscribeButtonContent()
    }
    Spacer(Modifier.height(12.dp))
    SecondaryButton(style, testTag = "group_subscription_later", text = stringResource(R.string.vkid_group_subscription_secondary)) {
        GroupSubscriptionAnalytics.nextTimeClick(VKID.instance.accessToken?.token)
        state.hide()
        onFail(VKIDGroupSubscriptionFail.Cancel())
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
@Suppress("LongMethod")
private fun ColumnScope.DataStateSubscribers(
    style: GroupSubscriptionStyle,
    data: GroupSubscriptionSheetStatusData
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .width(72.dp)
                .height(24.dp)
        ) {
            data.userImageUrls.forEachIndexed { index, userImageUrl ->
                val transformation = if (index > 0) UserImageTransformation(backgroundColor(style).toArgb()) else CircleCropTransformation()
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
            val numberOfSubs = SubscriberCountFormatter.format(data.subscriberCount)
            val subscribersText = pluralStringResource(R.plurals.vkid_group_subscription_subscribers, data.subscriberCount, numberOfSubs) + " "
            val friendsText = "· " + pluralStringResource(R.plurals.vkid_group_subscription_friends, data.friendsCount, data.friendsCount)
            Text(
                text = subscribersText,
                modifier = Modifier,
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    color = textSecondaryColor(style),
                    fontSize = 15.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Medium,
                ),
            )
            if (data.friendsCount > 0) {
                Text(
                    text = friendsText,
                    modifier = Modifier,
                    style = TextStyle(
                        textAlign = TextAlign.Center,
                        color = textSecondaryColor(style),
                        fontSize = 15.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Medium,
                    ),
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun ColumnScope.DataStateLabels(
    style: GroupSubscriptionStyle,
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
                    color = textPrimaryColor(style),
                    fontSize = 23.sp,
                    lineHeight = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
        if (data.isGroupVerified) {
            Spacer(modifier = Modifier.width(6.dp))
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
    if (data.groupDescription.isNotBlank()) {
        Spacer(modifier = Modifier.height(12.dp))
        Row {
            Text(
                text = data.groupDescription,
                modifier = Modifier,
                style = TextStyle(
                    textAlign = TextAlign.Start,
                    color = textSecondaryColor(style),
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Normal,
                ),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun ColumnScope.DataStateHeader(
    onFail: (VKIDGroupSubscriptionFail) -> Unit,
    state: GroupSubscriptionSheetState,
    data: GroupSubscriptionSheetStatusData
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Spacer(Modifier.weight(1f))
        CloseIcon {
            GroupSubscriptionAnalytics.close(VKID.instance.accessToken?.token)
            state.hide()
            onFail(VKIDGroupSubscriptionFail.Close())
        }
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
    var previousValue by remember { mutableStateOf(SheetValue.Hidden) }
    val materialSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true, confirmValueChange = {
        if (it == SheetValue.Hidden && it != previousValue) {
            GroupSubscriptionAnalytics.close(VKID.instance.accessToken?.token)
        }
        previousValue = it
        true
    })
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
        style = GroupSubscriptionStyle.Light(),
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
        {},
    ) {
        Text("button text")
    }
}
