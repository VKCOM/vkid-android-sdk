import kotlinx.validation.ApiValidationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class VKIDBinaryCompatibilityValidatorPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        allprojects {
            pluginManager.apply("org.jetbrains.kotlinx.binary-compatibility-validator")
            extensions.configure<ApiValidationExtension> {
                nonPublicMarkers.add("com.vk.id.common.InternalVKIDApi")
            }
        }
    }
}
