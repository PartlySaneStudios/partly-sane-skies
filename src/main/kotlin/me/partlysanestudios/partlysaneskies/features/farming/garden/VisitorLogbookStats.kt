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


package me.partlysanestudios.partlysaneskies.features.farming.garden

import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIWrappedText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.constraint
import gg.essential.elementa.dsl.percent
import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.data.skyblockdata.Rarity
import me.partlysanestudios.partlysaneskies.features.gui.SidePanel
import me.partlysanestudios.partlysaneskies.render.gui.constraints.ScaledPixelConstraint.Companion.scaledPixels
import me.partlysanestudios.partlysaneskies.utils.ElementaUtils.applyBackground
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.containerInventory
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.getItemstackList
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.getLore
import me.partlysanestudios.partlysaneskies.utils.StringUtils.formatNumber
import me.partlysanestudios.partlysaneskies.utils.StringUtils.removeColorCodes
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraftforge.client.event.GuiScreenEvent
import java.awt.Color
import java.util.*
import kotlin.collections.HashMap

object VisitorLogbookStats: SidePanel() {

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
        return if (!isVisitorLogbook()) {
            false
        } else if (!PartlySaneSkies.config.visitorLogbookStats) {
            false
        } else {
            true
        }
    }


    override fun onPanelRender(event: GuiScreenEvent.BackgroundDrawnEvent) {
        alignPanel()

        val slots = (PartlySaneSkies.minecraft.currentScreen as GuiChest).containerInventory.getItemstackList()
        val visited = HashMap<Rarity, Int>()
        val accepted = HashMap<Rarity, Int>()
        for (item in slots) { // I don't want to touch this - Su // I touched this - Su
            val lore = item.getLore()

            if (lore.isEmpty()) {
                continue
            }

            val noColorLore = lore.removeColorCodes()
            //§7Times Visited: §0
            //Times Visited: 0
            //§7Offers Accepted: §a0
            //Offers Accepted: 0
            val timesVisitedRegex = "Times Visited: (\\d+(?:,\\d+)*)".toRegex()
            val offersAcceptedRegex = "Offer Accepted: (\\d+(?:,\\d+)*)".toRegex()
            var rarity = Rarity.UNKNOWN
            for (line in noColorLore) {
                for (enum in Rarity.entries) {
                    if (line.lowercase().contains(enum.toString().lowercase())) {
                        rarity = enum
                        break
                    }
                }
                if (offersAcceptedRegex.containsMatchIn(line)) {
                    val find = offersAcceptedRegex.find(line)?.destructured ?: continue
                    accepted[rarity] = (accepted[rarity] ?: 0) + 1
                } else if (timesVisitedRegex.containsMatchIn(line)) {
                    val find = timesVisitedRegex.find(line)?.destructured ?: continue
                    visited[rarity] = (visited[rarity] ?: 0) + 1
                }
            }
        }

        textComponent.setText(getString(visited, accepted))
    }

    private fun getString(visited:  HashMap<Rarity, Int>, accepted: HashMap<Rarity, Int>): String {
        var str = ""
        for (rarity in accepted.keys) {
            str +=
            """${rarity.colorCode}${rarity.displayName}
                §7Times Visited: §d${(visited[rarity] ?: 0).formatNumber()}
                §7Accepted: §d${(accepted[rarity] ?: 0).formatNumber()}
                §7Denied/Pending: §d${((visited[rarity] ?: 0) - (accepted[rarity] ?: 0)).formatNumber()}
                
            """.trimIndent()

        }

        return str
    }

    private fun isVisitorLogbook(): Boolean {
        val gui = PartlySaneSkies.minecraft.currentScreen ?: return false
        if (gui !is GuiChest) {
            return false
        }
        val logbook = gui.containerInventory

        return logbook.displayName.formattedText.removeColorCodes().contains("Visitor's Logbook")
    }
}