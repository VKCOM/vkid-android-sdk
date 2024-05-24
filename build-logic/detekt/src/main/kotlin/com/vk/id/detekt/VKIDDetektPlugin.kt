package com.vk.id.detekt

import org.gradle.api.Plugin
import org.gradle.api.Project

class VKIDDetektPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.configureDetekt(isCompose = false)
    }
}
