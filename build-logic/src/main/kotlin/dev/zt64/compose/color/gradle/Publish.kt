package dev.zt64.compose.color.gradle

import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.SonatypeHost
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure

fun Project.publishing(libName: String) {
    apply(plugin = "com.vanniktech.maven.publish")

    configure<MavenPublishBaseExtension> {
        publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL, automaticRelease = true)
        coordinates("dev.zt64", libName, project.version.toString())
        signAllPublications()

        pom {
            name = libName
            description = project.description
            inceptionYear = "2024"
            url = "https://github.com/zt64/compose-color"

            developers {
                developer {
                    id = "zt64"
                    name = "zt64"
                    email = "31907977+zt64@users.noreply.github.com"
                }
            }

            licenses {
                license {
                    name = "GNU v3.0"
                    url = "https://www.gnu.org/licenses/gpl-3.0.html"
                }
            }

            scm {
                url = "https://github.com/zt64/compose-color"
                connection = "scm:git:git://github.com/zt64/compose-color.git"
                developerConnection = "scm:git:ssh://git@github.com/zt64/compose-color.git"
            }
        }
    }
}