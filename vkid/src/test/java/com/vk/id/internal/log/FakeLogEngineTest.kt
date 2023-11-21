package com.vk.id.internal.log

import android.util.Log
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
public class FakeLogEngineTest : BehaviorSpec({
    beforeEach {
        mockkStatic(Log::class)
        every { Log.i(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
    }

    Given("Fake log engine") {
        val fakeLogEngine = FakeLogEngine()
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
            // Since the FakeLogEngine doesn't do any action, this test passes if no exception is thrown.
        }
    }
})
