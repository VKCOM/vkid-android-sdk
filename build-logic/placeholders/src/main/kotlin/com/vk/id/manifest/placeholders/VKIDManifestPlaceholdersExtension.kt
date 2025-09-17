package com.vk.id.manifest.placeholders

/**
 * Configures manifest placeholders for VKID integration.
 */
public open class VKIDManifestPlaceholdersExtension {
    /** Specifies redirect host manifest placeholder */
    public var vkidRedirectHost: String? = null

    /** Specifies redirect scheme manifest placeholder */
    public var vkidRedirectScheme: String? = null

    /** Specifies client id manifest placeholder */
    public var vkidClientId: String? = null

    /** Specifies client secret manifest placeholder */
    public var vkidClientSecret: String? = null

    /**
     * Generates placeholders based on the client id and client secret.
     * Client id and client secret will be taken as is, redirect host will be equal to "vk.ru" and redirect scheme will be "vk" + client id
     *
     * @param clientId A client id
     * @param clientSecret A client secret
     */
    public fun init(
        clientId: String,
        clientSecret: String
    ) {
        vkidRedirectHost = "vk.ru"
        vkidRedirectScheme = "vk$clientId"
        vkidClientId = clientId
        vkidClientSecret = clientSecret
    }
}
