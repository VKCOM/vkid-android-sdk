package com.vk.id.test

import com.google.gson.Gson
import com.vk.id.commn.InternalVKIDApi
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Timeout

@InternalVKIDApi
internal class MockVKIDCall<T>(
    private val result: Result<T>
) : Call {
    companion object {
        private val gson = Gson()
    }

    private var isExecuted = false
    override fun cancel() = Unit

    override fun clone(): Call = MockVKIDCall(result)

    override fun enqueue(responseCallback: Callback) = responseCallback.onResponse(this, execute())
    override fun execute(): Response {
        isExecuted = true
        val (body, code) = result.getOrNull()?.let {
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val json = gson.toJson(it)
            json.toResponseBody(mediaType) to 200
        } ?: (null to 500)
        return Response.Builder()
            .body(body)
            .request(Request.Builder().url("https://vk.com").build())
            .protocol(Protocol.HTTP_2)
            .message("")
            .code(code)
            .build()
    }

    override fun isCanceled(): Boolean = false

    override fun isExecuted(): Boolean = isExecuted

    override fun request(): Request = Request.Builder().build()

    override fun timeout(): Timeout = Timeout.NONE
}
