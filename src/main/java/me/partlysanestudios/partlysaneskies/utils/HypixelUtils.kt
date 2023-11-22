//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.utils

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.WikiArticleOpener
import me.partlysanestudios.partlysaneskies.utils.StringUtils.removeColorCodes
import me.partlysanestudios.partlysaneskies.utils.StringUtils.stripLeading
import me.partlysanestudios.partlysaneskies.utils.StringUtils.stripTrailing
import net.minecraft.item.ItemStack
import java.util.*

object HypixelUtils {
    fun inDungeons(): Boolean {
        val regionName = HypixelUtils.getRegionName()
        var noColorCodeRegionName = regionName.removeColorCodes()
        if (noColorCodeRegionName.isEmpty()) {
            return false
        }
        noColorCodeRegionName = stripLeading(noColorCodeRegionName)
        noColorCodeRegionName = stripTrailing(noColorCodeRegionName)
        noColorCodeRegionName = noColorCodeRegionName.replace("\\P{Print}".toRegex(), "") // Removes the RANDOM EMOJIS
        return noColorCodeRegionName.lowercase(Locale.getDefault()).contains("catacombs")
    }

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
        var bits: String = ""
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

}