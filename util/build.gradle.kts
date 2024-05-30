import dev.zt64.compose.color.gradle.apple
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    id("kmp-library")
    alias(libs.plugins.compose.compiler)
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
                implementation(compose.ui)
                implementation(compose.uiUtil)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
    }
}