import groovy.lang.MissingPropertyException

pluginManagement {
    repositories {
        // Snapshots
        maven("https://maven.deftu.dev/snapshots")
        mavenLocal()

        // Repositories
        maven("https://maven.deftu.dev/releases")
        maven("https://maven.fabricmc.net")
        maven("https://maven.architectury.dev/")
        maven("https://maven.minecraftforge.net")
        maven("https://repo.essential.gg/repository/maven-public")
        maven("https://repo.spongepowered.org/maven/")

        // Default repositories
        gradlePluginPortal()
        mavenCentral()
    }
}

val projectName: String = extra["mod.name"]?.toString() ?: throw MissingPropertyException("mod.name has not been set.")
rootProject.name = projectName
rootProject.buildFileName = "build.gradle.kts"
