package me.partlysanestudios.partlysaneskies.modschecker

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.partlysanestudios.partlysaneskies.modschecker.ModsListChecker.KnownMod
import java.io.File
import java.net.URLClassLoader

class ModsFolderChecker {

    fun loadModFile(jarFile: File): ModFile {
        val modInfoContent = getMcModInfoFile(jarFile)

        if (modInfoContent.equals("")) {
            return ModFile("", "", "", "")
        }




        if (!isJsonString(modInfoContent)) {
            return ModFile("", "", "", "")
        }

        val json = JsonParser().parse(modInfoContent).asJsonArray[0].asJsonObject

        val modId = json.get("modid")
        val name = json.get("name")
        val version = json.get("version")

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



    class ModFile(val modId: String, val modPath: String, val version: String, val name: String) {

    }
}