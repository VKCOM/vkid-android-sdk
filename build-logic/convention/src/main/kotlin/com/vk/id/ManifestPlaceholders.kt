package com.vk.id

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.util.Properties

internal fun Project.configureManifestPlaceholders(
    commonExtension: CommonExtension<*, *, *, *, *>,
) = with(commonExtension) {
    defaultConfig {
        if (shouldInjectManifestPlaceholders()) {
            val secrets = Properties()
            try {
                secrets.load(FileInputStream(rootProject.file("secrets.properties")))
                val clientId = secrets["VKIDClientID"] ?: throw IllegalStateException("Add VKIDClientID to file secrets.properties")
                val clientSecret = secrets["VKIDClientSecret"] ?: throw IllegalStateException("Add VKIDClientSecret to file secrets.properties")
                addManifestPlaceholders(
                    mapOf(
                        "VKIDRedirectHost" to "vk.com",
                        "VKIDRedirectScheme" to "vk$clientId",
                        "VKIDClientID" to clientId,
                        "VKIDClientSecret" to clientSecret
                    )
                )
            } catch (e: FileNotFoundException) {
                logger.error(
                    "Warning! Sample would not work!\nCreate the 'secrets.properties' file in the root folder and add your 'VKIDClientID' and 'VKIDClientSecret' to it." +
                            "\nFor more information, refer to the 'README.md' file."
                )
            }
        }
    }
}

private fun Project.shouldInjectManifestPlaceholders() = gradle
    .startParameter
    .taskNames
    .map { it.lowercase() }
    .any { it.contains("assemble") || it.contains("test") }
