import com.vk.id.configureAndroidCompose
import com.vk.id.configureKotest
import com.vk.id.configureStrictMode
import org.gradle.api.Plugin
import org.gradle.api.Project

class VKIDLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("vkid.android.library")
                apply("vkid.detekt.compose")
            }
            configureAndroidCompose()
            configureStrictMode()
            configureKotest()
        }
    }
}