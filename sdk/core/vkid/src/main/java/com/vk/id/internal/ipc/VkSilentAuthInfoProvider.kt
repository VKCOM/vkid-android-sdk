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

import android.content.Context
import android.os.IBinder
import com.vk.id.internal.auth.app.SilentAuthInfoUtils
import com.vk.id.internal.auth.app.SilentAuthProviderData
import com.vk.id.internal.auth.app.SilentAuthServicesProvider
import com.vk.id.internal.auth.device.DeviceIdProvider
import com.vk.id.internal.ipc.VkSilentInfoItemsGrouper.groupByWeightAndUserHash
import com.vk.id.internal.log.createLoggerForClass
import com.vk.silentauth.ISilentAuthInfoProvider
import com.vk.silentauth.SilentAuthInfo
import com.vk.silentauth.SilentAuthInfoWithProviderWeight
import java.util.UUID
import java.util.concurrent.TimeUnit

/**
 * Class for retrieving information about applications that hold logged in vk account.
 * Binding to services with specific intent action and then retrieving info via aidl mechanism is used for this.
 *
 * @param context
 *   context that can be used for [Context.getApplicationContext]
 */
internal class VkSilentAuthInfoProvider(
    context: Context,
    private val servicesProvider: SilentAuthServicesProvider,
    private val deviceIdProvider: DeviceIdProvider,
    override val defaultTimeout: Long = TimeUnit.SECONDS.toMillis(DEFAULT_BINDING_TIMEOUT_SECONDS),
) : SilentAuthInfoProvider, IPCClientBaseProvider<ISilentAuthInfoProvider>() {
    private val logger = createLoggerForClass()

    override val intentName: String
        get() = SilentAuthServicesProvider.ACTION_GET_INFO

    override fun setProvider(
        connectionInfo: ConnectionInfo<ISilentAuthInfoProvider>?,
        service: IBinder?
    ) {
        connectionInfo?.provider = ISilentAuthInfoProvider.Stub.asInterface(service)
    }

    override var appContext: Context = context.applicationContext

    private var appId: Int = 0

    private var apiVersion: String? = SILENT_AUTH_API_VERSION

    /**
     * @param timeout timeout for retrieving info. This timeout is general, i.e. if first app will take X millis to give info,
     *   for remaining apps timeout will be equal to (timeout-X) millis
     */
    override suspend fun getSilentAuthInfos(timeout: Long): List<SilentAuthInfo> {
        if (appId == 0) {
            return emptyList()
        }

        val startTime = System.currentTimeMillis()

        val services = servicesProvider.getSilentAuthServices().onEach { prepareSpecificApp(it.componentName) }

        val resultItems = services.map { getSpecificAppSilentAuthInfos(it, startTime, timeout) }
        return resultItems.map {
            if (it.exception != null) {
                logger.error("Exception while fetching silent auth info: ${it.exception.message}", it.exception)
            }
            it.infoItems
        }
            .flatten()
            .groupByWeightAndUserHash()
            .map { it.info }
    }

    override fun setAppId(appId: Int) {
        this.appId = appId
    }

    override fun setApiVersion(apiVersion: String) {
        this.apiVersion = apiVersion
    }

    private fun getSpecificAppSilentAuthInfos(
        component: SilentAuthProviderData,
        startTime: Long,
        timeout: Long
    ): SilentAuthResult {
        val provider = getProvider(component.componentName, startTime, timeout)
        return getInfosFromAidl(provider, component)
    }

    @Suppress("TooGenericExceptionCaught")
    private fun getInfosFromAidl(
        provider: ISilentAuthInfoProvider?,
        component: SilentAuthProviderData
    ): SilentAuthResult {
        provider ?: return SilentAuthResult(NullPointerException("Provider is null"))

        val signature = getSignature()
        return if (signature != null) {
            try {
                val infoItems = provider.getSilentAuthInfos(
                    appId,
                    appContext.packageName,
                    SilentAuthInfoUtils.calculateDigestBase64(signature),
                    UUID.randomUUID().toString(),
                    apiVersion,
                    deviceIdProvider.getDeviceId(appContext),
                    null
                )
                    .map { infoItem ->
                        SilentAuthInfoWithProviderWeight(
                            info = infoItem.copy(applicationProviderPackage = component.componentName.packageName),
                            providerWeight = component.weight,
                        )
                    }
                SilentAuthResult(infoItems)
            } catch (exception: Exception) {
                SilentAuthResult(exception)
            }
        } else {
            SilentAuthResult(NullPointerException("Signature is null"))
        }
    }

    internal data class SilentAuthResult(
        val infoItems: List<SilentAuthInfoWithProviderWeight>,
        val exception: Exception?
    ) {
        constructor(infoItems: List<SilentAuthInfoWithProviderWeight>) : this(infoItems, null)
        constructor(exception: Exception) : this(emptyList(), exception)
    }

    companion object {
        internal const val SILENT_AUTH_API_VERSION = "5.219"
        internal const val DEFAULT_BINDING_TIMEOUT_SECONDS = 30L
    }
}
