import com.vk.id.stringProperty
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.configurationcache.extensions.capitalized
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class VKIDPluginPublishConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = MAVEN_PUBLISH_PLUGIN_ID)
            apply(plugin = SIGNING_PLUGIN_ID)

            version = stringProperty(VERSION_NAME)
            group = stringProperty(GROUP)

            configure<JavaPluginExtension> {
                withSourcesJar()
                withJavadocJar()
            }

            afterEvaluate {
                configurePublishingExtension(component = "kotlin", signAllPublications = true)
                tasks.getByName("publishPluginMavenPublicationToMavenRepository").dependsOn("sign${project.name.capitalized()}Publication")
                tasks.getByName("publish${project.name.capitalized()}PublicationToMavenRepository").dependsOn("signPluginMavenPublication")
            }
        }
    }
}
