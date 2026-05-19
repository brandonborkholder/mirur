plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.21"
    id("org.jetbrains.intellij.platform") version "2.7.2"
}

group = "io.mirur"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2026.1")
        bundledPlugin("com.intellij.java")
        pluginVerifier()
        testFramework(org.jetbrains.intellij.platform.gradle.TestFrameworkType.Platform)
    }
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "261"
        }
    }
}

tasks {
    patchPluginXml {
        sinceBuild.set("261")
        untilBuild.set("261.*")
    }
}



tasks.register("smokeTest") {
    group = "verification"
    description = "Validates IntelliJ plugin descriptor and key registrations."

    doLast {
        val pluginXml = file("src/main/resources/META-INF/plugin.xml")
        require(pluginXml.exists()) { "plugin.xml is missing" }

        val text = pluginXml.readText()
        require("<id>io.mirur.intellij</id>" in text) { "plugin id missing" }
        require("Mirur.SubmitSelection" in text) { "Submit action missing" }
        require("MirurToolWindowFactory" in text) { "Tool window factory registration missing" }
        require("MirurSettingsService" in text) { "Settings service registration missing" }
    }
}

tasks.named("check") {
    dependsOn("smokeTest")
}
