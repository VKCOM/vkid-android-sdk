package com.vk.id.internal.log

import android.util.Log
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Before
import org.junit.Test

public class FakeLogEngineTest {
    @Before
    public fun setUp() {
        mockkStatic(Log::class)
        every { Log.i(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
    }

    @Test
    public fun `test FakeLogEngine does not log`() {
        val fakeLogEngine = FakeLogEngine()
        fakeLogEngine.log(
            LogEngine.LogLevel.DEBUG,
            "TestTag",
            "TestMessage",
            Throwable("Throwable")
        )

        verify(exactly = 0) {
            Log.d(any(), any())
        }
        verify(exactly = 0) {
            Log.d(any(), any(), any())
        }
        // Since the FakeLogEngine doesn't do any action, this test passes if no exception is thrown.
    }
}
