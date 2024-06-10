//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.data.skyblockdata

import net.minecraft.item.Item
import net.minecraft.util.ResourceLocation
import java.util.*

class SkyblockItem(
    val id: String,
    val rarity: Rarity,
    val name: String,
    val npcSellPrice: Double,
    var bazaarBuyPrice: Double,
    var bazaarSellPrice: Double,
    var averageBazaarBuy: Double,
    var averageBazaarSell: Double,
    var lowestBin: Double,
    var averageLowestBin: Double,
    var material: String,
    var unstackable: Boolean
) {
    companion object {
        val emptyItem = SkyblockItem("", Rarity.UNKNOWN, "", 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, "", false)
    }

    var bitCost = 0

    fun getSellPrice(): Double {
        return if (bazaarSellPrice != 0.0) {
            bazaarSellPrice
        } else if (lowestBin != 0.0) {
            lowestBin
        } else if (npcSellPrice != 0.0) {
            npcSellPrice
        } else {
            0.0
        }
    }

    fun getBuyPrice(): Double {
        return if (bazaarBuyPrice != 0.0) {
            bazaarBuyPrice
        } else if (lowestBin != 0.0) {
            lowestBin
        } else {
            0.0
        }
    }

    fun getAverageBuyPrice(): Double {
        return if (averageBazaarBuy != 0.0) {
            averageBazaarBuy
        } else if (lowestBin != 0.0) {
            averageLowestBin
        } else if (npcSellPrice != 0.0) {
            npcSellPrice
        } else {
            0.0
        }
    }

    fun getAverageSellPrice(): Double {
        return if (bazaarSellPrice != 0.0) {
            averageBazaarSell
        } else if (lowestBin != 0.0) {
            averageLowestBin
        } else if (npcSellPrice != 0.0) {
            npcSellPrice
        } else {
            0.0
        }
    }

    fun getBestPrice(): Double {
        val list = ArrayList<Double>()
        list.add(bazaarSellPrice)
        list.add(lowestBin)
        list.add(npcSellPrice)
        list.sortWith(Comparator.naturalOrder())
        return list[list.size - 1]
    }

    fun hasSellPrice(): Boolean {
        return getSellPrice() != 0.0
    }

    fun hasBuyPrice(): Boolean {
        return getBuyPrice() != 0.0
    }

    fun hasBitCost(): Boolean {
        return bitCost != 0
    }

    fun getStackSize(): Int {
        return if (unstackable) {
            1
        } else {
            Item.itemRegistry?.getObject(
                ResourceLocation(
                    "minecraft",
                    material.lowercase(Locale.getDefault())
                )
            )?.itemStackLimit ?: 64
        }
    }

    fun hasAverageLowestBin(): Boolean {
        return averageLowestBin != 0.0
    }

    fun hasAverageBazaarSell(): Boolean {
        return averageLowestBin != 0.0
    }

    fun hasAverageBazaarBuy(): Boolean {
        return averageLowestBin != 0.0
    }

    fun hasAverageSellPrice(): Boolean {
        return getAverageSellPrice() != 0.0
    }

    fun hasAverageBuyPrice(): Boolean {
        return getAverageBuyPrice() != 0.0
    }

}