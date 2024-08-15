import org.gradle.api.Plugin
import org.gradle.api.Project

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

            val allSubTasks = subprojects.map { ":${it.name}:dependencies" }
            tasks.register("allDependencies") {
                dependsOn(allSubTasks)
            }
        }
    }
}
