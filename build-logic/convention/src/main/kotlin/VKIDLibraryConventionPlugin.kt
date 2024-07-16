import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.gradle.LibraryExtension
import com.vk.id.Versions
import com.vk.id.configureAndroidLint
import com.vk.id.configureKotest
import com.vk.id.configureKotlinAndroid
import com.vk.id.configureStrictMode
import com.vk.id.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class VKIDLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("vkid.detekt")
                apply(libs.findPlugin("kover").get().get().pluginId)
            }

            extensions.configure<LibraryExtension> {
                defaultConfig.targetSdk = Versions.targetSdk
                resourcePrefix("vkid_")
            }
            extensions.configure<LibraryAndroidComponentsExtension> {
                disableUnnecessaryAndroidTests(target)
            }
            configureKotlinAndroid()
            configureStrictMode()
            configureKotest()
            configureAndroidLint()
            dependencies {
                add("androidTestImplementation", kotlin("test"))
                add("testImplementation", kotlin("test"))
            }
        }
    }
}