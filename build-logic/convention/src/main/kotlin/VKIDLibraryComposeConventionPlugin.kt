import com.vk.id.configureAndroidCompose
import com.vk.id.configureDetekt
import com.vk.id.configureKotest
import org.gradle.api.Plugin
import org.gradle.api.Project

class VKIDLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("vkid.android.library")
            configureAndroidCompose()
            configureKotest()
            configureDetekt(isCompose = true)
        }
    }
}