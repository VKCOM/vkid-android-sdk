import com.android.build.api.dsl.ApplicationExtension
import com.vk.id.configureAndroidCompose
import com.vk.id.configureDetekt
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("vkid.android.application")
            val extension = extensions.getByType<ApplicationExtension>()
            configureAndroidCompose(extension)
            configureDetekt(isCompose = true)
        }
    }
}