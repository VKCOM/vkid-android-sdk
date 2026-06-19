plugins {
    kotlin("jvm") apply false
    id("org.jetbrains.dokka")
    id("vkid.tools.android.detekt")
}

dependencies {
    dokka(project(":analytics"))
    dokka(project(":common"))
    dokka(project(":group-subscription-common"))
    dokka(project(":group-subscription-compose"))
    dokka(project(":group-subscription-xml"))
    dokka(project(":logger"))
    dokka(project(":multibranding-common"))
    dokka(project(":multibranding-compose"))
    dokka(project(":multibranding-xml"))
    dokka(project(":network"))
    dokka(project(":onetap-common"))
    dokka(project(":onetap-compose"))
    dokka(project(":onetap-xml"))
    dokka(project(":vk-sdk-support"))
    dokka(project(":vkid"))
}

dokka {
    dokkaPublications.html {
        outputDirectory.set(rootProject.file("docs"))
    }
}

registerAssembleTask("assembleDebug")
registerAssembleTask("assembleRelease")

// Регистрируется для совместимости с CI задачами
private fun registerAssembleTask(name: String, configuration: Task.() -> Unit = {}) {
    val task = tasks.findByName(name) ?: tasks.create(name)
    task.dependsOn("assemble")
    task.configuration()
}