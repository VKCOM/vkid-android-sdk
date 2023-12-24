package com.vk.id.util

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.plugins.ExtensionContainer

fun ExtensionContainer.android(
    configuration: CommonExtension<*, *, *, *, *>.() -> Unit
) = (getByName("android") as CommonExtension<*, *, *, *, *>).apply(configuration)