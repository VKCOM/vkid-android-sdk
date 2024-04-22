package com.vk.id.network

import com.vk.id.common.InternalVKIDApi
import okhttp3.Interceptor

@InternalVKIDApi
public object VKIDAdditionalInterceptors {
    public fun getInterceptor(): Interceptor? = null
    public fun tmp(): Int = 1
}
