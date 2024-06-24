import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.util.Properties

class VKIDAppConventionPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        val android = extensions.getByName("android") as CommonExtension<*, *, *, *, *, *>
        android.apply {
            defaultConfig {
                if (shouldInjectManifestPlaceholders()) {
                    val secrets = Properties()
                    try {
                        secrets.load(FileInputStream(rootProject.file("sample/app/secrets.properties")))
                        val clientId = secrets["VKIDClientID"]
                            ?: throw IllegalStateException("Add VKIDClientID to file sample/app/secrets.properties")
                        val clientSecret = secrets["VKIDClientSecret"]
                            ?: throw IllegalStateException("Add VKIDClientSecret to file sample/app/secrets.properties")
                        addManifestPlaceholders(
                            mapOf(
                                "VKIDRedirectHost" to "vk.com",
                                "VKIDRedirectScheme" to "vk$clientId",
                                "VKIDClientID" to clientId,
                                "VKIDClientSecret" to clientSecret
                            )
                        )
                    } catch (e: FileNotFoundException) {
                        logger.error(
                            "Warning! Build will not work!\nCreate the 'secrets.properties' file in the 'sample/app' folder and add your 'VKIDClientID' and 'VKIDClientSecret' to it." +
                                "\nFor more information, refer to the 'README.md' file."
                        )
                        throw e
                    }
                }
            }
        }
    }
}

/**
 * The project should sync without placeholders
 */
private fun Project.shouldInjectManifestPlaceholders() = gradle
    .startParameter
    .taskNames
    .map { it.lowercase() }
    .any {
        it.contains("assemble")
            || it.endsWith("test")
            || it.contains("lint")
            || it.contains("dokka")
            || it.contains("generatebaselineprofile")
            || it.contains("updatedebugscreenshottest")
            || it.contains("healthmetrics")
    }
