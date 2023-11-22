//
// Written by J10a1n15. Rewritten by Su386 in Kotlin.
// See LICENSE for copyright and license notices.
// Hard inspired by NotEnoughUpdates - https://github.com/NotEnoughUpdates/NotEnoughUpdates
//
package me.partlysanestudios.partlysaneskies.utils

import com.google.common.collect.ComparisonChain
import com.google.common.collect.Ordering
import net.minecraft.client.Minecraft
import net.minecraft.client.network.NetworkPlayerInfo
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.util.stream.Collectors

object TabListUtils {
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
}