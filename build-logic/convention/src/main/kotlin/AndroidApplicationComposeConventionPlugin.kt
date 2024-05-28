import com.vk.id.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("vkid.android.application")
                apply("vkid.detekt.compose")
            }
            configureAndroidCompose()
        }
    }
}