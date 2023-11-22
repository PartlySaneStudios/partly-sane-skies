package me.partlysanestudios.partlysaneskies.modschecker

import com.google.gson.JsonParser
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.awt.Color
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.security.MessageDigest
import java.util.jar.JarFile
import javax.swing.JFrame
import javax.swing.JLabel
import kotlin.io.path.Path
import kotlin.io.path.pathString


object ModsFolderChecker {
    var knownMods: List<KnownMod> = ArrayList()
    private val logger: Logger = LogManager.getLogger("Partly Sane Skies")


    fun openWindow() {
        val ui = Frame()


    }
    fun generateString(): String {
//        Loads the data
        loadModDataFromRepo()
//        Makes a list of all the mods in the golder
        val modsInFolder: List<File> = getModsInFolder()

//        Creates two new lists where we will add the supicious mods
        val unknownFiles = ArrayList<ModFile>()
        val unknownMods = ArrayList<ModFile>()
        val outdatedMods = ArrayList<ModFile>()

        for (jarFile: File in modsInFolder) {

//            println("Jar file $jarFile")

            val modFile = loadModFile(jarFile)

//            println("Mod file $modFile")



            if (!modFile.isSafe()) {
                if (modFile.isEmpty()) {
                    unknownFiles.add(modFile)
                } else {
                    unknownMods.add(modFile)

                }
            }
            else if (!modFile.findMod().latest && modFile.isSafe()) {
                outdatedMods.add(modFile)
            }

        }


        var str = "Mods: "

        if (unknownFiles.size > 0) {
            str += "Unknown Files:\n"
            str += "These files are not recognized as mods by Partly Sane Skies.\nProceed at your own risk.\n\n"

            for (mod in unknownFiles) {
                str += "- \"${mod.modName}\" (${mod.path})\n"
            }

            str += "\n\n"
        }

        if (unknownMods.size > 0) {
            str += "Unknown Mods:\n"
            str += "These mods have not been verified by Partly Sane Skies.\nProceed at your own risk.\n\n"

            for (mod in unknownMods) {
                str += "- \"${mod.modName}\" (${mod.path})\n"
            }

            str += "\n\n"
        }

        if (outdatedMods.size > 0) {
            str += "Outdated mods Mods:\n\n"

            for (mod in unknownMods) {
                str += "- \"${mod.modName}\" (${mod.path}) | Download Link: ${mod.findMod().download}\n"
            }

            str += "\n\n"
        }


        return str
    }

    private fun loadModFile(jarFile: File): ModFile {
        val modInfoContent = getMcModInfoFile(jarFile)

        if (modInfoContent.isBlank()) {
            return ModFile("", jarFile.name, "", "")
        }

        if (!isJsonString(modInfoContent)) {
            return ModFile("", jarFile.name, "", "")
        }

        val json = JsonParser().parse(modInfoContent).asJsonArray[0].asJsonObject

        val modId = json.get("modid").asString
        val name = json.get("name").asString
        val version = json.get("version").asString
        val fileName = jarFile.name

        return ModFile(modId, fileName, version, name)

    }

    private fun isJsonString(str: String): Boolean {
        return try {
            JsonParser().parse(str)
            true
        } catch (e: Exception) {
            false
        }
    }


    private fun getModsInFolder(): List<File> {
        val modsFolder = File("./mods/")
        return modsFolder.listFiles()?.toList() ?: emptyList()
    }

    fun getMcModInfoFile(jarFile: File): String {
        val jar = JarFile(jarFile)

        // Specify the path inside the JAR (adjust as needed)
        val entry = jar.getJarEntry("mcmod.info")

        val fileContent = entry?.let {
            jar.getInputStream(it).use { input ->
                input.reader(Charsets.UTF_8).readText()
            }
        } ?: ""

        // Close the JarFile
        jar.close()

        return fileContent
    }





    //    Written by hannibal02
    private fun loadModDataFromRepo() {
        val userName = "PartlySaneStudios"
        val branchName = "main"
        val url = URL("https://raw.githubusercontent.com/" + userName +
                "/partly-sane-skies-public-data" + "/" + branchName + "/data/constants/mods.json")

        // Opens a new connection with the url
        val httpURLConnection = url.openConnection()
        httpURLConnection.setRequestProperty("User-Agent", "Partly-Sane-Skies/" + PartlySaneSkies.VERSION)


        with(url.openConnection() as HttpURLConnection) {
            requestMethod = "GET"  // optional default is GET

            if (responseCode != HttpURLConnection.HTTP_OK) {
                println("The connection failed")
            }

            var responseContent = ""
            inputStream.bufferedReader().use {
                it.lines().forEach { line ->
                    responseContent += line
                }
            }
            knownMods = ArrayList()

            val modsDataJsonObject = JsonParser().parse(responseContent).asJsonObject
            for (entry in modsDataJsonObject.getAsJsonObject("mods").entrySet()) {
                val info = ModDataJson.ModInfo()
                val value = entry.value.asJsonObject
                info.download = value.get("download").asString

                for (version in value.getAsJsonObject("versions").entrySet()) {
                    info.versions[version.key] = version.value.asString
                }


                ModDataJson.mods[entry.key] = info
            }
            knownMods = read(ModDataJson)
        }


    }


    //    Written by hannibal02
    private fun read(modData: ModDataJson): MutableList<KnownMod> {
        val list: MutableList<KnownMod> = java.util.ArrayList()
        for ((modName, modInfo) in modData.mods) {
            val download = modInfo.download
            var latest: KnownMod? = null
            for ((version, hash) in modInfo.versions) {
                latest = KnownMod(modName, version, hash, download)
                list.add(latest)
            }
            if (latest != null) {
                latest.latest = true
            }
        }
        return list
    }


    fun findNewestMod(name: String): KnownMod {
        for (mod in knownMods) {
            if (mod.name == name) {
                if (mod.latest) {
                    return mod
                }
            }
        }

        return KnownMod("", "", "", "")
    }


    //    Written by hannibal02
    fun generateHash(file: File): String {
        try {
            FileInputStream(file).use { stream ->
                val digest = MessageDigest.getInstance("SHA-256")
                val buffer = ByteArray(8192)
                var bytesRead: Int
                while (stream.read(buffer).also { bytesRead = it } != -1) {
                    digest.update(buffer, 0, bytesRead)
                }
                val md5Hash = digest.digest()
                val md5HashStr = StringBuilder()
                for (b in md5Hash) {
                    md5HashStr.append(String.format("%02x", b))
                }
                return md5HashStr.toString()
            }
        } catch (e: java.lang.Exception) {
            throw IOException("Error generating MD5 hash: " + e.message)
        }
    }

    //    Written by hannibal02 and Su386
    class KnownMod(val name: String, val version: String, val hash: String, val download: String) {
        var latest = false

        fun isEmpty(): Boolean {
            return name.isEmpty() && version.isEmpty() && hash.isEmpty()
        }

    }


    class ModFile(val modId: String, val fileName: String, val version: String, val modName: String) {
        val path = Path("mods/${fileName}")
        private var hashString: String = ""
        fun isEmpty(): Boolean {
            return modId.isEmpty() && version.isEmpty() && modName.isEmpty()
        }

        fun isSafe(): Boolean {
            val jarFile = File(path.pathString)

            val modInfo = getMcModInfoFile(jarFile)

            if (modInfo.isEmpty()) {
                return false
            }

            val hash = getHash()

            val knownMod = this.findMod()

            return !knownMod.isEmpty()
        }

        fun getHash(): String {
            if (hashString.isEmpty()) {
                hashString = generateHash(File(path.pathString))

            }

            return hashString
        }

        fun findMod(): KnownMod {
            val hash = getHash()

            for (mod in knownMods) {
                if (mod.hash == hash) {
                    return mod
                }
            }
            return KnownMod("", "", "", "")
        }
    }




    class Frame: JFrame() {
        init {
            setAlwaysOnTop(true)
            setResizable(false)
//            defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
//            setUndecorated(true)
            setSize(400, 150)
            setLocationRelativeTo(null)
            background = Color(255, 255, 255, 255)
            //        Loads the data
            loadModDataFromRepo()
//        Makes a list of all the mods in the golder
            val modsInFolder: List<File> = getModsInFolder()

//        Creates two new lists where we will add the supicious mods
            val unknownFiles = ArrayList<ModFile>()
            val unknownMods = ArrayList<ModFile>()
            val outdatedMods = ArrayList<ModFile>()

            for (jarFile: File in modsInFolder) {

//            println("Jar file $jarFile")
                val modFile = loadModFile(jarFile)

//            println("Mod file $modFile")
                if (!modFile.isSafe()) {
                    if (modFile.isEmpty()) {
                        unknownFiles.add(modFile)
                    } else {
                        unknownMods.add(modFile)

                    }
                }
                else if (!modFile.findMod().latest && modFile.isSafe()) {
                    outdatedMods.add(modFile)
                }

            }


            val titleLabel = JLabel("Mods")

            val titleFont = titleLabel.font.deriveFont(5)
            val headingFont = titleLabel.font.deriveFont(2.5f)

            titleLabel.font = titleFont
            contentPane.add(titleLabel)
            pack()

            if (unknownFiles.size > 0) {
                val unknownFileHeading = JLabel("Unknown Files:")
                unknownFileHeading.font = headingFont
                unknownFileHeading.foreground = Color(255, 0, 0)
                contentPane.add(unknownFileHeading)
                pack()

                contentPane.add(JLabel("These files are not recognized as mods by Partly Sane Skies."))
                pack()
                contentPane.add(JLabel("Proceed at your own risk."))
                pack()

                for (mod in unknownFiles) {
                    contentPane.add(JLabel("- \"${mod.modName}\" (${mod.path})"))
                    pack()
                }
            }

            if (unknownMods.size > 0) {
                val unknownModHeading = JLabel("Unknown Mod:")
                unknownModHeading.font = headingFont
                unknownModHeading.foreground = Color(255, 0, 0)
                contentPane.add(unknownModHeading)
                pack()
                contentPane.add(JLabel("These mods have not been verified by Partly Sane Skies.\nProceed at your own risk.\n\n"))
                pack()
                contentPane.add(JLabel("Proceed at your own risk."))
                pack()

                for (mod in unknownFiles) {
                    contentPane.add(JLabel("- \"${mod.modName}\" (${mod.path})"))
                    pack()
                }
            }

            if (outdatedMods.size > 0) {
                val outdatedModHeading = JLabel("Outdated Mod:")
                outdatedModHeading.font = headingFont
                outdatedModHeading.foreground = Color(255, 200, 0)
                contentPane.add(outdatedModHeading)
                pack()

                contentPane.add(JLabel("These mods have not been verified by Partly Sane Skies.\nProceed at your own risk.\n\n"))
                pack()
                contentPane.add(JLabel("Proceed at your own risk."))
                pack()

                for (mod in unknownFiles) {
                    contentPane.add(JLabel("- \"${mod.modName}\" (${mod.path})"))
                    pack()

                }
            }




//            contentPane.add(textLabel)
            pack()
            setVisible(true)
        }
    }
}