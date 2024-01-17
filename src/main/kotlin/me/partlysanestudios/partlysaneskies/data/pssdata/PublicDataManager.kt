//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.data.pssdata

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.commands.PSSCommand
import me.partlysanestudios.partlysaneskies.data.api.Request
import me.partlysanestudios.partlysaneskies.data.api.RequestsManager
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockDataManager
import me.partlysanestudios.partlysaneskies.features.dungeons.playerrating.PlayerRating
import me.partlysanestudios.partlysaneskies.features.economy.minioncalculator.MinionData
import me.partlysanestudios.partlysaneskies.features.farming.MathematicalHoeRightClicks
import me.partlysanestudios.partlysaneskies.features.farming.garden.CompostValue
import me.partlysanestudios.partlysaneskies.features.farming.garden.SkymartValue
import me.partlysanestudios.partlysaneskies.utils.ChatUtils.sendClientMessage
import me.partlysanestudios.partlysaneskies.utils.SystemUtils
import net.minecraft.command.ICommandSender
import net.minecraft.util.ChatComponentText
import org.apache.logging.log4j.Level
import java.net.MalformedURLException

object PublicDataManager {
    // Add all initializing of public data here
    private val dataInitFunctions: Array<() -> Unit> = arrayOf(
        SkymartValue::initCopperValues,
        CompostValue::init,
        MinionData::init,
        SkyblockDataManager::initBitValues,
        MathematicalHoeRightClicks::loadHoes,
        PlayerRating::initPatterns
    )

    private val fileCache = HashMap<String, String>()
    private val lock = Lock()

    fun initAllPublicData() {
        Thread() {
            for (element in dataInitFunctions) {
                try {
                    SystemUtils.log(Level.INFO, "Loading ${element.javaClass.name} $element ")
                    element.invoke()
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {

                }
            }
        }.start()

    }

    /**
     * @return the current repo's owner
     */
    fun getRepoOwner(): String {
        return PartlySaneSkies.config.repoOwner?: "PartlySaneStudios"
    }

    /**
     * @return the current repo's name
     */
    fun getRepoName(): String {
        return PartlySaneSkies.config.repoName?: "partly-sane-skies-public-data"
    }

    /**
     * Gets the file from either the cache or the github repo
     *
     * @param path the path to the file from the /data/ folder on the github repo
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
            return fileCache[fixedPath]?: ""
        }

        try {
            RequestsManager.newRequest(
                Request("https://raw.githubusercontent.com/" + getRepoOwner() + "/" + getRepoName() + "/main/data/" + fixedPath, {
                    if (!it.hasSucceeded()) {
                        synchronized(lock) { lock.notifyAll() }
                        return@Request
                    }
                    fileCache[path] = it.getResponse()
                    synchronized(lock) { lock.notifyAll() }
                }))
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
        } else fileCache[fixedPath]?: ""
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
                initAllPublicData()
            }.register()
    }

    private class Lock: Object()

}