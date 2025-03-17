import dev.zt64.compose.pipette.gradle.apple
import dev.zt64.compose.pipette.gradle.publishing
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("kmp-library")
    alias(libs.plugins.compose.compiler)
}

@OptIn(ExperimentalWasmDsl::class)
kotlin {
    jvm()
    apple()

    wasmJs {
        browser()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.util)

                implementation(compose.runtime)
                implementation(compose.ui)
                implementation(compose.foundation)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
                @OptIn(ExperimentalComposeLibrary::class)
                implementation(compose.uiTest)
            }
        }

        jvmTest {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
    }
}

publishing("compose-pipette")