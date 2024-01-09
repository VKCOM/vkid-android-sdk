package com.vk.id.internal.user

import com.vk.id.VKIDUser
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.concurrent.CoroutinesDispatchers
import com.vk.id.internal.ipc.VkSilentAuthInfoProvider
import com.vk.id.internal.util.currentTime
import com.vk.id.toVKIDUser
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

internal class UserDataFetcher(
    private val dispatchers: CoroutinesDispatchers,
    private val serviceCredentials: ServiceCredentials,
    private val vkSilentAuthInfoProvider: VkSilentAuthInfoProvider,
) {
    // we want all instances of vkid call provider only once at a time
    companion object {
        private val fetchUserMutex = Mutex()
        private var cachedUserData: CachedUserWithTimeFetched? = null
        private const val CACHED_USER_TIME_TO_EXPIRE_MS = 500
    }

    suspend fun fetchUserData(): VKIDUser? = fetchUserMutex.withLock {
        cachedUserData?.let {
            if (it.isValid) {
                return it.data
            }
        }

        val info = withContext(dispatchers.io) {
            vkSilentAuthInfoProvider.setAppId(serviceCredentials.clientID.toInt())
            vkSilentAuthInfoProvider.getSilentAuthInfos().firstOrNull()
        }
        val user = info?.toVKIDUser()
        cachedUserData = CachedUserWithTimeFetched(user)
        return user
    }

    private class CachedUserWithTimeFetched(val data: VKIDUser?, val timeFetched: Long = currentTime()) {
        val isValid: Boolean get() {
            return currentTime() - timeFetched <= CACHED_USER_TIME_TO_EXPIRE_MS
        }
    }
}
