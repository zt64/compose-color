plugins {
    id("kmp-library")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.ui)
                implementation(compose.foundation)
            }
        }
    }
}