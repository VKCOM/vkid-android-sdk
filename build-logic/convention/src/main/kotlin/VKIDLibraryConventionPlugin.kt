import com.android.build.gradle.LibraryExtension
import com.vk.id.Versions
import com.vk.id.configureDetekt
import com.vk.id.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class VKIDLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = Versions.targetSdk

                tasks.withType<KotlinCompile>().configureEach {
                    kotlinOptions {
                        // Force implicit visibility modifiers to avoid mistakes like exposing internal api
                        freeCompilerArgs = freeCompilerArgs + "-Xexplicit-api=strict"
                    }
                }

                resourcePrefix("vkid_")
            }
            configureDetekt(isCompose = false)
            dependencies {
                add("androidTestImplementation", kotlin("test"))
                add("testImplementation", kotlin("test"))
            }
        }
    }
}