//
// Written by J10a1n15 and Su386.
// See LICENSE for copyright and license notices.
// Hard inspired by NotEnoughUpdates - https://github.com/NotEnoughUpdates/NotEnoughUpdates
//


package me.partlysanestudios.partlysaneskies.utils

import com.google.common.collect.ComparisonChain
import com.google.common.collect.Ordering
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.utils.StringUtils.removeColorCodes
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.network.NetworkPlayerInfo
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.scoreboard.ScorePlayerTeam
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.apache.commons.lang3.reflect.FieldUtils
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

    fun getCurrentlyHoldingItem(): ItemStack {
        return PartlySaneSkies.minecraft.thePlayer.heldItem
    }

    // Returns an array of length 2, where the 1st index is the upper inventory,
    // and the 2nd index is the lower inventory.
    // Returns null if there is no inventory, also returns null if there is no access to inventory
    fun getSeparateUpperLowerInventories(gui: GuiScreen): Array<IInventory?> {
        val upperInventory: IInventory
        val lowerInventory: IInventory
        try {
            upperInventory = FieldUtils.readDeclaredField(
                gui,
                getDecodedFieldName("upperChestInventory"), true
            ) as IInventory
            lowerInventory = FieldUtils.readDeclaredField(
                gui,
                getDecodedFieldName("lowerChestInventory"), true
            ) as IInventory
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            return arrayOf(null, null)
        }
        return arrayOf(upperInventory, lowerInventory)
    }

    fun ItemStack.getLore(): java.util.ArrayList<String> {
        if (this == null) {
            return java.util.ArrayList()
        }
        if (!this.hasTagCompound() || !this.tagCompound.hasKey("display") || !this.tagCompound.getCompoundTag(
                "display"
            ).hasKey("Lore")
        ) {
            return java.util.ArrayList()
        }
        val tagList = this.tagCompound.getCompoundTag("display").getTagList("Lore", 8)
        val loreList = java.util.ArrayList<String>()
        for (i in 0 until tagList.tagCount()) {
            loreList.add(tagList.getStringTagAt(i))
        }
        return loreList
    }

    fun getLoreAsString(item: ItemStack): String {
        val loreList: List<String> = item.getLore()
        val loreString = StringBuilder()
        for (loreLine in loreList) {
            loreString.append(loreLine).append("\n")
        }
        return loreString.toString()
    }

    fun rightClickOnSlot(slot: Int) {
        val controller = PartlySaneSkies.minecraft.playerController
        controller.windowClick(
            PartlySaneSkies.minecraft.thePlayer.openContainer.windowId,
            slot,
            1,
            0,
            PartlySaneSkies.minecraft.thePlayer
        )
    }

    fun clickOnSlot(slot: Int) {
        val controller = PartlySaneSkies.minecraft.playerController
        controller.windowClick(
            PartlySaneSkies.minecraft.thePlayer.openContainer.windowId,
            slot,
            0,
            0,
            PartlySaneSkies.minecraft.thePlayer
        )
    }


    fun getDecodedFieldName(codedName: String?): String? {
        return object : HashMap<String?, String?>() {
            init {
                put("footer", "field_175255_h")
                put("header", "field_175256_i")
                put("upperChestInventory", "field_147015_w")
                put("lowerChestInventory", "field_147016_v")
                put("persistentChatGUI", "field_73840_e")
                put("sentMessages", "field_146248_g")
                put("streamIndicator", "field_152127_m")
                put("updateCounter", "field_73837_f")
                put("overlayPlayerList", "field_175196_v")
                put("guiIngame", "field_175251_g")
                put("chatMessages", "field_146253_i")
                put("theSlot", "field_147006_u")
                put("stackTagCompound", "field_77990_d")
            }
        }[codedName]
    }

    fun isArrOfStringsInLore(arr: Array<String?>, lore: Array<String>): Boolean {
        for (line in lore) {
            for (arrItem in arr) {
                if (line.contains(arrItem!!)) {
                    return true
                }
            }
        }
        return false
    }
}