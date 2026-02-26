plugins {
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.detekt) apply false
    id("vkid.tools.android.detekt") version "0.0.2" apply true
    id("vkid.tools.android.detekt.compose") version "0.0.2" apply true
    id("vkid.tools.android.publish.library") version "0.0.6" apply false
    id("vkid.tools.android.publish.plugin") version "0.0.6" apply false
}

vkidDetekt {
    detektConfigPath = "${rootProject.projectDir.absolutePath}/detekt/config/detekt.yml"
    detektComposeConfigPath = "${rootProject.projectDir.absolutePath}/detekt/config/detekt-compose.yml"
}

val detekt = tasks.findByName("detekt")!!
subprojects.mapNotNull { it.tasks.findByName("detekt") }.forEach(detekt::dependsOn)
