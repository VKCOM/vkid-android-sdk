package com.vk.id.internal.auth

import android.content.Context
import com.vk.id.internal.auth.browser.AuthBrowser

internal class AuthProvidersChooserDefault(private val context: Context) : AuthProvidersChooser {
    override fun chooseBest(): VKIDAuthProvider {
        // todo actually choose
        return AuthBrowser()
    }
}