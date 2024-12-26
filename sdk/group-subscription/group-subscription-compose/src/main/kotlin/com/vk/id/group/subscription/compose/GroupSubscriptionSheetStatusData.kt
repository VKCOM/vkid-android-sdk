package com.vk.id.group.subscription.compose

import android.os.Parcelable
import androidx.compose.runtime.Stable
import kotlinx.parcelize.Parcelize

@Parcelize
@Stable
internal data class GroupSubscriptionSheetStatusData(
    val groupImageUrl: String,
    val groupName: String,
    val groupDescription: String,
    val userImageUrls: List<String>,
    val numberOfSubscribers: Int,
    val numberOfFriends: Int,
    val isGroupVerified: Boolean,
) : Parcelable
