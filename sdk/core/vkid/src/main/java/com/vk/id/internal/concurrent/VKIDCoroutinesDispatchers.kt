package com.vk.id.internal.concurrent

import kotlinx.coroutines.CoroutineDispatcher

internal interface VKIDCoroutinesDispatchers {
    val io: CoroutineDispatcher
}
