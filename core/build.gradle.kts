import dev.zt64.compose.color.gradle.apple
import dev.zt64.compose.color.gradle.publishing
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    id("kmp-library")
    alias(libs.plugins.compose.compiler)
    // alias(libs.plugins.compose.jb)
    // alias(libs.plugins.publish)
    alias(libs.plugins.compatibility)
}

kotlin {
    jvm()
    apple()

    @OptIn(ExperimentalWasmDsl::class)
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

publishing("compose-color")