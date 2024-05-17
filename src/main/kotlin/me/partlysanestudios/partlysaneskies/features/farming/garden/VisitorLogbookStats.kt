/* VisitorLogbookStats.kt
 * A Kotlin class written by Erymanthus[#5074] | (u/)RayDeeUx and Su386
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
import me.partlysanestudios.partlysaneskies.PartlySaneSkies.Companion.minecraft
import me.partlysanestudios.partlysaneskies.data.cache.VisitorLogbookData.getAllVisitors
import me.partlysanestudios.partlysaneskies.data.cache.VisitorLogbookData.isVisitorLogbook
import me.partlysanestudios.partlysaneskies.data.skyblockdata.Rarity
import me.partlysanestudios.partlysaneskies.features.gui.SidePanel
import me.partlysanestudios.partlysaneskies.render.gui.constraints.ScaledPixelConstraint.Companion.scaledPixels
import me.partlysanestudios.partlysaneskies.utils.ChatUtils.sendClientMessage
import me.partlysanestudios.partlysaneskies.utils.ElementaUtils.applyBackground
import me.partlysanestudios.partlysaneskies.utils.MinecraftUtils.getLore
import me.partlysanestudios.partlysaneskies.utils.StringUtils.formatNumber
import net.minecraftforge.client.event.GuiScreenEvent
import java.awt.Color
import kotlin.collections.HashMap
import kotlin.math.log

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
        return if (!isVisitorLogbook(minecraft.currentScreen)) {
            false
        } else if (!PartlySaneSkies.config.visitorLogbookStats) {
            false
        } else {
            true
        }
    }



    override fun onPanelRender(event: GuiScreenEvent.BackgroundDrawnEvent) {
        alignPanel()

        val timesVisited = HashMap<Rarity, Int>()
        val timesAccepted = HashMap<Rarity, Int>()

        for (visitor in getAllVisitors()) { // I don't want to touch this - Su // I touched this - Su
            if (visitor.rarity == Rarity.UNKNOWN) {
            }
            timesAccepted[visitor.rarity] = (timesAccepted[visitor.rarity] ?: 0) + visitor.timesAccepted
            timesVisited[visitor.rarity] = (timesVisited[visitor.rarity] ?: 0) + visitor.timesVisited
        }
        val text = getString(timesVisited, timesAccepted)
        textComponent.setText(text)
    }

    private fun getString(visited:  HashMap<Rarity, Int>, accepted: HashMap<Rarity, Int>): String {
        var str = ""
        val rarities = Rarity.entries.toTypedArray()
        rarities.sortBy { it.order }
        for (rarity in rarities) {
            if (!visited.contains(rarity)) {
                continue
            }
            str +=
            """
                ${rarity.colorCode}${rarity.displayName}
                §7Times Visited: §d${(visited[rarity] ?: 0).formatNumber()}
                §7Accepted: §d${(accepted[rarity] ?: 0).formatNumber()}
                §7Denied/Pending: §d${((visited[rarity] ?: 0) - (accepted[rarity] ?: 0)).formatNumber()}
                
                
            """.trimIndent()

        }

        return str
    }
}