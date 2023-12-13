import com.android.build.gradle.LibraryExtension
import com.vk.id.Versions
import com.vk.id.configureAndroidLint
import com.vk.id.configureDetekt
import com.vk.id.configureKotest
import com.vk.id.configureKotlinAndroid
import com.vk.id.configureStrictMode
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
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = Versions.targetSdk
                resourcePrefix("vkid_")
            }
            configureStrictMode()
            configureKotest()
            configureDetekt(isCompose = false)
            configureAndroidLint()
            dependencies {
                add("androidTestImplementation", kotlin("test"))
                add("testImplementation", kotlin("test"))
            }
        }
    }
}