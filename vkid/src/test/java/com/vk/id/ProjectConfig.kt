package com.vk.id

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.config.ProjectConfiguration

internal object ProjectConfig : AbstractProjectConfig() {

   override val parallelism = 10

   override val concurrentTests: Int = ProjectConfiguration.MaxConcurrency

   override var dispatcherAffinity: Boolean? = false
}