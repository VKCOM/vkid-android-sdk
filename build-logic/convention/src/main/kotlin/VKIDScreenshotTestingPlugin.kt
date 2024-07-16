import com.vk.id.libs
import com.vk.id.util.android
import org.gradle.api.Plugin
import org.gradle.api.Project

class VKIDScreenshotTestingPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        return with(target) {
            with(pluginManager) {
                apply("vkid.placeholders")
                apply(libs.findPlugin("screenshot").get().get().pluginId)
            }
            extensions.android {
                @Suppress("UnstableApiUsage")
                experimentalProperties["android.experimental.enableScreenshotTest"] = true
            }
        }
    }
}