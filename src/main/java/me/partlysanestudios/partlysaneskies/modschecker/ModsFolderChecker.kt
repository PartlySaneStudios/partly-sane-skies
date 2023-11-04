package me.partlysanestudios.partlysaneskies.modschecker

import com.google.gson.JsonParser
import java.io.File
import java.net.URLClassLoader

class ModsFolderChecker {

    fun loadModFile(jarFile: File): ModFile {
        val modInfoContent = getMcModInfoFile(jarFile)

        if (modInfoContent.isBlank()) {
            return ModFile("", jarFile.name, "", "", "")
        }




        if (!isJsonString(modInfoContent)) {
            return ModFile("", jarFile.name, "", "", "")
        }

        val json = JsonParser().parse(modInfoContent).asJsonArray[0].asJsonObject

        val modId = json.get("modid").asString
        val name = json.get("name").asString
        val version = json.get("version").asString
        val hash = ModsListChecker.generateHash(jarFile)
        val fileName = jarFile.name

        return ModFile(modId, fileName, version, name, hash)

    }

    fun isJsonString(str: String): Boolean {
        return try {
            JsonParser().parse(str)
            true
        } catch (e: Exception) {
            false
        }
    }


    fun getModFileNames(): List<File> {
        val modsFolder = File("/mods/")
        return modsFolder.walk().filter { it.isFile }
            .filter { it.endsWith(".jar") }
            .toList()
    }

    fun getMcModInfoFile(jarFile: File): String {
        val url = jarFile.toURI().toURL()
        val classLoader = URLClassLoader(arrayOf(url))
        val fileContent = classLoader.getResourceAsStream("/mcmod.info")?.use { it.reader(Charsets.UTF_8).readText() }
        return fileContent ?: ""
    }



    class ModFile(val modId: String, val fileName: String, val version: String, val modName: String, val hash: String) {

    }
}