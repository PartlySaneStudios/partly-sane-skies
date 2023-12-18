import dev.deftu.gradle.utils.GameSide

plugins {
    java
    kotlin("jvm") version("1.9.21")
    val dgtVersion = "1.22.0"
    id("dev.deftu.gradle.tools.resources") version(dgtVersion)
    id("dev.deftu.gradle.tools") version(dgtVersion)
    id("dev.deftu.gradle.tools.shadow") version(dgtVersion)
    id("dev.deftu.gradle.tools.blossom") version(dgtVersion)
    id("dev.deftu.gradle.tools.minecraft.loom") version(dgtVersion)
    id("dev.deftu.gradle.tools.minecraft.releases") version(dgtVersion)
    id("dev.deftu.gradle.tools.github-publishing") version(dgtVersion)
}

toolkit.useDevAuth()

toolkitLoomHelper {
    useTweaker("cc.polyfrost.oneconfig.loader.stage0.LaunchWrapperTweaker")
    disableRunConfigs(GameSide.SERVER)
    useForgeMixin("pss")
}

repositories {
    maven("https://repo.polyfrost.cc/releases")
    maven("https://repo.sk1er.club/repository/maven-public/")
    maven("https://repo.sk1er.club/repository/maven-releases/")
    maven("https://repo.spongepowered.org/maven/")
}

val shadowImpl: Configuration by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}

dependencies {
    implementation(shade("gg.essential:elementa-${mcData.versionStr}-${mcData.loader.name}:531") {
        isTransitive = false
    })
    implementation(shade("gg.essential:universalcraft-${mcData.versionStr}-${mcData.loader.name}:262") {
        isTransitive = false
    })
    shadowImpl("org.spongepowered:mixin:0.7.11-SNAPSHOT") {
        isTransitive = false
    }
    implementation(kotlin("stdlib"))
    modCompileOnly("cc.polyfrost:oneconfig-${mcData.versionStr}-${mcData.loader.name}:0.2.0-alpha+")
    implementation(shade("cc.polyfrost:oneconfig-wrapper-launchwrapper:1.0.0-beta+")!!)
    minecraft("com.mojang:minecraft:1.8.9")
    forge("net.minecraftforge:forge:1.8.9-11.15.1.2318-1.8.9")
}

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