plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.21"
    id("org.jetbrains.intellij.platform") version "2.16.0"
}

group = "io.mirur"
version = "0.1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

kotlin {
    jvmToolchain(21)
}

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
        marketplace()
    }
}

val localIdePath = providers.environmentVariable("MIRUR_INTELLIJ_IDE_PATH").orNull

dependencies {
    intellijPlatform {
        if (!localIdePath.isNullOrBlank()) {
            local(localIdePath)
        } else {
            intellijIdeaCommunity("2024.2.4")
        }
        bundledPlugin("com.intellij.java")
        pluginVerifier()
        // no IntelliJ test framework dependency yet; smokeTest covers plugin descriptor wiring
    }
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "242"
            untilBuild = "242.*"
        }
    }
}

tasks.register("smokeTest") {
    group = "verification"
    description = "Validates IntelliJ plugin descriptor and key registrations."

    doLast {
        val pluginXml = file("src/main/resources/META-INF/plugin.xml")
        require(pluginXml.exists()) { "plugin.xml is missing" }

        val text = pluginXml.readText()
        require("<id>io.mirur</id>" in text) { "plugin id missing" }
        require("Mirur.SubmitSelection" in text) { "Submit action missing" }
        require("MirurToolWindowFactory" in text) { "Tool window factory registration missing" }
        require("MirurSettingsService" in text) { "Settings service registration missing" }
    }
}

tasks.test {
    enabled = false
}

tasks.named("buildPlugin") {
    dependsOn("smokeTest")
}
