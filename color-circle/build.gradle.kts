plugins {
    id("kmp-library")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.util)
                implementation(compose.ui)
                implementation(compose.foundation)
            }
        }
    }
}