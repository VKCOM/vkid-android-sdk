package com.vk.id.group.subscription.compose.ui

import android.os.Parcelable
import androidx.compose.runtime.Stable
import kotlinx.parcelize.Parcelize

@Parcelize
@Stable
internal data class GroupSubscriptionSheetStatusData(
    val groupImageUrl: String,
    val groupDescription: String,
    val groupName: String,
    val userImageUrls: List<String>,
    val subscriberCount: Int,
    val friendsCount: Int,
    val isGroupVerified: Boolean,
) : Parcelable
