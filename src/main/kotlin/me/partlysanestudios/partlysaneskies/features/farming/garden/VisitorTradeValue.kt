//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.farming.garden

import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIWrappedText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.constraint
import gg.essential.elementa.dsl.percent
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.config
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.minecraft
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockDataManager.getId
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockDataManager.getItem
import me.partlysanestudios.partlysaneskies.features.gui.SidePanel
import me.partlysanestudios.partlysaneskies.render.gui.constraints.ScaledPixelConstraint.Companion.scaledPixels
import me.partlysanestudios.partlysaneskies.utils.ElementaUtils.applyBackground
import me.partlysanestudios.partlysaneskies.utils.MathUtils.round
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.chestInventory
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.getLore
import me.partlysanestudios.partlysaneskies.utils.StringUtils.formatNumber
import me.partlysanestudios.partlysaneskies.utils.StringUtils.removeColorCodes
import me.partlysanestudios.partlysaneskies.utils.StringUtils.stripLeading
import me.partlysanestudios.partlysaneskies.utils.StringUtils.stripTrailing
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraftforge.client.event.GuiScreenEvent
import java.awt.Color

object VisitorTradeValue: SidePanel() {
    override val panelBaseComponent: UIComponent = UIBlock().applyBackground().constrain {
        x = 800.scaledPixels
        y = CenterConstraint()
        width = 225.scaledPixels
        height = 350.scaledPixels
        color = Color(0, 0, 0, 0).constraint
    }

    private val textComponent = UIWrappedText().constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        height = 95.percent
        width = 95.percent
        textScale = 1.scaledPixels
    } childOf panelBaseComponent

    override fun shouldDisplayPanel(): Boolean {
        if (!config.gardenShopTradeInfo) {
            return false
        }
        
        if (minecraft.currentScreen == null) {
            return false
        }
        
        if (minecraft.currentScreen !is GuiChest) {
            return false
        }

        val trader = (minecraft.currentScreen as GuiChest).chestInventory

        // Slots 29 and 33 are where the buttons should be
        val acceptButton = trader.getStackInSlot(29)
        val refuseButton = trader.getStackInSlot(33)

        if (acceptButton == null) {
            return false
        }
        if (refuseButton == null) {
            return false
        }

        val acceptButtonName = acceptButton.displayName.removeColorCodes()
        val refuseButtonName = refuseButton.displayName.removeColorCodes()

        // If the names are not equal to the desired names, then you know it screen is not the trader screen
        return if (refuseButtonName != "Refuse Offer") {
            false
        } else  {
            acceptButtonName == "Accept Offer"
        }
    }

    override fun onPanelRender(event: GuiScreenEvent.BackgroundDrawnEvent) {
        alignPanel()

        var textString = ""

        val totalCost = getTotalCost()

        textString += if (totalCost > 0) {
            "§e§lTotal Cost: §r§d${totalCost.round(2).formatNumber()}\n\n"
        } else {
            "§e§lTotal Cost: §o§8(Unknown)§r\n\n"
        }

        textString += "§e§lCopper Received: §r§d${getCopperReturn().round(2).formatNumber()}\n\n"


        val pricePerCopper: Double = getTotalCost() / getCopperReturn()
        textString += if (pricePerCopper > 0) {
            "§e§lCoins/Copper: §r§d${pricePerCopper.round(2).formatNumber()}\n\n"
        } else {
            "§e§lCoins/Copper: §o§8(Unknown)§r\n\n"
        }

        var priceBreakdown = ""
        val coinCostMap: HashMap<String, Double> = getCoinCostMap()
        for ((key, value) in getQuantityCostMap().entries) {
            val cost = coinCostMap[key] ?: continue

            priceBreakdown += if (cost >= 0) {
                "§7x§d${value}§7 $key for a total of §d${cost.round(2).formatNumber()}§7 coins.\n"
            } else {
                "§7x§d$value §7$key for a total of §o§8(Unknown)§r§7 coins.\n"
            }
        }

        textString += "§e§lPrice Breakdown:§r\n"
        textString += priceBreakdown
        textString += "\n\n"

        textString += "§e§lRewards:§r\n"
        for (line in getRewardsLore()) {
            textString += "$line\n"
        }

        textComponent.setText(textString)
    }

    // Returns a hashmap containing the name of an item and the quantity
    private fun getQuantityCostMap(): HashMap<String, Int> {
        val trader = (minecraft.currentScreen as GuiChest).chestInventory

        // Slots 29 is where the accept buttons is
        val acceptButton = trader.getStackInSlot(29)
        val formattedAcceptButtonLore = acceptButton.getLore()

        // Removes all the format codes from lore
        val unformattedAcceptButtonLore = formattedAcceptButtonLore.removeColorCodes()
        val costLineIndex = unformattedAcceptButtonLore.indexOf("Items Required:")
        val rewardsStartIndex = unformattedAcceptButtonLore.indexOf("Rewards:")

        // Finds each item of the lore
        val cost = ArrayList<String>()
        for (i in costLineIndex + 1 until rewardsStartIndex) {
            cost.add(unformattedAcceptButtonLore[i])
        }
        val costMap = HashMap<String, Int>()
        for (costLine in cost) {
            // All the messages are formatted <Name> x<Cost>
            // so the name is up until the last 'x,' and the cost starts
            // after the x
            var costStartIndex = costLine.lastIndexOf("x")
            // If the item does not have a multiple, it means it only has one
            var singleItem = false
            var amountString: String
            // If there is no x, check to see if there is something there
            if (costStartIndex == -1) {
//                If there is something there, it's an item with only one
                if (costLine.length > 5) {
                    singleItem = true
                    costStartIndex = costLine.length
                } else {
                    continue
                }
            }

            // Gets the name of ihe item and formats it
            var name = costLine.substring(0, costStartIndex)
            name = stripLeading(name)
            name = stripTrailing(name)
            var amount: Int
            if (singleItem) {
                amount = 1
            } else {
                // Gets the cost of the item and converts it to an integer
                amountString = costLine.substring(costStartIndex + 1)
                // Replaces all non-numeric characters in the string
                amountString = amountString.replace("[^\\d.]".toRegex(), "")
                amountString = amountString.replace(",", "")
                amountString = amountString.replace(".", "")
                amount = amountString.toInt()
            }

            // Adds it to the cost map
            costMap[name] = amount
        }
        return costMap
    }

    private fun getRewardsLore(): List<String> {
        val trader = (minecraft.currentScreen as GuiChest).chestInventory

        // Slots 29 is where the accept buttons is
        val acceptButton = trader.getStackInSlot(29)
        val formattedAcceptButtonLore = acceptButton.getLore()

        // Removes all the format codes from lore
        val unformattedAcceptButtonLore = formattedAcceptButtonLore.removeColorCodes()
        val rewardsStartIndex = unformattedAcceptButtonLore.indexOf("Rewards:")
        return formattedAcceptButtonLore.subList(rewardsStartIndex, formattedAcceptButtonLore.size)
    }

    private fun getCopperReturn(): Int {
        val unformattedRewardsLore: List<String> = getRewardsLore().removeColorCodes()
        for (line in unformattedRewardsLore) {
            if (!line.contains(" Copper")) {
                continue
            }
            var strippedLine = stripLeading(line)
            strippedLine = stripTrailing(strippedLine)
            val amountStartIndex = strippedLine.indexOf("+") + 1
            val amountEndIndex = strippedLine.indexOf(" C")
            val amountString = strippedLine.substring(amountStartIndex, amountEndIndex)
            return amountString.toInt()
        }
        return -1
    }

    private fun getItemCost(itemId: String, quantity: Int): Double {
        return quantity * (getItem(itemId)?.getBuyPrice() ?: return 0.0)
    }

    private fun getCoinCostMap(): HashMap<String, Double> {
        val quantityMap: HashMap<String, Int> = getQuantityCostMap()
        val coinMap = HashMap<String, Double>()
        for ((key, value) in quantityMap) {
            val id = getId(key)

            val price: Double = getItemCost(id, value)

            coinMap[key] = price
        }
        return coinMap
    }

    private fun getTotalCost(): Double {
        val costMap: HashMap<String, Double> = getCoinCostMap()
        var totalCost = 0.0

        for (individualCost in costMap.values) {
            totalCost += individualCost
        }

        return totalCost
    }
    
}