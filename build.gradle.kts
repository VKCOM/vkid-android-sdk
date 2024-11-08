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
    id("vkid.health.metrics") version "1.0.0-alpha03" apply true
    id("vkid.detekt") apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kover) apply true
    alias(libs.plugins.screenshot) apply false
    id("vkid.manifest.placeholders") version "1.1.0" apply true
}

dependencies {
    subprojects
        .filter { it.name != projects.baselineProfile.name }
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
        subprojects
            .asSequence()
            .filter { it.name != projects.baselineProfile.name }
            .filter { it.name != projects.detektRules.name }
            .map { ":${it.name}:$name" }
            .forEach { dependsOn(it) }
        configuration()
    }
}

vkidManifestPlaceholders {
    if (!shouldInjectManifestPlaceholders()) return@vkidManifestPlaceholders
    fun error() = logger.error(
        "Warning! Build will not work!\nCreate the 'secrets.properties' file in the 'sample/app' folder and add your 'VKIDClientID' and 'VKIDClientSecret' to it." +
            "\nFor more information, refer to the 'README.md' file."
    )

    val properties = Properties()
    properties.load(file("sample/app/secrets.properties").inputStream())
    val clientId = properties["VKIDClientID"] ?: error()
    val clientSecret = properties["VKIDClientSecret"] ?: error()
    init(
        clientId = clientId.toString(),
        clientSecret = clientSecret.toString(),
    )
}

healthMetrics {
    val localProperties by lazy {
        Properties().apply { load(rootProject.file("local.properties").inputStream()) }
    }
    gitlab(
        host = { localProperties.getProperty("healthmetrics.gitlab.host") },
        token = { localProperties.getProperty("healthmetrics.gitlab.token") },
        projectId = { "2796" },
    )
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
        apkAnalyzerPath = { localProperties.getProperty("healthmetrics.apksize.apkanalyzerpath") }
    }
    apkSize {
        title = "Pure SDK size"
        targetProject = projects.sampleMetricsApp.dependencyProject
        targetBuildType = "withSdk"
        sourceBuildType = "withDeps"
        apkAnalyzerPath = { localProperties.getProperty("healthmetrics.apksize.apkanalyzerpath") }
    }
    publicApiChanges()
}

/**
 * The project should sync without placeholders
 */
private fun Project.shouldInjectManifestPlaceholders() = gradle
    .startParameter
    .taskNames
    .map { it.lowercase() }
    .any {
        it.contains("assemble")
            || it.endsWith("test")
            || it.contains("lint")
            || it.contains("dokka")
            || it.contains("generatebaselineprofile")
            || it.contains("updatedebugscreenshottest")
            || it.contains("healthmetrics")
    }
