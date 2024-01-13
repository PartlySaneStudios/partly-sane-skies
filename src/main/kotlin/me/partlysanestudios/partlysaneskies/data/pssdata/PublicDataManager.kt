package me.partlysanestudios.partlysaneskies.data.pssdata

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.data.api.Request
import me.partlysanestudios.partlysaneskies.data.api.RequestRunnable
import me.partlysanestudios.partlysaneskies.data.api.RequestsManager
import me.partlysanestudios.partlysaneskies.commands.PSSCommand
import me.partlysanestudios.partlysaneskies.utils.ChatUtils.sendClientMessage
import net.minecraft.command.ICommandSender
import net.minecraft.util.ChatComponentText
import java.net.MalformedURLException

object PublicDataManager {
    private val fileCache = HashMap<String, String>()
    private val lock = Lock()


    fun getRepoOwner(): String {
        return if (PartlySaneSkies.config == null) {
            "PartlySaneStudios"
        } else PartlySaneSkies.config.repoOwner
    }

    fun getRepoName(): String {
        return if (PartlySaneSkies.config == null) {
            "partly-sane-skies-public-data"
        } else PartlySaneSkies.config.repoName
    }

    fun getFile(path: String): String? {
        var fixedPath = path
        if (fixedPath.startsWith("/")) {
            fixedPath = fixedPath.substring(1)
        }
        if (fixedPath.endsWith("/")) {
            fixedPath = fixedPath.substring(0, fixedPath.length - 1)
        }
        if (fileCache.containsKey(fixedPath)) {
            return fileCache[fixedPath]
        }
        try {
            RequestsManager.newRequest(
                Request("https://raw.githubusercontent.com/" + getRepoOwner() + "/" + getRepoName() + "/main/data/" + path,
                    RequestRunnable { r: Request ->
                        if (!r.hasSucceeded()) {
                            (lock as Object).notifyAll()
                            return@RequestRunnable
                        }
                        fileCache[path] = r.getResponse()
                        synchronized(lock) { (lock as Object).notifyAll() }
                    })
            )
        } catch (e: MalformedURLException) {
            synchronized(lock) { (lock as Object).notifyAll() }
        }
        try {
            synchronized(lock) { (lock as Object).wait() }
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        }
        return if (!fileCache.containsKey(path)) {
            ""
        } else fileCache[path]
    }

    fun registerDataCommand() {
        PSSCommand("updatepssdata")
            .addAlias("clearhashmap")
            .addAlias("clearpssdata")
            .setDescription("Clears your Partly Sane Studios data")
            .setRunnable { _: ICommandSender?, _: Array<String?>? ->
                val chatcomponent = ChatComponentText(
                    """
                §b§4-----------------------------------------------------§7
                Data Refreshed
                §b§4-----------------------------------------------------§0
                """.trimIndent()
                )
                fileCache.clear()
                sendClientMessage(chatcomponent)
            }.register()
    }


    private class Lock

}