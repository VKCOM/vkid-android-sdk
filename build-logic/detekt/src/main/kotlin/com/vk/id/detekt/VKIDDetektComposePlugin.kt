package com.vk.id.detekt

import org.gradle.api.Plugin
import org.gradle.api.Project

class VKIDDetektComposePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.configureDetekt(isCompose = true)
    }
}
