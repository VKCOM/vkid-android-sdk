package com.vk.id.onetap.xml

import android.view.View
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.vk.id.VKID
import com.vk.id.onetap.common.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal fun fetchUserData(
    view: View,
    vkid: VKID,
    stateUpdatingProgressListener: StateUpdatingProgressListener,
) {
    val lifecycleOwner = view.findViewTreeLifecycleOwner()!!
    val resources = view.resources
    var fetchUserJob: Job? = null
    lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onResume(owner: LifecycleOwner) {
            fetchUserJob = lifecycleOwner.lifecycleScope.launch {
                stateUpdatingProgressListener.onPreFetch()
                val user = vkid.fetchUserData().getOrNull()
                val (newText, newIconUrl) = if (user != null) {
                    resources.getString(R.string.vkid_log_in_as, user.firstName) to user.photo200
                } else {
                    resources.getString(R.string.vkid_log_in_with_vkid) to null
                }
                stateUpdatingProgressListener.onFetched(newText, newIconUrl)
            }
        }

        override fun onPause(owner: LifecycleOwner) {
            stateUpdatingProgressListener.onDispose()
            fetchUserJob?.cancel()
        }
    })
}
