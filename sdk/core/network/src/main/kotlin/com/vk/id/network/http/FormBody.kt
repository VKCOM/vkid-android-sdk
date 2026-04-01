@file:OptIn(InternalVKIDApi::class)

package com.vk.id.network.http

import com.vk.id.common.InternalVKIDApi
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * HTTP request body builder for form-encoded data (application/x-www-form-urlencoded).
 */
@InternalVKIDApi
public class FormBody private constructor(
    private val content: String
) {
    internal companion object {

        /**
         * Creates FormBody from already URL-encoded content string.
         * Use this when you have pre-encoded form data.
         *
         * @param encodedContent URL-encoded form data string
         * @return FormBody instance
         */
        fun fromEncodedContent(encodedContent: String): FormBody {
            return FormBody(encodedContent)
        }
    }

    /**
     * Returns the encoded form data as a string.
     */
    internal fun content(): String = content

    /**
     * Builder for creating form-encoded request bodies.
     */
    @InternalVKIDApi
    public class Builder {
        private val encodedNames: MutableList<String> = mutableListOf()
        private val encodedValues: MutableList<String> = mutableListOf()

        /**
         * Adds a key-value pair to the form body.
         * The values are URL-encoded automatically.
         *
         * @param name the field name
         * @param value the field value
         * @return this builder instance for method chaining
         */
        @InternalVKIDApi
        public fun add(name: String, value: String): Builder = apply {
            encodedNames.add(name)
            encodedValues.add(value)
        }

        /**
         * Adds a key-value pair to the form body.
         * The values are URL-encoded automatically.
         *
         * @param name the field name
         * @param value the field value (converted to string)
         * @return this builder instance for method chaining
         */
        @InternalVKIDApi
        public fun add(name: String, value: Int): Builder = apply {
            encodedNames.add(name)
            encodedValues.add(value.toString())
        }

        /**
         * Adds a key-value pair to the form body.
         * The values are URL-encoded automatically.
         *
         * @param name the field name
         * @param value the field value (converted to string)
         * @return this builder instance for method chaining
         */
        @InternalVKIDApi
        public fun add(name: String, value: Long): Builder = apply {
            encodedNames.add(name)
            encodedValues.add(value.toString())
        }

        /**
         * Adds a key-value pair to the form body.
         * The values are URL-encoded automatically.
         *
         * @param name the field name
         * @param value the field value (converted to string)
         * @return this builder instance for method chaining
         */
        @InternalVKIDApi
        public fun add(name: String, value: Boolean): Builder = apply {
            encodedNames.add(name)
            encodedValues.add(value.toString())
        }

        /**
         * Builds the form-encoded body string.
         *
         * @return a FormBody instance with the encoded content
         */
        @InternalVKIDApi
        public fun build(): FormBody {
            val content = StringBuilder()
            for (i in encodedNames.indices) {
                if (i > 0) {
                    content.append('&')
                }
                content.append(urlEncode(encodedNames[i]))
                content.append('=')
                content.append(urlEncode(encodedValues[i]))
            }
            return FormBody(content.toString())
        }

        /**
         * URL-encodes a string value using UTF-8 charset.
         *
         * @param value the string to encode
         * @return the URL-encoded string
         */
        private fun urlEncode(value: String): String {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString())
        }
    }
}
