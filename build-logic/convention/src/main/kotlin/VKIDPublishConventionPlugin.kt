import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class VKIDPublishConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = MAVEN_PUBLISH_PLUGIN_ID)
            apply(plugin = SIGNING_PLUGIN_ID)

            extensions.configure<LibraryExtension> {
                publishing {
                    singleVariant("release") {
                        withSourcesJar()
                    }
                }
            }

            afterEvaluate {
                configurePublishingExtension(component = "release", signAllPublications = false)
            }

            with(target.rootProject.tasks) {
                (findByName(PUBLISH_TASK_NAME) ?: create(PUBLISH_TASK_NAME)).dependsOn(target.tasks.findByName("publish"))
            }
        }
    }

    private companion object {
        const val PUBLISH_TASK_NAME = "publishSdk"
        const val MAVEN_PUBLISH_PLUGIN_ID = "maven-publish"
        const val SIGNING_PLUGIN_ID = "signing"
    }
}
