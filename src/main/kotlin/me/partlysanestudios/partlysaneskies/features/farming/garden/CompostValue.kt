//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.farming.garden

import com.google.gson.JsonParser
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
import me.partlysanestudios.partlysaneskies.data.pssdata.PublicDataManager
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockDataManager.getItem
import me.partlysanestudios.partlysaneskies.events.SubscribePSSEvent
import me.partlysanestudios.partlysaneskies.events.data.LoadPublicDataEvent
import me.partlysanestudios.partlysaneskies.features.gui.SidePanel
import me.partlysanestudios.partlysaneskies.render.gui.constraints.ScaledPixelConstraint.Companion.scaledPixels
import me.partlysanestudios.partlysaneskies.utils.ElementaUtils.applyBackground
import me.partlysanestudios.partlysaneskies.utils.MathUtils.round
import me.partlysanestudios.partlysaneskies.utils.MathUtils.sortMap
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.containerInventory
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.getLore
import me.partlysanestudios.partlysaneskies.utils.StringUtils.formatNumber
import me.partlysanestudios.partlysaneskies.utils.StringUtils.parseAbbreviatedNumber
import me.partlysanestudios.partlysaneskies.utils.StringUtils.removeColorCodes
import me.partlysanestudios.partlysaneskies.utils.StringUtils.stripLeading
import me.partlysanestudios.partlysaneskies.utils.StringUtils.stripTrailing
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.inventory.IInventory
import net.minecraftforge.client.event.GuiScreenEvent
import java.awt.Color
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set
import kotlin.math.ceil

object CompostValue : SidePanel() {
    override val panelBaseComponent: UIComponent = UIBlock().applyBackground()
        .constrain {
            x = 800.scaledPixels
            y = CenterConstraint()
            width = 225.scaledPixels
            height = 350.scaledPixels
            color = Color(0, 0, 0, 0).constraint
        }

    private val textComponent = UIWrappedText()
        .constrain {
            x = CenterConstraint()
            y = CenterConstraint()
            height = 95.percent
            width = 95.percent
            textScale = 1.scaledPixels
        } childOf panelBaseComponent

    private var maxCompost = 0.0
    private var fillLevel = 0.0
    private var compostCost = 0.0

    private val compostValueMap = HashMap<String, Double>()
    private val costPerOrganicMatterMap = HashMap<String, Double>()
    private val costPerCompostMap = HashMap<String, Double>()

    override fun onPanelRender(event: GuiScreenEvent.BackgroundDrawnEvent) {
        val composter: IInventory = (minecraft.currentScreen as GuiChest).containerInventory

        alignPanel()

        try {
            compostCost = getCompostCost(composter)
            fillLevel = getOrganicMatterFillLevel(composter)
            maxCompost = getOrganicMatterLimit(composter)

            updateString()
        } catch (_: NullPointerException) {
        }
    }

    override fun shouldDisplayPanel(): Boolean {
        if (!config.bestCropsToCompost) {
            return false
        }

        if (minecraft.currentScreen == null) {
            return false
        }

        if (minecraft.currentScreen !is GuiChest) {
            return false
        }

        val composter = (minecraft.currentScreen as GuiChest).containerInventory

        val collectCompostButton = composter.getStackInSlot(13) ?: return false

        val collectCompostButtonName = collectCompostButton.displayName.removeColorCodes()

        if (collectCompostButtonName != "Collect Compost") {
            return false
        }
        if (!composter.displayName.formattedText.removeColorCodes().contains("Composter")) {
            return false
        }

        return true
    }

    @SubscribePSSEvent
    fun loadCompostValues(event: LoadPublicDataEvent) {
        val data = PublicDataManager.getFile("constants/organic_matter.json")
        val obj = JsonParser().parse(data).asJsonObject
        for (en in obj.entrySet()) {
            compostValueMap[en.key] = en.value.asDouble
        }
    }

    private fun updateString() {
        var textString = "§e§lTop Crops:\n\n"
        textString += getString()
        textString += "\n\n§e§lCompost:\n\n"
        val compostSellPrice = getItem("COMPOST")?.bazaarSellPrice ?: 0.0

        var compostAmount = getCurrentCompostAbleToMake()
        if (maxCompost == fillLevel) {
            compostAmount = getMaxCompostAbleToMake()
        }

        textString += "§7x§d${
            compostAmount.round(0).formatNumber()
        }§7 Compost currently sells for §d${
            (compostSellPrice * compostAmount).round(1).formatNumber()
        }§7 coins.\n§8(${compostSellPrice.round(1).formatNumber()}/Compost)"

        textComponent.setText(textString)
    }

    private fun getString(): String {
        var str = ""
        updateCostPerOrganicMatterMap()
        updateCostPerCompostMap()
        val map = costPerCompostMap.sortMap()
        var i = 1
        for ((id, costPerCompost) in map) {
            val cropPerCompost = compostCost / (compostValueMap[id] ?: continue)
            val cropName = getItem(id)?.name ?: continue
            var compostAmount = getCurrentCompostAbleToMake()
            if (maxCompost == fillLevel) {
                compostAmount = getMaxCompostAbleToMake()
            }
            str += "§6$i. §7x§d${ceil(cropPerCompost * compostAmount).formatNumber()} $cropName§7 costing §d${
                (costPerCompost * compostAmount).round(
                    1,
                ).formatNumber()
            }§7 coins to fill. \n§8(x${ceil(cropPerCompost).formatNumber()}/Compost)\n"
            i++
            if (i > 5) {
                break
            }
        }

        return str
    }

    private fun updateCostPerOrganicMatterMap() {
        for ((key, value) in compostValueMap) {
            costPerOrganicMatterMap[key] = (getItem(key)?.getBuyPrice() ?: continue) / value
        }
    }

    private fun updateCostPerCompostMap() {
        for ((key, value) in costPerOrganicMatterMap) {
            costPerCompostMap[key] = value * compostCost
        }
    }

    private fun getCompostCost(inventory: IInventory): Double = getCompostCostString(inventory).parseAbbreviatedNumber()

    private fun getCompostCostString(composterInventory: IInventory): String {
        val infoItem = composterInventory.getStackInSlot(46)
        val loreList = infoItem.getLore()
        var costLine = "4,000"
        val regex = "The composter must have (\\d{1,3}(,\\d{3}))\\b"
        val pattern = Pattern.compile(regex)
        var matcher: Matcher
        for (line in loreList) {
            val unformattedLine = line.removeColorCodes()
            matcher = pattern.matcher(unformattedLine)
            if (matcher.find()) {
                costLine = unformattedLine
                break
            }
        }
        matcher = pattern.matcher(costLine)
        return if (matcher.find()) {
            matcher.group(1)
        } else {
            "4000"
        }
    }

    private fun getOrganicMatterFillLevel(inventory: IInventory): Double {
        val organicMatterFillLevelString = getOrganicMatterFillLevelString(inventory)

        return if (organicMatterFillLevelString.isEmpty()) {
            0.0
        } else {
            organicMatterFillLevelString.parseAbbreviatedNumber()
        }
    }

    private fun getOrganicMatterFillLevelString(composterInventory: IInventory): String {
        val infoItem = composterInventory.getStackInSlot(46)
        val loreList = infoItem.getLore()
        var costLine = "0"
        val regex = "\\b(\\d{1,3}(,\\d{3})*(\\.\\d+)?|\\d+k)\\b"
        val pattern = Pattern.compile(regex)
        var matcher: Matcher
        for (line in loreList) {
            val unformattedLine = line.removeColorCodes()
            matcher = pattern.matcher(unformattedLine)
            if (matcher.find()) {
                costLine = unformattedLine
                break
            }
        }
        matcher = pattern.matcher(costLine)
        return if (matcher.find()) {
            matcher.group(1)
        } else {
            "0"
        }
    }

    private fun getOrganicMatterLimit(inventory: IInventory): Double = getOrganicMatterLimitString(inventory).parseAbbreviatedNumber()

    private fun getMaxCompostAbleToMake(): Double = maxCompost / compostCost

    private fun getCurrentCompostAbleToMake(): Double = (maxCompost - fillLevel) / compostCost

    private fun getOrganicMatterLimitString(composterInventory: IInventory): String {
        val infoItem = composterInventory.getStackInSlot(46)
        val loreList = infoItem.getLore()
        var amountLine = "0/40k"
        val regex = "(.*?)/(.*?)"
        val pattern = Pattern.compile(regex)
        for (line in loreList) {
            val unformattedLine = line.removeColorCodes()
            val matcher = pattern.matcher(unformattedLine)
            if (matcher.find()) {
                amountLine = unformattedLine
                break
            }
        }
        amountLine = amountLine.removeColorCodes()
        amountLine = stripLeading(amountLine)
        amountLine = stripTrailing(amountLine)
        val indexOfStart = amountLine.indexOf("/")
        amountLine = amountLine.substring(indexOfStart + 1)
        return amountLine
    }
}
