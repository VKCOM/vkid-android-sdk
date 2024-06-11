import com.vk.id.health.metrics.buildspeed.buildSpeed
import com.vk.id.health.metrics.gitlab.gitlab
import com.vk.id.health.metrics.storage.firestore

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.binary.compatibility.validator) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.detekt) apply false
    id("vkid.android.dependency.lock") apply true
    alias(libs.plugins.androidTest) apply false
    alias(libs.plugins.baselineprofile) apply false
    id("vkid.android.project-substitution") apply true
    id("vkid.health.metrics") apply true
    id("vkid.detekt") apply false
    alias(libs.plugins.compose.compiler) apply false
}

registerGeneralTask("detekt") {
    dependsOn(gradle.includedBuild("build-logic").task(":detekt"))
}
registerGeneralTask("clean")
registerGeneralTask("assembleDebug")
registerGeneralTask("assembleRelease")

private fun registerGeneralTask(name: String, configuration: Task.() -> Unit = {}) {
    tasks.register(name) {
        subprojects.mapNotNull { it.tasks.findByName(name) }.forEach(::dependsOn)
        configuration()
    }
}

healthMetrics {
    gitlab()
    firestore(rootProject.file("build-logic/metrics/service-credentials.json"))
    buildSpeed {
        measuredTaskPaths = setOf(":clean", ":assembleDebug")
    }
}
