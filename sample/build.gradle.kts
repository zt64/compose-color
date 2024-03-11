import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose)
}

kotlin {
    jvm()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "sample"
        browser {
            commonWebpackConfig {
                outputFileName = "sample.js"
            }
        }
        binaries.executable()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.lib)
                implementation(projects.util)

                implementation(compose.runtime)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)

                implementation(libs.materialKolor)
                implementation(libs.settings)
            }
        }

        jvmMain {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
    }
}

compose {
    desktop {
        application {
            mainClass = "dev.zt64.compose.color.sample.MainKt"
        }
    }

    experimental.web.application {}
}