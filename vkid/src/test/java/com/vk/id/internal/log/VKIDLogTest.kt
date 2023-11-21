package com.vk.id.internal.log

import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
public class VKIDLogTest {
    private lateinit var mockEngine: LogEngine

    @BeforeEach
    public fun setUp() {
        mockEngine = mockk(relaxed = true)
        VKIDLog.setLogEngine(mockEngine)
    }

    @Test
    public fun `test setting log engine`() {
        val anotherMockEngine = mockk<LogEngine>(relaxed = true)
        VKIDLog.setLogEngine(anotherMockEngine)

        VKIDLog.log(LogEngine.LogLevel.DEBUG, "TestTag", "TestMessage", null)

        verify { anotherMockEngine.log(LogEngine.LogLevel.DEBUG, "TestTag", "TestMessage", null) }
        confirmVerified(anotherMockEngine)

        verify(exactly = 0) {
            mockEngine.log(
                any(),
                any(),
                any(),
                any()
            )
        } // Ensure the old engine is not used
    }

    @Test
    public fun `test creating logger with tag and logging info`() {
        val logger = VKIDLog.createLoggerForTag("TestTag")

        logger.info("TestInfoMessage")
        logger.debug("TestDebugMessage")
        val throwable = Throwable("TestError")
        logger.error("TestErrorMessage", throwable)

        verify { mockEngine.log(LogEngine.LogLevel.INFO, "TestTag", "TestInfoMessage", null) }
        verify { mockEngine.log(LogEngine.LogLevel.DEBUG, "TestTag", "TestDebugMessage", null) }
        verify {
            mockEngine.log(
                LogEngine.LogLevel.ERROR,
                "TestTag",
                "TestErrorMessage",
                throwable
            )
        }
    }
}
