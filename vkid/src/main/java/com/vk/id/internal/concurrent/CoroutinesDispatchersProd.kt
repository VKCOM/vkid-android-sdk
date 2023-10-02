package com.vk.id.internal.concurrent

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal class CoroutinesDispatchersProd : CoroutinesDispatchers {
    override val IO: CoroutineDispatcher
        get() = Dispatchers.IO
}
