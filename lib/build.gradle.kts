import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.publish)
    alias(libs.plugins.compatibility)
}

kotlin {
    explicitApi()

    jvm()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.ui)
                implementation(compose.foundation)
            }
        }
        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
    }
}

@Suppress("UnstableApiUsage")
mavenPublishing {
    publishToMavenCentral(SonatypeHost.DEFAULT, automaticRelease = true)
    coordinates("dev.zt64.compose.color", "compose-color", project.version.toString())
    signAllPublications()

    pom {
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
                name = "GNU General Public License v3.0"
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