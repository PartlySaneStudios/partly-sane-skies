//
// Written by J10a1n15. Rewritten by Su386 in Kotlin.
// See LICENSE for copyright and license notices.
// Hard inspired by NotEnoughUpdates - https://github.com/NotEnoughUpdates/NotEnoughUpdates
//
package me.partlysanestudios.partlysaneskies.utils

import com.google.common.collect.ComparisonChain
import com.google.common.collect.Ordering
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.utils.StringUtils.removeColorCodes
import net.minecraft.client.Minecraft
import net.minecraft.client.network.NetworkPlayerInfo
import net.minecraft.scoreboard.ScorePlayerTeam
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.util.stream.Collectors

object MinecraftUtils {
    private val playerOrdering = Ordering.from { overlay1: NetworkPlayerInfo?, overlay2: NetworkPlayerInfo? ->
        comparePlayers(
            overlay1!!, overlay2!!
        )
    }


    @get:SideOnly(Side.CLIENT)
    val tabList: List<String>
        get() {
            val players = Minecraft.getMinecraft().thePlayer.sendQueue.playerInfoMap.stream()
                .sorted(playerOrdering)
                .collect(Collectors.toList())
            return players.stream()
                .map { info: NetworkPlayerInfo? ->
                    Minecraft.getMinecraft().ingameGUI.tabList.getPlayerName(info)
                }
                .collect(Collectors.toList())
        }

    private fun comparePlayers(overlay1: NetworkPlayerInfo, overlay2: NetworkPlayerInfo): Int {
        val team1 = overlay1.playerTeam
        val team2 = overlay2.playerTeam
        return ComparisonChain.start()
            .compare(
                if (team1 != null) team1.registeredName else "",
                if (team2 != null) team2.registeredName else ""
            )
            .compare(overlay1.gameProfile.name, overlay2.gameProfile.name)
            .result()
    }

    // Returns the name of the scoreboard without color codes
    fun getScoreboardName(): String {
        val scoreboardName =
            PartlySaneSkies.minecraft.thePlayer.worldScoreboard.getObjectiveInDisplaySlot(1).displayName
        return scoreboardName.removeColorCodes()
    }

    // Returns a list of lines on the scoreboard,
    // where each line is a new entry
    fun getScoreboardLines(): List<String> {
        return try {
            val scoreboard = PartlySaneSkies.minecraft.theWorld.scoreboard
            val objective = scoreboard.getObjectiveInDisplaySlot(1)
            val scoreCollection = scoreboard.getSortedScores(objective)
            val scoreLines: MutableList<String> = ArrayList()
            for (score in scoreCollection) {
                scoreLines.add(
                    ScorePlayerTeam.formatPlayerName(
                        scoreboard.getPlayersTeam(score.playerName),
                        score.playerName
                    )
                )
            }
            scoreLines
        } catch (e: IllegalArgumentException) {
            if (e.message == "Cannot locate declared field class net.minecraft.client.gui.inventory.GuiChest.field_147015_w") {
                println("Strange error message in PartlySaneSkies.getScoreboardLines()")
            }
            e.printStackTrace()
            emptyList()
        }
    }
}