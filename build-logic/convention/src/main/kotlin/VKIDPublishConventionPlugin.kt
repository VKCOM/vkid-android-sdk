import com.android.build.gradle.LibraryExtension
import com.vk.id.stringProperty
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.plugins.signing.SigningExtension
import java.net.URI
import java.util.Base64

class VKIDPublishConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = MAVEN_PUBLISH_PLUGIN_ID)
            apply(plugin = SIGNING_PLUGIN_ID)

            configurePublishSingleVariant()
        }
    }

    private fun Project.configurePublishSingleVariant() {
        val projectName = name

        extensions.configure<LibraryExtension> {
            publishing {
                singleVariant("release") {
                    withSourcesJar()
                }
            }
        }

        pluginManager.withPlugin(MAVEN_PUBLISH_PLUGIN_ID) {
            extensions.configure(PublishingExtension::class.java) {
                repositories {
                    maven {
                        url = URI(stringProperty(RELEASE_REPOSITORY_URL))
                        credentials {
                            username = stringProperty(REPOSITORY_USERNAME)
                            password = stringProperty(REPOSITORY_PASSWORD)
                        }
                    }
                }
                publications {
                    register(projectName, MavenPublication::class.java) {
                        groupId = stringProperty(GROUP)
                        artifactId = stringProperty(POM_ARTIFACT_ID)
                        version = stringProperty(VERSION_NAME)

                        afterEvaluate {
                            from(components["release"])
                            applyPom(this)
                        }
                    }
                }
                if (!stringProperty(VERSION_NAME).endsWith("SNAPSHOT")) {
                    extensions.configure(SigningExtension::class.java) {
                        val keyId = stringProperty(SIGNING_KEY_ID)
                        val password = stringProperty(SIGNING_PASSWORD)
                        val secretKey = String(Base64.getDecoder().decode(stringProperty(SECRET_KEY_RING_BASE64)))
                        if (keyId.isNotEmpty()) {
                            println("Sign key id: $keyId")
                        }
                        useInMemoryPgpKeys(keyId, secretKey, password)
                        sign(publications[projectName])
                    }
                }
            }
        }
    }

    private fun MavenPublication.applyPom(project: Project) {
        fun Property<String>.pset(propertyName: String) = set(project.stringProperty(propertyName))

        pom {
            name.pset(POM_NAME)
            description.pset(POM_DESCRIPTION)
            url.pset(POM_URL)
            packaging = project.stringProperty(POM_PACKAGING)
            scm {
                url.pset(POM_SCM_URL)
                connection.pset(POM_SCM_CONNECTION)
                developerConnection.pset(POM_SCM_DEV_CONNECTION)
            }
            licenses {
                license {
                    name.pset(POM_LICENCE_NAME)
                    url.pset(POM_LICENCE_URL)
                    distribution.pset(POM_LICENCE_DISTRIBUTION)
                }
            }
            developers {
                developer {
                    id.pset(POM_DEVELOPER_ID)
                    name.pset(POM_DEVELOPER_NAME)
                    email.pset(POM_DEVELOPER_EMAIL)
                }
            }
        }
    }

    private companion object {
        const val GROUP = "GROUP"
        const val POM_ARTIFACT_ID = "POM_ARTIFACT_ID"
        const val VERSION_NAME = "VERSION_NAME"

        const val RELEASE_REPOSITORY_URL = "RELEASE_REPOSITORY_URL"
        const val REPOSITORY_USERNAME = "REPOSITORY_USERNAME"
        const val REPOSITORY_PASSWORD = "REPOSITORY_PASSWORD"
        const val SIGNING_KEY_ID = "SIGNING_KEY_ID"
        const val SECRET_KEY_RING_BASE64 = "SECRET_KEY_RING_BASE64"
        const val SIGNING_PASSWORD = "SIGNING_PASSWORD"

        const val POM_URL = "POM_URL"
        const val POM_NAME = "POM_NAME"
        const val POM_DESCRIPTION = "POM_DESCRIPTION"
        const val POM_PACKAGING = "POM_PACKAGING"

        const val MAVEN_PUBLISH_PLUGIN_ID = "maven-publish"
        const val SIGNING_PLUGIN_ID = "signing"

        const val POM_SCM_URL = "POM_SCM_URL"
        const val POM_SCM_CONNECTION = "POM_SCM_CONNECTION"
        const val POM_SCM_DEV_CONNECTION = "POM_SCM_DEV_CONNECTION"

        const val POM_LICENCE_NAME = "POM_LICENCE_NAME"
        const val POM_LICENCE_URL = "POM_LICENCE_URL"
        const val POM_LICENCE_DISTRIBUTION = "POM_LICENCE_DISTRIBUTION"

        const val POM_DEVELOPER_ID = "POM_DEVELOPER_ID"
        const val POM_DEVELOPER_NAME = "POM_DEVELOPER_NAME"
        const val POM_DEVELOPER_EMAIL = "POM_DEVELOPER_EMAIL"
    }
}
