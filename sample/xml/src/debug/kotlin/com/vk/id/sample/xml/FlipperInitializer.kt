@file:OptIn(InternalVKIDApi::class)

package com.vk.id.sample.xml

import android.content.Context
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.soloader.SoLoader
import com.vk.id.common.InternalVKIDApi
import com.vk.id.network.InternalVKIDAdditionalInterceptors

internal object FlipperInitializer {
    fun init(context: Context) {
        SoLoader.init(context, false)

        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(context)) {
            val client = AndroidFlipperClient.getInstance(context)
            client.addPlugin(InspectorFlipperPlugin(context, DescriptorMapping.withDefaults()))
            client.addPlugin(InternalVKIDAdditionalInterceptors.networkFlipperPlugin)
            client.start()
        }
    }
}