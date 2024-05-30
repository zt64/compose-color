package dev.zt64.compose.color.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpBasePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        configureKmp(target)
    }

    private fun configureKmp(target: Project) {
        target.apply(plugin = "org.jetbrains.kotlin.multiplatform")
        target.configure<KotlinMultiplatformExtension> {
            jvmToolchain(17)
        }
    }
}