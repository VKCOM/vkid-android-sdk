package com.vk.id.internal.auth

import com.vk.id.AccessToken
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.AuthCodeData
import com.vk.id.auth.VKIDAuthCallback
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

internal class AuthCallbacksHolderTest : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    Given("A callbacks holder") {
        val holder = AuthCallbacksHolder()
        When("A callback is added") {
            val callback = createCallback()
            holder.add(callback)
            Then("It can be received through get all") {
                holder.getAll() shouldBe setOf(callback)
            }
        }
        When("Two callbacks are added") {
            val firstCallback = createCallback()
            val secondCallback = createCallback()
            holder.add(firstCallback)
            holder.add(secondCallback)
            Then("They can be received through get all") {
                holder.getAll() shouldBe setOf(firstCallback, secondCallback)
            }
        }
        When("A callback is added and holder is cleared") {
            holder.add(createCallback())
            holder.clear()
            Then("Holder is empty") {
                holder.getAll() shouldBe emptySet()
            }
        }
    }
})

private fun createCallback() = object : VKIDAuthCallback {
    override fun onSuccess(accessToken: AccessToken) = Unit
    override fun onFail(fail: VKIDAuthFail) = Unit
    override fun onAuthCode(data: AuthCodeData) = Unit
}
