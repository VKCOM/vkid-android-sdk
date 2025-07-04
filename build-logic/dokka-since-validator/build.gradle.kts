import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
    `maven-publish`
}

group = "com.vk.id"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(libs.dokka.core)
    implementation(libs.dokka.base)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

java {
    withSourcesJar()
}

publishing {
    publications {
        creating(MavenPublication::class) {
            artifactId = project.name
            from(components["kotlin"])

            pom {
                name.set("Dokka since validator plugin")
                description.set("This plugins validates that all public api KDoc contains @since annotation")
            }
        }
    }

    repositories {
        mavenLocal()
    }
}