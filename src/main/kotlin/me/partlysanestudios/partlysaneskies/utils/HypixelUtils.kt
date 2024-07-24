//
// Written by Su386 and J10a1n15.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.utils

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.data.skyblockdata.IslandType
import me.partlysanestudios.partlysaneskies.utils.StringUtils.removeColorCodes
import me.partlysanestudios.partlysaneskies.utils.StringUtils.stripLeading
import me.partlysanestudios.partlysaneskies.utils.StringUtils.stripTrailing
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import java.util.Locale

object HypixelUtils {
    // Returns if the current gamemode is skyblock
    fun isSkyblock(): Boolean = MinecraftUtils.getScoreboardName().lowercase(Locale.getDefault()).contains("skyblock")

    // Returns if the current server is hypixel
    fun isHypixel(): Boolean {
        try {
            return PartlySaneSkies.minecraft.currentServerData.serverIP
                .contains(".hypixel.net")
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
        } else {
            try {
                money.toLong()
            } catch (event: java.lang.NumberFormatException) {
                0
            }
        }
    }

    // Gets the current skyblock region from the scoreboard
    fun getRegionName(): String {
        if (!isSkyblock()) {
            return ""
        }
        val scoreboard = MinecraftUtils.getScoreboardLines()
        for (line in scoreboard) {
            if (
                stripLeading(line).contains("⏣") ||
                stripLeading(line).contains("ф")
            ) {
                return stripLeading(line).replace(Regex("[⏣ф]"), "")
            }
        }
        return ""
    }

    /**
     * Gets the item id from an item
     * @return The item id
     */
    fun ItemStack.getItemId(): String = this.getItemAttributes()?.getString("id") ?: ""

    fun ItemStack.getHypixelEnchants(): Map<String, Int> {
        val map = HashMap<String, Int>()

        val enchantsCompound = this.getItemAttributes()?.getCompoundTag("enchantments") ?: return map

        for (key in enchantsCompound.keySet) {
            val enchLevel = enchantsCompound.getInteger(key)

            map[key] = enchLevel
        }

        return map
    }

    /**
     * Gets the item attributes from an item
     * @return The item attributes
     */
    fun ItemStack.getItemAttributes(): NBTTagCompound? = this.tagCompound?.getCompoundTag("ExtraAttributes")

    fun inAdvancedMiningIsland() =
        IslandType.DWARVEN_MINES.onIsland() || IslandType.CRYSTAL_HOLLOWS.onIsland() || IslandType.MINESHAFT.onIsland()
}
