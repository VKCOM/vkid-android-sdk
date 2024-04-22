package com.vk.id.network

import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.vk.id.common.InternalVKIDApi
import okhttp3.Interceptor

@InternalVKIDApi
public object VKIDAdditionalInterceptors {
    public val networkFlipperPlugin: NetworkFlipperPlugin by lazy { NetworkFlipperPlugin() }
    public fun getInterceptor(): Interceptor? = FlipperOkhttpInterceptor(networkFlipperPlugin)
}