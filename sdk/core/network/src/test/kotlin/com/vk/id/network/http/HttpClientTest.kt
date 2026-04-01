@file:OptIn(InternalVKIDApi::class)

package com.vk.id.network.http

import com.vk.id.common.InternalVKIDApi
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

internal class HttpClientTest : BehaviorSpec({
    Given("HttpRequest classes") {
        When("Create GET request") {
            val request = HttpRequest.get(url = "https://example.com/test")

            Then("Should create GET request with URL") {
                request.url shouldBe "https://example.com/test"
                request.method shouldBe "GET"
                request.body shouldBe null
            }
        }

        When("Create POST request") {
            val body = """{"test": "data"}"""
            val request = HttpRequest.post(
                url = "https://example.com/test",
                body = body
            )

            Then("Should create POST request with body") {
                request.url shouldBe "https://example.com/test"
                request.method shouldBe "POST"
                request.body shouldBe body
            }
        }
    }

    Given("HttpResponse class") {
        When("Create successful response") {
            val response = HttpResponse(
                body = """{"result": "success"}""",
                code = 200,
                message = "OK",
                request = HttpRequest.get("")
            )

            Then("Should create response with data") {
                response.code shouldBe 200
                response.message shouldBe "OK"
                response.body shouldBe """{"result": "success"}"""
                response.isSuccessful shouldBe true
            }
        }

        When("Create error response") {
            val response = HttpResponse(
                body = "Error occurred",
                code = 404,
                message = "Not Found",
                request = HttpRequest.get("")
            )

            Then("Should create error response") {
                response.code shouldBe 404
                response.message shouldBe "Not Found"
                response.isSuccessful shouldBe false
            }
        }

        When("Create redirect response") {
            val response = HttpResponse(
                body = "",
                code = 302,
                message = "Found",
                request = HttpRequest.get("")
            )

            Then("Should create redirect response") {
                response.code shouldBe 302
                response.message shouldBe "Found"
                response.isSuccessful shouldBe false
            }
        }
    }

    Given("FormBody class") {
        When("Create form body using builder") {
            val body = FormBody.Builder()
                .add("client_id", "test_client")
                .add("client_secret", "secret123")
                .add("grant_type", "authorization_code")
                .build()

            Then("Should create URL-encoded form body") {
                body.content().contains("client_id=test_client") shouldBe true
                body.content().contains("client_secret=secret123") shouldBe true
                body.content().contains("grant_type=authorization_code") shouldBe true
            }
        }

        When("Create form body with special characters") {
            val body = FormBody.Builder()
                .add("email", "test@example.com")
                .add("password", "pass!@#$%")
                .build()

            Then("Should URL-encode special characters") {
                body.content().contains("email=test%40example.com") shouldBe true
                body.content().contains("password") shouldBe true
            }
        }
    }
})
