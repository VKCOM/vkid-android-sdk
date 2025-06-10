@file:OptIn(InternalVKIDApi::class)

package com.vk.id.group.subscription.compose.storage

import com.vk.id.common.InternalVKIDApi
import com.vk.id.storage.InternalVKIDPreferencesStorage
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import java.util.Date

internal class InternalVKIDGroupSubscriptionPrefsStorageTest : BehaviorSpec({

    val key = "GROUP_SUBSCRIPTION_DISPLAYS_123"
    val key2 = "GROUP_SUBSCRIPTION_DISPLAYS_456"
    val prefs = mockk<InternalVKIDPreferencesStorage>()
    val userId = 123L
    val userId2 = 456L
    Given("A storage") {
        val storage = GroupSubscriptionPrefsStorage(prefs)
        When("Saves empty set of displays") {
            every { prefs.set(key, "") } just runs
            storage.saveDisplays(userId, emptySet())
            Then("Saves empty string to prefs") {
                verify { prefs.set(key, "") }
            }
        }
        When("Prefs has blank string saved") {
            every { prefs.getString(key) } returns ""
            val displays = storage.getDisplays(userId)
            Then("Returns empty set of displays") {
                displays shouldBe emptySet()
            }
        }
        When("Saves one display") {
            every { prefs.set(key, "10") } just runs
            storage.saveDisplays(userId, setOf(Date(10L)))
            Then("Saves this display as long to prefs") {
                verify { prefs.set(key, "10") }
            }
        }
        When("Has one display saved") {
            every { prefs.getString(key) } returns "10"
            val displays = storage.getDisplays(userId)
            Then("Returns this display as date") {
                displays shouldBe setOf(Date(10L))
            }
        }
        When("Saves three displays") {
            every { prefs.set(key, "10,20,30") } just runs
            storage.saveDisplays(userId, setOf(Date(10L), Date(20L), Date(30L)))
            Then("Saves these displays as longs to prefs") {
                verify { prefs.set(key, "10,20,30") }
            }
        }
        When("Has three displays saved") {
            every { prefs.getString(key) } returns "10,20,30"
            val displays = storage.getDisplays(userId)
            Then("Returns these displays as dates") {
                displays shouldBe setOf(Date(10L), Date(20L), Date(30L))
            }
        }
        When("Saves displays for one user") {
            every { prefs.set(key, "10,20,30") } just runs
            storage.saveDisplays(userId, setOf(Date(10L), Date(20L), Date(30L)))
            And("Retrieves displays for another") {
                every { prefs.getString(key2) } returns ""
                val displays = storage.getDisplays(userId2)
                Then("Returns empty set of displays") {
                    displays shouldBe emptySet()
                }
            }
            And("Saves displays for another") {
                every { prefs.set(key2, "10") } just runs
                storage.saveDisplays(userId2, setOf(Date(10L)))
                And("Retrieves first user's displays") {
                    every { prefs.getString(key) } returns "10,20,30"
                    val displays = storage.getDisplays(userId)
                    Then("Returns first user's displays") {
                        displays shouldBe setOf(Date(10L), Date(20L), Date(30L))
                    }
                }
                And("Retrieves second user's displays") {
                    every { prefs.getString(key2) } returns "10"
                    val displays = storage.getDisplays(userId2)
                    Then("Returns second user's displays") {
                        displays shouldBe setOf(Date(10L))
                    }
                }
            }
        }
    }
})
