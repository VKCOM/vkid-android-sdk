import com.vk.id.libs
import com.vk.id.uninstallTestAppTask
import com.vk.id.util.android
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidTestsConventionPlugin : Plugin<Project> {

    override fun apply(
        target: Project
    ): Unit = with(target) {
        with(pluginManager) {
            apply("vkid.placeholders")
        }
        extensions.android {
            defaultConfig {
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }
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
            add("androidTestImplementation", libs.findLibrary("androidx-test-junit-ktx").get())
        }

        tasks.configureEach {
            if (name == "connectedDebugAndroidTest") {
                val namespace = project.extensions.android.namespace ?: error("Project namespace is unspecified")
                doLast {
                    uninstallTestAppTask(namespace)
                }
            }
        }
    }
}