package com.vk.id.internal.concurrent

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.vk.id.VKIDCall
import java.lang.ref.WeakReference
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

internal class LifecycleAwareExecutor(private val executorService: ExecutorService) {

    private val activeCalls: MutableList<WeakReference<VKIDCall<*>>> = mutableListOf()
    private var lifecycleCallback: Application.ActivityLifecycleCallbacks? = null

    fun attachActivity(activity: Activity) {
        registerLifeCycleCallback(activity.application, activity)
    }

    fun submit(task: Runnable): Future<*> = executorService.submit(task)

    fun <T>executeCall(apiCall: VKIDCall<T>): Result<T>  {
        activeCalls.add(WeakReference(apiCall))
        return apiCall.execute()
    }

    fun execute(runnable: Runnable) = executorService.execute(runnable)

    private fun registerLifeCycleCallback(app: Application, activity: Activity) {
        lifecycleCallback = object: Application.ActivityLifecycleCallbacks {
            private var activityRef = WeakReference(activity)
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                // we can loose reference on activity recreation on screen rotate
                if (savedInstanceState?.getBoolean(REGISTERED_ACTIVITY_FLAG) == true) {
                    activityRef = WeakReference(activity)
                }
            }
            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                outState.putBoolean(REGISTERED_ACTIVITY_FLAG, true)
            }
            override fun onActivityDestroyed(activity: Activity) {
                if (activityRef.get() === activity && activity.isFinishing) {
                    activeCalls.forEach { it.get()?.cancel() }
                    executorService.shutdown()
                    app.unregisterActivityLifecycleCallbacks(this)
                }
            }
        }
        lifecycleCallback?.let {
            app.registerActivityLifecycleCallbacks(it)
        }
    }
    private companion object {
        const val REGISTERED_ACTIVITY_FLAG = "VKID_CALLBACK_REGISTERED_ACTIVITY_FLAG"
    }
}