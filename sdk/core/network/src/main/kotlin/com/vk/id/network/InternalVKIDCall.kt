package com.vk.id.network

import androidx.annotation.WorkerThread
import com.vk.id.common.InternalVKIDApi
import okhttp3.Call
import okhttp3.Response
import org.json.JSONException
import java.io.IOException

@InternalVKIDApi
public interface InternalVKIDCall<out T> {
    @WorkerThread
    public fun execute(): Result<T>

    /**
     * Function to cancel [InternalVKIDCall]
     */
    public fun cancel()
}

@InternalVKIDApi
public fun <T> Call.internalVKIDWrapToVKIDCall(
    responseMapping: (response: Response) -> T,
): InternalVKIDCall<T> {
    return object : InternalVKIDCall<T> {
        override fun execute(): Result<T> {
            return try {
                val response = this@internalVKIDWrapToVKIDCall.execute()
                Result.success(responseMapping(response))
            } catch (ioe: IOException) {
                Result.failure(ioe)
            } catch (je: JSONException) {
                Result.failure(je)
            }
        }

        override fun cancel() = this@internalVKIDWrapToVKIDCall.cancel()
    }
}
