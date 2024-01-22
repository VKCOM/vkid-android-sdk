import com.vk.id.booleanProperty
import com.vk.id.stringProperty
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.DependencySubstitution

/**
 * A plugin that replaces sample project dependencies with module dependencies.
 * This allows to use published modules or modules from mavenLocal
 *
 * Usage:
 *   Add `SUBSTITUTE_SAMPLE_PROJECTS_WITH_MODULES=true` to root gradle.properties
 */
class ProjectSubstitutionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        val group = stringProperty(GROUP)
        val version = stringProperty(VERSION_NAME)
        val shouldSubstituteProjects = booleanProperty("SUBSTITUTE_SAMPLE_PROJECTS_WITH_MODULES", false)
        if (!shouldSubstituteProjects) return
        subprojects.filter(::isSampleProject).forEach {
            it.configurations.all {
                resolutionStrategy.dependencySubstitution {
                    all {
                        if (isSampleProject()) {
                            val module = requested.displayName.removePrefix("project :")
                            useTarget(module("$group:$module:$version"))
                        }
                    }
                }
            }
        }
    }

    private fun DependencySubstitution.isSampleProject() = requested.displayName.startsWith("project")
            && !requested.displayName.startsWith("project :sample")

    private fun isSampleProject(project: Project) = project.projectDir.absolutePath.contains("/sample/")

    private companion object {
        const val VERSION_NAME = "VERSION_NAME"
        const val GROUP = "GROUP"
    }
}