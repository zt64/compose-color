plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.cocoapods) apply false

    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.compose.jb) apply false

    alias(libs.plugins.android.application) apply false

    alias(libs.plugins.publish) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.compatibility) apply false
}

allprojects {
    apply {
        // plugin("org.jlleitschuh.gradle.ktlint")
    }

    // configure<KtlintExtension> {
    //     version.set(rootProject.libs.versions.ktlint)
    // }

    group = "dev.zt64"
    version = "1.0.0"
}

// subprojects {
//     dependencies {
//         val ktlintRuleset by configurations
//
//         ktlintRuleset(rootProject.libs.ktlint.rules.compose)
//     }
// }