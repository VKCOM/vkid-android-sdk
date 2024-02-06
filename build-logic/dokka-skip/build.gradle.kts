import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
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
                name.set("Dokka skip plugin")
                description.set("This plugins skips generatation of documentation for @InternalVKIDApi")
            }
        }
    }

    repositories {
        mavenLocal()
    }
}