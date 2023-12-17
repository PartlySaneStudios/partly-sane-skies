import org.gradle.internal.impldep.org.bouncycastle.asn1.x500.style.RFC4519Style.owner
import xyz.deftu.gradle.utils.GameSide

plugins {
    java
    kotlin("jvm") version("1.6.21")
    val dgtVersion = "1.18.4"
    id("xyz.deftu.gradle.tools") version(dgtVersion)
    id("xyz.deftu.gradle.tools.shadow") version(dgtVersion)
    id("xyz.deftu.gradle.tools.blossom") version(dgtVersion)
    id("xyz.deftu.gradle.tools.minecraft.loom") version(dgtVersion)
    id("xyz.deftu.gradle.tools.minecraft.releases") version(dgtVersion)
    id("xyz.deftu.gradle.tools.github-publishing") version(dgtVersion)
}

toolkit.useDevAuth()

toolkitLoomHelper {
    useTweaker("cc.polyfrost.oneconfig.loader.stage0.LaunchWrapperTweaker")
    disableRunConfigs(GameSide.SERVER)
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
    implementation(kotlin("gradle-plugin"))
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

val runtimeMod by configurations.creating {
    isTransitive = false
    isVisible = false
}

loom {
    launchConfigs {
        "client" {
            property("mixin.debug", "true")
            property("asmhelper.verbose", "true")
            arg("--tweakClass", "cc.polyfrost.oneconfig.loader.stage0.LaunchWrapperTweaker")
            arg("--mixin", "mixins.pss.json")
            val modFiles = runtimeMod.files
            arg("--mods", modFiles.joinToString(",") { it.relativeTo(file("run")).path })
        }
    }
    runConfigs {
        delete("server")
    }
    forge {
        pack200Provider.set(dev.architectury.pack200.java.Pack200Adapter())
        mixinConfig("mixins.pss.json")
    }
    @Suppress("UnstableApiUsage")
    mixin {
        defaultRefmapName.set("mixins.neuhax.refmap.json")
    }
}