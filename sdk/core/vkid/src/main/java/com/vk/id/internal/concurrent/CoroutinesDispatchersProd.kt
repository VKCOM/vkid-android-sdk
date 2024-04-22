@file:OptIn(InternalVKIDApi::class)

package com.vk.id.internal.concurrent

import com.vk.id.common.InternalVKIDApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal class CoroutinesDispatchersProd : VKIDCoroutinesDispatchers {
    override val io: CoroutineDispatcher
        get() = Dispatchers.IO
}
