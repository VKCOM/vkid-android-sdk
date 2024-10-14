package com.vk.id.common.activity

import android.content.Context
import android.content.Intent
import com.vk.id.common.InternalVKIDApi
import com.vk.id.common.mockprovider.TestAuthProviderActivity
import com.vk.id.internal.context.InternalVKIDActivityStarter
import com.vk.id.test.InternalVKIDAuthStarter

@OptIn(InternalVKIDApi::class)
internal class MockProviderActivityStarter(private val context: Context) : InternalVKIDActivityStarter {
    override fun startActivity(intent: Intent) {
        val testProviderIntent = Intent(context, TestAuthProviderActivity::class.java)
        testProviderIntent.data = intent.data
        InternalVKIDAuthStarter.startAuth(context, testProviderIntent)
    }
}
