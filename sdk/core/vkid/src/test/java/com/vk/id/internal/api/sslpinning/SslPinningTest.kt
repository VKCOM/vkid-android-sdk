package com.vk.id.internal.api.sslpinning

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.res.Resources
import com.vk.id.R
import com.vk.id.internal.api.HttpClientProvider
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import okhttp3.Request
import java.io.File
import java.io.FileInputStream
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLHandshakeException

internal class SslPinningTest : BehaviorSpec({
    // To test pinning:
    // - Enable charles
    // - Enable mac proxy in charles
    // - Disable VPN
    // - Change flag to true
    val charlesEnabled = true
    Given("Http client builder with ssl pinning") {
        val context = mockk<Context>()
        val resources = mockk<Resources>()
        every { context.resources } returns resources
        every { resources.openRawResource(R.raw.vkid_cacerts_pins) } returns FileInputStream(File("src/main/res/raw/vkid_cacerts_pins"))
        val applicationInfo = ApplicationInfo()
        applicationInfo.flags = 0
        every { context.applicationInfo } returns applicationInfo
        val builder = HttpClientProvider(context).provideBuilderWithSslPinning()
            .readTimeout(300, TimeUnit.SECONDS)
            .writeTimeout(300, TimeUnit.SECONDS)
            .connectTimeout(300, TimeUnit.SECONDS)
        val request = Request.Builder().url("https://api.vk.com/method/utils.getServerTime").build()
        if (!charlesEnabled) {
            When("Request is made without proxy") {
                val client = builder.build()
                val response = client.newCall(request).execute()
                response.code shouldBe 200
            }
        } else {
            When("Request is made with proxy") {
                // todo: When proxy on mac mini is ready uncomment and update proxy address
                // val client = builder
                //    .proxy(Proxy(Proxy.Type.HTTP, InetSocketAddress("178.218.44.79", 3128)))
                //    .build()
                val client = builder.build()
                val exception = try {
                    client.newCall(request).execute()
                    null
                } catch (e: SSLHandshakeException) {
                    e
                }
                exception.shouldNotBeNull()
            }
        }
    }
})
