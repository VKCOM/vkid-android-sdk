package com.vk.id.network.groupsubscription.data

import com.vk.id.common.InternalVKIDApi

@InternalVKIDApi
@Suppress("ForbiddenPublicDataClass")
public data class InternalVKIDGroupByIdData(
    val groupId: String,
    val name: String,
    val imageUrl: String,
    val description: String,
    val isVerified: Boolean,
)
