package com.vk.id.network.groupsubscription.data

import com.vk.id.common.InternalVKIDApi

@InternalVKIDApi
@Suppress("ForbiddenPublicDataClass")
public data class InternalVKIDGroupMembersData(
    val count: Int,
    val members: List<InternalVKIDMemberData>,
)
