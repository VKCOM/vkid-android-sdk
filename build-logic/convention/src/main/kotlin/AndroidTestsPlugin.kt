import com.android.build.api.dsl.CommonExtension
import com.vk.id.configureManifestPlaceholders
import com.vk.id.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidTestsPlugin : Plugin<Project> {

    override fun apply(
        target: Project
    ) = with(target) {
        (extensions.getByName("android") as CommonExtension<*, *, *, *, *>).apply {
            defaultConfig {
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }
            configureManifestPlaceholders(this)
        }

        dependencies {
            // https://github.com/KasperskyLab/Kaspresso/issues/578
            add("debugImplementation", libs.findLibrary("android-material").get())
            add("debugImplementation", libs.findLibrary("androidx-compose-ui-test-manifest").get())
            add("androidTestUtil", libs.findLibrary("androidx-test-orchestrator").get())
            add("androidTestImplementation", libs.findLibrary("kaspresso").get())
            add("androidTestImplementation", libs.findLibrary("kaspresso-compose").get())
            add("androidTestImplementation", libs.findLibrary("androidx-compose-ui-test-junit4").get())
            add("androidTestImplementation", libs.findLibrary("kotest-assertions").get())
        }
    }
}