import com.vk.id.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.dokka.gradle.DokkaMultiModuleTask
import org.jetbrains.dokka.gradle.DokkaTaskPartial

class VKIDDokkaPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.rootProject.configureRootDokka()
        target.configureDokka()
    }

    private fun Project.configureRootDokka() {
        addDokkaPlugin()
        rootProject.tasks.withType<DokkaMultiModuleTask>().configureEach {
            outputDirectory.set(rootProject.file("docs"))
        }
    }

    private fun Project.configureDokka() {
        addDokkaPlugin()
        val dokkaPlugin = configurations.getByName("dokkaPlugin")
        dependencies {
            dokkaPlugin("com.vk.id:dokka-since-validator:1.0-SNAPSHOT")
            dokkaPlugin("com.vk.id:dokka-skip:1.0-SNAPSHOT")
        }
        tasks.withType<DokkaTaskPartial>().configureEach {
            failOnWarning.set(true)
            suppressInheritedMembers.set(true)
            dokkaSourceSets.configureEach {
                reportUndocumented.set(true)
                jdkVersion.set(11)
            }
        }
    }

    private fun Project.addDokkaPlugin() {
        apply(plugin = libs.findPlugin("dokka").get().get().pluginId)
    }
}