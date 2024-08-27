package com.vk.id.network

import com.vk.id.common.InternalVKIDApi
import okhttp3.Interceptor

@InternalVKIDApi
public object InternalVKIDAdditionalInterceptors {
    public fun getInterceptor(): Interceptor? = null
    public val networkFlipperPlugin: Any? = null
}
