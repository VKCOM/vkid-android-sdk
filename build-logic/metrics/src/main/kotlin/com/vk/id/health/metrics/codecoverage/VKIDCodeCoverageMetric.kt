package com.vk.id.health.metrics.codecoverage

import com.vk.id.health.metrics.VKIDHealthMetricsExtension
import com.vk.id.health.metrics.VKIDSingleRunHealthMetric
import com.vk.id.health.metrics.utils.formatChangePercent
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.configurationcache.extensions.capitalized
import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale

public fun VKIDHealthMetricsExtension.codeCoverage(configuration: VKIDCodeCoverageMetric.Builder.() -> Unit) {
    stepsInternal.add(VKIDCodeCoverageMetric.Builder().apply(configuration).build())
}

public class VKIDCodeCoverageMetric internal constructor(
    private val title: String?,
    private val targetProject: Project,
    private val koverDirectory: File?,
) : VKIDSingleRunHealthMetric {

    private companion object {
        private const val LINES_BETWEEN_TITLE_AND_PERCENT = 45
    }

    private val repository = CodeCoverageRepository(targetProject)

    override val task: Task = targetProject.rootProject.tasks.create("healthMetricsCodeCoverage${targetProject.name.capitalized()}") {
        doLast {
            val koverDirectory = koverDirectory ?: targetProject.layout.buildDirectory.file("reports/kover").get().asFile
            val file = File(koverDirectory, "html/index.html")
            val lines = file.readLines()
            val titleIndex = lines.indexOfFirst { it.contains("Overall Coverage Summary") }
            val percent = lines[titleIndex + LINES_BETWEEN_TITLE_AND_PERCENT].trim()
            val newCoverage = NumberFormat.getInstance(Locale.FRANCE).parse(percent.dropLast(1)).toDouble()
            repository.saveCodeCoverage(newCoverage)
            val title = title ?: "Code coverage for ${targetProject.name}"
            val oldCoverage = repository.getCodeCoverage()
            val formattedNewCoverage = BigDecimal(newCoverage).setScale(2, RoundingMode.HALF_EVEN).stripTrailingZeros()
            val diff = """
                |## $title
                |$formattedNewCoverage% (${formatChangePercent(oldCoverage, newCoverage, increaseIsNegative = false)})
            """.trimMargin()
            repository.saveDiff(diff)
        }
    }

    override fun getDiff(): String = repository.getDiff()

    public class Builder {

        public var title: String? = null
        public var targetProject: Project? = null
        public var customKoverDirectory: File? = null

        internal fun build(): VKIDCodeCoverageMetric {
            return VKIDCodeCoverageMetric(
                title = title,
                targetProject = checkNotNull(targetProject) { "Project is not specified" },
                koverDirectory = customKoverDirectory,
            )
        }
    }
}
