/* VisitorLogbookStats.kt
 * A Kotlin class written by Erymanthus for Su386's Partly Sane Skies mod.
 * See LICENSE for copyright and license notices.
 *
 * KOTLIN ON TOP BABYYYYYYYY
 *
 * j10a — Today at 4:47 AM
 * A sidebar like the bits shop one, would include:
 * total visitors accepted | seen
 * total visitors accepted | seen uncommon
 * total visitors accepted | seen rare
 * total visitors accepted | seen leg
 * total visitors accepted | seen special (not sure)
 * 
 * (presumably seen across all araities)
*/

package me.partlysanestudios.partlysaneskies.garden

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIRoundedRectangle
import gg.essential.elementa.components.UIWrappedText
import gg.essential.elementa.components.Window
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.PixelConstraint
import gg.essential.universal.UMatrixStack
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockDataManager
import me.partlysanestudios.partlysaneskies.data.skyblockdata.SkyblockItem
import me.partlysanestudios.partlysaneskies.system.ThemeManager
import me.partlysanestudios.partlysaneskies.utils.StringUtils
import me.partlysanestudios.partlysaneskies.utils.Utils
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

import java.awt.*
import java.io.IOException
import java.util.List
import java.util.*

class VisitorLogbookStats {

    var seenStats = mutableListOf<Int>(0, 0, 0, 0, 0) //total | uncommon | rare | leg | special
    var acceptedStats = mutableListOf<Int>(0, 0, 0, 0, 0) //total | uncommon | rare | leg | special

    @SubscribeEvent
    fun onItemTooltip(event: ItemTooltipEvent) {
        if (!isVisitorLogbook()) return
        if (!PartlySaneSkies.config.visitorLogbookStats) return
        if (event.itemStack == null) return

        val eItemStack = event.itemStack
        val lore = Utils.getLore(eItemStack)

        if (lore.isEmpty()) return


        for (line in lore) {
            if 
        }
    }

    fun isVisitorLogbook(): Boolean {
        val gui = PartlySaneSkies.minecraft.currentScreen
        if (gui == null) {
            return false
        }
        if (!(gui is GuiChest)) {
            return false
        }

        val inventories = PartlySaneSkies.getSeparateUpperLowerInventories(gui)
        val logbook = inventories[0]

        return StringUtils.removeColorCodes(logbook.getDisplayName().getFormattedText()).contains("Visitor's Logbook")
    }

    var window = Window(ElementaVersion.V2)

    var box = new UIRoundedRectangle(widthScaledConstraint(5).getValue())
            .setColor(new Color(0, 0, 0, 0))
            .setChildOf(window)
    
    var image = ThemeManager.getCurrentBackgroundUIImage()
            .setChildOf(box)
    
    var pad = 5
    var textComponent = (UIWrappedText) new UIWrappedText()
        .setChildOf(box)

    @SubscribeEvent
    fun renderInformation(event: GuiScreenEvent.BackgroundDrawnEvent) {
        if (!isVisitorLogbook()) {
            box.hide()
            return
        }
        if (!PartlySaneSkies.config.visitorLogbookStats) {
            return
        }

        box.unhide(true)
        box.setX(widthScaledConstraint(700))
            .setY(new CenterConstraint())
            .setWidth(widthScaledConstraint(250))
            .setHeight(widthScaledConstraint(300))

        image.setX(new CenterConstraint())
            .setY(new CenterConstraint())
            .setWidth(new PixelConstraint(box.getWidth()))
            .setHeight(new PixelConstraint(box.getHeight()))
        
        textComponent.setX(widthScaledConstraint(pad))
            .setTextScale(widthScaledConstraint(1f))
            .setY(widthScaledConstraint(2 * pad))
            .setWidth(new PixelConstraint(box.getWidth() - widthScaledConstraint(2 * pad).getValue()))
            

        var textString = "§e§lVisitor Stats:\n\n"

        textString += getString()
        textString += "\n\n"
        textString = (textString)
        textComponent.setText(textString)

        window.draw(new UMatrixStack())
    }

    private fun float getWidthScaleFactor() {
        return window.getWidth() / 1097f
    }

    private fun PixelConstraint widthScaledConstraint(float value) {
        return new PixelConstraint(value * getWidthScaleFactor())
    }

}
