package com.vk.id.internal.api.sslpinning.okhttp.security

internal open class SSLCertificateStore(
    open val networkKeyStore: SSLKeyStore,
) {
    val keyStore get() = networkKeyStore.keyStore
}
