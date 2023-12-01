package com.vk.id.internal.log

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify

public class VKIDLogTest : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    lateinit var mockEngine: LogEngine

    beforeSpec {
        mockEngine = mockk(relaxed = true)
        VKIDLog.setLogEngine(mockEngine)
    }

    Given("A log engine") {
        val anotherMockEngine = mockk<LogEngine>(relaxed = true)

        When("Sets log engine and logs") {
            VKIDLog.setLogEngine(anotherMockEngine)
            VKIDLog.log(LogEngine.LogLevel.DEBUG, "TestTag", "TestMessage", null)

            Then("Log engine is invoked") {
                verify { anotherMockEngine.log(LogEngine.LogLevel.DEBUG, "TestTag", "TestMessage", null) }
                confirmVerified(anotherMockEngine)
            }

            Then("Previous log engine is not called") {
                verify(exactly = 0) {
                    mockEngine.log(
                        any(),
                        any(),
                        any(),
                        any()
                    )
                }
            }
        }
    }

    Given("A logger with tag") {
        val logger = VKIDLog.createLoggerForTag("TestTag")

        When("Logs info level message") {
            logger.info("TestInfoMessage")

            Then("The engine is called") {
                verify { mockEngine.log(LogEngine.LogLevel.INFO, "TestTag", "TestInfoMessage", null) }
            }
        }

        When("Logs debug level message") {
            logger.debug("TestDebugMessage")

            Then("The engine is called") {
                verify { mockEngine.log(LogEngine.LogLevel.DEBUG, "TestTag", "TestDebugMessage", null) }
            }
        }

        When("Logs error level message") {
            val throwable = Throwable("TestError")
            logger.error("TestErrorMessage", throwable)

            Then("The engine is called") {
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
    }
})
