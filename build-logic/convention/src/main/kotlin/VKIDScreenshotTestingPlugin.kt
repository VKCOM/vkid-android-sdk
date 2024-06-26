import com.vk.id.libs
import com.vk.id.util.android
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

class VKIDScreenshotTestingPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        return with(target) {
            with(pluginManager) {
                apply("vkid.placeholders")
            }
            extensions.android {
                @Suppress("UnstableApiUsage")
                experimentalProperties["android.experimental.enableScreenshotTest"] = true
            }
        }
    }
}