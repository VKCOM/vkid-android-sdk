/**
 * Copyright (c) 2020 - present, LLC “V Kontakte”
 *
 * 1. Permission is hereby granted to any person obtaining a copy of this Software to
 * use the Software without charge.
 *
 * 2. Restrictions
 * You may not modify, merge, publish, distribute, sublicense, and/or sell copies,
 * create derivative works based upon the Software or any part thereof.
 *
 * 3. Termination
 * This License is effective until terminated. LLC “V Kontakte” may terminate this
 * License at any time without any negative consequences to our rights.
 * You may terminate this License at any time by deleting the Software and all copies
 * thereof. Upon termination of this license for any reason, you shall continue to be
 * bound by the provisions of Section 2 above.
 * Termination will be without prejudice to any rights LLC “V Kontakte” may have as
 * a result of this agreement.
 *
 * 4. Disclaimer of warranty and liability
 * THE SOFTWARE IS MADE AVAILABLE ON THE “AS IS” BASIS. LLC “V KONTAKTE” DISCLAIMS
 * ALL WARRANTIES THAT THE SOFTWARE MAY BE SUITABLE OR UNSUITABLE FOR ANY SPECIFIC
 * PURPOSES OF USE. LLC “V KONTAKTE” CAN NOT GUARANTEE AND DOES NOT PROMISE ANY
 * SPECIFIC RESULTS OF USE OF THE SOFTWARE.
 * UNDER NO CIRCUMSTANCES LLC “V KONTAKTE” BEAR LIABILITY TO THE LICENSEE OR ANY
 * THIRD PARTIES FOR ANY DAMAGE IN CONNECTION WITH USE OF THE SOFTWARE.
*/
package com.vk.id.internal.ipc

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.IBinder
import com.vk.id.common.InternalVKIDApi
import com.vk.id.logger.createLoggerForClass
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.math.max

internal abstract class IPCClientBaseProvider<T> {

    @OptIn(InternalVKIDApi::class)
    private val logger = createLoggerForClass()

    open lateinit var appContext: Context

    abstract val intentName: String

    abstract fun setProvider(
        connectionInfo: ConnectionInfo<T>?,
        service: IBinder?
    )

    protected val providerComparator: Comparator<String> = Comparator { o1, o2 -> o1.compareTo(o2) }

    protected val componentComparator = Comparator<ComponentName> { a, b ->
        providerComparator.compare(a?.packageName, b?.packageName)
    }

    protected val connectionsMap = ConcurrentHashMap<ComponentName, ConnectionInfo<T>>()

    protected fun getProvider(
        component: ComponentName,
        startTime: Long,
        timeout: Long
    ): T? {
        // attempts variable is because of IMPORTANT block in prepareSpecificApp()
        @Suppress("MagicNumber")
        var attempts = 3
        var provider: T? = null
        while (provider == null && attempts-- > 0) {
            val connectionInfo = prepareSpecificApp(component)
            if (connectionInfo != null) {
                provider = connectionInfo.provider
                if (provider != null) {
                    break
                }

                if (waitForConnection(connectionInfo, startTime, timeout)) {
                    provider = connectionInfo.provider ?: connectionsMap[component]?.provider
                } else {
                    return null
                }
            }
        }
        return provider
    }

    protected fun getSignature(): Signature? {
        return appContext.packageManager
            .getPackageInfo(appContext.packageName, PackageManager.GET_SIGNATURES)
            .signatures
            .firstOrNull()
    }

    @OptIn(InternalVKIDApi::class)
    @Suppress("TooGenericExceptionCaught")
    protected fun prepareSpecificApp(component: ComponentName): ConnectionInfo<T>? {
        var connectionInfo = connectionsMap[component]
        if (connectionInfo?.provider != null) {
            return connectionInfo
        }

        @Suppress("MagicNumber")
        val latch = CountDownLatch(1)
        if (connectionInfo == null) {
            val connection = object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    connectionsMap[component]?.let {
                        synchronized(it.lock) {
                            setProvider(connectionInfo, service)
                            it.connectionState = ConnectionInfo.CONNECTION_STATE_CONNECTED
                            it.latch.countDown()
                        }
                    }
                }

                override fun onServiceDisconnected(name: ComponentName?) {
                    // Not unbind here. If we will need connection to this component, it's okay to call
                    // bind again. Or if system will recreate service, it will be connected using this
                    // connection.
                    connectionsMap[component]?.let {
                        synchronized(it.lock) {
                            it.provider = null
                            it.connectionState = ConnectionInfo.CONNECTION_STATE_DISCONNECTED
                        }
                    }
                }
            }
            connectionInfo = ConnectionInfo(latch, connection)
            connectionsMap[component] = connectionInfo
        } else {
            synchronized(connectionInfo.lock) {
                if (connectionInfo.connectionState == ConnectionInfo.CONNECTION_STATE_UNKNOWN) {
                    // We have started the binding from some thread but then called prepareSpecificApp() from
                    // some other thread before onServiceConnected() is called so wait using existing latch
                    // will be okay
                } else if (connectionInfo.connectionState == ConnectionInfo.CONNECTION_STATE_CONNECTED) {
                    // Okay, we connected from some other thread, and some kind of race contidition occured,
                    // so just do nothing, wait using existing latch will be okay
                } else if (connectionInfo.connectionState == ConnectionInfo.CONNECTION_STATE_DISCONNECTED) {
                    // Refresh latch because we've tried to connect to service, but onServiceDisconnected()
                    // was called and we did reset provider, so we need wait for onServiceConnected() again.
                    //
                    // IMPORTANT!
                    // I'm not sure if onServiceDisconnected() can be called without onServiceConnected(). Because
                    // if it does, there is possibility of this situation:
                    // We have called bind() during prepare() on some thread (T1), then we called prepareSpecificApp() in
                    // getSpecificAppSilentAuthInfos() so we called latch.await() on this thread (T2). Then onServiceConnected()
                    // is called on T1. We have to call bind again() without blocking the T2 (or have to wait until
                    // system recreates service, but c'mon...).
                    //
                    // But I hope this situation will never become.
                    connectionInfo.latch.countDown()
                    connectionInfo.latch = latch
                }
            }
        }

        synchronized(connectionInfo.lock) {
            connectionInfo.connectionState = ConnectionInfo.CONNECTION_STATE_UNKNOWN
        }

        val intent = Intent(intentName)
            .setComponent(component)
        val bound = try {
            appContext.bindService(intent, connectionInfo.connection, Service.BIND_AUTO_CREATE)
        } catch (e: Exception) {
            logger.error("Error while binding to ${component.packageName}", e)
            false
        }
        return if (bound) {
            connectionInfo
        } else {
            null
        }
    }

    @OptIn(InternalVKIDApi::class)
    private fun <T> waitForConnection(connectionInfo: ConnectionInfo<T>, startTime: Long, timeout: Long): Boolean {
        val actualTimeout = calculateActualTimeout(startTime, timeout)
        return try {
            connectionInfo.latch.await(actualTimeout, TimeUnit.MILLISECONDS)
        } catch (e: InterruptedException) {
            logger.error("Interrupted wait for connection", e)
            return false
        }
    }

    private fun calculateActualTimeout(startTime: Long, timeout: Long): Long {
        val currentTime = System.currentTimeMillis()
        val expiredTime = currentTime - startTime
        return max(timeout - expiredTime, 0)
    }
}
