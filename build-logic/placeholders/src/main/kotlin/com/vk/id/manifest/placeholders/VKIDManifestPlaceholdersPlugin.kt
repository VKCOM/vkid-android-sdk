package com.vk.id.manifest.placeholders

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.util.Properties

internal class VKIDManifestPlaceholdersPlugin : Plugin<Project> {

    private companion object {
        const val REDIRECT_HOST_PLACEHOLDER = "VKIDRedirectHost"
        const val REDIRECT_SCHEME_PLACEHOLDER = "VKIDRedirectScheme"
        const val CLIENT_ID_PLACEHOLDER = "VKIDClientID"
        const val CLIENT_SECRET_PLACEHOLDER = "VKIDClientSecret"
        var extensionInternal: VKIDManifestPlaceholdersExtension? = null
        val extension
            get() = extensionInternal
                ?: error("VKID manifest placeholders weren't initialized. Please apply and configure the plugin in the root project")
    }

    override fun apply(target: Project) {
        if (target == target.rootProject) {
            extensionInternal = target.createExtension()
        }
        target.androidApplication()?.apply {
            val localProperties = target.loadLocalProperties()
            addPlaceholder(REDIRECT_HOST_PLACEHOLDER, localProperties, extension.vkidRedirectHost, "vkidRedirectHost")
            addPlaceholder(REDIRECT_SCHEME_PLACEHOLDER, localProperties, extension.vkidRedirectScheme, "vkidRedirectScheme")
            addPlaceholder(CLIENT_ID_PLACEHOLDER, localProperties, extension.vkidClientId, "vkidClientId")
            addPlaceholder(CLIENT_SECRET_PLACEHOLDER, localProperties, extension.vkidClientSecret, "vkidClientSecret")
        }
    }

    private fun Project.createExtension() = extensions.create("vkidManifestPlaceholders", VKIDManifestPlaceholdersExtension::class)

    private fun Project.androidApplication() = extensions.findByName("android") as? CommonExtension<*, *, *, *, *, *>

    private fun Project.loadLocalProperties() = Properties().also {
        try {
            it.load(FileInputStream(rootProject.file("local.properties")))
        } catch (@Suppress("SwallowedException") e: FileNotFoundException) {
            // Ignore
        }
    }

    private fun CommonExtension<*, *, *, *, *, *>.addPlaceholder(
        name: String,
        localProperties: Properties,
        extensionValue: String?,
        extensionName: String,
    ) {
        (extensionValue ?: localProperties[name] ?: errorNoPlaceholder(name, extensionName))
            ?.let { placeholderValue -> defaultConfig.addManifestPlaceholders(mapOf(name to placeholderValue)) }
    }

    private fun errorNoPlaceholder(localPropertiesName: String, extensionName: String): String? {
        System.err.println(
            "VKID manifest placeholder $localPropertiesName is not specified. " +
                "Please provide it as $localPropertiesName=XXX in root project's local.properties or " +
                "in the plugin extension vkidManifestPlaceholders { $extensionName = XXX }"
        )
        return null
    }
}
