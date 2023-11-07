import com.android.build.gradle.LibraryExtension
import com.vk.id.configureAndroidCompose
import com.vk.id.configureDetekt
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class VKIDLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("vkid.android.library")
            val extension = extensions.getByType<LibraryExtension>()
            configureAndroidCompose(extension)
            configureDetekt(isCompose = true)
        }
    }
}