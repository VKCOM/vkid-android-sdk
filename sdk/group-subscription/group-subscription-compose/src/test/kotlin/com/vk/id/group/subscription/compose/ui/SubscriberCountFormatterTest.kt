package com.vk.id.group.subscription.compose.ui

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

internal class SubscriberCountFormatterTest : BehaviorSpec({
    val formatter = SubscriberCountFormatter
    Given("0 subscribers") {
        val subscriberCount = 0
        When("Formats") {
            val formatted = formatter.format(subscriberCount)
            Then("Formats as 0") {
                formatted shouldBe "0"
            }
        }
    }
    Given("10 subscribers") {
        val subscriberCount = 10
        When("Formats") {
            val formatted = formatter.format(subscriberCount)
            Then("Formats as 10") {
                formatted shouldBe "10"
            }
        }
    }
    Given("1000 subscribers") {
        val subscriberCount = 1000
        When("Formats") {
            val formatted = formatter.format(subscriberCount)
            Then("Formats as 1K") {
                formatted shouldBe "1K"
            }
        }
    }
    Given("1200 subscribers") {
        val subscriberCount = 1200
        When("Formats") {
            val formatted = formatter.format(subscriberCount)
            Then("Formats as 1,2K") {
                formatted shouldBe "1,2K"
            }
        }
    }
    Given("12345 subscribers") {
        val subscriberCount = 12345
        When("Formats") {
            val formatted = formatter.format(subscriberCount)
            Then("Formats as 12K") {
                formatted shouldBe "12K"
            }
        }
    }
    Given("1034567 subscribers") {
        val subscriberCount = 1034567
        When("Formats") {
            val formatted = formatter.format(subscriberCount)
            Then("Formats as 1M") {
                formatted shouldBe "1M"
            }
        }
    }
    Given("1234567 subscribers") {
        val subscriberCount = 1234567
        When("Formats") {
            val formatted = formatter.format(subscriberCount)
            Then("Formats as 1.2M") {
                formatted shouldBe "1.2M"
            }
        }
    }
    Given("12345678 subscribers") {
        val subscriberCount = 12345678
        When("Formats") {
            val formatted = formatter.format(subscriberCount)
            Then("Formats as 12M") {
                formatted shouldBe "12M"
            }
        }
    }
})
