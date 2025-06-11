package com.vk.id.network.groupsubscription.data

import com.vk.id.common.InternalVKIDApi

@InternalVKIDApi
@Suppress("ForbiddenPublicDataClass")
public data class InternalVKIDGroupData(
    val imageUrl: String,
    val name: String,
    val description: String,
    val userImageUrls: List<String>,
    val subscriberCount: Int,
    val friendsCount: Int,
    val isVerified: Boolean,
)
