package com.vk.id

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project

fun Project.configureAndroidLint() {
    (extensions.getByName("android") as CommonExtension<*, *, *, *, *>).apply {
        lint {
            checkDependencies = true
            ignoreTestSources = true
            abortOnError = true
            warningsAsErrors = true
            htmlReport = true
            xmlReport = false
            disable += "GradleDependency"
        }
    }
}
