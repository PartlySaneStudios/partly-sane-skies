//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.dungeons

import com.google.gson.JsonParser
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.RANK_NAMES
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.minecraft
import me.partlysanestudios.partlysaneskies.commands.PSSCommand
import me.partlysanestudios.partlysaneskies.data.pssdata.PublicDataManager.getFile
import me.partlysanestudios.partlysaneskies.data.skyblockdata.IslandType
import me.partlysanestudios.partlysaneskies.events.SubscribePSSEvent
import me.partlysanestudios.partlysaneskies.events.data.LoadPublicDataEvent
import me.partlysanestudios.partlysaneskies.events.skyblock.dungeons.DungeonEndEvent
import me.partlysanestudios.partlysaneskies.events.skyblock.dungeons.DungeonStartEvent
import me.partlysanestudios.partlysaneskies.utils.ChatUtils.sendClientMessage
import me.partlysanestudios.partlysaneskies.utils.MathUtils.round
import me.partlysanestudios.partlysaneskies.utils.StringUtils.removeColorCodes
import me.partlysanestudios.partlysaneskies.utils.StringUtils.stripLeading
import me.partlysanestudios.partlysaneskies.utils.StringUtils.stripTrailing
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object PlayerRating {
    private var currentPlayer = ""

    private var positiveRegexs = HashMap<Regex, String>()

    // A map that has the <Pattern, <Cause Category, Points>>
    private val playerPointCategoryMap = HashMap<String, HashMap<String, Int>>()

    // A map that has <Player, Total Points> Map
    private val totalPlayerPoints = HashMap<String, Int>()

    // A map which has <Category, Total Count>
    private val categoryPointMap = HashMap<String, Int>()
    private var totalPoints = 0

    private var lastMessage = ""

    @SubscribePSSEvent
    fun initPatterns(event: LoadPublicDataEvent) {
        currentPlayer = minecraft.session.username
        val str = getFile("constants/dungeon_player_rate_regex_strings.json")
        if (str == "") {
            return
        }
        val regexJson = JsonParser().parse(str).getAsJsonObject()
        val positiveRegexsJson = regexJson.getAsJsonObject("positive_strings")
        for ((key, value) in positiveRegexsJson.entrySet()) {
            positiveRegexs[key.toRegex()] = value.asString
        }
    }

    fun rackPoints(playerString: String, category: String) {
        var player = playerString
        if (player.equals("You", ignoreCase = true)) {
            player = currentPlayer
        }
        for (rank in RANK_NAMES) {
            player = player.replace(rank, "")
        }
        player = player.replace("\\P{Print}".toRegex(), "")
        player = stripLeading(player)
        player = stripTrailing(player)

        // If the player has already been registered
        if (playerPointCategoryMap.containsKey(player)) {
            val individualPlayerPointMap = playerPointCategoryMap[player]!!
            // If the player already has this category registered
            if (individualPlayerPointMap.containsKey(category)) {
                individualPlayerPointMap[category] = individualPlayerPointMap[category]!! + 1
            } else {
                individualPlayerPointMap[category] = 1
            }
            totalPlayerPoints[player] = totalPlayerPoints[player]!! + 1
        } else {
            val individualPlayerPointMap = java.util.HashMap<String, Int>()
            individualPlayerPointMap[category] = 1
            playerPointCategoryMap[player] =
                individualPlayerPointMap
            totalPlayerPoints[player] = 1
        }

        // If the categoryPointMap contains the category
        if (categoryPointMap.containsKey(category)) {
            categoryPointMap[category] = categoryPointMap[category]!! + 1
        } else {
            categoryPointMap[category] = 1
        }
        totalPoints++
    }

    private fun getDisplayString(): String {
        var str = StringBuilder()
        if (config.enhancedDungeonPlayerBreakdown == 0) {
            for ((key) in playerPointCategoryMap) {
                val totalPoints = ((totalPlayerPoints[key]?.toDouble() ?: 0.0) / totalPoints * 100.0).round(0)
                val playerStr = "§d$key  §9$totalPoints%§7 | "
                str.append(playerStr)
            }
            return str.toString()
        }
        str.append("§a§nDungeon Overview:\n\n")
        for ((playerName, value) in playerPointCategoryMap) {
            val totalPoints = ((totalPlayerPoints[playerName]?.toDouble() ?: 0.0) / totalPoints * 100.0).round(0)
            val playerStr = StringBuilder("§d$playerName§7 completed §d$totalPoints%§7 of the dungeon.\n")
            if (config.enhancedDungeonPlayerBreakdown == 2) {
                playerStr.append("§2   Breakdown:\n")
                for ((key, value1) in value) {
                    playerStr
                        .append("     §d")
                        .append((value1.toDouble() / (categoryPointMap[key] ?: 0) * 100.0).round(0))
                        .append("%§7 of ")
                        .append(key)
                        .append("\n")
                }
            }
            str.append(playerStr)
        }
        str = StringBuilder(str.toString())
        return str.toString()
    }

    private fun getChatMessage(): String {
        val str = java.lang.StringBuilder()
        str.append("Partly Sane Skies > ")
        for ((key) in playerPointCategoryMap) {
            val totalPoints = ((totalPlayerPoints[key]?.toDouble() ?: 0.0) / totalPoints * 100.0).round(0)
            val playerStr = "$key  $totalPoints% | "
            str.append(playerStr)
        }
        return str.toString()
    }

    private fun getSlackingMembers(): ArrayList<String> {
        val strList = ArrayList<String>()
        for ((key) in playerPointCategoryMap) {
            if (totalPlayerPoints[key]!! / (totalPoints * 1.0) > config.dungeonSnitcherPercent / 100f) {
                continue
            }
            strList.add("PSS Slacker Snitcher -> $key looks to be slacking. (This could be a mistake)")
        }
        return strList
    }

    private fun handleMessage(message: String) {
        for ((key, value) in positiveRegexs) {
            if (!(key.containsMatchIn(message.removeColorCodes()))) {
                continue
            }

            val (playerName) = key.find(message)?.destructured ?: return
            rackPoints(playerName, value)
        }
    }

    private fun reset() {
        categoryPointMap.clear()
        totalPlayerPoints.clear()
        playerPointCategoryMap.clear()
        totalPoints = 0
    }

    private fun reprintLastScore() {
        sendClientMessage(lastMessage, true)
    }

    fun registerReprintCommand() {
        PSSCommand(
            "reprintscore",
            mutableListOf("rps", "rs"),
            "Reprints the last score in a dungeon run",
        ) { reprintLastScore() }.register()
    }

    @SubscribePSSEvent
    fun onDungeonStart(event: DungeonStartEvent?) {
        if (!(config.dungeonPlayerBreakdown || config.dungeonSnitcher)) {
            return
        }
        reset()
    }

    @SubscribePSSEvent
    fun onDungeonEnd(event: DungeonEndEvent?) {
        if (!(config.dungeonPlayerBreakdown || config.dungeonSnitcher)) {
            return
        }
        val string = getDisplayString()
        lastMessage = string
        val chatMessageString = getChatMessage()
        val slackingMembers = getSlackingMembers()

        Thread {
            try {
                Thread.sleep((config.dungeonPlayerBreakdownDelay * 1000).toLong())
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            minecraft.addScheduledTask {
                if (string == "") {
                    return@addScheduledTask
                }
                sendClientMessage(string, true)
                if (config.partyChatDungeonPlayerBreakdown) {
                    minecraft.thePlayer.sendChatMessage("/pc $chatMessageString")
                }
            }
            if (config.dungeonSnitcher) {
                for (str in slackingMembers) {
                    try {
                        Thread.sleep(750)
                    } catch (e: InterruptedException) {
                        throw RuntimeException(e)
                    }
                    minecraft.addScheduledTask {
                        minecraft.thePlayer.sendChatMessage("/pc $str")
                    }
                }
            }
        }.start()
        reset()
    }

    @SubscribeEvent
    fun onChatEvent(event: ClientChatReceivedEvent) {
        if (!(config.dungeonPlayerBreakdown || config.dungeonSnitcher)) {
            return
        }
        if (event.message.unformattedText.contains("You are playing on profile:")) {
            reset()
            return
        }
        if (!IslandType.CATACOMBS.onIsland()) {
            return
        }

        handleMessage(event.message.unformattedText)
    }
}
