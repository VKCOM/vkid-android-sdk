package com.vk.id.group.subscription.compose.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal sealed class GroupSubscriptionSheetStatus : Parcelable {

    @Parcelize
    data object Init : GroupSubscriptionSheetStatus()

    @Parcelize
    data class Loaded(
        val data: GroupSubscriptionSheetStatusData
    ) : GroupSubscriptionSheetStatus()

    @Parcelize
    data class Subscribing(
        val data: GroupSubscriptionSheetStatusData
    ) : GroupSubscriptionSheetStatus()

    @Parcelize
    internal data class Failure(
        val data: GroupSubscriptionSheetStatusData
    ) : GroupSubscriptionSheetStatus()

    @Parcelize
    data class Resubscribing(
        val data: GroupSubscriptionSheetStatusData
    ) : GroupSubscriptionSheetStatus()
}
