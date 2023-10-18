import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

/**
 * Usage: `./gradlew allDependencies --write-locks`
 */
class DependencyLockPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            subprojects {
                configurations.all {
                    resolutionStrategy.activateDependencyLocking()
                }
            }

            val allSubTasks = subprojects.map { ":${it.name}:dependencies" }.map(project.tasks::getByPath)
            tasks.register("allDependencies") {
                dependsOn(allSubTasks)
            }
        }
    }
}
