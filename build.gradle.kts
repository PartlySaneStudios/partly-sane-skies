import dev.deftu.gradle.utils.GameSide

plugins {
    java
    kotlin("jvm") version("2.0.0")
    val dgtVersion = "2.35.0"
    id("dev.deftu.gradle.tools") version(dgtVersion)
    id("dev.deftu.gradle.tools.shadow") version(dgtVersion)
    id("dev.deftu.gradle.tools.jvm.kotlin") version(dgtVersion)
    id("dev.deftu.gradle.tools.bloom") version(dgtVersion)
    id("dev.deftu.gradle.tools.resources") version(dgtVersion)
    id("dev.deftu.gradle.tools.minecraft.loom") version(dgtVersion)
    id("dev.deftu.gradle.tools.publishing.github") version(dgtVersion)
    id("dev.deftu.gradle.tools.minecraft.releases") version(dgtVersion)
}

loom {
    forge {
        pack200Provider.set(dev.architectury.pack200.java.Pack200Adapter())
    }
}

toolkitLoomHelper {
    useTweaker("cc.polyfrost.oneconfig.loader.stage0.LaunchWrapperTweaker")
    disableRunConfigs(GameSide.SERVER)
    useForgeMixin("pss")
    useDevAuth("+")
}

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://repo.polyfrost.cc/releases")
    maven("https://maven.deftu.dev/releases")
    maven("https://maven.fabricmc.net")
    maven("https://maven.architectury.dev/")
    maven("https://maven.minecraftforge.net")
    maven("https://repo.essential.gg/repository/maven-public")
    maven("https://repo.spongepowered.org/maven/")
}

dependencies {
    implementation(shade("gg.essential:elementa:704") {
        isTransitive = false
    })
    implementation(shade("gg.essential:universalcraft-${mcData.version}-${mcData.loader.friendlyString}:401") {
        isTransitive = false
    })
    implementation(shade("org.spongepowered:mixin:0.7.11-SNAPSHOT") {
        isTransitive = false
    })
    implementation(shade("cc.polyfrost:oneconfig-wrapper-launchwrapper:1.0.0-beta17")!!)

    implementation(shade("com.github.NetheriteMiner:DiscordIPC:3106be5") {
        isTransitive = false
    })
    
    implementation(kotlin("stdlib"))

    compileOnly("cc.polyfrost:oneconfig-${mcData.version}-${mcData.loader.friendlyString}:0.2.2-alpha+")
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

tasks {
    fatJar {
        relocate("com.jagrosh.discordipc", "me.partlysanestudios.partlysaneskies.deps.discordipc")
    }
}

bloom {
    val dogfood: String by project
    val releaseChannel: String by project

    replacement("@DOGFOOD@", dogfood)
    replacement("@RELEASE_CHANNEL@", releaseChannel)
}
