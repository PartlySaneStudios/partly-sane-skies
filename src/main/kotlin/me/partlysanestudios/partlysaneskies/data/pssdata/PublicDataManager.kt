//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.data.pssdata

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.commands.PSSCommand
import me.partlysanestudios.partlysaneskies.data.api.Request
import me.partlysanestudios.partlysaneskies.data.api.RequestsManager
import me.partlysanestudios.partlysaneskies.events.data.LoadPublicDataEvent
import me.partlysanestudios.partlysaneskies.utils.ChatUtils.sendClientMessage
import net.minecraft.command.ICommandSender
import net.minecraft.util.ChatComponentText
import java.net.MalformedURLException

object PublicDataManager {
    // Add all initializing of public data here
    private val fileCache = HashMap<String, String>()
    private val lock = Lock()

    /**
     * @return the current repo's owner
     */
    fun getRepoOwner(): String {
        return PartlySaneSkies.config.repoOwner ?: "PartlySaneStudios"
    }

    /**
     * @return the current repo's name
     */
    fun getRepoName(): String {
        return PartlySaneSkies.config.repoName ?: "partly-sane-skies-public-data"
    }

    /**
     * Gets the file from either the cache or the GitHub repo
     *
     * @param path the path to the file from the /data/ folder on the GitHub repo
     * @return a string version of the json file
     */
    fun getFile(path: String): String {
        var fixedPath = path
        if (fixedPath.startsWith("/")) {
            fixedPath = fixedPath.substring(1)
        }
        if (fixedPath.endsWith("/")) {
            fixedPath = fixedPath.substring(0, fixedPath.length - 1)
        }
        if (fileCache.containsKey(fixedPath)) {
            return fileCache[fixedPath] ?: ""
        }

        try {
            val url = getPublicDataUrl(getRepoOwner(), getRepoName(), fixedPath)
            RequestsManager.newRequest(
                Request(url, {
                    if (!it.hasSucceeded()) {
                        synchronized(lock) { lock.notifyAll() }
                        return@Request
                    }
                    fileCache[path] = it.getResponse()
                    synchronized(lock) { lock.notifyAll() }
                })
            )
        } catch (e: MalformedURLException) {
            synchronized(lock) { lock.notifyAll() }
        }
        try {
            synchronized(lock) { lock.wait() }
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        }

        return if (!fileCache.containsKey(path)) {
            ""
        } else fileCache[fixedPath] ?: ""
    }

    fun getPublicDataUrl(repoOwner: String, repoName: String, filePath: String): String {
        return if (config.useGithubForPublicData) {
            "https://raw.githubusercontent.com/${repoOwner}/${repoName}/main/data/${filePath}"
        } else {
            "${config.apiUrl}/v1/pss/publicdata?owner=${repoOwner}&repo=${repoName}&path=/data/${filePath}"
        }
    }

    /**
     * Creates and registers the clear data command
     */
    fun registerDataCommand() {
        PSSCommand("updatepssdata")
            .addAlias("clearhashmap")
            .addAlias("clearpssdata")
            .addAlias("psscleardata")
            .addAlias("pssclearcache")
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
                LoadPublicDataEvent.onDataLoad()
            }.register()
    }
    private class Lock : Object()

}