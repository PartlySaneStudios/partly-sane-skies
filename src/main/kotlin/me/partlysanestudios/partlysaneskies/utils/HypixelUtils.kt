//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.utils

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.features.information.WikiArticleOpener
import me.partlysanestudios.partlysaneskies.utils.StringUtils.removeColorCodes
import me.partlysanestudios.partlysaneskies.utils.StringUtils.stripLeading
import me.partlysanestudios.partlysaneskies.utils.StringUtils.stripTrailing
import net.minecraft.item.ItemStack
import java.util.*

object HypixelUtils {
    // Returns if the current gamemode is skyblock
    fun isSkyblock(): Boolean {
        try {
            if (MinecraftUtils.getScoreboardName().lowercase(Locale.getDefault()).contains("skyblock")) {
                return true
            }
        } catch (expt: NullPointerException) {
            return false
        }
        return false
    }

    // Returns if the current server is hypixel
    fun isHypixel(): Boolean {
        try {
            return PartlySaneSkies.minecraft.currentServerData.serverIP.contains(".hypixel.net")
        } catch (ignored: NullPointerException) {
        }
        return false
    }

    // Gets the number of bits from the scoreboard
    fun getBits(): Long {
        if (!isSkyblock()) {
            return 0L
        }
        val scoreboard = MinecraftUtils.getScoreboardLines()
        var bits = ""
        for (line in scoreboard) {
            if (stripLeading(line).contains("Bits:")) {
                bits = stripLeading(line.removeColorCodes()).replace("Bits: ", "")
                bits = stripLeading(bits)
                bits = bits.replace(",", "")
                bits = bits.replace("\\P{Print}".toRegex(), "")
                break
            }
        }
        if (bits == "") {
            return 0L
        }
        val charsToRemove = arrayOf("(", ")", ".", "-", "+")
        for (removalChar in charsToRemove) {
            if (bits.contains(removalChar)) {
                val indexOfEndOfCount = bits.indexOf(removalChar)
                bits = bits.substring(0, indexOfEndOfCount)
            }
        }
        bits = stripLeading(bits)
        bits = stripTrailing(bits)
        return try {
            bits.toLong()
        } catch (event: NumberFormatException) {
            0
        }
    }

    // Gets the number of coins in your purse from the scoreboard
    fun getCoins(): Long {
        if (!isSkyblock()) {
            return 0L
        }
        val scoreboard = MinecraftUtils.getScoreboardLines()
        var money = ""
        for (line in scoreboard) {
            if (stripLeading(line).contains("Piggy:") || stripLeading(line).contains("Purse:")) {
                money = stripLeading(line.removeColorCodes()).replace("Piggy: ", "")
                money = stripLeading(money.removeColorCodes()).replace("Purse: ", "")
                money = stripLeading(money)
                money = money.replace(",", "")
                money = money.replace("\\P{Print}".toRegex(), "")
                break
            }
        }
        return if (money == "") {
            0L
        } else try {
            money.toLong()
        } catch (event: java.lang.NumberFormatException) {
            0
        }
    }

    // Gets the current skyblock region from the scoreboard
    fun getRegionName(): String {
        if (!isSkyblock()) {
            return ""
        }
        val scoreboard = MinecraftUtils.getScoreboardLines()
        var location = ""
        for (line in scoreboard) {
            if (stripLeading(line).contains("⏣") || stripLeading(
                    line
                ).contains("ф")
            ) {
                location = if (stripLeading(line).contains("⏣")) stripLeading(
                    line
                ).replace("⏣", "") else stripLeading(line).replace("ф", "")
                location = stripLeading(location)
                break
            }
        }
        return location
    }

    fun getItemId(item: ItemStack?): String {
        if (item == null) {
            return ""
        }
        return if (WikiArticleOpener.getItemAttributes(item) == null) {
            ""
        } else WikiArticleOpener.getItemAttributes(item).getString("id")
    }

    fun getCurrentIsland(): IslandType {
        for (line in MinecraftUtils.getTabList()) {
            if (line.removeColorCodes().startsWith("Area: ") || line.removeColorCodes().startsWith("Dungeon: ")) {
                val islandName = line.removeColorCodes().replace("Area: ", "").replace("Dungeon: ", "").trim()

                return IslandType.values().firstOrNull { it.islandName.equals(islandName, ignoreCase = true) } ?: IslandType.NONE
            }
        }

        return IslandType.NONE
    }
}

/*
    Inspired by SkyHanni, https://github.com/hannibal002/SkyHanni/
 */
enum class IslandType(val islandName: String) {
    HUB("Hub"),
    DUNGEON_HUB("Dungeon Hub"),
    PRIVATE_ISLAND("Private Island"),
    GARDEN("Garden"),
    THE_PARK("The Park"),
    SPIDERS_DEN("Spider's Den"),
    CRIMSON_ISLE("Crimson Isle"),
    THE_END("The End"),
    GOLD_MINE("Gold Mine"),
    DEEP_CAVERNS("Deep Caverns"),
    DWARVEN_MINES("Dwarven Mines"),
    CRYSTAL_HOLLOWS("Crystal Hollows"),
    FARMING_ISLAND("The Farming Islands"),
    WINTER_ISLAND("Jerry's Workshop"), // value by sh, unconfirmed
    RIFT("The Rift"),
    CATACOMBS("Catacombs"),
    KUUDRA("Kuudra"),

    NONE("");

    fun onIsland(): Boolean {
        return this.islandName == HypixelUtils.getCurrentIsland().islandName
    }
}