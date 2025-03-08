import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("kmp-library")
    alias(libs.plugins.compose.compiler)
}

@OptIn(ExperimentalWasmDsl::class)
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.ui)
                implementation(compose.uiUtil)
            }
        }
    }
}