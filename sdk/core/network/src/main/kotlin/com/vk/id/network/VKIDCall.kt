package com.vk.id.network

import androidx.annotation.WorkerThread
import com.vk.id.common.InternalVKIDApi
import okhttp3.Call
import okhttp3.Response
import org.json.JSONException
import java.io.IOException

@InternalVKIDApi
public interface VKIDCall<out T> {
    @WorkerThread
    public fun execute(): Result<T>

    /**
     * Function to cancel [VKIDCall]
     */
    public fun cancel()
}

@InternalVKIDApi
public fun <T> Call.wrapToVKIDCall(
    responseMapping: (response: Response) -> T,
): VKIDCall<T> {
    return object : VKIDCall<T> {
        override fun execute(): Result<T> {
            return try {
                val response = this@wrapToVKIDCall.execute()
                Result.success(responseMapping(response))
            } catch (ioe: IOException) {
                Result.failure(ioe)
            } catch (je: JSONException) {
                Result.failure(je)
            }
        }

        override fun cancel() = this@wrapToVKIDCall.cancel()
    }
}
