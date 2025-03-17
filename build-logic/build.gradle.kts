plugins {
    `kotlin-dsl`
}

fun DependencyHandler.implementation(dependency: Provider<PluginDependency>) {
    implementation(dependency.map { "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}" })
}

fun DependencyHandler.compileOnly(dependency: Provider<PluginDependency>) {
    compileOnly(dependency.map { "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}" })
}

dependencies {
    compileOnly(libs.plugins.kotlin.multiplatform)
    compileOnly(libs.plugins.compose.jb)
    compileOnly(libs.plugins.ktlint)
    compileOnly(libs.plugins.publish)
}

gradlePlugin {
    plugins.register("kmp-base") {
        id = "kmp-base"
        implementationClass = "dev.zt64.compose.pipette.gradle.KmpBasePlugin"
    }

    plugins.register("kmp-library") {
        id = "kmp-library"
        implementationClass = "dev.zt64.compose.pipette.gradle.KmpLibraryPlugin"
    }
}