package com.vk.id.internal.log

import android.util.Log
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
public class AndroidLogcatLogEngineTest {
    @BeforeEach
    public fun setUp() {
        mockkStatic(Log::class)
        every { Log.i(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
    }

    @Test
    public fun `test AndroidLogcatLogEngine logs to Logcat`() {
        val androidLogcatLogEngine = AndroidLogcatLogEngine()

        androidLogcatLogEngine.log(LogEngine.LogLevel.INFO, "TestTag", "TestMessage", null)
        verify { Log.i("TestTag", "TestMessage") }

        androidLogcatLogEngine.log(LogEngine.LogLevel.DEBUG, "TestTag", "TestMessage", null)
        verify { Log.d("TestTag", "TestMessage") }

        val throwable = Throwable("TestError")
        androidLogcatLogEngine.log(LogEngine.LogLevel.ERROR, "TestTag", "TestMessage", throwable)
        verify { Log.e("TestTag", "TestMessage", throwable) }
    }
}
