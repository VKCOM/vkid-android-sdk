import com.android.build.api.dsl.LibraryExtension
import com.vk.id.health.metrics.buildspeed.buildSpeed
import com.vk.id.health.metrics.gitlab.gitlab
import com.vk.id.health.metrics.storage.firestore
import com.vk.id.health.metrics.apksize.apkSize
import com.vk.id.health.metrics.apichange.publicApiChanges
import com.vk.id.health.metrics.codecoverage.codeCoverage
import java.util.Properties

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
    alias(libs.plugins.kover) apply true
    alias(libs.plugins.screenshot) apply false
}

dependencies {
    subprojects
        .filter { it.extensions.findByType<LibraryExtension>() != null }
        .filter { it.projectDir.path.contains("/sdk/") }
        .forEach { kover(it) }
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
    val localProperties = Properties()
    localProperties.load(rootProject.file("local.properties").inputStream())
    gitlab {
        localProperties.getProperty("healthmetrics.gitlab.token")
    }
    firestore(rootProject.file("build-logic/metrics/service-credentials.json"))
    codeCoverage {
        title = "Code coverage"
        targetProject = rootProject
        customKoverDirectory = rootProject.file("artifacts/kover")
            .let { if (it.exists()) it else rootProject.layout.buildDirectory.file("reports/kover").get().asFile }
    }
    buildSpeed {
        title = "Build speed of :assembleDebug"
        measuredTaskPaths = setOf(":assembleDebug")
        iterations = 3
        warmUps = 2
        cleanAfterEachBuild = true
    }
    apkSize {
        title = "SDK size with all dependencies"
        targetProject = projects.sampleMetricsApp.dependencyProject
        targetBuildType = "withSdk"
        sourceBuildType = "debug"
        apkAnalyzerPath = localProperties.getProperty("healthmetrics.apksize.apkanalyzerpath")
    }
    apkSize {
        title = "Pure SDK size"
        targetProject = projects.sampleMetricsApp.dependencyProject
        targetBuildType = "withSdk"
        sourceBuildType = "withDeps"
        apkAnalyzerPath = localProperties.getProperty("healthmetrics.apksize.apkanalyzerpath")
    }
    publicApiChanges()
}
