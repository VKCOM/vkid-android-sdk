plugins {
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.detekt) apply false
}

tasks.register("detekt") {
    subprojects.mapNotNull { it.tasks.findByName("detekt") }.forEach(::dependsOn)
}
