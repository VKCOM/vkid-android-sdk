package com.vk.id.group.subscription.compose.interactor

internal class ClientLimitReachedException : IllegalStateException("Limit of displays for group subscription is reached")
