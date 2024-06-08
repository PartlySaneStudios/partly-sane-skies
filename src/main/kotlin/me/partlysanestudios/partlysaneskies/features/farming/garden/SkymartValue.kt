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
import me.partlysanestudios.partlysaneskies.data.pssdata.PublicDataManager.getFile
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockDataManager.getItem
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockItem
import me.partlysanestudios.partlysaneskies.events.SubscribePSSEvent
import me.partlysanestudios.partlysaneskies.events.data.LoadPublicDataEvent
import me.partlysanestudios.partlysaneskies.features.gui.SidePanel
import me.partlysanestudios.partlysaneskies.render.gui.constraints.ScaledPixelConstraint.Companion.scaledPixels
import me.partlysanestudios.partlysaneskies.utils.ElementaUtils.applyBackground
import me.partlysanestudios.partlysaneskies.utils.MathUtils.round
import me.partlysanestudios.partlysaneskies.utils.MathUtils.sortMap
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.containerInventory
import me.partlysanestudios.partlysaneskies.utils.StringUtils.formatNumber
import me.partlysanestudios.partlysaneskies.utils.StringUtils.removeColorCodes
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraftforge.client.event.GuiScreenEvent
import java.awt.Color

object SkymartValue : SidePanel() {
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
        if (!config.skymartValue) {
            return false
        }

        if (minecraft.currentScreen == null) {
            return false
        }

        if (minecraft.currentScreen !is GuiChest) {
            return false
        }

        val skymart = (minecraft.currentScreen as GuiChest).containerInventory

        return skymart.displayName.formattedText.removeColorCodes().contains("SkyMart")
    }


    private val copperCost = HashMap<String, Int>()

    override fun onPanelRender(event: GuiScreenEvent.BackgroundDrawnEvent) {
        alignPanel()

        var textString = "§e§lTop Items:\n\n"

        textString += getString()

        textComponent.setText(textString)
    }


    @SubscribePSSEvent
    fun initCopperValues(event: LoadPublicDataEvent) {
        val str = getFile("constants/skymart_copper.json")
        val skymartObject = JsonParser().parse(str).getAsJsonObject().getAsJsonObject("skymart")
        for ((key, value) in skymartObject.entrySet()) {
            copperCost[key] = value.asInt
        }
    }


    private fun getString(): String {
        var str = ""
        val map = HashMap<String, Double>()
        for (id in copperCost.keys) {
            val item = getItem(id) ?: continue
            map[id] = item.getSellPrice() / (copperCost[id] ?: 1)
        }
        val sortedMap = map.sortMap()
        var i = 1
        for ((key, value) in sortedMap) {

            val item = getItem(key) ?: SkyblockItem.emptyItem
            str += "§6$i. §d${item.name}§7 costs §d${copperCost[key]?.formatNumber() ?: 0}§7 copper and sells for §d${item.getSellPrice().round(1).formatNumber()}§7 coins \n§8 (${value.round(1).formatNumber()} coins per copper)\n"

            i++
            if (i > 5) {
                break
            }
        }

        return str
    }
}