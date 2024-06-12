import dev.deftu.gradle.utils.GameSide

plugins {
    java
    kotlin("jvm") version("2.0.0")
    val dgtVersion = "1.28.0"
    id("dev.deftu.gradle.tools") version(dgtVersion)
    id("dev.deftu.gradle.tools.shadow") version(dgtVersion)
    id("dev.deftu.gradle.tools.kotlin") version(dgtVersion)
    id("dev.deftu.gradle.tools.bloom") version(dgtVersion)
    id("dev.deftu.gradle.tools.resources") version(dgtVersion)
    id("dev.deftu.gradle.tools.minecraft.loom") version(dgtVersion)
    id("dev.deftu.gradle.tools.github-publishing") version(dgtVersion)
    id("dev.deftu.gradle.tools.minecraft.releases") version(dgtVersion)
}

loom {
    forge {
        pack200Provider.set(dev.architectury.pack200.java.Pack200Adapter())
    }
}

toolkit.useDevAuth()

toolkitLoomHelper {
    useTweaker("cc.polyfrost.oneconfig.loader.stage0.LaunchWrapperTweaker")
    disableRunConfigs(GameSide.SERVER)
    useForgeMixin("pss")
}

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://repo.polyfrost.cc/releases")
    maven("https://repo.sk1er.club/repository/maven-public/")
    maven("https://repo.sk1er.club/repository/maven-releases/")
    maven("https://repo.spongepowered.org/maven/")
}

dependencies {
    implementation(shade("gg.essential:elementa-${mcData.versionStr}-${mcData.loader.name}:636") {
        isTransitive = false
    })
    implementation(shade("gg.essential:universalcraft-${mcData.versionStr}-${mcData.loader.name}:323") {
        isTransitive = false
    })
    implementation(shade("org.spongepowered:mixin:0.7.11-SNAPSHOT") {
        isTransitive = false
    })
    implementation(shade("cc.polyfrost:oneconfig-wrapper-launchwrapper:1.0.0-beta+")!!)

    implementation(shade("com.github.NetheriteMiner:DiscordIPC:3106be5") {
        isTransitive = false
    })
    
    implementation(kotlin("stdlib"))

    compileOnly("cc.polyfrost:oneconfig-${mcData.versionStr}-${mcData.loader.name}:0.2.2-alpha+")
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

bloom {
    val dogfood: String by project
    val releaseChannel: String by project

    replaceToken("@DOGFOOD@", dogfood)
    replaceToken("@RELEASE_CHANNEL@", releaseChannel)
}