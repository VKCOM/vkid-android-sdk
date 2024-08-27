package com.vk.id.network

import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.vk.id.common.InternalVKIDApi
import okhttp3.Interceptor

@InternalVKIDApi
public object InternalVKIDAdditionalInterceptors {
    public val networkFlipperPluginInternal: NetworkFlipperPlugin by lazy { NetworkFlipperPlugin() }
    public val networkFlipperPlugin: Any? = networkFlipperPluginInternal
    public fun getInterceptor(): Interceptor? = FlipperOkhttpInterceptor(networkFlipperPluginInternal)
}