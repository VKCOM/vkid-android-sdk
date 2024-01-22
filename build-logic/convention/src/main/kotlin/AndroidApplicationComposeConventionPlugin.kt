import com.vk.id.configureAndroidCompose
import com.vk.id.configureDetekt
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("vkid.android.application")
            configureAndroidCompose()
            configureDetekt(isCompose = true)
        }
    }
}