import xyz.deftu.gradle.utils.GameSide


plugins {
    val kotlin_version = "1.6.21"
    java
    kotlin("jvm") version(kotlin_version)
    val dgtVersion = "1.18.4"
    id("xyz.deftu.gradle.tools") version(dgtVersion)
    id("xyz.deftu.gradle.tools.shadow") version(dgtVersion)
    id("xyz.deftu.gradle.tools.blossom") version(dgtVersion)
    id("xyz.deftu.gradle.tools.minecraft.loom") version(dgtVersion)
    id("xyz.deftu.gradle.tools.minecraft.releases") version(dgtVersion)
    id("xyz.deftu.gradle.tools.github-publishing") version(dgtVersion)
}

toolkitLoomHelper {
    useTweaker("cc.polyfrost.oneconfig.loader.stage0.LaunchWrapperTweaker")
    useTweaker("me.partlysanestudios.partlysaneskies.modschecker.LaunchWrapperTweaker")
    disableRunConfigs(GameSide.SERVER)
}

repositories {
    maven("https://repo.polyfrost.cc/releases")
    maven("https://repo.sk1er.club/repository/maven-public/")
    maven("https://repo.sk1er.club/repository/maven-releases/")
}

dependencies {
    val kotlin_version = "1.6.21"
    implementation(shade("gg.essential:elementa-${mcData.versionStr}-${mcData.loader.name}:531") {
        isTransitive = false
    })
    implementation(shade("gg.essential:universalcraft-${mcData.versionStr}-${mcData.loader.name}:262") {
        isTransitive = false
    })
    implementation(kotlin("stdlib"))
    implementation(kotlin("gradle-plugin"))
    modCompileOnly("cc.polyfrost:oneconfig-${mcData.versionStr}-${mcData.loader.name}:0.2.0-alpha+")
    implementation(shade("cc.polyfrost:oneconfig-wrapper-launchwrapper:1.0.0-beta+")!!)
    minecraft("com.mojang:minecraft:1.8.9")
    forge("net.minecraftforge:forge:1.8.9-11.15.1.2318-1.8.9")
    compileOnly("org.spongepowered:mixin:0.7.11-SNAPSHOT")
    implementation(shade("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version") {})

    modCompileOnly("org.apache.logging.log4j:log4j-api:2.19.0")
    modCompileOnly("org.apache.logging.log4j:log4j-core:2.19.0")
    modCompileOnly("com.google.code.gson:gson:2.2.4")


}

toolkit.useDevAuth()

toolkitReleases {
    if (file("changelogs/${modData.version}.md").exists()) {
        changelogFile.set(file("changelogs/${modData.version}.md"))
    }
    modrinth {
        projectId.set("jlWHBQtc")
    }
}

toolkitGitHubPublishing {
    owner.set("PartlySaneStudios")
    repository.set("partly-sane-skies")
}

blossom {
    val dogfood: String by project
    replaceToken("@DOGFOOD@", dogfood)
}

tasks {
    jar {
        // Sets the jar manifest attributes.
        manifest.attributes += mapOf(
            "ModSide" to "CLIENT",
            "TweakOrder" to "0",
            "MixinConfigs" to "mixins.${modData.id}.json",
            "TweakClass" to "me.partlysanestudios.partlysaneskies.modschecker.LaunchWrapperTweaker"
        )
        archiveClassifier.set("")
        enabled = false
    }
}