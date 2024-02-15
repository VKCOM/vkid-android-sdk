package com.vk.id.internal.api.sslpinning.okhttp.security

import android.content.Context
import androidx.annotation.WorkerThread
import com.vk.id.R
import com.vk.id.internal.log.createLoggerForClass
import java.io.BufferedInputStream
import java.io.InputStream
import java.math.BigInteger
import java.security.KeyStore
import java.security.cert.Certificate
import java.security.cert.X509Certificate
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.Future
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

internal open class SSLKeyStore(
    context: Context,
    private val useDefaultHost: Boolean,
    private val additionalCertificates: List<Certificate> = emptyList()
) {

    private val logger = createLoggerForClass()

    interface SSLKeyStoreInitializationListener {
        @WorkerThread
        fun onSSLKeyStoreInitializationFailed(e: Throwable)

        @WorkerThread
        fun onSSLKeyStoreInitializationComplete()
    }

    private sealed class InitializationState {
        object Uninitialized : InitializationState()
        object Successful : InitializationState()
        class Failed(val e: Throwable) : InitializationState()
    }

    private val listeners = CopyOnWriteArrayList<SSLKeyStoreInitializationListener>()

    private val mutCertificates = CopyOnWriteArrayList<Certificate>()
    private val store: AtomicReference<KeyStore> = AtomicReference()
    private val initFuture: Future<*>

    val keyStore: KeyStore?
        get() {
            awaitInitialization()
            return store.get()
        }

    @Volatile
    private var initializationState: InitializationState = InitializationState.Uninitialized

    private val serialNumberVkRUCertificate = BigInteger("551222861474729630828211419619667128155611726319")

    init {
        val source = BufferedInputStream(
            context.resources.openRawResource(R.raw.vkid_cacerts),
            CA_CERTS_FILE_SIZE
        )

        val keyStorePassword = "changeit"
        initFuture = ThreadPoolExecutor(
            1,
            1,
            KEEP_ALIVE_TIME,
            TimeUnit.MILLISECONDS,
            LinkedBlockingQueue(),
            ThreadFactory {
                Thread(it, "vk-thread-ssl-cert-prefetch").apply {
                    priority = Thread.MAX_PRIORITY
                }
            }
        ).apply { allowCoreThreadTimeOut(true) }
            .submit { initStore(source, keyStorePassword) }
    }

    fun addInitializationListener(listener: SSLKeyStoreInitializationListener) {
        synchronized(initializationState) {
            when (val state = initializationState) {
                is InitializationState.Uninitialized -> listeners.add(listener)
                is InitializationState.Successful -> listener.onSSLKeyStoreInitializationComplete()
                is InitializationState.Failed -> listener.onSSLKeyStoreInitializationFailed(state.e)
            }
        }
    }

    private fun awaitInitialization() {
        initFuture.get()
    }

    private fun initStore(source: InputStream, keyStorePassword: String) {
        runCatching {
            val keyStore = KeyStore.getInstance("BKS")
            loadCertificates(source, keyStore, keyStorePassword)
            mutCertificates.addAll(additionalCertificates)
            notifyAllListenersWithCompletion()
        }.onFailure {
            logger.error("Can't load SSL certificates", it)
            notifyAllListenersWithFailure(it)
        }
    }

    private fun loadCertificates(source: InputStream, keyStore: KeyStore, password: String) {
        source.use {
            keyStore.load(source, password.toCharArray())
            if (store.compareAndSet(null, keyStore)) {
                val certificates = keyStore.aliases()
                    .iterator()
                    .asSequence()
                    .map { alias ->
                        keyStore.getCertificate(alias)
                    }.filter(::filterVkRuCertificate)
                    .toList()

                mutCertificates.addAll(certificates)
            }
        }
    }

    private fun filterVkRuCertificate(cert: Certificate): Boolean {
        // use all bundle certs
        if (!useDefaultHost) return true

        return cert is X509Certificate &&
            cert.serialNumber != serialNumberVkRUCertificate
    }

    private fun notifyAllListenersWithFailure(exception: Throwable) {
        synchronized(initializationState) {
            initializationState = InitializationState.Failed(exception)
        }
        listeners.forEach { listener ->
            listener.onSSLKeyStoreInitializationFailed(exception)
        }
    }

    private fun notifyAllListenersWithCompletion() {
        synchronized(initializationState) {
            initializationState = InitializationState.Successful
        }
        listeners.forEach { listener ->
            listener.onSSLKeyStoreInitializationComplete()
        }
    }

    companion object {
        protected const val CA_CERTS_FILE_SIZE = 1024 * 256
        private const val KEEP_ALIVE_TIME = 100L
    }
}
