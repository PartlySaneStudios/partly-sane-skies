//
// Written by Su386.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.features.economy

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
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockDataManager.bitIds
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockDataManager.getItem
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockDataManager.initBitValues
import me.partlysanestudios.partlysaneskies.features.gui.SidePanel
import me.partlysanestudios.partlysaneskies.render.gui.constraints.ScaledPixelConstraint.Companion.scaledPixels
import me.partlysanestudios.partlysaneskies.utils.ElementaUtils.applyBackground
import me.partlysanestudios.partlysaneskies.utils.HypixelUtils.getBits
import me.partlysanestudios.partlysaneskies.utils.MathUtils.round
import me.partlysanestudios.partlysaneskies.utils.MathUtils.sortMap
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.containerInventory
import me.partlysanestudios.partlysaneskies.utils.StringUtils.formatNumber
import me.partlysanestudios.partlysaneskies.utils.StringUtils.removeColorCodes
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraftforge.client.event.GuiScreenEvent
import java.awt.Color
import java.io.IOException

object BitsShopValue : SidePanel() {
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

    override fun onPanelRender(event: GuiScreenEvent.BackgroundDrawnEvent) {
        alignPanel()

        var textString = "§e§lTop Items:\n\n"

        textString += getString()
        textString += "\n\n"
        textComponent.setText(textString)
    }

    override fun shouldDisplayPanel(): Boolean {
        if (!config.bestBitShopItem) {
            return false
        }

        if (minecraft.currentScreen == null) {
            return false
        }

        if (minecraft.currentScreen !is GuiChest) {
            return false
        }

        val shop = (minecraft.currentScreen as GuiChest).containerInventory

        val title = shop.displayName.formattedText.removeColorCodes()

        return title.contains("Community Shop") || title.startsWith("Bits Shop - ")
    }

    private fun getString(): String {
        var str = ""
        val map = HashMap<String, Double>()
        val bitCount = getBits()
        val filterAffordable = config.bitShopOnlyShowAffordable

        if (bitIds.isEmpty()) {
            Thread {
                try {
                    initBitValues()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }.start()
        }

        for (id in bitIds) {
            val item = getItem(id)
            if (filterAffordable && bitCount < (item?.bitCost ?: 0)) {
                continue
            }
            map[id] = (item?.getSellPrice() ?: 0.0) / (item?.bitCost ?: 0)
        }
        val sortedMap = map.sortMap(true)
        var i = 1
        for ((key, value) in sortedMap) {
            val item = getItem(key) ?: continue
            str += "§6$i. §d ${item.name}§7 costs §d${item.bitCost.formatNumber()}§7 bits " +
                    "and sells for §d${item.getSellPrice().round(1).formatNumber()}§7 coins " +
                    "\n§8 (${value.round(1).formatNumber()} coins per bit)\n"
            i++
            if (i > 5) {
                break
            }
        }
        if (filterAffordable) {
            str += "\n\n§8§oOnly showing affordable items"
        }

        return str
    }
}