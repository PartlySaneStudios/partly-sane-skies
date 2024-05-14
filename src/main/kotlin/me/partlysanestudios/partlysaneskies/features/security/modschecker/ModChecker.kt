//
// Written by hannibal002 and Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.security.modschecker

import com.google.gson.Gson
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.minecraft
import me.partlysanestudios.partlysaneskies.commands.PSSCommand
import me.partlysanestudios.partlysaneskies.data.api.Request
import me.partlysanestudios.partlysaneskies.data.api.RequestsManager.newRequest
import me.partlysanestudios.partlysaneskies.data.pssdata.PublicDataManager.getRepoName
import me.partlysanestudios.partlysaneskies.data.pssdata.PublicDataManager.getRepoOwner
import me.partlysanestudios.partlysaneskies.features.debug.DebugKey.isDebugMode
import me.partlysanestudios.partlysaneskies.utils.ChatUtils.sendClientMessage
import me.partlysanestudios.partlysaneskies.utils.SystemUtils.copyStringToClipboard
import me.partlysanestudios.partlysaneskies.utils.SystemUtils.isValidURL
import net.minecraft.command.ICommandSender
import net.minecraft.event.ClickEvent
import net.minecraft.event.HoverEvent
import net.minecraft.util.ChatComponentText
import net.minecraft.util.IChatComponent
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.ModContainer
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.security.MessageDigest

object ModChecker {
    fun registerModCheckCommand() {
        PSSCommand(
            "modcheck", ArrayList(), "Checks the mods in your mod folder if they are updated"
        ) { s: ICommandSender, a: Array<String> ->
            Thread {
                if (a.isNotEmpty()) {
                    sendClientMessage("Loading... (using data from custom repository)")
                    loadModDataFromRepo(
                        getRepoOwner(),
                        getRepoName()
                    )
                } else {
                    sendClientMessage("Loading...")
                    loadModDataFromRepo()
                }
            }.start()
        }.addAlias("modscheck", "modchecker", "modschecker", "pssmodscheck", "pssmodchecker", "pssmodschecker")
            .register()
    }

    private var knownMods: List<KnownMod> = ArrayList<KnownMod>()
    private var hasRunOnStartup = false
    fun runOnStartup() {
        Thread(Runnable {
            if (!config.checkModsOnStartup) {
                return@Runnable
            }
            try {
                Thread.sleep(5000)
            } catch (e: InterruptedException) {
                throw RuntimeException(e)
            }
            if (!hasRunOnStartup) {
                hasRunOnStartup = true
                sendClientMessage("Loading...")
                loadModDataFromRepo()
            }
        }).start()
    }

    fun run() {
        var modsFound = 0
        val chatMessage: IChatComponent = ChatComponentText("")
        val debugBuilder = StringBuilder()
        val knownMods = ArrayList<ModContainer>()
        val unknownMods = ArrayList<ModContainer>()
        val outdatedMods = ArrayList<ModContainer>()
        for (container in Loader.instance().activeModList) {
            val version = container.version
            val displayVersion = container.displayVersion
            val modName = container.name
            val modFile = container.source
            val fileName = modFile.getName()

            // Cannot read hash of Minecraft Coder Pack or other stuff like Smooth Font Core
            if (fileName == "minecraft.jar") {
                continue
            }
            try {
                val hash = generateHash(modFile)

                // FML has the same hash as "Minecraft Forge", therefore ignoring it
                if (modName == "Forge Mod Loader") {
                    if (hash == "596512ad5f12f95d8a3170321543d4455d23b8fe649c68580c5f828fe74f6668") {
                        continue
                    }
                }
                val mod = findModFromHash(hash)
                if (mod == null) {
                    unknownMods.add(container)
                } else {
                    if (mod.latest) {
                        knownMods.add(container)
                    } else {
                        outdatedMods.add(container)
                    }
                }
                modsFound++
            } catch (e: IOException) {
                sendClientMessage("Error reading hash of mod $fileName!", true)
                debugBuilder.append("\nerror reading hash!")
                debugBuilder.append("\nerror reading hash!")
                debugBuilder.append("\nfileName: $fileName")
                debugBuilder.append("\nmodName: $modName")
                debugBuilder.append("\nversion: $version")
                debugBuilder.append("\ndisplayVersion: $displayVersion")
                debugBuilder.append("\n ")
            }
        }
        chatMessage.appendSibling(ChatComponentText("\n§7Disclaimer: You should always exercise caution when downloading things from the internet. The PSS Mod Checker is not foolproof. Use at your own risk."))
        if (config.showUpToDateMods) {
            if (knownMods.isNotEmpty()) {
                chatMessage.appendSibling(
                    ChatComponentText(
                        """
                        
                        
                        §6Up to date Mods: (${knownMods.size})
                        """.trimIndent()
                    )
                )
            }
            for (container in knownMods) {
                val modFile = container.source
                var hash = ""
                try {
                    hash = generateHash(modFile)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                val mod = findModFromHash(hash)
                    ?: continue
                val message: IChatComponent = ChatComponentText("\n§a${mod.name} §7is up to date")
                if (isValidURL(mod.downloadLink)) {
                    message.chatStyle.setChatClickEvent(ClickEvent(ClickEvent.Action.OPEN_URL, mod.downloadLink))
                    message.chatStyle.setChatHoverEvent(
                        HoverEvent(
                            HoverEvent.Action.SHOW_TEXT,
                            ChatComponentText("Click for the official website for " + mod.name + "!")
                        )
                    )
                }
                chatMessage.appendSibling(message)
            }
        }
        if (outdatedMods.isNotEmpty()) {
            chatMessage.appendSibling(ChatComponentText("\n\n\n§6Out of Date Mods: (${outdatedMods.size})"))
        }
        for (container in outdatedMods) {
            val modFile = container.source
            var hash = ""
            try {
                hash = generateHash(modFile)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val mod = findModFromHash(hash) ?: continue
            val latestVersion = findNewestModFromId(mod.id).version
            val message: IChatComponent =
                ChatComponentText("\n§e${mod.name} §7is §coutdated §7(§e${mod.version} §7-> §e$latestVersion§7)")
            if (isValidURL(mod.downloadLink)) {
                message.chatStyle.setChatClickEvent(ClickEvent(ClickEvent.Action.OPEN_URL, mod.downloadLink))
                message.chatStyle.setChatHoverEvent(
                    HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        ChatComponentText("Click for the official website for " + mod.name + "!")
                    )
                )
            }
            chatMessage.appendSibling(message)
        }
        if (unknownMods.isNotEmpty()) {
            chatMessage.appendSibling(
                ChatComponentText(
                    """
                    
                    
                    §cUnknown Mods: (${unknownMods.size})
                    """.trimIndent()
                )
            )
            chatMessage.appendSibling(ChatComponentText("\n§7These mods have not been verified by PSS admins!"))
        }
        for (container in unknownMods) {
//            val version = container.version
//            val displayVersion = container.displayVersion
            val modName = container.name
            val modFile = container.source
            val fileName = modFile.getName()
            var hash = ""
            try {
                hash = generateHash(modFile)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            var message = ChatComponentText("\n§c$modName §7($fileName) is §cunknown!")
            try {
                val mod = findNewestModFromId(container.modId)
                message =
                    ChatComponentText("\n§c$modName §7($fileName) is §cunknown! §c(Verified version of ${mod.name} found.)")

                if (isValidURL(mod.downloadLink)) {
                    message.chatStyle.setChatClickEvent(ClickEvent(ClickEvent.Action.OPEN_URL, mod.downloadLink))
                    message.chatStyle.setChatHoverEvent(
                        HoverEvent(
                            HoverEvent.Action.SHOW_TEXT,
                            ChatComponentText("Click for the official website for " + mod.name + "!")
                        )
                    )
                }
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
            chatMessage.appendSibling(message)
            debugBuilder.append(
                """
                    
                    "${container.modId}": {
                    """.trimIndent()
            )
            debugBuilder.append("\n    \"name\": \"$modName\",")
            debugBuilder.append(
                """
    "download": "${container.metadata.url}","""
            )
            debugBuilder.append("\n    \"versions\": {")
            debugBuilder.append(
                """
        "${container.version}": "$hash""""
            )
            debugBuilder.append("\n    },")
            debugBuilder.append("\n    \"betaVersions\": {")
            debugBuilder.append(
                """
        "${container.version}": "$hash""""
            )
            debugBuilder.append("\n    }")
            debugBuilder.append("\n},")
        }
        chatMessage.appendSibling(ChatComponentText("\n\n§9If you believe any of these mods may be a mistake, report it in the PSS discord! §7(/pssdiscord)"))
        if (isDebugMode()) {
            sendClientMessage(
                """
                    §8Unknown Mods:
                    ${
                    insertCharacterAfterNewLine(
                        debugBuilder.toString(),
                        "§8"
                    )
                }
                    
                    
                    """.trimIndent()
            )
            copyStringToClipboard("```json\n$debugBuilder\n```")
        }
        sendClientMessage(" \n§7Found $modsFound mods:")
        minecraft.ingameGUI
            .chatGUI
            .printChatMessage(chatMessage)
    }

    private fun insertCharacterAfterNewLine(originalString: String, insertionChar: String): String {
        val stringBuilder = StringBuilder()
        for (c in originalString.toCharArray()) {
            stringBuilder.append(c)
            if (c == '\n') {
                stringBuilder.append(insertionChar)
            }
        }
        return stringBuilder.toString()
    }

    private fun loadModDataFromRepo(
        userName: String = "PartlySaneStudios",
        repoName: String = "partly-sane-skies-public-data",
    ) {
        val url: String = if (config.useGithubForPublicData) {
            "https://raw.githubusercontent.com/$userName/$repoName/main/data/mods.json"
        } else {
            config.apiUrl + "/v1/pss/publicdata?owner=" + userName + "&repo=" + repoName + "&path=/data/mods.json"
        }
        newRequest(Request(url, { request: Request ->
            knownMods = ArrayList()
            try {
                knownMods = read(Gson().fromJson(request.getResponse(), ModDataJson::class.java))
                run()
            } catch (e: Exception) {
                sendClientMessage("§cError reading the mod data from repo!")
                e.printStackTrace()
            }
        }))
    }

    private fun read(modData: ModDataJson): List<KnownMod> {
        val list: MutableList<KnownMod> = ArrayList()
        for ((modId, modInfo) in modData.mods ?: return ArrayList()) {
            val download = modInfo.download
            var latest: KnownMod? = null
            var versions: Map<String, String>
            var betaVersions: Map<String, String> =
                HashMap() // Creates a variable to add all the beta versions as outdated
            if (config.lookForBetaMods) {
                versions = modInfo.betaVersions
            } else {
                betaVersions = modInfo.betaVersions
                versions = modInfo.versions
            }
            for ((version, hash) in versions) {
                latest = KnownMod(modId, modInfo.name, version, download, hash)
                list.add(latest)
            }
            for ((version, hash) in betaVersions) {
                list.add(KnownMod(modId, modInfo.name, version, download, hash))
            }
            if (latest != null) {
                latest.latest = true
            }
        }
        return list
    }

    private fun findNewestModFromId(id: String): KnownMod {
        for (mod in knownMods) {
            if (mod.id == id) {
                if (mod.latest) {
                    return mod
                }
            }
        }
        throw IllegalStateException("Found no newest mod with the id `$id'")
    }

    private fun findModFromHash(hash: String): KnownMod? {
        for (mod in knownMods) {
            if (mod.hash == hash) {
                return mod
            }
        }
        return null
    }

    @Throws(IOException::class)
    private fun generateHash(file: File): String {
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
        } catch (e: Exception) {
            throw IOException("Error generating MD5 hash: " + e.message)
        }
    }

    internal class KnownMod(
        internal val id: String,
        internal val name: String,
        internal val version: String,
        internal val downloadLink: String,
        internal val hash: String,
    ) {
        internal var latest = false
    }
}
