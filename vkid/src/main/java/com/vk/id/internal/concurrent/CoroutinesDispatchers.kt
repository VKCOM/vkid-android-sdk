package com.vk.id.internal.concurrent

import kotlinx.coroutines.CoroutineDispatcher

internal interface CoroutinesDispatchers {
    val IO: CoroutineDispatcher
}