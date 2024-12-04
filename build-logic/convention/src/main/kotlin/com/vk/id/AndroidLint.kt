package com.vk.id

import com.vk.id.util.android
import org.gradle.api.Project

fun Project.configureAndroidLint() {
    extensions.android {
        lint {
            checkDependencies = true
            ignoreTestSources = true
            abortOnError = true
            warningsAsErrors = true
            htmlReport = true
            xmlReport = false
            disable += setOf(
                "GradleDependency",
                "AndroidGradlePluginVersion",
                "PrivateResource",
                "ObsoleteLintCustomCheck",
                "IconLocation",
                "UnusedResources",
            )
        }
    }
}
