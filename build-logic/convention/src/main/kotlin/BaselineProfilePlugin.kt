import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

/**
 * Usage: `./gradlew generateBaselineProfiles`
 */
class BaselineProfilePlugin : Plugin<Project> {

    companion object {
        private const val TASK_NAME = "generateBaselineProfiles"
    }

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("androidx.baselineprofile")

            dependencies {
                "baselineProfile"(project(":baseline-profile"))
            }
            val task = rootProject.tasks.findByName(TASK_NAME) ?: rootProject.tasks.create(TASK_NAME)
            task.dependsOn(":${target.name}:generateBaselineProfile")
        }
    }
}
