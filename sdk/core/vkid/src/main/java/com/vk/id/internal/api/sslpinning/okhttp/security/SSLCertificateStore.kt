package com.vk.id.internal.api.sslpinning.okhttp.security

internal open class SSLCertificateStore(
    open val networkKeyStore: SSLKeyStore,
    protected val isPinningEnabled: Boolean
) {
    val keyStore get() = if (isPinningEnabled) networkKeyStore.keyStore else null
}
