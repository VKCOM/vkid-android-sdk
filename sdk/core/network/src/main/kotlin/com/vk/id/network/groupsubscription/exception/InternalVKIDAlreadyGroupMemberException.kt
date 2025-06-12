package com.vk.id.network.groupsubscription.exception

import com.vk.id.common.InternalVKIDApi

@InternalVKIDApi
public class InternalVKIDAlreadyGroupMemberException : IllegalStateException("User is already a group member")
