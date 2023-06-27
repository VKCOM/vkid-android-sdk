package com.vk.id

import com.vk.id.internal.di.VKIDDeps
import io.mockk.mockk

public class VKIDTest {
    private val deps: VKIDDeps = mockk()
    private val underTest = VKID("clientId", "clientSecret", "redirectUri", deps)
}