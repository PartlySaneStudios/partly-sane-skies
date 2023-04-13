import xyz.deftu.gradle.utils.GameSide

plugins {
    java
    val dgtVersion = "1.10.4"
    id("xyz.deftu.gradle.tools") version(dgtVersion)
    id("xyz.deftu.gradle.tools.shadow") version(dgtVersion)
    id("xyz.deftu.gradle.tools.blossom") version(dgtVersion)
    id("xyz.deftu.gradle.tools.minecraft.loom") version(dgtVersion)
    id("xyz.deftu.gradle.tools.minecraft.releases") version(dgtVersion)
}

deftu.useDevAuth()

loomHelper {
    useTweaker("cc.polyfrost.oneconfig.loader.stage0.LaunchWrapperTweaker")
    disableRunConfigs(GameSide.SERVER)
}

repositories {
    maven("https://repo.polyfrost.cc/releases")
    maven("https://repo.sk1er.club/repository/maven-public/")
    maven("https://repo.sk1er.club/repository/maven-releases/")
}


dependencies {
    implementation(shade("gg.essential:elementa-${mcData.versionStr}-${mcData.loader.name}:531") {
        isTransitive = false
    })
    implementation(shade("gg.essential:universalcraft-${mcData.versionStr}-${mcData.loader.name}:262") {
        isTransitive = false
    })
    // shade("gg.essential:loader-launchwrapper:1.1.3")

    // implementation("gg.essential:elementa-1${mcData.versionStr}-${mcData.loader.name}:531")
    modCompileOnly("cc.polyfrost:oneconfig-${mcData.versionStr}-${mcData.loader.name}:0.2.0-alpha+")
    implementation(shade("cc.polyfrost:oneconfig-wrapper-launchwrapper:1.0.0-beta+")!!)
}

releases {
    if (file("changelogs/${modData.version}.md").exists()) {
        changelogFile.set(file("changelogs/${modData.version}.md"))
    }
    github {
        owner.set("PartlySaneStudios")
        repository.set("partly-sane-skies")
    }
    modrinth {
        projectId.set("jlWHBQtc")
    }
}