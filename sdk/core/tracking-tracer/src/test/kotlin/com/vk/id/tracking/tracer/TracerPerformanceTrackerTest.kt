package com.vk.id.tracking.tracer

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import ru.ok.tracer.lite.performance.metrics.TracerPerformanceMetricsLite
import java.util.concurrent.TimeUnit

internal class TracerPerformanceTrackerTest : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    val tracer = mockk<TracerPerformanceMetricsLite>()
    val systemClockProvider = mockk<() -> Long>()
    Given("Tracer performance tracker") {
        val tracker = TracerPerformanceTracker(tracer, systemClockProvider)
        When("Starts tracking") {
            every { systemClockProvider() } returns 0
            tracker.startTracking("1")
            And("Finishes tracking after 1 millisecond") {
                every { systemClockProvider() } returns 1000
                every { tracer.sample(any(), any(), any<TimeUnit>()) } just Runs
                tracker.endTracking("1")
                Then("Requests time twice") {
                    verify(exactly = 2) { systemClockProvider() }
                }
                Then("Reports performance metric") {
                    verify { tracer.sample("1", 1000, TimeUnit.NANOSECONDS) }
                }
            }
            And("Finishes tracking after 1 milli") {
                every { systemClockProvider() } returns 1000
                And("Reporting throws an error") {
                    every { tracer.sample(any(), any(), any<TimeUnit>()) } throws IllegalStateException("Some error")
                    tracker.endTracking("1")
                    Then("Requests time twice") {
                        verify(exactly = 2) { systemClockProvider() }
                    }
                    Then("Tries to report performance metric") {
                        verify { tracer.sample("1", 1000, TimeUnit.NANOSECONDS) }
                    }
                }
            }
            And("Starts another tracking") {
                every { systemClockProvider() } returns 0
                tracker.startTracking("2")
                And("Finishes tracking first metric after 1 millisecond") {
                    every { systemClockProvider() } returns 1000
                    every { tracer.sample(any(), any(), any<TimeUnit>()) } just Runs
                    tracker.endTracking("1")
                    And("Finishes tracking second metric after 1 more millisecond") {
                        every { systemClockProvider() } returns 2000
                        tracker.endTracking("2")
                        Then("Requests time four times") {
                            verify(exactly = 4) { systemClockProvider() }
                        }
                        Then("Reports first performance metric") {
                            verify { tracer.sample("1", 1000, TimeUnit.NANOSECONDS) }
                        }
                        Then("Reports second performance metric") {
                            verify { tracer.sample("2", 2000, TimeUnit.NANOSECONDS) }
                        }
                    }
                }
            }
            And("Starts another tracking for the same key after 500 nanos") {
                every { systemClockProvider() } returns 500
                tracker.startTracking("1")
                And("Finishes tracking after 500 more nanos") {
                    every { systemClockProvider() } returns 1000
                    every { tracer.sample(any(), any(), any<TimeUnit>()) } just Runs
                    tracker.endTracking("1")
                    And("Finishes tracking after 1 more millisecond") {
                        every { systemClockProvider() } returns 2000
                        tracker.endTracking("1")
                        Then("Requests time three times") {
                            verify(exactly = 3) { systemClockProvider() }
                        }
                        Then("Reports performance metric only for the first time and second start time") {
                            verify(exactly = 1) { tracer.sample("1", 500, TimeUnit.NANOSECONDS) }
                        }
                    }
                }
            }
        }
        When("Runs tracking") {
            every { systemClockProvider() } returns 0 andThen 1000
            every { tracer.sample(any(), any(), any<TimeUnit>()) } just Runs
            tracker.runTracking("1") {}
            Then("Requests time twice") {
                verify(exactly = 2) { systemClockProvider() }
            }
            Then("Reports performance metric") {
                verify { tracer.sample("1", 1000, TimeUnit.NANOSECONDS) }
            }
        }
        When("Runs tracking suspend") {
            every { systemClockProvider() } returns 0 andThen 1000
            every { tracer.sample(any(), any(), any<TimeUnit>()) } just Runs
            tracker.runTrackingSuspend("1") {}
            Then("Requests time twice") {
                verify(exactly = 2) { systemClockProvider() }
            }
            Then("Reports performance metric") {
                verify { tracer.sample("1", 1000, TimeUnit.NANOSECONDS) }
            }
        }
    }
})
