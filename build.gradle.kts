import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.publish) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.compatibility) apply false
}

allprojects {
    apply {
        plugin("org.jlleitschuh.gradle.ktlint")
    }

    configure<KtlintExtension> {
        version = "1.2.1"
    }

    group = "dev.zt64"
    version = "1.0.0"
}

subprojects {
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "17"
        }
    }

    dependencies {
        val ktlintRuleset by configurations

        ktlintRuleset(rootProject.libs.ktlint.rules.compose)
    }
}