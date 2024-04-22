@file:OptIn(InternalVKIDApi::class)

package com.vk.id.logger

import android.util.Log
import com.vk.id.common.InternalVKIDApi
import io.kotest.core.spec.DoNotParallelize
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.verify

@DoNotParallelize
public class FakeLogEngineTest : BehaviorSpec({

    beforeSpec {
        mockkStatic(Log::class)
        every { Log.i(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
    }

    Given("Fake log engine") {
        val fakeLogEngine = VKIDFakeLogEngine()
        When("It is asked to log") {
            fakeLogEngine.log(
                LogEngine.LogLevel.DEBUG,
                "TestTag",
                "TestMessage",
                Throwable("Throwable")
            )
            Then("Log with two arguments is not called") {
                verify(exactly = 0) {
                    Log.d(any(), any())
                }
            }
            Then("Log with three arguments is not called") {
                verify(exactly = 0) {
                    Log.d(any(), any(), any())
                }
            }
            // Since the FakeLogEngine doesn't do any action, these tests passes if no exception is thrown.
        }
    }
})
