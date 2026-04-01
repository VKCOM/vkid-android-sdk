@file:OptIn(InternalVKIDApi::class)

package com.vk.id.network.http

import com.vk.id.common.InternalVKIDApi
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

internal class FormBodyTest : BehaviorSpec({
    Given("FormBody.Builder") {
        When("add string values") {
            val formBody = FormBody.Builder()
                .add("username", "testuser")
                .add("password", "secret123")
                .build()

            Then("should create properly encoded form body") {
                val content = formBody.content()
                content shouldBe "username=testuser&password=secret123"
            }
        }

        When("add integer value") {
            val formBody = FormBody.Builder()
                .add("age", 25)
                .build()

            Then("should convert integer to string and encode") {
                val content = formBody.content()
                content shouldBe "age=25"
            }
        }

        When("add long value") {
            val formBody = FormBody.Builder()
                .add("timestamp", 1234567890L)
                .build()

            Then("should convert long to string and encode") {
                val content = formBody.content()
                content shouldBe "timestamp=1234567890"
            }
        }

        When("add boolean value") {
            val formBody = FormBody.Builder()
                .add("enabled", true)
                .add("verified", false)
                .build()

            Then("should convert boolean to string and encode") {
                val content = formBody.content()
                content shouldBe "enabled=true&verified=false"
            }
        }

        When("add values with special characters") {
            val formBody = FormBody.Builder()
                .add("email", "test@example.com")
                .add("message", "Hello World!")
                .build()

            Then("should URL-encode special characters") {
                val content = formBody.content()
                val expectedEmail = URLEncoder.encode("test@example.com", StandardCharsets.UTF_8.toString())
                val expectedMessage = URLEncoder.encode("Hello World!", StandardCharsets.UTF_8.toString())
                content shouldBe "email=$expectedEmail&message=$expectedMessage"
            }
        }

        When("add empty values") {
            val formBody = FormBody.Builder()
                .add("empty", "")
                .add("blank", "   ")
                .build()

            Then("should handle empty strings") {
                val content = formBody.content()
                content shouldBe "empty=&blank=+++"
            }
        }

        When("build with single parameter") {
            val formBody = FormBody.Builder()
                .add("single", "value")
                .build()

            Then("should not add ampersand separator") {
                val content = formBody.content()
                content shouldBe "single=value"
            }
        }

        When("build with multiple parameters") {
            val formBody = FormBody.Builder()
                .add("param1", "value1")
                .add("param2", "value2")
                .add("param3", "value3")
                .build()

            Then("should separate parameters with ampersand") {
                val content = formBody.content()
                content shouldBe "param1=value1&param2=value2&param3=value3"
            }
        }
    }

    Given("FormBody.fromEncodedContent") {
        When("create from pre-encoded content") {
            val encodedContent = "username=test%40example.com&password=secret%23123"
            val formBody = FormBody.fromEncodedContent(encodedContent)

            Then("should preserve encoded content") {
                formBody.content() shouldBe encodedContent
            }
        }

        When("create from empty string") {
            val formBody = FormBody.fromEncodedContent("")

            Then("should create empty form body") {
                formBody.content() shouldBe ""
            }
        }

        When("create from complex encoded content") {
            val encodedContent =
                "grant_type=authorization_code&code=SplxlOBeZQQYbYS6WxSbIA&redirect_uri=https%3A%2F%2Fclient.example.com%2Fcb"
            val formBody = FormBody.fromEncodedContent(encodedContent)

            Then("should preserve complex encoded content") {
                formBody.content() shouldBe encodedContent
            }
        }
    }

    Given("FormBody content method") {
        When("get content from builder") {
            val builder = FormBody.Builder()
                .add("key", "value")
            val formBody = builder.build()

            Then("should return encoded content") {
                formBody.content() shouldBe "key=value"
            }
        }

        When("get content from fromEncodedContent") {
            val encodedContent = "test=encoded"
            val formBody = FormBody.fromEncodedContent(encodedContent)

            Then("should return the same encoded content") {
                formBody.content() shouldBe encodedContent
            }
        }
    }

    Given("URL encoding") {
        When("encode spaces") {
            val formBody = FormBody.Builder()
                .add("query", "hello world")
                .build()

            Then("should encode spaces as plus signs") {
                val content = formBody.content()
                content shouldBe "query=hello+world"
            }
        }

        When("encode special symbols") {
            val formBody = FormBody.Builder()
                .add("symbols", "!@#$%^&*()")
                .build()

            Then("should properly encode special symbols") {
                val content = formBody.content()
                val expected = URLEncoder.encode("!@#$%^&*()", StandardCharsets.UTF_8.toString())
                content shouldBe "symbols=$expected"
            }
        }

        When("encode unicode characters") {
            val formBody = FormBody.Builder()
                .add("text", "Привет мир")
                .build()

            Then("should properly encode unicode") {
                val content = formBody.content()
                val expected = URLEncoder.encode("Привет мир", StandardCharsets.UTF_8.toString())
                content shouldBe "text=$expected"
            }
        }
    }
})
