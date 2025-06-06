import dev.deftu.gradle.utils.GameSide

plugins {
    java
    kotlin("jvm")
    val dgtVersion = "2.35.0"
    id("dev.deftu.gradle.multiversion")
    id("dev.deftu.gradle.tools")
    id("dev.deftu.gradle.tools.shadow")
    id("dev.deftu.gradle.tools.jvm.kotlin")
    id("dev.deftu.gradle.tools.bloom")
    id("dev.deftu.gradle.tools.resources")
    id("dev.deftu.gradle.tools.minecraft.loom")
    id("dev.deftu.gradle.tools.publishing.github")
    id("dev.deftu.gradle.tools.minecraft.releases")
}
//
// loom {
//     forge {
//         pack200Provider.set(dev.architectury.pack200.java.Pack200Adapter())
//     }
// }

toolkitLoomHelper {
    useOneConfig {
        version = "1.0.0-alpha.106"
        loaderVersion = "1.1.0-alpha.46"

        applyLoaderTweaker = true
    }


    useTweaker("cc.polyfrost.oneconfig.loader.stage0.LaunchWrapperTweaker")
    disableRunConfigs(GameSide.SERVER)
    if (mcData.isForge) {
        // Configures the Mixin tweaker if we are building for Forge.
        useForgeMixin("pss")
    }
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
    implementation(shade("com.github.NetheriteMiner:DiscordIPC:3106be5") {
        isTransitive = false
    })

    implementation(shade("gg.essential:elementa:704") {
        isTransitive = false
    })

    val universalCraftVersion = when(mcData.version.toString()) {
        "1.16.5" -> "1.16.2"
        "1.18.2" -> "1.18.1"
        "1.21.1" -> "1.21"
        "1.21.2" -> "1.21"
        else -> mcData.version.toString()
    }
    implementation(shade("gg.essential:universalcraft-${universalCraftVersion}-${mcData.loader.friendlyString}:401") {
        isTransitive = false
    })

    if (mcData.isFabric) {
        if (mcData.isLegacyFabric) {
            // 1.8.9 - 1.13
            modImplementation("net.legacyfabric.legacy-fabric-api:legacy-fabric-api:${mcData.dependencies.legacyFabric.legacyFabricApiVersion}")
        } else {
            // 1.16.5+
            modImplementation("net.fabricmc.fabric-api:fabric-api:${mcData.dependencies.fabric.fabricApiVersion}")
        }
    }
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
