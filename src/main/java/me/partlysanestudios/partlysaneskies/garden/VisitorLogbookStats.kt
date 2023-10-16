/* VisitorLogbookStats.kt
 * A Kotlin class written by Erymanthus[#5074] | (u/)RayDeeUx
 * for Su386 and FlagMaster's Partly Sane Skies mod.
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
import gg.essential.elementa.components.UIRoundedRectangle
import gg.essential.elementa.components.UIWrappedText
import gg.essential.elementa.components.Window
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.PixelConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.universal.UMatrixStack
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.system.ThemeManager
import me.partlysanestudios.partlysaneskies.utils.StringUtils
import me.partlysanestudios.partlysaneskies.utils.Utils
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.*

class VisitorLogbookStats {

    private val tiers: List<String> = listOf<String>("§f§zTotal", "§aUncommon", "§9Rare", "§6Legendary", "§cSpecial", "§eUnknown") //total | uncommon | rare | leg | special | UNKNOWN
    private var theBaseString = ""

    @SubscribeEvent
    fun onGuiScreen(event: GuiScreenEvent.BackgroundDrawnEvent) {
        if (!isVisitorLogbook()) return
        if (!PartlySaneSkies.config.visitorLogbookStats) return
        val slots = ((PartlySaneSkies.minecraft.currentScreen as GuiChest)).inventorySlots.inventorySlots
        var seenStats: MutableList<Int> = mutableListOf<Int>(0, 0, 0, 0, 0, 0) //total | uncommon | rare | leg | special | UNKNOWN
        var acceptedStats: MutableList<Int> = mutableListOf<Int>(0, 0, 0, 0, 0, 0) //total | uncommon | rare | leg | special | UNKNOWN
        theBaseString = ""
        for (s in slots) {
            if (s.getStack() == null) continue //"cOnDiTiOn 'S.GeTsTaCk() == NuLL' is aLwAyS FaLsE" stfu intellij i dont give a fuck

            val eItemStack = s.getStack()
            val lore = Utils.getLore(eItemStack)

            if (lore.isEmpty()) continue
            if (lore.first().contains("Page ")) break
            if (StringUtils.removeColorCodes(lore.first()).isEmpty() || lore.first().contains("This NPC hasn't visited you") || lore.first().contains("Various NPCs ") || lore.first().contains("Requirements")) continue
            
            var noPlcwList = mutableListOf<String>()

            //fuck formatting codes
            for (line in lore) noPlcwList.add(StringUtils.removeColorCodes(line))
            //§7Times Visited: §a0
            //Times Visited: 0
            //§7Offers Accepted: §a0
            //Offers Accepted: 0
            val rarityIndex = when (noPlcwList.find{ it.contains("SPECIAL") || it.contains("LEGENDARY")  || it.contains("RARE") || it.contains("UNCOMMON") }) {
                "UNCOMMON" -> 1
                "RARE" -> 2
                "LEGENDARY" -> 3
                "SPECIAL" -> 4
                else -> 5
            }
            val lineTimesVisited = noPlcwList.find{ it.contains("Times Visited: ") } ?: break
            val lineOffersAccepted = noPlcwList.find{ it.contains("Offers Accepted: ") } ?: break
            seenStats[rarityIndex] += lineTimesVisited.split(" ").last().replace(",", "").replace(".", "").toInt()
            acceptedStats[rarityIndex] += lineOffersAccepted.split(" ").last().replace(",", "").replace(".", "").toInt()
            seenStats[0] += lineTimesVisited.split(" ").last().replace(",", "").replace(".", "").toInt()
            acceptedStats[0] += lineOffersAccepted.split(" ").last().replace(",", "").replace(".", "").toInt()
        }
        getString(seenStats, acceptedStats)
    }

    private fun getString(seenStats: MutableList<Int>, acceptedStats: MutableList<Int>) {
        for (indexInt in 0..(tiers.size - 1)) { //"'RaNgEtO' oR ThE '..' cALL sHoULd bE RePLaCeD wiTh 'UnTiL'" SHUT THE FUCK UP INTELLIJ LET ME CODE HOWEVER THE FUCK I WANT TO
            theBaseString += "\n${tiers[indexInt]}: [${seenStats[indexInt]} | ${acceptedStats[indexInt]} | ${Math.abs(seenStats[indexInt] - acceptedStats[indexInt])}]"
        }
    }

    private fun isVisitorLogbook(): Boolean {
        val gui = PartlySaneSkies.minecraft.currentScreen ?: return false
        if (gui !is GuiChest) {
            return false
        }

        val inventories = PartlySaneSkies.getSeparateUpperLowerInventories(gui)
        val logbook = inventories[0]

        return StringUtils.removeColorCodes(logbook.getDisplayName().getFormattedText()).contains("Visitor's Logbook")
    }

    val window = Window(ElementaVersion.V2)

    val box = UIRoundedRectangle(5f)
            .setColor(Color(0, 0, 0, 0))
            .setChildOf(window)
    
    val image = ThemeManager.getCurrentBackgroundUIImage()
            .setChildOf(box)
    
    val pad = 5
    var textComponent: UIWrappedText = UIWrappedText() childOf box

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
        box.setX(widthScaledConstraint(700f))
            .setY(CenterConstraint())
            .setWidth(widthScaledConstraint(250f))
            .setHeight(widthScaledConstraint(300f))

        image.setX(CenterConstraint())
            .setY(CenterConstraint())
            .setWidth(PixelConstraint(box.getWidth()))
            .setHeight(PixelConstraint(box.getHeight()))
        
        textComponent.setX(widthScaledConstraint(pad.toFloat()))
            .setTextScale(widthScaledConstraint(1f))
            .setY(widthScaledConstraint(2 * (pad.toFloat())))
            .setWidth(PixelConstraint(box.getWidth() - (2 * (pad.toFloat()))))


        var textString = "§2Garden Visitor Stats (for current page):\n§7Format: [visit | accept | denied + pending]\n"
        textString += theBaseString
        textComponent.setText(textString)

        window.draw(UMatrixStack())
    }

    private fun getWidthScaleFactor(): Float {
        return window.getWidth() / 1097f
    }

    private fun widthScaledConstraint(value: Float): PixelConstraint  {
        return PixelConstraint(value * getWidthScaleFactor())
    }

}
