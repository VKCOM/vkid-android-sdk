package com.vk.dto.common.id

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

internal class UserIdTest : BehaviorSpec({

    Given("User id") {
        When("User id is zero") {
            val userId = UserId(0)
            Then("It is not real") {
                userId.isReal() shouldBe false
            }
            Then("It is not user id") {
                userId.isUserId() shouldBe false
            }
            Then("It is not group id") {
                userId.isGroupId() shouldBe false
            }
        }
        When("User id is not zero") {
            val userId = UserId(1)
            Then("It is real") {
                userId.isReal() shouldBe true
            }
            Then("It is user id") {
                userId.isUserId() shouldBe true
            }
            Then("It is not group id") {
                userId.isGroupId() shouldBe false
            }
        }
        When("User id is negative") {
            val userId = UserId(-1)
            Then("It is real") {
                userId.isReal() shouldBe true
            }
            Then("It is not user id") {
                userId.isUserId() shouldBe false
            }
            Then("It is group id") {
                userId.isGroupId() shouldBe true
            }
        }
    }
})
