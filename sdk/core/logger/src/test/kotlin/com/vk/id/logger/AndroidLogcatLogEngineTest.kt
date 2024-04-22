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
public class AndroidLogcatLogEngineTest : BehaviorSpec({

    beforeSpec {
        mockkStatic(Log::class)
        every { Log.i(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
    }

    Given("test AndroidLogcatLogEngine") {
        val androidLogcatLogEngine = VKIDAndroidLogcatLogEngine()

        When("logs to info Logcat") {
            androidLogcatLogEngine.log(LogEngine.LogLevel.INFO, "TestTag", "TestMessage", null)

            Then("Calls info level logger") {
                verify { Log.i("TestTag", "TestMessage") }
            }
        }

        When("logs to debug Logcat") {
            androidLogcatLogEngine.log(LogEngine.LogLevel.DEBUG, "TestTag", "TestMessage", null)

            Then("Calls debug level logger") {
                verify { Log.d("TestTag", "TestMessage") }
            }
        }

        When("logs to error Logcat") {
            val throwable = Throwable("TestError")
            androidLogcatLogEngine.log(LogEngine.LogLevel.ERROR, "TestTag", "TestMessage", throwable)

            Then("Calls error level logger") {
                verify { Log.e("TestTag", "TestMessage", throwable) }
            }
        }
    }
})
