// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("org.jetbrains.kotlin.jvm") version "1.9.23" apply false
    id("com.android.library") version "8.2.2" apply false
    kotlin("plugin.serialization") version "1.9.23"
    id("com.google.devtools.ksp") version "1.9.23-1.0.20"
    id("com.google.dagger.hilt.android") version "2.49" apply false
    id("org.jetbrains.kotlinx.kover") version "0.7.6"
}

subprojects {
    apply(plugin = "org.jetbrains.kotlinx.kover")
}
