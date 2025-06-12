package com.vk.id.dokka_since_validator

import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.plugability.DokkaPlugin
import org.jetbrains.dokka.plugability.DokkaPluginApiPreview
import org.jetbrains.dokka.plugability.PluginApiPreviewAcknowledgement

/**
 * A dokka plugin that validates that all public api KDoc contains @since annotation
 */
class SinceValidatorPlugin : DokkaPlugin() {
    val filterExtension by extending {
        plugin<DokkaBase>().preMergeDocumentableTransformer providing ::SincePresenceChecker
    }

    @OptIn(DokkaPluginApiPreview::class)
    override fun pluginApiPreviewAcknowledgement() = PluginApiPreviewAcknowledgement
}