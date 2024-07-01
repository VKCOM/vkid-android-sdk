package com.vk.id.health.metrics.apksize

import com.android.build.gradle.AbstractAppExtension
import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.api.BaseVariantOutput
import com.vk.id.health.metrics.VKIDHealthMetricsExtension
import com.vk.id.health.metrics.VKIDHeathMetric
import com.vk.id.health.metrics.utils.exec
import com.vk.id.health.metrics.utils.formatChangePercent
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.configurationcache.extensions.capitalized
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Properties

public fun VKIDHealthMetricsExtension.apkSize(configuration: VKIDApkSizeMetric.Builder.() -> Unit) {
    stepsInternal.add(VKIDApkSizeMetric.Builder().apply(configuration).build())
}

public class VKIDApkSizeMetric(
    private val title: String?,
    private val targetProject: Project,
    private val targetBuildType: ApplicationVariant,
    private val sourceProject: Project,
    private val sourceBuildType: ApplicationVariant?,
) : VKIDHeathMetric {

    override val isExternal: Boolean = false

    private val taskSuffix = "${targetProject.name.capitalized()}${targetBuildType.name.capitalized()}" +
        "${sourceProject.name.capitalized()}${sourceBuildType?.name?.capitalized().orEmpty()}"
    override val task: Task = targetProject.rootProject.tasks.create("healthMetricsApkSize$taskSuffix") {
        dependsOn(":${targetProject.name}:assemble${targetBuildType.name.capitalized()}")
        sourceBuildType?.let { dependsOn(":${sourceProject.name}:assemble${it.name.capitalized()}") }
        doLast {
            val targetSize = getApkSize(targetBuildType.apkFilePath)
            val sourceSize = sourceBuildType?.let { getApkSize(it.apkFilePath) } ?: "0"
            val apkSize = targetSize.toLong() - sourceSize.toLong()
            val storage = ApkSizeRepository(
                targetProject = targetProject,
                targetBuildType = targetBuildType,
                sourceProject = sourceProject,
                sourceBuildType = sourceBuildType
            )
            storage.saveApkSize(apkSize)
            val oldApkSize = storage.getApkSize()

            @Suppress("MagicNumber")
            val apkSizeMb = BigDecimal(apkSize.toDouble() / 1024 / 1024).setScale(2, RoundingMode.HALF_EVEN)
            val targetIdentifier = "${targetProject.name}#${targetBuildType.name}"
            val sourceIdentifier = sourceBuildType?.let { " ${sourceProject.name}${it.name}" }.orEmpty()
            val title = title ?: ("Apk size report for $targetIdentifier$sourceIdentifier")
            val diff = """
                |# $title
                |${apkSizeMb}mb (${formatChangePercent(oldApkSize, apkSize)})
            """.trimMargin()
            storage.saveDiff(diff)
        }
    }

    private fun getApkSize(apkPath: String): String {
        val properties = Properties()
        properties.load(targetProject.rootProject.file("local.properties").inputStream())
        val apkanalyzerPath = properties.getProperty("healthmetrics.apksize.apkanalyzerpath")
            ?: "/opt/android/sdk/cmdline-tools/tools/bin/apkanalyzer"
        return exec("$apkanalyzerPath apk file-size $apkPath").first()
    }

    private val ApplicationVariant.apkFilePath: String
        get() = (outputs.filterIsInstance<BaseVariantOutput>().firstOrNull() ?: error("No apk for variant $name"))
            .outputFile
            .absolutePath

    override fun getDiff(): String = ApkSizeRepository(
        targetProject = targetProject,
        targetBuildType = targetBuildType,
        sourceProject = sourceProject,
        sourceBuildType = sourceBuildType
    ).getDiff()

    override val properties: Array<String> = emptyArray()

    public class Builder {

        public var title: String? = null
        public var targetProject: Project? = null
        public var targetBuildType: String = "release"
        public var sourceProject: Project? = null
        public var sourceBuildType: String? = null

        internal fun build(): VKIDApkSizeMetric {
            return VKIDApkSizeMetric(
                title = title,
                targetProject = checkNotNull(targetProject) { "Project is not specified" },
                targetBuildType = releaseVariant(targetProject!!, targetBuildType) ?: error("No release variant"),
                sourceProject = sourceProject ?: targetProject!!,
                sourceBuildType = sourceBuildType?.let { releaseVariant(targetProject!!, it) },
            )
        }

        private fun releaseVariant(project: Project, name: String) = project.android().applicationVariants.find { it.name == name }
        private fun Project.android() = extensions.getByName("android") as AbstractAppExtension
    }
}
