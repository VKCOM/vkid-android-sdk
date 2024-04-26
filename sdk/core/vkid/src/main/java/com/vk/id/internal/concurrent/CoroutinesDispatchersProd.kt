package com.vk.id.internal.concurrent

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal class CoroutinesDispatchersProd : VKIDCoroutinesDispatchers {
    override val io: CoroutineDispatcher
        get() = Dispatchers.IO
}
